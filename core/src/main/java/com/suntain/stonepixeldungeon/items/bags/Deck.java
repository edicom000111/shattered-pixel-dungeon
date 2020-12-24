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

package com.suntain.stonepixeldungeon.items.bags;

import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.cards.Card;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;

public class Deck extends Bag {

	{
		image = ItemSpriteSheet.DECK;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Card;
	}
	
	@Override
	public int price() {
		return 30;
	}

}
