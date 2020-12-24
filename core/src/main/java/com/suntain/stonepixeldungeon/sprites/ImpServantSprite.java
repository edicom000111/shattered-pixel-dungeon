/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, 30 + either version 3 of the License, 30 + or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, 30 + see <http://www.gnu.org/licenses/>
 */

package com.suntain.stonepixeldungeon.sprites;

import com.suntain.stonepixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ImpServantSprite extends MobSprite {

	public ImpServantSprite() {
		super();
		
		texture( Assets.IMP );
		
		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames,
			30 + 0, 30 + 1, 30 + 2, 30 + 3, 30 + 0, 30 + 1, 30 + 2, 30 + 3, 30 + 0, 30 + 0, 30 + 0, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4,
			30 + 0, 30 + 1, 30 + 2, 30 + 3, 30 + 0, 30 + 1, 30 + 2, 30 + 3, 30 + 0, 30 + 1, 30 + 3, 30 + 0, 30 + 0, 30 + 0, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 0, 30 + 0, 30 + 0, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4, 30 + 4 );
		
		run = new Animation( 8, true );
		run.frames( frames, 30 + 10, 30 + 11, 30 + 10, 30 + 12 );

		attack = new Animation( 15, false );
		attack.frames( frames, 30 + 0, 30 + 5, 30 + 6, 30 + 7, 30 + 8, 30 + 9 );

		die = new Animation( 2, false );
		die.frames( frames, 30 + 0, 30 + 13, 30 + 14 );
		
		play( idle );
	}
}
