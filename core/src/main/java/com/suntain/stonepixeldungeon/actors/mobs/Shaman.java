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
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Slow;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.ShamanSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Shaman extends Mob {

	private static final float TIME_TO_ZAP	= 2f;
	
	{
		spriteClass = ShamanSprite.class;

		HP = HT = 30;
		defenseSkill = 17;

		EXP = 7;
		maxLvl = 16;
		

	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 5, 25 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return fieldOfView[pos] && Dungeon.level.distance( pos, enemy.pos ) <= 3;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.distance( pos, enemy.pos ) <= 1 && Dungeon.depth != 15) {
			
			return super.doAttack( enemy );
			
		} else {
			
			boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else zap();
			
			return !visible;
		}
	}

	public static class LightningBolt{}

	private void zap() {
		spend( TIME_TO_ZAP );
		if (hit( this, enemy, true )) {
			//Buff.prolong( enemy, Slow.class, Slow.duration( enemy ) );
			if(Dungeon.level.distance( pos, enemy.pos ) <= 3) Buff.prolong( enemy, Slow.class, 1f );
			/*int dmg = Random.NormalIntRange(1, 4);
			if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
				dmg *= 1.5f;
			}*/
			//int dmg = 1;
			//enemy.damage( dmg, new LightningBolt() );

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