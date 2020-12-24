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
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.mobs.Historian;
import com.suntain.stonepixeldungeon.actors.mobs.Shaman;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class HistorianSprite extends MobSprite {

	private Animation after;
	private int zapPos;
	public HistorianSprite() {
		super();
		
		texture( Assets.HISTORIAN );
		
		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1 );
		
		run = new Animation( 12, true );
		run.frames( frames, 4, 5, 6, 1 );
		
		attack = new Animation( 20, false );
		attack.frames( frames, 10, 11, 12, 13, 14, 15, 16 );

		after = new Animation( 20, false );
		after.frames( frames,   26, 16, 26, 16, 17, 18, 19 );

		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 20, 21, 22, 23, 24, 25);

		play( idle );
	}
	
	public void zap( int pos ) {
		turnTo( ch.pos , pos );
		play( zap );
		zapPos = pos;
		//new PointF( x + width / 2, 0 )
	}


	public void after(){
		play( after );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (animCallback != null) {
			Callback executing = animCallback;
			animCallback = null;
			executing.call();
		} else {

			if (anim == zap) {
				parent.add(new Beam.GreenRay(DungeonTilemap.tileCenterToWorld(ch.pos), DungeonTilemap.tileCenterToWorld(ch.pos % Dungeon.level.width())));
				after();
			} else if(anim == after){
				parent.add(new Beam.GreenRay(DungeonTilemap.tileCenterToWorld(zapPos), DungeonTilemap.tileCenterToWorld(zapPos % Dungeon.level.width())));
				((Historian)ch).onZapComplete();
				idle();
			}

		}
		if (anim == die) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					HistorianSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
		//super.onComplete(anim);
	}

}
