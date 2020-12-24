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

public class ClawDruidSprite extends MobSprite {

	protected Animation mode1_idle;
	protected Animation mode1_run;
	protected Animation mode1_attack;
	protected Animation mode1_die;
	protected Animation mode2_idle;
	protected Animation mode2_run;
	protected Animation mode2_attack;
	protected Animation mode2_die;

	public ClawDruidSprite() {
		super();
		
		texture( Assets.Cat );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		idle = new Animation( 2, true );
		idle.frames( frames,1, 4 );

		run = new Animation( 10, true );
		run.frames( frames, 2, 3, 4 );

		attack = new Animation( 15, false );
		attack.frames( frames, 0, 6, 7 );

		die = new Animation( 10, false );
		die.frames( frames, 8, 9, 10 );

		mode1_idle = new Animation( 2, true );
		mode1_idle.frames( frames,1, 4 );

		mode1_run = new Animation( 10, true );
		mode1_run.frames( frames, 2, 3, 4 );

		mode1_attack = new Animation( 15, false );
		mode1_attack.frames( frames, 0, 6, 7 );

		mode1_die = new Animation( 10, false );
		mode1_die.frames( frames, 8, 9, 10 );

		mode2_idle = new Animation( 2, true );
		mode2_idle.frames( frames,1, 4 );

		mode2_run = new Animation( 10, true );
		mode2_run.frames( frames, 2, 3, 4 );

		mode2_attack = new Animation( 15, false );
		mode2_attack.frames( frames, 0, 6, 7 );

		mode2_die = new Animation( 10, false );
		mode2_die.frames( frames, 8, 9, 10 );

		play( idle );
	}

	public void mode1(){
		idle = mode1_idle;
		run = mode1_run;
		attack = mode1_attack;
		die = mode1_die;
	}

	public void mode2(){
		idle = mode2_idle;
		run = mode2_run;
		attack = mode2_attack;
		die = mode2_die;
	}
}
