/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.suntain.stonepixeldungeon.plants;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.items.food.Blandfruit;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;

public class BlandfruitBush extends Plant {

	{
		image = 12;
	}

	@Override
	public void activate( Char ch ) {
		Dungeon.level.drop( new Blandfruit(), pos ).sprite.drop();
	}

	//This seed no longer drops, but has a sprite as it did drop prior to 0.7.0
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_FADELEAF;

			plantClass = BlandfruitBush.class;
		}

	}
}
