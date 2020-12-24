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

package com.suntain.stonepixeldungeon.levels.painters;

/*
public class CobaltPainter extends Painter {

	@Override
	public static boolean paint( Level level, ArrayList<Room> rooms ) {

		Room.Door entrance = room.entrance();
		entrance.set(Room.Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);
		Point c = room.center();
		set( level, c.x, c.y , Terrain.EMPTY_WELL );
		level.specialEntrance = c.y * 32 + c.x;
		level.specialTarget = -3;
	}

	private static boolean validPlantPos(Level level, int pos){
		if (level.map[pos] != Terrain.GRASS){
			return false;
		}

		for (int i : PathFinder.NEIGHBOURS9){
			if (level.findMob(pos+i) != null){
				return false;
			}
		}

		return true;
	}

	private static void placePlant(Level level, int pos, Mob plant){
		plant.pos = pos;
		level.mobs.add( plant );

		for(int i : PathFinder.NEIGHBOURS8) {
			if (level.map[pos + i] == Terrain.GRASS){
				set(level, pos + i, Terrain.HIGH_GRASS);
			}
		}
	}

}
*/