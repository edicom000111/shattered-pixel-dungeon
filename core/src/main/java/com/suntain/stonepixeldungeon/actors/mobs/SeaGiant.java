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
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Ghost;
import com.suntain.stonepixeldungeon.sprites.MurlocSprite;
import com.suntain.stonepixeldungeon.sprites.SeaGiantSprite;
import com.watabou.utils.Random;


public class SeaGiant extends Mob {


	{
		spriteClass = SeaGiantSprite.class;

		HP = HT = 30;
		defenseSkill = 7;

		EXP = 6;

		properties.add(Property.MINIBOSS);
		SuperAttack = true;
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 8 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );
		Ghost.Quest.process();
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}
}