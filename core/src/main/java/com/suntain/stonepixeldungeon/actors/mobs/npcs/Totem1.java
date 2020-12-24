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
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.Totem1Sprite;
import com.watabou.utils.Bundle;

public class Totem1 extends Totem {
//회복
	{
		spriteClass = Totem1Sprite.class;
		limited_time = 15;
	}


	@Override
	protected boolean act() {
		if(limited_time > 0) {
			CellEmitter.get(pos).burst(Speck.factory(Speck.HEALING), 1);
			if(Dungeon.level.adjacent( pos, curUser.pos )){
				int sec = curUser.HT > 30 ? 2 : 1;
				if(curUser.HP < curUser.HT) {
					CellEmitter.get(curUser.pos).burst(Speck.factory(Speck.HEALING), 1);
					curUser.HP += sec;
					if(curUser.HP >= curUser.HT) curUser.HP = curUser.HT;
				}

			}
		}
		return super.act();
	}
}