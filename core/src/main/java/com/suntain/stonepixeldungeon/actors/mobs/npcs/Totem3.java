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

package com.suntain.stonepixeldungeon.actors.mobs.npcs;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.Totem3Sprite;
import com.watabou.utils.Bundle;

public class Totem3 extends Totem {
//주문공격력

	{
		spriteClass = Totem3Sprite.class;
		limited_time = 20;
		count = 0;
	}

	@Override
	protected boolean act() {
		if(limited_time > 0) {
			if(distance(curUser) < 5) {
				//if(isAlive() && Dungeon.level.heroFOV[pos] && invisible <= 0) CellEmitter.get(pos).burst(Speck.factory(Speck.UP), 1);
				Buff b = curUser.buff(ExtraDamage.class);
				if(b == null) {
					b = new ExtraDamage();
					curUser.add(b);
					b.target = curUser;
					((ExtraDamage)b).attach(2, 2);
				}
				else {
					if(count == 0) count = ((ExtraDamage) b).extraDamage;
					((ExtraDamage) b).attach(-1, count + 2);
				}
			} else{
				Buff b = curUser.buff(ExtraDamage.class);
				if(b != null) {
					((ExtraDamage) b).extraDamage -= 2;
					count = 0;
				}

			}
		}
		return super.act();
	}
}