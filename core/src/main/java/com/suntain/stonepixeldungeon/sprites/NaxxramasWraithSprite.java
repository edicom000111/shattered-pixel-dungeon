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
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.effects.particles.ShaftParticle;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.TextureFilm;

public class NaxxramasWraithSprite extends MobSprite {

	public NaxxramasWraithSprite() {
		super();
		
		texture( Assets.NAXXRAMAS_WRAITH );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2, 3 );
		
		run = new Animation( 10, true );
		run.frames( frames, 9, 10, 11, 12 );

		attack = new Animation( 10, false );
		attack.frames( frames, 0, 18, 19 );

		die = new Animation( 8, false );
		die.frames( frames, 0, 27, 28, 29, 30);
		
		play( idle );
	}
	
	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}
	
	@Override
	public void die() {
		super.die();
		//emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
		//emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
	}
	
	@Override
	public int blood() {
		return 0xFFFFFF;
	}
}
