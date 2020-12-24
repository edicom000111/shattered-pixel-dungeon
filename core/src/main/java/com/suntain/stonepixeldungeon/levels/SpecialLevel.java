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

package com.suntain.stonepixeldungeon.levels;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.StonePixelDungeon;
import com.suntain.stonepixeldungeon.actors.buffs.Buff;
import com.suntain.stonepixeldungeon.actors.buffs.Quest;
import com.suntain.stonepixeldungeon.actors.mobs.GnollTrickster;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.actors.mobs.WereWolf_1to4;
import com.suntain.stonepixeldungeon.levels.builders.LoopBuilder;
import com.suntain.stonepixeldungeon.levels.painters.Painter;
import com.suntain.stonepixeldungeon.levels.painters.SewerPainter;
import com.suntain.stonepixeldungeon.levels.rooms.Room;
import com.suntain.stonepixeldungeon.levels.rooms.secret.SecretRoom;
import com.suntain.stonepixeldungeon.levels.rooms.special.PitRoom;
import com.suntain.stonepixeldungeon.levels.rooms.special.ShopRoom;
import com.suntain.stonepixeldungeon.levels.rooms.special.SpecialRoom;
import com.suntain.stonepixeldungeon.levels.rooms.standard.EntranceRoom;
import com.suntain.stonepixeldungeon.levels.rooms.standard.ExitRoom;
import com.suntain.stonepixeldungeon.levels.rooms.standard.StandardRoom;
import com.suntain.stonepixeldungeon.levels.traps.*;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class SpecialLevel extends Level {
	public SpecialLevel(int t){
		specialTarget = t;
	}
	public SpecialLevel(){
		super();
	}
	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	protected ArrayList<Room> rooms;
	protected Room roomEntrance;
	protected Room roomExit;


	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int e = Terrain.EMPTY;
	private static final int S = Terrain.SIGN;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	private static final int[] MAP =
			{
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, Terrain.STATUE, e, W, e, W, e, Terrain.STATUE, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, D, W, W, W, e, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, e, Terrain.EMPTY_WELL, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, D, W, W, W, e, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, Terrain.STATUE, e, W, e, W, e, Terrain.STATUE, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			};

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}



	@Override
	public boolean build(){
		Buff.affect(Dungeon.hero, Quest.class);
		setSize(32, 32);
		if(Dungeon.depth == -3){
			map = MAP.clone();
			exit = 5 + 32 * 5;
			Dungeon.hero.MT ++;
			Dungeon.hero.MP = Dungeon.hero.MT;
            return true;
		} else {

			ArrayList<Room> initRooms = initRooms();
			Random.shuffle(initRooms);

			do {
				for (Room r : initRooms){
					r.neigbours.clear();
					r.connected.clear();
				}
				rooms = new LoopBuilder()
						.setLoopShape( 2 ,
								Random.Float(0.4f, 0.7f),
								Random.Float(0f, 0.5f)).build((ArrayList<Room>)initRooms.clone());
			} while (rooms == null);

			map[exit] = Terrain.EMPTY_WELL;
			map[entrance] = Terrain.EMPTY;

			return painter().paint(this, rooms);
		}
	}


	@Override
	protected void createItems() {
		//super.createItems();
	}

	@Override
	protected void createMobs() {
		if(Dungeon.depth == -3){
            int pos[] = {2 + 32*2, 2 + 32*8, 8 + 2*32, 8 + 8*32};
			Class<? extends Mob> cl[] = (Class<? extends Mob>[]) new Class<?>[]{WereWolf_1to4.class, GnollTrickster.class, GnollTrickster.class, GnollTrickster.class};
            int i = 0;
            while(i < 4){
                int r = Random.Int(0, 4);
                if(pos[r] != 0){
                    try {
                        Mob mob = cl[i].newInstance();
                        mob.pos = pos[r];
                        mob.state = mob.HUNTING;
                        mobs.add(mob);
                    } catch (Exception e) {
                        StonePixelDungeon.reportException(e);
                    }
                    pos[r] = 0;
                    i ++;
                }
            }
		}
		else {
			int mobsToSpawn = 10;
			ArrayList<Room> stdRooms = new ArrayList<>();
			for (Room room : rooms) {
				if (room instanceof StandardRoom && room != roomEntrance) {
					for (int i = 0; i < ((StandardRoom) room).sizeCat.roomValue; i++) {
						stdRooms.add(room);
					}
				}
			}
			Random.shuffle(stdRooms);
			Iterator<Room> stdRoomIter = stdRooms.iterator();

			while (mobsToSpawn > 0) {
				if (!stdRoomIter.hasNext())
					stdRoomIter = stdRooms.iterator();
				Room roomToSpawn = stdRoomIter.next();

				Mob mob = createMob();
				mob.pos = pointToCell(roomToSpawn.random());

				if (findMob(mob.pos) == null && passable[mob.pos] && mob.pos != exit) {
					mobsToSpawn--;
					mobs.add(mob);

					//TODO: perhaps externalize this logic into a method. Do I want to make mobs more likely to clump deeper down?
					if (mobsToSpawn > 0 && Random.Int(4) == 0){
						mob = createMob();
						mob.pos = pointToCell(roomToSpawn.random());

						if (findMob(mob.pos)  == null && passable[mob.pos] && mob.pos != exit) {
							mobsToSpawn--;
							mobs.add(mob);
						}
					}
				}
			}

			for (Mob m : mobs){
				if (map[m.pos] == Terrain.HIGH_GRASS || map[m.pos] == Terrain.FURROWED_GRASS) {
					map[m.pos] = Terrain.GRASS;
					losBlocking[m.pos] = false;
				}

			}
		}

	}
	@Override
	public int nMobs(){
		return 0;
	}

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        if(Dungeon.depth != -3) bundle.put( "rooms", rooms );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        if(Dungeon.depth != -3) {
            rooms = new ArrayList<>((Collection<Room>) ((Collection<?>) bundle.getCollection("rooms")));
			for (Room r : rooms) {
				r.onLevelLoad( this );
				if (r instanceof EntranceRoom ){
					roomEntrance = r;
				} else if (r instanceof ExitRoom ){
					roomExit = r;
				}
			}
        }
    }


	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add ( roomEntrance = new EntranceRoom());
		initRooms.add( roomExit = new ExitRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			StandardRoom s;
			do {
				s = StandardRoom.createRoom();
			} while (!s.setSizeCat( standards-i ));
			i += s.sizeCat.roomValue-1;
			initRooms.add(s);
		}

		if (Dungeon.shopOnLevel())
			initRooms.add(new ShopRoom());

		int specials = specialRooms();
		SpecialRoom.initForFloor();
		for (int i = 0; i < specials; i++) {
			SpecialRoom s = SpecialRoom.createRoom();
			if (s instanceof PitRoom) specials++;
			initRooms.add(s);
		}

		int secrets = SecretRoom.secretsForFloor(Dungeon.depth);
		for (int i = 0; i < secrets; i++)
			initRooms.add(SecretRoom.createRoom());

		return initRooms;
	}

	protected int standardRooms(){
		return 0;
	}

	protected int specialRooms(){
		return 0;
	}

	protected Painter painter() {
		return new SewerPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}


	protected int nTraps() {
		return Random.NormalIntRange( 1, 3+(Dungeon.depth/3) );
	}

	protected Class<?>[] trapClasses(){
		return new Class<?>[]{WornDartTrap.class};
	}

	protected float[] trapChances() {
		return new float[]{1};
	}
}
