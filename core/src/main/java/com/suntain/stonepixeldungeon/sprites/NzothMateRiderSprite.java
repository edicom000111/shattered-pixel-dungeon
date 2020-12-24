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
import com.watabou.noosa.TextureFilm;

public class NzothMateRiderSprite extends MobSprite {

	public NzothMateRiderSprite() {
		super();
		
		texture( Assets.NZOTH_MATE_RIDER );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 0, 1, 1, 0, 2, 2 );
		
		run = new Animation( 15, true );
		run.frames( frames, 0, 1, 1, 0, 2, 2 );
		
		attack = new Animation( 25, false );
		attack.frames( frames, 0,6,7,8,9,10,11 );
		
		die = new Animation( 12, false );
		die.frames( frames, 3, 4, 4, 3, 5, 5);

		play( idle );
	}

}
