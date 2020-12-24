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
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Invisibility;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Ancestral_Healing extends Card {

	{
		Cost = 0;
		image = ItemSpriteSheet.ANCESTRAL_HEALING;
		curUser = Dungeon.hero;
	}

	@Override
	public void active(Char c) {
		c.HP += 25;
		if(c.HP > c.HT) c.HP = c.HT;
		if(c instanceof Hero) GLog.w(Messages.get(Ancestral_Healing.class, "message"));
		if(Dungeon.level.heroFOV[c.pos]) Dungeon.hero.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		Invisibility.dispel();
		Object[] mobs = Dungeon.level.mobs.toArray();
		for (int i = 0; i < mobs.length; i++) if(mobs[i] != this && ((Mob)mobs[i]).alignment != Char.Alignment.NEUTRAL){
			((Mob) mobs[i]).beckon(c.pos);
			((Mob) mobs[i]).enemy = c;
		}
	}

	@Override
	public int price() {
		return 100 * quantity;
	}



}
