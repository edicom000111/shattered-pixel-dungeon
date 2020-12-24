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
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.blobs.LazerWarn;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.MagicMissile;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.cards.Card;
import com.suntain.stonepixeldungeon.items.cards.Rogue_Ability;
import com.suntain.stonepixeldungeon.items.cards.Shaman_Ability;
import com.suntain.stonepixeldungeon.items.cards.Warrior_Ability;
import com.suntain.stonepixeldungeon.mechanics.Ballistica;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.AnduinSprite;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.GarroshSprite;
import com.suntain.stonepixeldungeon.sprites.GnollTricksterSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Anduin extends DeathKnight {

	public static final int TELEPORT_DAMAGE = 50;
	{
		HP = HT = 350;
		EXP = 45;
		defenseSkill = 25;
		spriteClass = AnduinSprite.class;
		phaze = TELEPORT_DAMAGE;
		//쳐맞을때 64, 184, 72, 192 로 이동 (+-4, +-60, 128 기준)
	}


	@Override
	protected boolean canAttack( Char enemy ) {
		if(enemy == null || !enemy.isAlive()) {
			TargetPos = 0;
			return false;
		}
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		Boolean result = (fieldOfView[enemy.pos] && distance(enemy) >= 3 ) || (!fieldOfView[enemy.pos] && distance(enemy) < 3) || TargetPos != 0 || (distance(enemy) < 3 && attack.collisionPos == enemy.pos) ||
				(Math.abs(pos/Dungeon.level.width() - enemy.pos/Dungeon.level.width()) <= 1 && Math.abs(pos%Dungeon.level.width() - enemy.pos%Dungeon.level.width()) <= 1);
		if(!result) TargetPos = 0;
		return result;
	}

	protected ArrayList<Blob> b = new ArrayList<>();
	@Override
	protected boolean doAttack( Char enemy ) {
		boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		if(TargetPos == 0){
			if(distance(enemy) < 3 && attack.collisionPos == enemy.pos){
				//에너지 날리기 (소리 뭐로?)
				spend(TICK);
				if (visible) {
					((AnduinSprite)sprite).enerzy_zap(enemy);
				} else energy_zap(enemy);
				Sample.INSTANCE.play( Assets.SND_ZAP );
				return !visible;
			}
			else if((!fieldOfView[enemy.pos] && distance(enemy) < 3) || (distance(enemy) >= 3 && Random.Int(0, 3) == 0)) {
				return zap();
			} else{
				// 카드사용
				if (visible) {
					((AnduinSprite)sprite).card(enemy.pos);
				} else card();
				return !visible;
			}
		} else {
			return zap();
		}
	}

	@Override
	public void notice() {
		super.notice();
		for(Mob mob : Dungeon.level.mobs) if(mob instanceof Historian) mob.beckon(Dungeon.hero.pos);
	}
	public Card getCard(){
		Class<Card>[] c = new Class[]{ Warrior_Ability.class, Shaman_Ability.class, Rogue_Ability.class};
		try {
			return c[(int)(c.length * Random.Float())].newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}



	public void card(){
		Card c = getCard();
		sprite.showStatus( CharSprite.NEUTRAL, c.getClass()+"" );
		c.active(this);
		spend(1f);
		MP = 0;
		c = null;
		next();
	}
	public void energy_zap(final Char enemy){
		MagicMissile.boltFromChar(sprite.parent, MagicMissile.SHADOW, sprite, enemy.pos,
				new Callback() {
					@Override
					public void call() {
						Char attacker = Anduin.this;
						Char victim = enemy;
						if(victim != null) {
							victim.damage(attacker.damageRoll(), this);
						}
					}
				});
	}












	@Override
	public void damage(int dmg, Object src) {

		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;
		phaze -= dmg;
		if(src instanceof Actor && dmg > 0 && phaze < 0) {
			phaze = TELEPORT_DAMAGE;
			jump();
			isWaiting = true;

		}

	}



	private void jump() {

		if (Dungeon.level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
		int newPos = 64 + Random.Int(0, 2) * 120 + Random.Int(0, 2) * 8;

		//쳐맞을때 64, 184, 72, 192 로 이동 (+-4, +-60, 128 기준)
		sprite.move( pos, newPos );
		move( newPos );

		if (Dungeon.level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
		Sample.INSTANCE.play( Assets.SND_PUFF );

		//spend( 1 / speed() );
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 40 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 15);
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
