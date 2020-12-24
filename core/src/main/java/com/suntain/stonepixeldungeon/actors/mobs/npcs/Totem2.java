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
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.Totem2Sprite;
import com.watabou.utils.Bundle;

public class Totem2 extends Totem {
//도발

	{
		HP = 1000000;
		spriteClass = Totem2Sprite.class;
		limited_time = 20;
		count = 15;
	}


	@Override
	protected boolean act() {
		if(limited_time > 0) {
			for (Mob mob : Dungeon.level.mobs) {
				if((curUser.alignment == Alignment.ALLY && mob.alignment == Alignment.ENEMY) || mob.alignment == Alignment.ALLY && curUser.alignment == Alignment.ENEMY)
				if(distance(mob) < 5 && mob.enemy != this){
					mob.beckon( this.pos );
					mob.aggro(this);
					if(isAlive() && Dungeon.level.heroFOV[pos] && invisible <= 0) CellEmitter.get(pos).burst(Speck.factory(Speck.RED_LIGHT), 1);
				}
			}
		}
		return super.act();
	}
	@Override
	public void damage( int dmg, Object src ) {
		count --;
		if(count <= 0) die(src);
	}
}