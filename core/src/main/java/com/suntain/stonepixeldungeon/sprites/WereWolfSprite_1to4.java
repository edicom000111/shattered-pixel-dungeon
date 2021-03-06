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
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WereWolfSprite_1to4 extends MobSprite {

	public WereWolfSprite_1to4() {
		super();

		texture( Assets.WereWolf );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new MovieClip.Animation( 5, true );
		idle.frames( frames, 0, 1, 2, 3, 4 );

		run = new MovieClip.Animation( 10, true );
		run.frames( frames, 5, 6, 7, 8, 7, 6, 5 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 9, 10, 11 );

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 12, 13, 14, 15, 16, 17 );

		play( idle );
	}

	@Override
	public int blood() {
		return 0xFFFFEA80;
	}
}
