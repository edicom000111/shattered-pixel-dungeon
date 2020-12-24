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

import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.items.potions.PotionOfHealing;
import com.suntain.stonepixeldungeon.sprites.GoldshireSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Basic extends Mob {

	{
		spriteClass = GoldshireSprite.class;
		
		HP = HT = 8;
		defenseSkill = 2;
		EXP = 1;
		maxLvl = 5;

		BUB = false;
		rush = false;
		flying = false;
		loot = new PotionOfHealing();
		lootChance = 0f;

		//properties.add(Property.BOSS);
	}


	@Override
	public boolean act() {
	return super.act();
	}

	@Override
	public void notice() {
		super.notice();
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return super.canAttack(enemy);
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		//SuperAttack = true;
		return super.doAttack(enemy);
	}

	@Override
	public void damage(int dmg, Object src) {
		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP; // 피해량
	}


	@Override
	public int attackProc( Char enemy, int damage ) {
		return super.attackProc(enemy, damage);
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 8;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 1);
	}

	@Override
	public void die( Object cause ) {
		super.die(cause);
	}

	/*private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Grim.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}*/
}
