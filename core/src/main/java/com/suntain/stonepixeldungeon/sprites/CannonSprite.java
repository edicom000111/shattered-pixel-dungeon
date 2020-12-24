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
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.mobs.Cannon;
import com.suntain.stonepixeldungeon.actors.mobs.Eye;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.effects.MagicMissile;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class CannonSprite extends MobSprite {

	private int zapPos;

	private Animation charging;
	private Emitter chargeParticles;

	public CannonSprite() {
		super();
		
		texture( Assets.CANNON );
		
		TextureFilm frames = new TextureFilm( texture, 22, 18 );
		
		idle = new Animation( 4, true );
		idle.frames( frames, 0, 0, 0, 1 );

		charging = new Animation( 12, true);
		charging.frames( frames, 2, 3, 4 );

		chargeParticles = centerEmitter();
		chargeParticles.autoKill = false;
		chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
		chargeParticles.on = false;
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 5, 6, 7 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 10, 11, 12, 13, 14);
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 0, 15, 16, 17, 18, 19 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (((Cannon)ch).beamCharged) play(charging);
	}

	@Override
	public void update() {
		super.update();
		chargeParticles.pos(center());
		chargeParticles.visible = visible;
	}

	public void charge( int pos ){
		turnTo(ch.pos, pos);
		play(charging);
	}

	@Override
	public void play(Animation anim) {
		chargeParticles.on = anim == charging;
		super.play(anim);
	}

	@Override
	public void zap( int pos ) {
		zapPos = pos;
		super.zap( pos );
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		
		if (anim == zap) {
			/*if (Actor.findChar(zapPos) != null){
				parent.add(new Beam.DeathRay(center(), Actor.findChar(zapPos).sprite.center()));
			} else {
				parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(zapPos)));
			}*/
			((Cannon)ch).deathGaze();
			ch.next();
		} else if (anim == die){
			chargeParticles.killAndErase();
		}
	}
}
