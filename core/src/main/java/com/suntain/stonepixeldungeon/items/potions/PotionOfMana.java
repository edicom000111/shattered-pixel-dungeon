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

package com.suntain.stonepixeldungeon.items.potions;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.utils.GLog;

public class PotionOfMana extends Potion {

	{
		initials = 13;

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
		GLog.p( Messages.get(this, "message") );
	}
	
	public static void heal( Hero hero ) {
		
		hero.MP += 5;
		if(hero.MP > hero.MT) hero.MP = hero.MT;
		
		hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.4f, 4 );
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
