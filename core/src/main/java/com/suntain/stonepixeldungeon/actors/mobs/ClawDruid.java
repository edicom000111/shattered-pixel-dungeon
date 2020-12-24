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

package com.suntain.stonepixeldungeon.actors.mobs;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.hero.HeroAction;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.Gold;
import com.suntain.stonepixeldungeon.sprites.ClawDruidSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ClawDruid extends Mob {
	private int mode = 0;
	{
		spriteClass = ClawDruidSprite.class;

		HP = HT = 15;
		defenseSkill = 5;
		EXP = 3;
		maxLvl = 8;
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put("mode", mode);
	}
	@Override
	public void notice(){
		/*

				Wound.drain(Dungeon.hero.pos);
				UpWound.drain(Dungeon.hero.pos);
				DownWound.drain(Dungeon.hero.pos);
		 */
		if(mode == 0 && enemy != null && enemy.isAlive()){
			CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.SND_PUFF);
			if(enemy.HP < enemy.HT / 2){
				mode = 1;
				rush = true;
				((ClawDruidSprite)sprite).mode1();
			} else{
				mode = 2;
				HT += 5;
				HP += 5;
				((ClawDruidSprite)sprite).mode2();
			}
		}
		super.notice();
	}


	@Override
	protected boolean act() {
		if(fieldOfView == null || enemy == null || !enemy.isAlive()) return super.act();
		Dungeon.level.updateFieldOfView( this, fieldOfView );
		boolean enemyInFOV = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
		if(Taunt) {
			if (enemy == null || !enemy.isAlive() || canAttack(enemy)) Taunt = false;
			else {
				if(enemy == Dungeon.hero) Dungeon.hero.curAction = new HeroAction.Move( pos );
				else{

				}
				//isWaiting = true;
				//spend(TICK);
				//next();
			}
		}
		if (enemyInFOV && !canAttack(enemy) && mode == 2 && !Taunt && enemy.canGetCloser(pos)) {
			//enemy.TauntedEnemy = this;
			spend(TICK);
			Taunt = true;
			if(enemy == Dungeon.hero) Dungeon.hero.curAction = new HeroAction.Move( pos );
			else{

			}
			sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
			Sample.INSTANCE.play( Assets.SND_CHALLENGE );
			return true;
		}
		return super.act();
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		mode = bundle.getInt("mode");
	}
		@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2 + (mode == 1 ? 3 : 0), 10 + (mode == 1 ? 3 : 0) );
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(2 + (mode == 2 ? 2 : 0), 5 + (mode == 2 ? 6 : 0));
	}
}
