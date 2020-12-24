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
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.mobs.Drakonid;
import com.suntain.stonepixeldungeon.actors.mobs.Historian;
import com.suntain.stonepixeldungeon.effects.Beam;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.Callback;

public class DrakonidSprite extends MobSprite {

	private Animation card_idle;
	private Animation card_run;
	private Animation attack_success;
	private Animation attack_fail;
	public DrakonidSprite() {
		super();
		
		texture( Assets.DRAKONID );
		
		TextureFilm frames = new TextureFilm( texture, 14, 18 );

		idle = new Animation( 1, true );
		idle.frames( frames, 0, 0, 1 );
		card_idle = new Animation( 1, true );
		card_idle.frames( frames, 5, 5, 6 );
		
		run = new Animation( 12, true );
		run.frames( frames, 10, 11, 12, 13 );
		card_run = new Animation( 12, true );
		card_run.frames( frames, 15, 16, 17, 18 );

		attack = new Animation( 10, false );
		attack.frames( frames, 20, 21, 22 );
		attack_success = new Animation( 10, false );
		attack_success.frames( frames, 23, 5 );
		attack_fail = new Animation( 10, false );
		attack_fail.frames( frames, 24, 0 );
		zap = new Animation( 10, false );
		zap.frames( frames, 25, 26, 27, 28, 29 );
		
		die = new Animation( 12, false );
		die.frames( frames, 30, 31, 32, 33, 34);

		play( idle );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (animCallback != null) {
			Callback executing = animCallback;
			animCallback = null;
			executing.call();
		} else {

			if (anim == attack) {
				//카드뺏기작업
				if(((Drakonid)ch).steal()) play(attack_success);
				else play(attack_fail);
			} else if(anim == attack_success){
				play(card_idle);
			} else if(anim == attack_fail || anim == zap){
				play(idle);
				if(anim == zap){
					// 카드효과발동
					((Drakonid)ch).zap();
				}
			}

		}
		if (anim == die) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					DrakonidSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
		//super.onComplete(anim);
	}


	@Override
	public void link(Char ch ){
		super.link(ch);
		if(((Drakonid)ch).card != null){
			curAnim = null;
			play(card_idle);
		}
	}


	@Override
	public void move( int from, int to ) {
		turnTo( from , to );
		if(((Drakonid)ch).card == null) play( run );
		else play(card_run);
		motion = new PosTweener( this, worldToCamera( to ), MOVE_INTERVAL );
		motion.listener = this;
		parent.add( motion );

		isMoving = true;

		if (visible && Dungeon.level.water[from] && !ch.flying) {
			GameScene.ripple( from );
		}

	}
}
