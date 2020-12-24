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
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.effects.Wound;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Inner_Rage extends Card {

	{
		Cost = 0;
		image = ItemSpriteSheet.INNER_RAGE;
		curUser = Dungeon.hero;
	}

	@Override
	public void active(Char c) {
		if(c.HP > c.HT / 2){
			//curUser.damage(curUser.HP - 1, this);
			c.HP /= 2;
			if(Dungeon.level.heroFOV[c.pos]) {
				Sample.INSTANCE.play(Assets.SND_HIT);
				Wound.hit(c.pos);
			}
			if(c instanceof Hero) GLog.w(Messages.get(Inner_Rage.class, "message"));
			Buff b = Dungeon.hero.buff(ExtraDamage.class);
			if(b == null) {
				b = new ExtraDamage();
				c.add(b);
				b.target = c;
			}
			((ExtraDamage)b).attach(40, 15);
		} else{
			if(c instanceof Hero) GLog.w(Messages.get(Inner_Rage.class, "failed"));
			c.MP += Cost;
			used ++;
		}
	}


	@Override
	public int price() {
		return 100 * quantity;
	}



}
