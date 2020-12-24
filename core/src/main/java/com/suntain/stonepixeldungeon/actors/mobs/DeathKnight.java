/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.suntain.stonepixeldungeon.actors.mobs;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Badges;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.LazerWarn;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.effects.Aura;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.suntain.stonepixeldungeon.levels.traps.Trap;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeathKnight extends Mob {
	public int TargetPos = 0;
	protected ArrayList<Blob> b = new ArrayList<>();
	int phaze = 0;
	boolean canSummon = true;

	{
		actPriority = MOB_PRIO + 2;
		properties.add(Property.BOSS);
		lootChance = 1f;
	}

	@Override
	public void die( Object cause ) {

		super.die(cause);
		//Dungeon.level.unseal();
		Dungeon.level.unseal();
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
		Badges.validateBossSlain();
		yell(Messages.get(this, "defeated"));
}
	
	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
		BossHealthBar.assignBoss(this);
		Dungeon.level.seal();
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("TargetPos", TargetPos);
		bundle.put("phaze", phaze);
		bundle.put("canSummon", canSummon);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		TargetPos = bundle.getInt("TargetPos");
		phaze = bundle.getInt("phaze");
		canSummon = bundle.getBoolean("canSummon");

	}


	public boolean zap(){
		spend(TICK);
		if (TargetPos == 0) {
			PathFinder.Path path = Dungeon.findPath(enemy, enemy.pos, pos,
					Dungeon.level.passable,
					Dungeon.level.heroFOV);
			TargetPos = enemy.pos;
			if (path != null) TargetPos = path.removeFirst();
			for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
				int j = TargetPos + PathFinder.NEIGHBOURS9[i];
				b.add(Blob.seed(j, 2, LazerWarn.class));
				GameScene.add(b.get(b.size()-1));
			}
		} else {
			for(Blob bb : b) bb.emitter.recycle(LazerWarn.LazerParticle.class);
			for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
				int j = TargetPos + PathFinder.NEIGHBOURS9[i];
				 sprite.zap(j);
			}
			TargetPos = 0;
			return !(Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos]);
		}
		return true;
	}
	private void zap(int pos) {
		if (pos == enemy.pos) {
			if (hit(this, enemy, true)) {
				int dmg = damageRoll();
				enemy.damage(dmg, new LightningBolt());

				if (enemy == Dungeon.hero) {

					Camera.main.shake(4, 0.5f);

					if (!enemy.isAlive()) {
						Dungeon.fail(getClass());
						GLog.n(Messages.get(this, "zap_kill"));
					}
				}
			} else {
				enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}
		}
	}

	public static class LightningBolt{}

	public void onZapComplete(int pos) {
		zap(pos);
		next();
	}




	public int summon(Class<? extends Mob>... mobs){
		ArrayList<Integer> candidates = new ArrayList<>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
				candidates.add( p );
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<>();

		int nMobs = mobs.length;

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );

			respawnPoints.add( candidates.remove( index ) );
			nMobs--;
		}
		nMobs = 0;

		ArrayList<Mob> real_mobs = new ArrayList<>();

		for (Integer point : respawnPoints) {
			Mob mob = null;
			try {
				mob = mobs[nMobs].newInstance();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			if (mob != null) {
				mob.state = mob.WANDERING;
				mob.pos = point;
				GameScene.add(mob, 1f);
				real_mobs.add(mob);
				nMobs ++;
			}
		}

		//important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
		Trap t;
		for (Mob mob : real_mobs){
			//manually trigger traps first to avoid sfx spam
			if ((t = Dungeon.level.traps.get(mob.pos)) != null && t.active){
				t.disarm();
				t.reveal();
				t.activate();
			}
			ScrollOfTeleportation.appear(mob, mob.pos);
			Dungeon.level.press(mob.pos, mob, true);
		}
		return real_mobs.size();
	}


}
