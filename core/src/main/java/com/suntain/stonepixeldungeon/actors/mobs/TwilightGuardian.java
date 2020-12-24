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
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Bleeding;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.actors.hero.HeroAction;
import com.suntain.stonepixeldungeon.effects.*;
import com.suntain.stonepixeldungeon.sprites.ClawDruidSprite;
import com.suntain.stonepixeldungeon.sprites.TwilightGuardianSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class TwilightGuardian extends Mob {
	private int mode = 0;
	{
		spriteClass = TwilightGuardianSprite.class;

		HP = HT = 80;
		defenseSkill = 17;
		EXP = 11;
		maxLvl = 21;
	}




	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );


		Buff b = enemy.buff(Bleeding.class);
		if (Random.Int( 2 ) == 0 && b == null) {
			Buff.affect( enemy, Bleeding.class ).set( damage );
		}

		Wound.hit(enemy);
		DownWound.hit(enemy);
		UpWound.hit(enemy);
		return damage;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 25 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 28;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 15);
	}

}
