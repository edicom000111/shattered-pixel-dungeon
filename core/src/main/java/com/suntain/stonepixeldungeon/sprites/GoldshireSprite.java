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

public class GoldshireSprite extends MobSprite {

	public GoldshireSprite() {
		super();

		//12*16 1~2 평소 3~8 이동 9~11 공격 11~完 죽음
		texture( Assets.Goldshire );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1 );

		run = new Animation( 10, true );
		run.frames( frames, 2, 3, 4, 5, 6, 7 );

		attack = new Animation( 15, false );
		attack.frames( frames, 8, 9, 10 );

		die = new Animation( 10, false );
		die.frames( frames, 10, 11, 12, 13, 14 );

		/*
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		idle = new Animation( 2, true );
		idle.frames( frames,1, 4 );
		
		run = new Animation( 10, true );
		run.frames( frames, 2, 3, 4 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 0, 6, 7 );
		
		die = new Animation( 10, false );
		die.frames( frames, 8, 9, 10 );*/

		play( idle );
	}
}
