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
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Cripple;
import com.suntain.stonepixeldungeon.effects.Chains;
import com.suntain.stonepixeldungeon.effects.Pushing;
import com.suntain.stonepixeldungeon.mechanics.Ballistica;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.ManaCycloneSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ManaCyclone extends Mob {

	public boolean teleport = false;
	{
		spriteClass = ManaCycloneSprite.class;
		
		HP = HT = 120;
		defenseSkill = 23;
		baseSpeed = 1f;
		EXP = 15;
		maxLvl = 28;

		HUNTING = new Hunting();
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10 + teleport, 30 + teleport * 2 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 33;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}

	@Override
	public boolean canAttack(Char enemy){
		return super.canAttack(enemy);
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		boolean visible = Dungeon.level.heroFOV[pos];
		/*
		거리 멀고, 빈 공간이 있으면 쭁
		
		 */
		if (visible) {
			sprite.attack( enemy.pos );
		} else {
			attack( enemy );
		}

		spend( attackDelay() );


		return !visible;
	}


	private boolean teleport(int target){
		if (teleport || properties().contains(Property.IMMOVABLE))
			return false;

		Ballistica route = new Ballistica(pos, target, Ballistica.PROJECTILE);
		int cell = route.path.get(route.path.size()-2);
		if(!Dungeon.level.avoid[ cell ]){

		}
		if (chain.collisionPos != enemy.pos
				|| chain.path.size() < 2
				|| Dungeon.level.pit[chain.path.get(1)])
			return false;
		else {
			int newPos = -1;
			for (int i : chain.subPath(1, chain.dist)){
				if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
					newPos = i;
					break;
				}
			}

			if (newPos == -1){
				return false;
			} else {
				final int newPosFinal = newPos;
				this.target = newPos;
				yell( Messages.get(this, "scorpion") );
				sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback() {
					public void call() {
						Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback(){
							public void call() {
								enemy.pos = newPosFinal;
								Dungeon.level.press(newPosFinal, enemy, true);
								Cripple.prolong(enemy, Cripple.class, 4f);
								if (enemy == Dungeon.hero) {
									Dungeon.hero.interrupt();
									Dungeon.observe();
									GameScene.updateFog();
								}
							}
						}), -1);
						next();
					}
				}));
			}
		}
		chainsUsed = true;
		return true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("teleport", teleport);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		teleport = bundle.getBoolean("teleport");
		super.restoreFromBundle( bundle );

	}
	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if (!teleport
					&& enemyInFOV
					&& !isCharmedBy( enemy )
					&& !canAttack( enemy )
					&& Dungeon.level.distance( pos, enemy.pos ) < 5
					&& Random.Int(3) == 0

					&& teleport(enemy.pos)){
				return false;
			} else {
				return super.act( enemyInFOV, justAlerted );
			}

		}
	}
}
