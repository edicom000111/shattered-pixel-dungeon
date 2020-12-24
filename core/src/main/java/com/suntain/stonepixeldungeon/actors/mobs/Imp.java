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

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.items.Heap;
import com.suntain.stonepixeldungeon.items.bombs.Bomb;
import com.suntain.stonepixeldungeon.items.food.MysteryMeat;
import com.suntain.stonepixeldungeon.levels.Level;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.ImpSprite;
import com.suntain.stonepixeldungeon.sprites.VoidWalkerSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Imp extends Mob {

	{
		spriteClass = ImpSprite.class;

		HP = HT = 10;
		defenseSkill = 2;

		EXP = 2;
		maxLvl = 8;

		//loot = new Bomb();
		//lootChance = 0.1f;
		// 죽였을때 드랍인지 불붙은건지 이상한 상황이 나옴.
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 9 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 10;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}


	@Override
	public int defenseProc(Char enemy, int damage) {
		if (Random.Int(0, 12) == 0 && state != FLEEING && Dungeon.hero.lvl <= this.maxLvl && HP >= damage + 2) {
			final Imp imp = this;
			Bomb b = new Bomb(){
				public void explode(int cell) {
					super.explode(cell);
					imp.state = WANDERING;
				}
			};
			Actor.addDelayed(b.fuse = new Bomb.Fuse().ignite(b), 2);
			Dungeon.level.drop(b, pos).sprite.drop( pos );
			state = FLEEING;
			GLog.w( Messages.get(this, "bomb") );
			isWaiting = true;
			//GameScene.updateMap(pos);
		}
		return super.defenseProc(enemy, damage);
	}

}
