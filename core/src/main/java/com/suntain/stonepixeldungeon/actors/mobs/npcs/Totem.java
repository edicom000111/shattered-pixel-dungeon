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

package com.suntain.stonepixeldungeon.actors.mobs.npcs;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.Totem2Sprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public abstract class Totem extends NPC {
//도발

	{
		HP = 1000000;
	}

	protected int limited_time;
	protected int count;
	public Char curUser = null;

	@Override
	protected boolean act() {
		if(limited_time-- > 0) spend(TICK);
		else die();
		return true;
	}
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put("time", limited_time);
		bundle.put("count", count);
		bundle.put("curUser", curUser);
		super.storeInBundle(bundle);

	}

	@Override
	public String description() {
		return Messages.get(this, "desc", limited_time, count);
	}
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		limited_time = bundle.getInt("limited_time");
		count = bundle.getInt("count");
		curUser =(Char) bundle.get("curUser");
		super.restoreFromBundle(bundle);
	}


	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public void add( Buff buff ) {
	}


	@Override
	public boolean interact() {
		if(curUser == Dungeon.hero) {
			GLog.i(Messages.get(this, "recall"));
			die();
		} else GLog.i(Messages.get(this, "resist"));
		return false;
	}

	public void die(){
		HP = 0;
		destroy();
		sprite.die();
	}
}