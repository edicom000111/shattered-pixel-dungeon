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
import com.suntain.stonepixeldungeon.actors.hero.Belongings;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.bags.Deck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
/*
public class Laza extends Card {

	{
		Cost = 5;
		image = ItemSpriteSheet.LAZA;
		curUser = Dungeon.hero;
		disposable = true;
	}

	@Override
	public void active(Char c) {
		if(c instanceof Hero) GLog.w(Messages.get(Laza.class, "message"));
		Sample.INSTANCE.play( Assets.SND_MASTERY );
		curUser.sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );

		Belongings stuff = Dungeon.hero.belongings;
		for(Item item : stuff.backpack.items.toArray(new Item[0])) {
			if (item instanceof Warrior_Ability || item instanceof Shaman_Ability || item instanceof Rogue_Ability || item instanceof Hunter_Ability) {
				((Card)item).Cost = 0;
			}
			if (item instanceof Deck){
				for(Item i : stuff.getItem( Deck.class ).items) {
					if (item instanceof Warrior_Ability || item instanceof Shaman_Ability || item instanceof Rogue_Ability || item instanceof Hunter_Ability) {
						((Card) i).Cost = 0;
					}
				}
			}
		}
	}

	@Override
	public int price() {
		return used == true ? 30 : 100;
	}


	@Override
	public String info() {
		return used == true ? Messages.get(this, "desc2") : Messages.get(this, "desc1");
	}

}
*/