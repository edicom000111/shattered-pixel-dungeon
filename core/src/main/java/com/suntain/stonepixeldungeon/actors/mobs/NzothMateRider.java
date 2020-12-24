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
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.effects.Pushing;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.NzothMateRiderSprite;
import com.suntain.stonepixeldungeon.sprites.TreantSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class NzothMateRider extends Mob {

	{
		spriteClass = NzothMateRiderSprite.class;

		HP = HT = 16;
		defenseSkill = 13;

		EXP = 0;
		maxLvl = 0;

		flying = true;

	}

	@Override
	public void die( Object cause ) {

		NzothMateBee bee = new NzothMateBee();
		bee.HP = bee.HT;
		bee.pos = pos;
		GameScene.add( bee );
		Actor.addDelayed( new Pushing( bee, pos, pos ), -1f );
		//bee.sprite.alpha( 0 );
		//bee.sprite.parent.add( new AlphaTweener( bee.sprite, 1, 0.15f ) );
		Sample.INSTANCE.play( Assets.SND_BEE );
		bee.sprite.turnTo(pos, target);
		super.die(cause);

	}
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 16 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 14;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3 );
	}
}
