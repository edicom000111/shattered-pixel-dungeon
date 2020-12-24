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

package com.suntain.stonepixeldungeon.items.cards;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.StonePixelDungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Totem;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Totem1;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Totem2;
import com.suntain.stonepixeldungeon.actors.mobs.npcs.Totem3;
import com.suntain.stonepixeldungeon.levels.Terrain;
import com.suntain.stonepixeldungeon.levels.features.Door;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Shaman_Ability extends Card {

	{
		Cost = 2;
		image = ItemSpriteSheet.SHAMAN_ABILITY;
		//TIME_TO_CARD = 0f;
	}

	@Override
	public void active(Char c) {
		int tpos[] = {c.pos+1, c.pos-1, c.pos + Dungeon.level.width(), c.pos - Dungeon.level.width()};
		boolean can[] = {false, false, false, false};
		boolean did =false;
		for(int i=0; i<tpos.length; i++){
			if((Terrain.flags[Dungeon.level.map[tpos[i]]] & Terrain.PASSABLE) != 0){
				if(Actor.findChar(tpos[i]) == null) {
					can[i] = true;
					did = true;
				}
			}
		}
		if(did) {
			while(true){
				int i = Random.Int(0, 4);
				if(can[i]){
					if(c instanceof Hero) GLog.w(Messages.get(Shaman_Ability.class, "message"));
					if(Dungeon.level.heroFOV[c.pos]) Sample.INSTANCE.play(Assets.SND_TELEPORT);
					Totem mob = null;
					Class<? extends Totem> cl;
					int rand = Random.Int(0, 3);
					if(rand == 0) cl = Totem1.class;
					else if(rand == 1) cl = Totem2.class;
					else cl = Totem3.class;
					try {
						mob = cl.newInstance();
					} catch (Exception e) {
						StonePixelDungeon.reportException(e);
					}
					mob.curUser = c;
					mob.pos = tpos[i];
					Dungeon.level.mobs.add(mob);
					GameScene.add(mob);
					if (Dungeon.level.map[mob.pos] == Terrain.DOOR) {
						Door.enter( mob.pos );
					}
					//c.spend(2f);
					break;
				}
			}
		}
		else {
			if(c instanceof Hero) GLog.i(Messages.get(this, "failed"));
			used ++;
			Dungeon.hero.MP += Cost;
		}
	}


	@Override
	public int price() {
		return 100 * quantity;
	}



}
