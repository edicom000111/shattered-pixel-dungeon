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
import com.suntain.stonepixeldungeon.actors.blobs.Electricity;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Paralysis;
import com.suntain.stonepixeldungeon.items.cards.Card;
import com.suntain.stonepixeldungeon.items.cards.Tidal_Surge;
import com.suntain.stonepixeldungeon.items.cards.Warrior_Ability;
import com.suntain.stonepixeldungeon.mechanics.Ballistica;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.DrakonidSprite;
import com.suntain.stonepixeldungeon.sprites.HistorianSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Drakonid extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	public Class card = null;
	Card c;
	
	{
		spriteClass = DrakonidSprite.class;

		HP = HT = 85;
		defenseSkill = 16;

		EXP = 12;
		maxLvl = 23;
		

	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 45 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 13);
	}

	@Override
	public void die( Object cause ) {
		if(card != null){
			if(c == null) try {
				c = (Card) card.newInstance();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			loot = c;
			lootChance = 0.2f;
		}
		super.die(cause);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		if(card == null) return super.canAttack(enemy);
		if(!c.usesTargeting) return true;

		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		if(attack.collisionPos == enemy.pos){
			c.TargetPos = enemy.pos;
			return true;
		}
		return false;
	}

	public boolean steal(){
		//카드뺏기
		if(enemy == Dungeon.hero && attack( enemy )){
			Card inv = Dungeon.hero.belongings.getRandomItem(Card.class);
			if(inv != null) {
				card = inv.getClass();
				try {
					c = (Card) card.newInstance();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			} else{
				c = null;
				card = null;
			}
			//카드가 있을 시 card = 해당카드, result = true;
		}
		next();
		return card != null;
	}
	@Override
	protected boolean doAttack( Char enemy ) {
		if(card == null) return super.doAttack(enemy);
		boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
		if (visible) {
			sprite.zap(enemy.pos);
		} else zap();
		return !visible;
	}

	public void zap(){
		spend( TIME_TO_ZAP );
		sprite.showStatus( CharSprite.NEUTRAL, card+"" );
		c.active(this);
		MP = 0;
		card = null;
		c = null;
		next();
	}



	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("currentCard", card);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		card = bundle.getClass("currentCard");
		if(card != null)
			try {
				c = (Card) card.newInstance();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

	}

}