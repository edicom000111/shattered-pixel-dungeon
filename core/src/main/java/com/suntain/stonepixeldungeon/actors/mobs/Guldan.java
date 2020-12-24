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
import com.suntain.stonepixeldungeon.Badges;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.LazerWarn;
import com.suntain.stonepixeldungeon.actors.buffs.Bleeding;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.effects.Aura;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.suntain.stonepixeldungeon.levels.traps.Trap;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.GuldanSprite;
import com.suntain.stonepixeldungeon.sprites.MalfurionSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Guldan extends DeathKnight {

		/*
		1페) 흡혈박쥐처럼 싸운다.
		피가 절반 이하이고 마나가 있다 -> 2페
		2페) 마나가 있을 시 1 소모 후 무작위 몹 소환
		마나 전부 사용시 1페로 간다
		 */

	{
		HP = HT = 100;
		EXP = 10;
		MT = MP = 10;
		defenseSkill = 8;
		spriteClass = GuldanSprite.class;
	}


	@Override
	public void onZapComplete(int pos) {
		zap();
		next();
	}

	@Override
	public boolean act() {
		if(MP > 0 && HP * 2 < HT && phaze == 0){
			spend(TICK);
			Sample.INSTANCE.play( Assets.SND_CHALLENGE, 1, 1, 0.7f );
			Camera.main.shake(4, 0.5f);
			phaze = 1;
		}
		if(phaze > 0 && MP > 0) {
			state = PASSIVE;
		}
		if(phaze == 1){
			spend(TICK);
			if(MP > 0) {
				phaze++; //몹이 죽으면 phaze - 1
				MP--;

				if(MP == 0) {
					phaze --;
					state = WANDERING;
				}
				boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
				if (visible) {
					sprite.zap(Dungeon.hero.pos);
				} else zap();
				return !visible;
			}
		}
		return super.act();
	}

	@Override
	public boolean zap() {
		summon(Bestiary.getMobRotation( Dungeon.depth - Random.Int(1, 4)).remove(0));
		return false;
	}



	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (damage > 0 && HP < HT) {
			HP ++;
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		}
		/*if (Random.Int( 0, 3 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set( damage * 3 / 2 );
		}*/
		return damage;
	}

	@Override
	public void damage( int dmg, Object src ) {
		System.out.println(phaze);
		if(phaze == 0) super.damage(dmg, src);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}


	@Override
	public int attackSkill( Char target ) {
		return 10;
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(  3, 10 );
	}



	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((HP*2 <= HT)) BossHealthBar.bleed(true);
	}
}
