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
import com.suntain.stonepixeldungeon.actors.buffs.Roots;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.Generator;
import com.suntain.stonepixeldungeon.sprites.LoreSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Lore extends Mob {
	public int count = 0;
	public int moveCount = 0;

	{
		spriteClass = LoreSprite.class;

		HP = HT = 60;
		defenseSkill = 15;
		EXP = 8;
		maxLvl = 17;

		loot = Generator.Category.SCROLL;
		lootChance = 0.25f;
		/*반경 칸에 있을 때 3턴에 1번씩 공격을 한다.
			맞을 경우 2~4턴의 속박이 걸린다.
			움직이는 속도는 3턴에 1번 1칸 움직인다.*/
	}


	@Override
	protected boolean getCloser( int target ) {
		moveCount++;
		if(moveCount % 3 == 0) moveCount = 0;
		if(moveCount == 0) return super.getCloser(target);
		else{
			return true;
		}
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (fieldOfView[enemy.pos] || count > 0) {
			moveCount = 0;
			spend(attackDelay());
			if(count == 0) {
				if(Dungeon.level.heroFOV[pos]) {
					((LoreSprite)sprite).attack(enemy.pos);
					return false;
				}
				else {
					attack(enemy);
					return true;
				}
			} else if(count == 1) {
				if(Dungeon.level.heroFOV[pos]) ((LoreSprite) sprite).back();
				count ++;
				return true;
			} else{
				HP += 3;
				if(HP > HT) HP = HT;
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				count = 0;
				return true;
			}
		}
		if(count > 0) count ++;
		if(count % 3 == 0) count = 0;
		return true;
	}


	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (damage > 0 && count == 0){
			Buff.prolong( enemy, Roots.class, 2f );
			count ++;
		}

		return damage;
	}

	@Override
	public float attackDelay(){
		return 1f;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		boolean can = ((Math.abs(pos/Dungeon.level.width() - enemy.pos/Dungeon.level.width()) <= 3 && Math.abs(pos%Dungeon.level.width() - enemy.pos%Dungeon.level.width()) <= 3) && fieldOfView[pos]) || count > 0;
		return can;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put("moveCount", moveCount);
		bundle.put("count", count);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		moveCount = bundle.getInt("moveCount");
		count = bundle.getInt("count");
	}
		@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10, 25 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 20;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}
}
