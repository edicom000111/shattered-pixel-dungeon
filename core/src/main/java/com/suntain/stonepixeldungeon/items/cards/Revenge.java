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

package com.suntain.stonepixeldungeon.items.cards;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Invisibility;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Revenge extends Card {

	{
		Cost = 2;
		image = ItemSpriteSheet.BLADE_FLURRY;
		curUser = Dungeon.hero;
	}

	@Override
	public void active(Char c) {
		if(c instanceof Hero) GLog.w(Messages.get(Revenge.class, "message"));
		if(Dungeon.level.heroFOV[c.pos]) {
			CellEmitter.get(c.pos).burst(Speck.factory(Speck.STEAM), 10);
			Sample.INSTANCE.play(Assets.SND_BROKEN_WEAPON);
		}
		Invisibility.dispel();
		Object[] mobs = Dungeon.level.mobs.toArray();
		if(c instanceof Hero) {
			for (int i = 0; i < mobs.length; i++)
				if (c.fieldOfView[((Mob) mobs[i]).pos]) {
					//((Mob)mobs[i]).enemySeen = false;
					c.attack(((Mob) mobs[i]));
					if (c.HP < c.HT / 3) {
						c.attack(((Mob) mobs[i]));
					}
				}
		}else if(c.alignment == Char.Alignment.ENEMY){
			for (int i = 0; i < mobs.length; i++) if(((Mob)mobs[i]).alignment == Char.Alignment.ALLY && c.fieldOfView[((Mob) mobs[i]).pos]){
				c.attack(((Mob) mobs[i]));
				if (c.HP < c.HT / 3) {
					c.attack(((Mob) mobs[i]));
				}
			}
			if(c.fieldOfView[Dungeon.hero.pos]){
				c.attack(Dungeon.hero);
				if (c.HP < c.HT / 3) {
					c.attack(Dungeon.hero);
				}
			}
		}
	}


	@Override
	public int price() {
		return 100 * quantity;
	}



}
