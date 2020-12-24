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
import com.suntain.stonepixeldungeon.Statistics;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Quest;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Ghost;
import com.suntain.stonepixeldungeon.sprites.MurlocSprite;
import com.watabou.utils.Random;



public class Murloc extends Mob {


	{
		spriteClass = MurlocSprite.class;

		HP = HT = 20;
		defenseSkill = 5;

		EXP = 4;

		properties.add(Property.MINIBOSS);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 10;
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );
		Ghost.Quest.process();
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}
}

/*
public class Murloc extends Mob {

	{
		spriteClass = MurlocSprite.class;

		EXP = 0;
	}

	public Murloc(){
		super();
		HP = HT = 5 + Statistics.deepestFloor * 3;
		defenseSkill = 10 + Statistics.deepestFloor;
	}
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( Dungeon.level.specialTarget, 4 + Dungeon.level.specialTarget );
	}

	@Override
	public int attackSkill( Char target ) {
		return 15 + Dungeon.level.specialTarget;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.level.specialTarget / 2);
	}
	@Override
	public void die( Object cause ) {
		Quest b = Dungeon.hero.buff(Quest.class);
		if(b != null) {
			b.kill++;
			b.check();
		}
		super.die(cause);

	}
}
*/