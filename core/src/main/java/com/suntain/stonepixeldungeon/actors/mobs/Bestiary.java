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

package com.suntain.stonepixeldungeon.actors.mobs;

import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {
	
	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}
	
	//returns a rotation of standard mobs, unshuffled.
	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
		switch(depth){
			case -3: case -2:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						GnollTrickster.class ));/*
			case -2:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Murloc.class ));*/


			case 1:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						VoidWalker.class));

			case 2:
				//1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						VoidWalker.class, VoidWalker.class,
						Imp.class));//ClawDruid
			//ClawDruid.class));

			case 3:
				//2, 2, 1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						VoidWalker.class,
						Imp.class, Imp.class, Imp.class,
						DoomGuard.class));

			case 4:
				//2, 2, 1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						VoidWalker.class,
						Imp.class,
						DoomGuard.class, DoomGuard.class));


			case 5:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Guldan.class ));


			case 6:
				//1, 3
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Korkron.class, Korkron.class,
						Cannon.class));

			case 7:
				//2, 3, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Korkron.class, Korkron.class,
						Cannon.class,
						NzothMateRider.class));

			case 8:
				//1, 2, 2, 3
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Korkron.class,
						Cannon.class,
						NzothMateRider.class, NzothMateRider.class, NzothMateRider.class,
						Commander.class));

			case 9:
				//2, 2, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Cannon.class,
						NzothMateRider.class, NzothMateRider.class,
						Commander.class, Commander.class));


			case 10:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Garrosh.class ));


			case 11:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Treant.class));

			case 12:
				//1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Treant.class, Treant.class,
						NaxxramasWraith.class));//ClawDruid
			//ClawDruid.class));

			case 13:
				//2, 2, 1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Treant.class,
						NaxxramasWraith.class, NaxxramasWraith.class, NaxxramasWraith.class,
						Shaman.class,
						Lore.class));

			case 14:
				//3, 2, 1, 3
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						NaxxramasWraith.class, NaxxramasWraith.class,
						Shaman.class, Shaman.class, Shaman.class,
						Lore.class));


			case 15:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Malfurion.class ));

			case 16:
				//5, 5, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Historian.class));

			case 17:
				//1, 1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Historian.class, Drakonid.class	));

			case 18:
				//1, 2, 1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Historian.class, Drakonid.class, TwilightGuardian.class
				));

			case 19:
				//1, 2, 3, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Historian.class, Drakonid.class, Drakonid.class, TwilightGuardian.class, TwilightGuardian.class));


			case 20:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Anduin.class ));

			case 21:
				//1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						ManaCyclone.class ));

			case 22:
				//1, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Succubus.class,
						Eye.class ));

			case 23:
				//1, 2, 1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class ));

			case 24: case 25: case 26:
				//1, 2, 3
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class, Scorpio.class, Scorpio.class ));


			default:
				//1
				return new ArrayList<Class<? extends Mob>>(Arrays.asList(
						Eye.class ));
		}
		
	}
	
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
		/*
		switch (depth){
			
			// Sewers
			default:
				return;
			case 4:
				//if (Random.Float() < 0.01f) rotation.add(Skeleton.class);
				//if (Random.Float() < 0.01f) rotation.add(Thief.class);
				return;
				
			// Prison
			case 6:
				if (Random.Float() < 0.2f)  rotation.add(Shaman.class);
				return;
			case 8:
				if (Random.Float() < 0.02f) rotation.add(Bat.class);
				return;
			case 9:
				if (Random.Float() < 0.02f) rotation.add(Bat.class);
				if (Random.Float() < 0.01f) rotation.add(Brute.class);
				return;
				
			// Caves
			case 13:
				if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				return;
			case 14:
				if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				if (Random.Float() < 0.01f) rotation.add(Monk.class);
				return;
				
			// City
			case 19:
				if (Random.Float() < 0.02f) rotation.add(Succubus.class);
				return;
		}*/
	}
	
	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			/*if (Random.Int( 50 ) == 0) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == WildBoar_1to4.class) {
					cl = CoreBoar_1to4.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Brute.class) {
					cl = Shielded.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				}
				rotation.set(i, cl);
			}*/
		}
	}
}
