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
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.GooSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Goo extends Mob {
	private int level = 0;
	{

		HP = HT = 45;
		EXP = 10;
		defenseSkill = 8;
		spriteClass = GooSprite.class;

		//loot = new LloydsBeacon().identify();
		lootChance = 0.8f;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
	}


	@Override
	public void damage(int dmg, Object src) {
		int beforeHitHP = HP;
		if(!(src instanceof Buff)) super.damage(dmg, src);
		dmg = beforeHitHP - HP;
		BossHealthBar.assignBoss(this);
		if(HP > 0 && !(src instanceof Buff)) {
			if (pos == 27 * 32 + 16) {
				int newPos = 5 * 32 + 16;
				CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
				sprite.move(pos, newPos);
				move(newPos);
				CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
				Sample.INSTANCE.play(Assets.SND_PUFF);
				for (int i = 0; i < 3; i++) {
					//Point p = room[(int)Math.floor(Math.random() * 7)].random();


					Mob mob = null;
					try {
						mob = Bestiary.getMobRotation(Dungeon.depth - 1 - (int) Math.floor(Math.random() * 4)).remove(0).newInstance();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
					mob.pos = (int) ((12 + Math.floor(Math.random())) * 36 + (3 + Math.floor(Math.random())) + 4 * (Math.floor(Math.random() * 7)));
					mob.beckon(Dungeon.hero.pos);
					//enemy = Dungeon.hero;
					Dungeon.level.mobs.add(mob);
					GameScene.add(mob);
				}
			} else {
				int newPos = 27 * 32 + 16;
				CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
				sprite.move(pos, newPos);
				move(newPos);
				CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
				Sample.INSTANCE.play(Assets.SND_PUFF);
				for (int i = 0; i < 3 + Math.max(3, Math.min(level - 4, 7)); i++) {
					//Point p = room[(int)Math.floor(Math.random() * 7) + 7].random();
					Mob mob = null;
					try {
						if(level < 10) mob = Bestiary.getMobRotation(Dungeon.depth - 1 - Math.min(level / 3, 3)).remove(0).newInstance();
						else {
							mob = Wraith.class.newInstance();
						}
					} catch (Exception e) {
						StonePixelDungeon.reportException(e);
					}
					mob.pos = (int) ((19 + Math.floor(Math.random())) * 36 + (3 + Math.floor(Math.random())) + 4 * (Math.floor(Math.random() * 7)));
					mob.beckon(Dungeon.hero.pos);
					//enemy = Dungeon.hero;
					Dungeon.level.mobs.add(mob);
					GameScene.add(mob);
				}
			}
			level ++;
			yell(Messages.get(this, "notice"));
			//GameScene.add( this );
		}
	}
	@Override
	public boolean act() {
		state = PASSIVE;
		return super.act();
	}


	@Override
	public void die( Object cause ) {

		super.die(cause);
		//Dungeon.level.unseal();
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
		Badges.validateBossSlain();
		yell(Messages.get(this, "defeated"));
}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("level", level);
		super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		level = bundle.getInt("level");
		super.restoreFromBundle( bundle );

		state = PASSIVE;
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		if ((HP*2 <= HT)) BossHealthBar.bleed(true);

	}
	
}
