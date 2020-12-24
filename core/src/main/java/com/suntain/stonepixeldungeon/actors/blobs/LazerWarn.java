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

package com.suntain.stonepixeldungeon.actors.blobs;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.effects.BlobEmitter;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class LazerWarn extends Blob {

	{
		actPriority = MOB_PRIO - 1;
	}
	@Override
	protected void evolve() {

		int cell;
		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j* Dungeon.level.width();
				off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

				if (off[cell] > 0) {
					volume += off[cell];
				}
			}
		}

	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		//emitter.start( LazerParticle.FACTORY, 0.03f, 0 );
		emitter.pour(new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((LazerParticle)emitter.recycle( LazerParticle.class )).reset( x, y );
			}
		}, 0.03f );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	public static class LazerParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((LazerParticle)emitter.recycle( LazerParticle.class )).reset( x, y );
			}
		};

		public LazerParticle() {
			super();

			color( 0xb3fa8d );
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
			//am = p > 0.5f ? (1 - p) * 2f : 1;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}
}
