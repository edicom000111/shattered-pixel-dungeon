/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, 15 + either version 3 of the License, 15 + or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, 15 + see <http://www.gnu.org/licenses/>
 */

package com.suntain.stonepixeldungeon.sprites;

import com.suntain.stonepixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ImpGangBossSprite extends MobSprite {

	public ImpGangBossSprite() {
		super();
		
		texture( Assets.IMP );
		
		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames,
			15 + 0, 15 + 1, 15 + 2, 15 + 3, 15 + 0, 15 + 1, 15 + 2, 15 + 3, 15 + 0, 15 + 0, 15 + 0, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4,
			15 + 0, 15 + 1, 15 + 2, 15 + 3, 15 + 0, 15 + 1, 15 + 2, 15 + 3, 15 + 0, 15 + 1, 15 + 3, 15 + 0, 15 + 0, 15 + 0, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 0, 15 + 0, 15 + 0, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4, 15 + 4 );
		
		run = new Animation( 8, true );
		run.frames( frames, 15 + 10, 15 + 11, 15 + 10, 15 + 12 );

		attack = new Animation( 15, false );
		attack.frames( frames, 15 + 0, 15 + 5, 15 + 6, 15 + 7, 15 + 8, 15 + 9 );

		die = new Animation( 2, false );
		die.frames( frames, 15 + 0, 15 + 13, 15 + 14 );
		
		play( idle );
	}
}
