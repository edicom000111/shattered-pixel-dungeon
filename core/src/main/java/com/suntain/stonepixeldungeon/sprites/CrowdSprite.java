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
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Crowd;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class CrowdSprite extends MobSprite {

	public CrowdSprite() {
		super();

		texture( Assets.CROWD );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		idle = new Animation(Random.Int(12, 16), true);
		switch(Random.Int(0, 6)) {
			case 0:
				idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3);
				die = new Animation( 20, false );
				die.frames( frames, 0 );
				break;
			case 1:
				idle.frames(frames, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 6, 7);
				die = new Animation( 20, false );
				die.frames( frames, 4 );
				break;
			case 2:
				idle.frames(frames, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 10, 11);
				die = new Animation( 20, false );
				die.frames( frames, 8 );
				break;
			case 3:
				idle.frames(frames, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 14, 15);
				die = new Animation( 20, false );
				die.frames( frames, 12 );
				break;
			case 4:
				idle.frames(frames, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 19);
				die = new Animation( 20, false );
				die.frames( frames, 16 );
				break;
			case 5:
				idle.frames(frames, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 22, 23 );
				die = new Animation( 20, false );
				die.frames( frames, 20 );
				break;
		}
		run = idle.clone();
		attack = idle.clone();
		play( idle );
		curFrame = Random.Int( curAnim.frames.length );

	}
}
