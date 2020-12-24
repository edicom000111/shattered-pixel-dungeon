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

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.GooWarn;
import com.suntain.stonepixeldungeon.actors.blobs.LazerWarn;
import com.suntain.stonepixeldungeon.actors.buffs.Quest;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Ghost;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.GnollTricksterSprite;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollTrickster extends Gnoll {

	protected int TargetPos = 0;
	{
		spriteClass = GnollTricksterSprite.class;

		HP = HT = 20;
		defenseSkill = 5;

		EXP = 5;

		state = WANDERING;



		if(Dungeon.hero.buff(Quest.class) != null){
			EXP = 0;
		} else properties.add(Property.MINIBOSS);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return (Dungeon.level.heroFOV[pos] && distance(enemy) >= 3 && Random.Int(0, 3) > 0) || (!Dungeon.level.heroFOV[pos] && distance(enemy) <= 3) || TargetPos != 0;
	}

	protected ArrayList<Blob> b = new ArrayList<>();
	@Override
	protected boolean doAttack( Char enemy ) {
		if (Dungeon.level.distance( pos, enemy.pos ) <= 1) {
			TargetPos = 0;
			return super.doAttack( enemy );

		} else {
			spend(1f);
			if(TargetPos == 0){
				PathFinder.Path path = Dungeon.findPath(enemy, enemy.pos, pos,
						Dungeon.level.passable,
						Dungeon.level.heroFOV);
				TargetPos = enemy.pos;
				if(path != null) TargetPos = path.removeFirst();
				for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
					int j = TargetPos + PathFinder.NEIGHBOURS9[i];
					b.add(Blob.seed(j, 2, LazerWarn.class));
					GameScene.add(b.get(b.size()-1));
				}
			} else {
				for(Blob bb : b) bb.emitter.recycle(LazerWarn.LazerParticle.class);
					for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
						int j = TargetPos + PathFinder.NEIGHBOURS9[i];
								((GnollTricksterSprite)sprite).zap(j);
					}
				System.out.println("위 "+!(Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos]));
				TargetPos = 0;
				return !(Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos]);
			}
			System.out.println("밑 false");
			return true;
		}
	}
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

	private void zap(int pos) {
		if (pos == enemy.pos) {
			if (hit(this, enemy, true)) {
				int dmg = Random.NormalIntRange(3, 10);
				if(Dungeon.level.QUEST == 3) dmg = Random.NormalIntRange(5, 15);
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
	/*@Override
	public void call() {
		next();
	}*/

	@Override
	public void die( Object cause ) {
		super.die( cause );

		Quest b = Dungeon.hero.buff(Quest.class);
		if(b != null) {
			b.kill++;
			b.check();
		} else Ghost.Quest.process();
	}



	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("TargetPos", TargetPos);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		TargetPos = bundle.getInt("TargetPos");
		super.restoreFromBundle( bundle );

	}

}
