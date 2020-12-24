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
import com.suntain.stonepixeldungeon.actors.mobs.GnollTrickster;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;

public class GnollTricksterSprite extends MobSprite {

	//private Emitter spray;

	public GnollTricksterSprite() {
		super();

		texture( Assets.GNOLL );


		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 2, true );
		idle.frames(frames, 13);

		run = new Animation( 12, true );
		run.frames( frames, 22, 23 );

		attack = new Animation( 12, false );
		attack.frames( frames, 18,14, 15 );

		zap = attack.clone();

		die = new Animation( 12, false );
		die.frames( frames, 16, 17, 12, 19, 20, 21 );
/*
		spray = centerEmitter();
		spray.autoKill = false;
		spray.pour( GooParticle.FACTORY, 0.04f );
		spray.on = false;*/

		play( idle );
	}

	@Override
	public void zap( int pos ) {
		parent.add(new Beam.GreenRay(DungeonTilemap.tileCenterToWorld(pos), DungeonTilemap.tileCenterToWorld(pos % Dungeon.level.width())));
		System.out.println("from "+pos % 32+" to "+pos);
		turnTo( ch.pos , pos );
		play( zap );
		//new PointF( x + width / 2, 0 )
		((GnollTrickster)ch).onZapComplete(pos);
	}



	/*
	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent(cell, ch.pos)) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( ch.pos, cell, new CurareDart(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					} );

			play( cast );
			turnTo( ch.pos , cell );

		} else {

			super.attack( cell );

		}
	}*/
/*
	public static class GooParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((GooParticle)emitter.recycle( GooParticle.class )).reset( x, y );
			}
		};

		public GooParticle() {
			super();

			color( 0x000000 );
			lifespan = 0.3f;

			acc.set( 0, +50 );
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;

			size = 4;
			speed.polar( -Random.Float( PointF.PI ), Random.Float( 32, 48 ) );
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.5f ? (1 - p) * 2f : 1;
		}
	}*/
}





