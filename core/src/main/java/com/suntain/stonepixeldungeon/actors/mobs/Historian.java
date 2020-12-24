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
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.Electricity;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Corruption;
import com.suntain.stonepixeldungeon.actors.buffs.Paralysis;
import com.suntain.stonepixeldungeon.actors.buffs.Slow;
import com.suntain.stonepixeldungeon.items.Generator;
import com.suntain.stonepixeldungeon.levels.CityBossLevel;
import com.suntain.stonepixeldungeon.levels.PrisonBossLevel;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.HistorianSprite;
import com.suntain.stonepixeldungeon.sprites.ShamanSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Historian extends Mob {

	private static final float TIME_TO_ZAP	= 1f;

	{
		spriteClass = HistorianSprite.class;

		HP = HT = 70;
		defenseSkill = 15;

		EXP = 10;
		maxLvl = 19;

		loot = Generator.Category.SCROLL;
		lootChance = 0.2f;

	}
	@Override
	protected boolean act() {
		if(Dungeon.depth == 20) if(((CityBossLevel)Dungeon.level).boss == null || !((CityBossLevel)Dungeon.level).boss.isAlive()){
			HP = 0;
			destroy();
			sprite.die();
		}
		return super.act();
	}

	@Override
	public void notice() {
		super.notice();
		//if(Dungeon.depth == 20) for(Mob mob : Dungeon.level.mobs) if((mob instanceof Historian || mob instanceof Anduin) && mob.state == SLEEPING) mob.beckon(Dungeon.hero.pos);
	}

	@Override
	public boolean getCloser(int target){
		if(Dungeon.depth == 20) {
			if (alignment == Alignment.ALLY && enemy == null && buff(Corruption.class) == null) {
				target = ((CityBossLevel)Dungeon.level).boss.pos;
			} else if (enemy != null) {
				target = enemy.pos;
			}
		}
		return super.getCloser(target);
	}
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 35 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return fieldOfView[pos] && Dungeon.level.distance( pos, enemy.pos ) <= 3;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
			boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else zap();
			
			return !visible;

	}

	public static class LightningBolt{}

	private void zap() {
		spend( TIME_TO_ZAP );
		if (hit( this, enemy, true )) {
			int dmg = damageRoll();
			if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
				dmg *= 1.5f;
			}
			if (Random.Int(0, 10) == 0 && !enemy.isImmune(Electricity.class)) Buff.prolong( enemy, Paralysis.class, 1f);
			enemy.damage(dmg, new LightningBolt());


			if (enemy == Dungeon.hero) {

				Camera.main.shake( 2, 0.3f );

				if (!enemy.isAlive()) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "zap_kill") );
				}
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}
}