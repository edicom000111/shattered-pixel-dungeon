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

package com.suntain.stonepixeldungeon.levels;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Bones;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.mobs.Anduin;
import com.suntain.stonepixeldungeon.actors.mobs.Historian;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.items.Heap;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.levels.painters.Painter;
import com.suntain.stonepixeldungeon.levels.rooms.Room;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Group;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CityBossLevel extends Level {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	protected Room roomEntrance;
	protected Room roomExit;
	private int stairs = 0;
	public Anduin boss = null;

	private static final int c = Terrain.CHASM;
	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int e = Terrain.EMPTY;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	private static final int S = Terrain.STATUE;
	private static final int L = Terrain.LOCKED_EXIT;

	private static final int[] MAP =
			{
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, L, W, W, W, W, W, W, W,
					W, W, W, W, e, W, e, E, e, W, e, W, W, W, W,
					W, W, W, e, W, e, W, e, W, e, W, e, W, W, W,
					W, W, e, e, e, W, e, e, e, W, e, e, e, W, W,
					W, e, W, e, W, e, W, e, W, e, W, e, W, e, W,
					W, W, e, W, e, W, e, e, e, W, e, W, e, W, W,
					W, e, W, e, W, e, W, e, W, e, W, e, W, e, W,
					W, W, e, W, e, W, e, e, e, W, e, W, e, W, W,
					W, e, W, e, W, e, W, e, W, e, W, e, W, e, W,
					W, W, e, W, e, W, e, e, e, W, e, W, e, W, W,
					W, e, W, e, W, e, W, e, W, e, W, e, W, e, W,
					W, W, e, e, e, W, e, e, e, W, e, e, e, W, W,
					W, W, W, e, W, e, W, S, W, e, W, e, W, W, W,
					W, W, W, W, e, W, e, W, e, W, e, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
			};

	private boolean enteredArena = false;
	private boolean keyDropped = false;
	private int stair = 0;

	@Override
	protected boolean build() {
		setSize(15, 16);
		map = MAP.clone();
		Dungeon.hero.MT ++;
		Dungeon.hero.MP = Dungeon.hero.MT;
		traps.clear();
		exit = 15+7;
		entrance = 15*2+7;



		try {
			boss = Anduin.class.newInstance();
			boss.pos = 187;
			mobs.add(boss);
			Historian m = new Historian();
			m.pos = 186;
			m.EXP = 0;
			mobs.add(m);
			m = new Historian();
			m.pos = 188;
			m.EXP = 0;
			mobs.add(m);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		}


		return true;
	}


	@Override
	public void seal() {
		if (entrance != 0) {
			super.seal();

			set(entrance, Terrain.WATER);
			GameScene.updateMap( entrance );
			GameScene.ripple( entrance );

			stairs = entrance;
			entrance = 0;


			enteredArena = true;
			//mob.state = mob.HUNTING;
			//GameScene.add(boss);
			//BossHealthBar.assignBoss(boss);
		}
	}

	public void unseal() {
		if(stairs != 0) {
			super.unseal();
			entrance = stairs;
			stairs = 0;
			set(entrance, Terrain.ENTRANCE);
			GameScene.updateMap( entrance );
		}
	}

	private static final String ENTERED = "entered";
	private static final String DROPPED = "droppped";
	private static final String STAIR = "stair";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENTERED, enteredArena);
		bundle.put(DROPPED, keyDropped);
		bundle.put(STAIR, stair);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		enteredArena = bundle.getBoolean(ENTERED);
		keyDropped = bundle.getBoolean(DROPPED);
		stairs = bundle.getInt(STAIR);
		if(enteredArena && !keyDropped){
			entrance = 0;
		}
		for(Mob m : mobs) if(m instanceof Anduin) boss = (Anduin)m;
		roomExit = roomEntrance; // ???
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals( ) {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}


	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}

	@Override
	public int randomRespawnCell() {
		int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]) {
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}


}
