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

package com.suntain.stonepixeldungeon.actors.buffs;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.Generator;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.cards.Card;
import com.suntain.stonepixeldungeon.items.quest.CeremonialCandle;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.ui.BuffIndicator;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Quest extends Buff {
	@Override
	public boolean act() {
		spend(TICK);
		if(Dungeon.level.QUEST == 0) detach();
		return true;
	}
	private static final String KILLS			= "kills";
	public int kill = 0;

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( KILLS, kill );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		kill = bundle.getInt(KILLS);
	}

		@Override
	public int icon() {
		return BuffIndicator.QUEST;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		if(Dungeon.level.QUEST == 2) return Messages.get(this, "desc2", kill);
		if(Dungeon.level.QUEST == 3) return Messages.get(this, "desc3", kill);

		else return "CAN'T FIND : "+Dungeon.level.QUEST;
	}

	public void check(){
		if(Dungeon.level.QUEST == 2 && kill >= 10) {
			detach();
			GLog.i(Messages.get(this, "clear2"));
			Item loot = createLoot(Card.lootCard());
			if (loot != null)
				Dungeon.level.drop( loot , Dungeon.hero.pos ).sprite.drop();
			CellEmitter.get(Dungeon.hero.pos).burst(Speck.factory(Speck.STEAM), 6);
			GameScene.QuestComplete();
		}
		if(Dungeon.level.QUEST == 3 && kill >= 4) {
			detach();
			GLog.i(Messages.get(this, "clear3"));
			Dungeon.level.drop(  new CeremonialCandle() , Dungeon.hero.pos ).sprite.drop();
			CellEmitter.get(Dungeon.hero.pos).burst(Speck.factory(Speck.STEAM), 6);
			GameScene.QuestComplete();
		}
	}
	protected Item createLoot(Object loot) {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.random( (Generator.Category)loot );

		} else if (loot instanceof Class<?>) {

			item = Generator.random( (Class<? extends Item>)loot );

		} else {

			item = (Item)loot;

		}
		return item;
	}
}
