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

import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class ExtraDamage extends FlavourBuff {
	{
		type = buffType.POSITIVE;
	}
	@Override
	public int icon() {
		return BuffIndicator.RAGE;
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public void tintIcon(Image icon) {
		greyIcon(icon, 10f, cooldown());
	}

	public float left;
	public int extraDamage = 0;

	public void attach(float a, int b){
		if(a != -1) left = a;
		else left ++;
		extraDamage += b;
	}

	public int getDamage(){
		return extraDamage;
	}
	@Override
	public void detach() {
		super.detach();
	}

	@Override
	public boolean act() {
		spend( TICK );
		left -= TICK;
		if(left <= 0) detach();
		return true;
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc", (int)left, (int) extraDamage);
		return desc;
	}

	private static final String LEFT	= "left";
	private static final String EXTRA_DAMAGE	= "extra_damage";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
		bundle.put(EXTRA_DAMAGE, extraDamage);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		extraDamage = bundle.getInt( EXTRA_DAMAGE );
		left = bundle.getFloat( LEFT );
	}

}
