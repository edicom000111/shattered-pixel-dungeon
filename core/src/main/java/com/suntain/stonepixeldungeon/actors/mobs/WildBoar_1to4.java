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

import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.items.food.MysteryMeat;
import com.suntain.stonepixeldungeon.sprites.TreantSprite;
import com.watabou.utils.Random;

public class WildBoar_1to4 extends Mob {

	{
		spriteClass = TreantSprite.class;
		
		HP = HT = 8;
		defenseSkill = 2;
		EXP = 2;
		maxLvl = 7;
		rush = true;
		loot = new MysteryMeat();
		lootChance = 0.167f;
		baseSpeed = 0.8f;
	}
	/*
	@Override
	public boolean moveSprite( int from, int to ) {
		if (sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
			sprite.move( from, to );
			move( to );
			if(Dungeon.level.distance( to, enemy.pos ) == 1 && Dungeon.level.distance( from, enemy.pos ) > 1){
				if(canAttack(enemy) && enemy.invisible <= 0){
					Actor.addDelayed(this, attackDelay());
					sprite.attack(enemy.pos);
					enemySeen = true;
				}
				//Buff.affect(enemy, Bleeding.class).set(2);
				//Buff.affect(enemy, Slow.class);
			}
			return true;
		} else {
			sprite.place( to );
			return true;
		}
	}*/

	private int moving = 0;

	@Override
	protected boolean getCloser( int target ) {
		//this is used so that the crab remains slower, but still detects the player at the expected rate.
		moving++;
		if (moving < 5) {
			return super.getCloser( target );
		} else {
			moving = 0;
			return true;
		}

	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 7 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 8;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}
}
