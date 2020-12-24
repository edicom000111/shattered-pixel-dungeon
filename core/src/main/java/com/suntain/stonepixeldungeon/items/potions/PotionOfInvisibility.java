/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.suntain.stonepixeldungeon.items.potions;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Invisibility;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfInvisibility extends Potion {

	{
		initials = 4;
	}

	@Override
	public void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
		GLog.i( Messages.get(this, "invisible") );
		Sample.INSTANCE.play( Assets.SND_MELD );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}

}