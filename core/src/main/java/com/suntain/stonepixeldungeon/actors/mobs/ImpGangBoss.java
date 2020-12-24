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
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Burning;
import com.suntain.stonepixeldungeon.actors.buffs.Corruption;
import com.suntain.stonepixeldungeon.actors.buffs.Poison;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Ghost;
import com.suntain.stonepixeldungeon.effects.Pushing;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.potions.PotionOfHealing;
import com.suntain.stonepixeldungeon.levels.Terrain;
import com.suntain.stonepixeldungeon.levels.features.Door;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.ImpGangBossSprite;
import com.suntain.stonepixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ImpGangBoss extends Mob {

	{
		spriteClass = ImpGangBossSprite.class;
		
		HP = HT = 25;
		defenseSkill = 5;

		EXP = 5;

		properties.add(Property.MINIBOSS);
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<>();
			boolean[] solid = Dungeon.level.solid;
			
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!solid[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}
	
			if (candidates.size() > 0) {
				
				ImpServant imp = new ImpServant();
				imp.HP = imp.HT;
				imp.pos = Random.element( candidates );
				imp.state = imp.HUNTING;
				
				if (Dungeon.level.map[imp.pos] == Terrain.DOOR) {
					Door.enter( imp.pos );
				}
				
				GameScene.add( imp, SPLIT_DELAY );
				Actor.addDelayed( new Pushing( imp, pos, imp.pos ), -1 );
			}
		}
		
		return super.defenseProc(enemy, damage);
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Ghost.Quest.process();
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}
}
