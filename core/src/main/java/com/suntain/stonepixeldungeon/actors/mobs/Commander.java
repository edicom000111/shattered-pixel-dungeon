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
import com.suntain.stonepixeldungeon.sprites.CommanderSprite;
import com.watabou.utils.Random;

public class Commander extends Mob {

	{
		spriteClass = CommanderSprite.class;
		
		HP = HT = 15;
		defenseSkill = 10;

		EXP = 6;
		maxLvl = 14;

		BUB = true;
		rush = true;

	}

	@Override
	public float speed() {
		return (5*super.speed())/6;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 16 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 15;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}
}
