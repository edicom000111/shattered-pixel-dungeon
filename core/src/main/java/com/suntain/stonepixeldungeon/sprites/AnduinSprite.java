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
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.mobs.Anduin;
import com.suntain.stonepixeldungeon.actors.mobs.Drakonid;
import com.suntain.stonepixeldungeon.actors.mobs.Malfurion;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class AnduinSprite extends MobSprite {

	Animation card_attack;
	public AnduinSprite() {
		super();
		
		texture( Assets.ANDUIN );
		
		TextureFilm frames = new TextureFilm( texture, 16, 17 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2, 3 );
		
		run = new Animation( 10, true );
		run.frames( frames, 5, 6, 7, 8 );

		attack = new Animation( 10, false );
		attack.frames( frames, 10, 11, 12 );
		card_attack = new Animation( 10, false );
		card_attack.frames( frames, 20, 21, 22, 23, 24 );
		
		die = new Animation( 12, false );
		die.frames( frames, 15, 16, 17, 18, 19 );

		zap = attack.clone();

		play( idle );
	}

	public void card(int pos){
		play(card_attack);
		turnTo(ch.pos, pos);
	}

	@Override
	public void onComplete( Animation anim ) {
		if (animCallback != null) {
			Callback executing = animCallback;
			animCallback = null;
			executing.call();
		} else {
			if (anim == attack) {

				idle();
				//ch.onAttackComplete();

			} else if (anim == operate) {

				idle();
				ch.onOperateComplete();

			} else if(anim == card_attack){
				play(idle);
				((Anduin)ch).card();
			}

		}
		if (anim == die) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					AnduinSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
		//super.onComplete(anim);
	}


	@Override
	public void zap( int pos ) {
		parent.add(new Beam.GreenRay(DungeonTilemap.tileCenterToWorld(pos), DungeonTilemap.tileCenterToWorld(pos % Dungeon.level.width())));
		System.out.println("from "+pos % 32+" to "+pos);
		turnTo( ch.pos , pos );
		play( zap );
		//new PointF( x + width / 2, 0 )
		((Anduin)ch).onZapComplete(pos);
	}


	public void enerzy_zap(Char c ) {
		turnTo( ch.pos , c.pos );
		play( attack );
		//new PointF( x + width / 2, 0 )
		((Anduin)ch).energy_zap(c);
		ch.next();
	}
}
