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

package com.suntain.stonepixeldungeon.sprites;

import com.suntain.stonepixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TreantSprite extends MobSprite {
	
	public TreantSprite() {
		super();
		
		texture( Assets.TREANT);
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		idle = new Animation( 5, true );
		idle.frames( frames, 0, 1, 0, 2, 0, 3, 0, 4 );
		
		run = new Animation( 20, true );
		run.frames( frames, 0, 7, 8, 9);
		
		attack = new Animation( 30, false );
		attack.frames( frames, 0, 14, 15, 16, 16, 17, 18, 19, 20 );
		
		die = new Animation( 20, false );
		die.frames( frames, 0, 21, 22, 23 );

		play( idle );
	}
}
