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
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.effects.Aura;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.levels.PrisonBossLevel;
import com.suntain.stonepixeldungeon.levels.Terrain;
import com.suntain.stonepixeldungeon.levels.features.Door;
import com.suntain.stonepixeldungeon.sprites.GarroshSprite;
import com.suntain.stonepixeldungeon.sprites.MalfurionSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Garrosh extends DeathKnight {

	{
		HP = HT = 120;
		EXP = 20;
		defenseSkill = 20;
		spriteClass = GarroshSprite.class;

	}

	@Override
	protected boolean doAttack(final Char enemy ) {
		if(Random.Int(0, 2) == 0){
			SuperAttack = false;
			return super.doAttack(enemy);
		} else{
			boolean visible = Dungeon.level.heroFOV[pos];
			SuperAttack = true;
			if (visible) {
				((GarroshSprite)sprite).super_attack(enemy.pos);
				//sprite.attack(enemy.pos);
			} else {
				attack( enemy );
			}

			spend( attackDelay() );

			return !visible;
		}
	}


	@Override
	public int damageRoll() {
		return SuperAttack ? Random.NormalIntRange( 7, 25 ) : Random.NormalIntRange( 3, 15 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 20;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
	}

}
