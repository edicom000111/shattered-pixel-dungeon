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
import com.suntain.stonepixeldungeon.actors.buffs.Quest;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Ghost;
import com.suntain.stonepixeldungeon.levels.Terrain;
import com.suntain.stonepixeldungeon.levels.features.Door;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.WereWolfSprite_1to4;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WereWolf_1to4 extends DirtEvil_1to4 {

protected int hit = 0;


	{
		spriteClass = WereWolfSprite_1to4.class;
		HP = HT = 30;
		defenseSkill = 6;
		EXP = 6;
		baseSpeed = 1f;
		properties.add(Property.MINIBOSS);
	}


	@Override
	public float attackDelay(){
		return 0f;
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		boolean visible = Dungeon.level.heroFOV[pos];
		hit ++;

		ArrayList<Integer> candidates = new ArrayList<>();
		boolean[] passable = Dungeon.level.passable;
		int a = enemy.pos % Dungeon.level.width() > pos % Dungeon.level.width() ? 1 : -1;
		if (enemy.pos % Dungeon.level.width() == pos % Dungeon.level.width()) a = 0;
		int b = enemy.pos / Dungeon.level.width() > pos / Dungeon.level.width() ? Dungeon.level.width() : -Dungeon.level.width();
		if (enemy.pos / Dungeon.level.width() == pos / Dungeon.level.width()) b = 0;
		int[] neighbours = {enemy.pos * 2 - pos, enemy.pos + a, enemy.pos + b};
		for (int n : neighbours) {
			if (passable[n] && Actor.findChar(n) == null) {
				candidates.add(n);
			}
		}
		if (candidates.size() > 0) {
			if(((Random.Int(0, 5) == 0 && hit > 2) || hit > 7)){
				spend(1f);
				hit = 0;
			}
		} else if(hit == 1){
			yell(Messages.get(this, "happy"));
		}

		if(hit == 0) SuperAttack = true;
		else SuperAttack = false;
		if (visible) {
			sprite.attack( enemy.pos );
		} else {
			attack( enemy );
		}
		return !visible;
	}



	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10 + hit, 30 + hit * 2 );
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
	public void die( Object cause ) {
		super.die( cause );
		Quest b = Dungeon.hero.buff(Quest.class);
		if(b != null) {
			b.kill++;
			b.check();
		} else Ghost.Quest.process();
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("drain", hit);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		hit = bundle.getInt("drain");
		super.restoreFromBundle( bundle );

	}
}




/*
	private int count = 10;
	{
			spriteClass = WereWolfSprite_1to4.class;

		HP = HT = 15;
				defenseSkill = 6; //see damage()
				baseSpeed = 3f;
				rush = true;
				EXP = 6;
				state = FLEEING;
				Buff.affect( this, Invisibility.class, 99999999 );

		properties.add(Property.MINIBOSS);
		}

@Override
public boolean act(){
		boolean justAlerted = alerted;
		alerted = false;

		sprite.hideAlert();

		if (paralysed > 0) {
		enemySeen = false;
		spend( TICK );
		return true;
		}

		enemy = chooseEnemy();

		if(count > -1) count ++;
		//count  생성하기
		if(state == FLEEING && count > 9){
		System.out.println("ww태세변환, count : "+count);
		baseSpeed = 3f;
		beckon(enemy.pos);
		count = -1;
		}*//*
		if(buff(Invisibility.class) == null){
			System.out.println("ww후퇴");
			baseSpeed = 10f;
			Buff.affect( this, Invisibility.class, 99999999 );
			state = FLEEING;
			count = 0;
		}*//*

		boolean enemyInFOV = enemy != null && enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0;

		return state.act( enemyInFOV, justAlerted );
		}

@Override
public void damage( int dmg, Object src ){
		System.out.println("ww데미지 입어서 은신풀림");
		Invisibility.dispel(this);
		System.out.println("ww후퇴");
		baseSpeed = 10f;
		Buff.affect( this, Invisibility.class, 99999999 );
		state = FLEEING;
		count = 0;
		super.damage( dmg, src );
		}


@Override
public int damageRoll() {
		System.out.println("ww공격을 해서 은신풀림");
		Invisibility.dispel(this);
		System.out.println("ww후퇴");
		baseSpeed = 10f;
		Buff.affect( this, Invisibility.class, 99999999 );
		state = FLEEING;
		count = 0;
		return Random.NormalIntRange( 5, 10 );
		}

@Override
public void die( Object cause ) {
		super.die( cause );

		Ghost.Quest.process();

		//Dungeon.level.drop( new MysteryMeat(), pos );
		//Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
		}


@Override
public void storeInBundle( Bundle bundle ) {
		bundle.put("SilentCount", count);
		super.storeInBundle(bundle);
		}

@Override
public void restoreFromBundle( Bundle bundle ) {
		count = bundle.getInt("SilentCount");
		super.restoreFromBundle( bundle );
		}*/