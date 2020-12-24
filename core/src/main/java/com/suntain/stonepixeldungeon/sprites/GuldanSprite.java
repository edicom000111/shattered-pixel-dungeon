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
import com.suntain.stonepixeldungeon.actors.mobs.Guldan;
import com.suntain.stonepixeldungeon.actors.mobs.Malfurion;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;

public class GuldanSprite extends MobSprite {

	public GuldanSprite() {
		super();
		
		texture( Assets.GULDAN );
		
		TextureFilm frames = new TextureFilm( texture, 16, 17 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 );
		
		run = new Animation( 15, true );
		run.frames( frames, 3, 4, 5, 6, 7, 8 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 9, 10, 11 );
		
		die = new Animation( 8, false );
		die.frames( frames, 12, 13, 14, 15 );

		zap = new Animation( 20, false );
		zap.frames( frames, 0, 16, 17, 18, 19, 20, 21, 22, 23, 24, 0);
		
		play( idle );
	}


	@Override
	public void zap( int pos ) {
		turnTo( ch.pos , pos );
		play( zap );
		//new PointF( x + width / 2, 0 )
		((Guldan)ch).onZapComplete(pos);
	}
}
