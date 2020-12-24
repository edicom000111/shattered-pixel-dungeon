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
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class FetidRatSprite extends MobSprite {
	
	private Emitter cloud;

	public FetidRatSprite() {
		super();

		texture( Assets.TREANT); // 사진 TREANT 이용

		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		idle = new Animation( 2, true );
		idle.frames( frames, 22, 23 );

		run = new Animation( 10, true );
		run.frames( frames, 28, 32);

		attack = new Animation( 15, false );
		attack.frames( frames, 25, 27, 24 );

		die = new Animation( 10, false );
		die.frames( frames, 29, 30, 31 );

		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		
		if (cloud == null) {
			cloud = emitter();
			cloud.pour( Speck.factory( Speck.STENCH ), 0.7f );
		}
	}
	
	@Override
	public void update() {
		
		super.update();
		
		if (cloud != null) {
			cloud.visible = visible;
		}
	}
	
	@Override
	public void die() {
		super.die();
		
		if (cloud != null) {
			cloud.on = false;
		}
	}
}