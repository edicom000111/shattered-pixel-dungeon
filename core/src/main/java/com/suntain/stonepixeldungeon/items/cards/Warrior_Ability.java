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
import com.suntain.stonepixeldungeon.actors.buffs.Barrier;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ShieldBuff;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Warrior_Ability extends Card {

	{
		Cost = 2;
		image = ItemSpriteSheet.WARRIOR_ABILITY;
	}

	@Override
	public void active(Char c){
		int SHLD = (int)(c.HT/6f + 5);
		int current = 0;
		for (ShieldBuff s : c.buffs(ShieldBuff.class)){
			current += s.shielding();
		}
		if(c.HT < SHLD + current) SHLD = c.HT - current;
		if(SHLD > 0) {
			Buff.affect(c, Barrier.class).setShield(SHLD);
			if(c instanceof Hero) GLog.w(Messages.get(Warrior_Ability.class, "message"));
			if(Dungeon.level.heroFOV[c.pos]) {
				Sample.INSTANCE.play(Assets.SND_EVOKE);
				CellEmitter.get(c.pos).burst(Speck.factory(Speck.FORGE), 6);
			}
		}
	}

	@Override
	public int price() {
		return 100 * quantity;
	}



}
