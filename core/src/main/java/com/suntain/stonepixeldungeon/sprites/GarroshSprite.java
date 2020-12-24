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
import com.suntain.stonepixeldungeon.actors.mobs.Garrosh;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GarroshSprite extends MobSprite {

	Animation super_attack;
	public GarroshSprite() {
		super();
		
		texture( Assets.GARROSH );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		idle = new Animation( 20, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 6, 7, 8, 9);
		
		attack = new Animation( 35, false );
		attack.frames( frames, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 );

		super_attack = new Animation( 35, false );
		super_attack.frames( frames, 0, 12, 13, 14, 15, 16, 17, 18, 19, 24, 25, 26, 27, 28, 29, 0 );
		
		die = new Animation( 15, false );
		die.frames( frames, 30, 31, 32, 33, 34, 35 );

		play( idle );
	}

	public void super_attack(int cell){
		turnTo( ch.pos, cell );
		play( super_attack );
	}

	@Override
	public void onComplete( Animation anim ) {
		if(anim == super_attack){
			idle();
			((Garrosh)ch).onAttackComplete();
		}
		else super.onComplete(anim);

	}
}
