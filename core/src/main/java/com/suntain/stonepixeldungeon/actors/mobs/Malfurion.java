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
import com.suntain.stonepixeldungeon.StonePixelDungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.LazerWarn;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.ExtraDamage;
import com.suntain.stonepixeldungeon.effects.*;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.suntain.stonepixeldungeon.levels.traps.Trap;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.GnollTricksterSprite;
import com.suntain.stonepixeldungeon.sprites.GooSprite;
import com.suntain.stonepixeldungeon.sprites.MalfurionSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Malfurion extends DeathKnight {

	{
		HP = HT = 200;
		EXP = 30;
		MT = MP = 80;

		/*
		1페) WARDING
		정신자극 쓰면서 움직이기

		NOTICE -> HUNTING
		1턴 전에 소환 안했을시) (몹개수 10마리 이상이면 소환 X)
		//자연의 군대 Treant: 1~8 소환, 마나 3 * 소환량
		//낙스라마스의 망령 4곳에 소환 NaxxramasWraith.class 마나 6
		//주변에 숲수 소환 Shaman.class 마나 8
		//주변에 고대정령 소환 Lore.class 마나 5
		// 주변에 숲수 고대정령 소환 마나 20
		1턴 전에 소환 했을시)
		//별빛섬광 마나 5

		// 체력or마나 30 이하면 마나 10 사용 후 전부 회복 후 NEIGHBOURS9 이끼자라게, 이후 소환 X 2페로 이동

		2페) 공격스킬만 사용
		// 주변 3칸 별빛섬광
		// 주변 1칸 휘둘러치기
		 */
		defenseSkill = 20;
		spriteClass = MalfurionSprite.class;

		//loot = new LloydsBeacon().identify();
		baseSpeed = 2f;

	}


	@Override
	protected boolean canAttack( Char enemy ) {
		if(enemy == null || !enemy.isAlive()) return false;
		//if(phaze == 0) return (Dungeon.level.heroFOV[pos] && distance(enemy) >= 3 && Random.Int(0, 3) > 0) || (!Dungeon.level.heroFOV[pos] && distance(enemy) <= 3) || TargetPos != 0 ||
		//		(Math.abs(pos/Dungeon.level.width() - enemy.pos/Dungeon.level.width()) <= 1 && Math.abs(pos%Dungeon.level.width() - enemy.pos%Dungeon.level.width()) <= 1);
		if(phaze == 0) return (fieldOfView[enemy.pos] && distance(enemy) >= 3 && Random.Int(0, 3) > 0) || (!fieldOfView[enemy.pos] && distance(enemy) <= 3) || TargetPos != 0 ||
				(Math.abs(pos/Dungeon.level.width() - enemy.pos/Dungeon.level.width()) <= 1 && Math.abs(pos%Dungeon.level.width() - enemy.pos%Dungeon.level.width()) <= 1);

		return true;
	}


	@Override
	public boolean act() {
		if(enemy == null || !enemy.isAlive() || !canAttack(enemy)){
			//정자
			if(phaze == 0){
				if(MP < MT) {
					MP ++;
					if(isAlive() && Dungeon.level.heroFOV[pos] && invisible <= 0) CellEmitter.get(pos).burst(Speck.factory(Speck.UP), 1);
				}
				if(MP > MT) MP = MT;
				canSummon = true;
				TargetPos = 0;
			}
		} else if((MP < 30 || HP < 30) && phaze == 0){
			sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
			Sample.INSTANCE.play( Assets.SND_CHALLENGE, 1, 1, 1.3f );
			baseSpeed = 1f;
			phaze = 1;
			MP = 0;
			HP = HT / 2;
			canSummon = false;
			return true;
		}
		return super.act();
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		//자연의 군대 Treant: 1~8 소환, 마나 3 * 소환량
		//낙스라마스의 망령 4곳에 소환 NaxxramasWraith.class 마나 6
		//주변에 숲수 소환 Shaman.class 마나 8
		//주변에 고대정령 소환 Lore.class 마나 5
		// 주변에 숲수 고대정령 소환 마나 20
		if(phaze == 0) {
			if (canSummon && Dungeon.level.mobs.size() < 6 && TargetPos == 0) {
				canSummon = false;
				int type = Random.Int(0, 4);
				if(type == 3) for(Mob mob : Dungeon.level.mobs) if(mob instanceof Shaman){ type = Random.Int(0,3); break;}
				if(type == 2) for(Mob mob : Dungeon.level.mobs) if(mob instanceof Lore){ type = Random.Int(0,2); break;}
				switch (type) {
					case 0:
						int summon = Random.Int(3,7);
						for (int i = 0; i < summon; i++) {
							if (summon(Treant.class) > 0) MP -= 3;
							else break;
						}
						break;
					case 1:
						MP -= summon(NaxxramasWraith.class, NaxxramasWraith.class, NaxxramasWraith.class) * 2;
						break;
					case 2:
						MP -= summon(Lore.class, Lore.class) * 10;
						break;
					case 3:
						MP -= summon(Shaman.class) * 8;
						break;
					default:
						System.out.println("ERROR : "+type);
				}

				//MP per turn 설정해서 갑자기 발진 못하게 방지하기



				sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
				Sample.INSTANCE.play( Assets.SND_CHALLENGE );
				spend(TICK);
				//sprite.attack(enemy.pos);
				return true;
			} else if(Dungeon.level.mobs.size() >= 6 && buff(ExtraDamage.class) == null && TargetPos == 0) {
				//야포
				spend(TICK);
				for (Mob mob : Dungeon.level.mobs) {
					Aura.hit(mob.pos);
					mob.isWaiting = true;
					mob.spend(TICK);
					Buff b = mob.buff(ExtraDamage.class);
					if(b == null) {
						b = new ExtraDamage();
						mob.add(b);
						b.target = mob;
					}
					((ExtraDamage)b).attach(6, 3);
				}
				//Dungeon.hero.interrupt(); //행동중지
				return true;
			} else{
				canSummon = true;
				if (Dungeon.level.distance(pos, enemy.pos) > 1 || TargetPos != 0) {
					System.out.println("type-3");
					return zap();
				} else {
					System.out.println("type-2");
					TargetPos = 0;
					return super.doAttack(enemy);
					//도망치기
				}
			}
		} else{
			if (Dungeon.level.distance(pos, enemy.pos) <= 1) {
				return super.doAttack(enemy);
			} else {
				return zap();
			}
		}
	}
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && phaze == 0) {
			return enemySeen && getFurther(target);
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	public int attackSkill( Char target ) {
		int attack = 25;
		if (phaze == 1) attack = 33;
		return attack;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(  15, 25 );
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (phaze >= 1) BossHealthBar.bleed(true);
	}

}
