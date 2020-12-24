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
import com.suntain.stonepixeldungeon.actors.mobs.Lore;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class LoreSprite extends MobSprite {
	private Animation back;
	private Animation stop;
	public LoreSprite() {
		super();
		
		texture( Assets.Lore );
		
		TextureFilm frames = new TextureFilm( texture, 24, 24);
		idle = new Animation( 1, true );
		idle.frames( frames, 0 );
		
		run = new Animation( 5, true );
		run.frames( frames, 5, 6 );

		attack = new Animation( 12, false );
		attack.frames( frames, 0, 1, 2, 3 );

		stop = new Animation( 1, true );
		stop.frames( frames, 3 );

		back = new Animation( 12, false );
		back.frames( frames, 3, 4 );
		
		die = new Animation( 10, false );
		die.frames( frames, 3, 7, 8, 9, 10, 11, 12 );

		play( idle );
	}

	public void back(){
		//turnTo( ch.pos , pos );
		play( back );
	}

	public void stop(){
		//turnTo( ch.pos , pos );
		play( stop );
	}


	@Override
	public void onComplete( Animation anim ) {
		if (animCallback != null) {
			Callback executing = animCallback;
			animCallback = null;
			executing.call();
		} else {

			if (anim == attack) {

				stop();
				((Lore)ch).onAttackComplete();

			} else if(anim == back){
				idle();
			}

		}
		if (anim == die) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					LoreSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
		//super.onComplete(anim);
	}
}
