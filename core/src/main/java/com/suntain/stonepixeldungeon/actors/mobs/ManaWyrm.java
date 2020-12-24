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

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.effects.particles.EnergyParticle;
import com.suntain.stonepixeldungeon.items.potions.PotionOfMana;
import com.suntain.stonepixeldungeon.sprites.ManaWyrmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ManaWyrm extends Mob {
	private boolean counting = false;
	{
		spriteClass = ManaWyrmSprite.class;
		rush = false;
		baseSpeed = 1f;
		HP = HT = 80;
		defenseSkill = 18;
		MT = 10;
		MP = 10;
		EXP = 12;
		maxLvl = 25;

		loot = new PotionOfMana();
		lootChance = 0.2f; //by default, see die()
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
		/*if((state == WANDERING || state == HUNTING) && Level.fieldOfView[pos]) counting = true;
		else counting = false;*/
		boolean start = true;
		if(Dungeon.depth == 5)for(Mob m : Dungeon.level.mobs) {
			if(m instanceof Malfurion && Math.abs(pos/Dungeon.level.width() - m.pos/Dungeon.level.width()) <= 1 && Math.abs(pos%Dungeon.level.width() - m.pos%Dungeon.level.width()) <= 1) start = false;
		}
		if(start) {
			if (justAlerted || ((state == WANDERING || state == HUNTING) && fieldOfView[pos])) counting = true;
			if (counting && enemyInFOV && !canAttack(enemy)) {
				if (MP > 0) {
					MP--;
					if (isAlive() && fieldOfView[enemy.pos] && invisible <= 0)
						CellEmitter.get(pos).burst(Speck.factory(Speck.UP), 1);
					isWaiting = true;
				} else {
					if (isAlive() && fieldOfView[enemy.pos] && invisible <= 0)
						CellEmitter.get(pos).burst(Speck.factory(Speck.RED_LIGHT), 1);
				}
			}
		}

		return state.act( enemyInFOV, justAlerted );
	}
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put("counting", counting);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		counting = bundle.getBoolean("counting");
	}
		@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10 + (10 - MP), 20 + (40 - MP * 4) );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}
}
