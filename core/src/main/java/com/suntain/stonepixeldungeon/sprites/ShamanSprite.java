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
import com.suntain.stonepixeldungeon.actors.mobs.Shaman;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;

public class ShamanSprite extends MobSprite {
	
	public ShamanSprite() {
		super();
		
		texture( Assets.SHAMAN );
		
		TextureFilm frames = new TextureFilm( texture, 24, 26 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1, 2, 3 );
		
		run = new Animation( 12, true );
		run.frames( frames, 4, 5, 6 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 7, 8, 9, 10 );
		
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 11, 12, 13, 14 );

		play( idle );
	}
	
	public void zap( int pos ) {
		turnTo( ch.pos , pos );
		play( zap );
		//new PointF( x + width / 2, 0 )
		parent.add(new Beam.GreenRay(DungeonTilemap.tileCenterToWorld(pos), DungeonTilemap.tileCenterToWorld(pos % Dungeon.level.width())));
		((Shaman)ch).onZapComplete();






		//parent.add( new Lightning( pos % 32, pos, (Shaman)ch ) );
		/*
		MagicMissile.boltFromChar(curUser.sprite.parent, MagicMissile.FORCE, curUser.sprite, cell,
				new Callback() {
					@Override
					public void call() {
						use(cell, true);
					}
				});*/
	}

}
