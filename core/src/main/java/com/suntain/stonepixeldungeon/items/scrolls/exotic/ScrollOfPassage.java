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

package com.suntain.stonepixeldungeon.items.scrolls.exotic;

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.items.artifacts.TimekeepersHourglass;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.plants.Swiftthistle;
import com.suntain.stonepixeldungeon.scenes.InterlevelScene;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

public class ScrollOfPassage extends ExoticScroll {
	
	{
		initials = 8;
	}
	
	@Override
	public void doRead() {
		
		setKnown();
		
		if (Dungeon.bossLevel()) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return;
			
		}
		
		Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null) buff.detach();
		buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
		if (buff != null) buff.detach();
		
		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth-2)%5));
		InterlevelScene.returnPos = -1;
		Game.switchScene( InterlevelScene.class );
	}
}
