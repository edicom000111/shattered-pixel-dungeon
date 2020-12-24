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

package com.suntain.stonepixeldungeon.actors.mobs;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.effects.*;
import com.suntain.stonepixeldungeon.effects.particles.EnergyParticle;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.NaxxramasWraithSprite;
import com.suntain.stonepixeldungeon.sprites.ThaurissanSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thaurissan extends Mob {

	int enemyPos;
	{
		spriteClass = ThaurissanSprite.class;
		
		HP = HT = 55;
		MP = MT = 1;
		defenseSkill = 18;
		EXP = 10;
		maxLvl = 20;

	}




	@Override
	public float speed() {
		return (5*super.speed())/6;
	}
	@Override
	public boolean act(){
		if(fieldOfView == null) return super.act();
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		boolean justAlerted = alerted;
		alerted = false;

		sprite.hideAlert();

		if (paralysed > 0) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		enemy = chooseEnemy();

		boolean enemyInFOV = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;

		// 적 발견시 몹스폰확률 높이고 마나 소비 후 계속 도망치기
		if(enemyInFOV && MP > 0){
			isWaiting = true;
			MP --;
			Dungeon.level.mobSpawnSpeed = 1f;
			sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
			Sample.INSTANCE.play( Assets.SND_CHALLENGE );
			GLog.n( Messages.get(this, "warning") );

		}
		state = FLEEING;
		//if(MP == 0){
		//	for(Mob mob : Dungeon.level.mobs) if(mob != this) mob.beckon(Dungeon.hero.pos);
		//}
 
		return state.act( enemyInFOV, justAlerted );
	}
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
	}


	@Override
	public void die( Object cause ) {
		Dungeon.level.mobSpawnSpeed = 0f;
		super.die( cause );
		yell( Messages.get(this, "die") );
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
}
