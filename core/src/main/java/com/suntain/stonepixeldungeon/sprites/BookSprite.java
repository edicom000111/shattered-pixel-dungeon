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

package com.suntain.stonepixeldungeon.sprites;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.effects.Splash;
import com.watabou.noosa.TextureFilm;

public class BookSprite extends MobSprite {

	public BookSprite() {
		super();
		
		texture( Assets.BOOK );
		
		TextureFilm frames = new TextureFilm( texture, 17, 17 );
		
		idle = new Animation( 15, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 3, 3, 1 );
		
		run = new Animation( 16, true );
		run.frames( frames, 22, 23, 24, 25, 26, 27, 28, 29, 22, 23, 24, 25, 26, 27, 28, 29, 32, 33, 34, 35, 36, 37, 38, 39 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 10, 11, 12, 13, 14, 15, 15, 15, 15, 16, 17, 18, 19, 20, 21 );
		
		die = new Animation( 30, false );
		die.frames( frames, 40, 41, 42, 43, 44, 45, 46, 46, 46, 46, 46, 46, 46, 46, 50, 51, 52, 53, 54, 55, 56, 56, 56, 56, 56, 56, 56, 56, 57, 58, 59, 47, 48, 49);
		
		play( idle );
	}

}
