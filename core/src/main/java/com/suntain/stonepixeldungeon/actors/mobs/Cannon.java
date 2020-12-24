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

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.buffs.Light;
import com.suntain.stonepixeldungeon.actors.buffs.Paralysis;
import com.suntain.stonepixeldungeon.actors.buffs.Terror;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.MagicMissile;
import com.suntain.stonepixeldungeon.effects.Pushing;
import com.suntain.stonepixeldungeon.effects.particles.PurpleParticle;
import com.suntain.stonepixeldungeon.items.Dewdrop;
import com.suntain.stonepixeldungeon.items.bombs.Bomb;
import com.suntain.stonepixeldungeon.items.wands.WandOfBlastWave;
import com.suntain.stonepixeldungeon.items.wands.WandOfDisintegration;
import com.suntain.stonepixeldungeon.items.weapon.enchantments.Grim;
import com.suntain.stonepixeldungeon.mechanics.Ballistica;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.CannonSprite;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Cannon extends Mob {
	
	{
		spriteClass = CannonSprite.class;
		
		HP = HT = 28;
		defenseSkill = 8;
		
		EXP = 5;
		maxLvl = 10;

		HUNTING = new Hunting();


		loot = new Bomb();
		lootChance = 0.1f;
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 10 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4 );
	}
	
	//private Ballistica beam;
	private int beamTarget = -1;
	private int beamCooldown;
	public boolean beamCharged;
	Ballistica bolt;

	@Override
	protected boolean canAttack( Char enemy ) {

		if (beamCooldown == 0) {
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);

			if (enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)){
				bolt = aim;
				beamTarget = aim.collisionPos;
				return true;
			} else
				//if the beam is charged, it has to attack, will aim at previous location of target.
				return beamCharged;
		} else return false;
	}

	@Override
	protected boolean act() {
		if (beamCharged && state != HUNTING){
			beamCharged = false;
		}
		if (bolt == null && beamTarget != -1) {
			bolt = new Ballistica(pos, beamTarget, Ballistica.PROJECTILE);
			sprite.turnTo(pos, beamTarget);
		}
		if (beamCooldown > 0) {
			beamCooldown--;
			if(beamCooldown == 0) state = WANDERING;
			else state = FLEEING;
		}
		return super.act();
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (beamCooldown > 0) {
			return super.doAttack(enemy);
		} else if (!beamCharged){
			((CannonSprite)sprite).charge( enemy.pos );
			spend( attackDelay()*2f );
			beamCharged = true;
			return true;
		} else {

			spend( attackDelay() );
			
			bolt = new Ballistica(pos, beamTarget, Ballistica.PROJECTILE);
			MagicMissile.boltFromChar( sprite.parent,
					MagicMissile.FORCE,
					sprite,
					bolt.collisionPos,
					null);
			Sample.INSTANCE.play(Assets.SND_ZAP);
			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[bolt.collisionPos] ) {
				sprite.zap( bolt.collisionPos );
				return false;
			} else {
				deathGaze();
				return true;
			}
		}

	}

	@Override
	public void damage(int dmg, Object src) {
		if (beamCharged) dmg /= 4;
		super.damage(dmg, src);
	}
	
	//used so resistances can differentiate between melee and magical attacks

	public void deathGaze(){
		if (!beamCharged || beamCooldown > 0 || bolt == null)
			return;

		beamCharged = false;
		beamCooldown = Random.IntRange(2, 6);

		boolean terrainAffected = false;

		for (int cell : bolt.subPath(1, bolt.dist)) {

			if (Dungeon.level.flamable[cell]) {

				Dungeon.level.destroy( cell );
				GameScene.updateMap( cell );
				terrainAffected = true;

			}

			Char ch = Actor.findChar( cell );
			if (ch == null) {
				continue;
			}
/*
			if (drain( this, ch, true )) {
				ch.damage( Random.NormalIntRange( 30, 50 ), new DeathGaze() );

				if (Dungeon.level.heroFOV[pos]) {
					ch.sprite.flash();
					bolt.collisionPosEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
				}

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "deathgaze_kill") );
				}
			} else {
				ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
			}*/
			zap(bolt);

		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		bolt = null;
		beamTarget = -1;
	}




	protected void zap(Ballistica ballistica) {
		Sample.INSTANCE.play( Assets.SND_BLAST );
		WandOfBlastWave.BlastWave.blast(ballistica.collisionPos);

		int damage = damageRoll();

		//presses all tiles in the AOE first
		for (int i : PathFinder.NEIGHBOURS9){
			Dungeon.level.press(ballistica.collisionPos+i, Actor.findChar(ballistica.collisionPos+i), true);
		}

		//throws other chars around the center.
		for (int i  : PathFinder.NEIGHBOURS8){
			Char ch = Actor.findChar(ballistica.collisionPos + i);

			if (ch != null){
				ch.damage(Math.round(damage * 0.667f), this);

				if (ch.isAlive()) {
					Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
					int strength = Dungeon.level.distance(pos, ballistica.collisionPos) >= 3 ? (Dungeon.level.distance(pos, ballistica.collisionPos) >= 6 ? 3 : 2) : 1;
					throwChar(ch, trajectory, strength);
				}
			}
		}

		//throws the char at the center of the blast
		Char ch = Actor.findChar(ballistica.collisionPos);
		if (ch != null){
			ch.damage(damage, this);

			if (ch.isAlive() && ballistica.path.size() > ballistica.dist+1) {
				Ballistica trajectory = new Ballistica(ch.pos, ballistica.path.get(ballistica.dist + 1), Ballistica.MAGIC_BOLT);
				int strength = (Dungeon.level.distance(pos, ballistica.collisionPos) >= 3 ? (Dungeon.level.distance(pos, ballistica.collisionPos) >= 6 ? 3 : 2) : 1) + 1;
				throwChar(ch, trajectory, strength);
			}
		}

		if (!ch.isAlive() && ch == Dungeon.hero){
			Dungeon.fail( getClass() );
			GLog.n( Messages.get( this, "ondeath") );
		}

	}

	public static void throwChar(final Char ch, final Ballistica trajectory, int power){
		int dist = Math.min(trajectory.dist, power);

		if (ch.properties().contains(Char.Property.BOSS))
			dist /= 2;

		if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

		if (Actor.findChar(trajectory.path.get(dist)) != null){
			dist--;
		}

		final int newPos = trajectory.path.get(dist);

		if (newPos == ch.pos) return;

		final int finalDist = dist;
		final int initialpos = ch.pos;

		Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
			public void call() {
				if (initialpos != ch.pos) {
					//something caused movement before pushing resolved, cancel to be safe.
					ch.sprite.place(ch.pos);
					return;
				}
				ch.pos = newPos;
				if (ch.pos == trajectory.collisionPos && ch.isAlive()) {
					ch.damage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), this);
					Paralysis.prolong(ch, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
				}
				Dungeon.level.press(ch.pos, ch, true);
				if (ch == Dungeon.hero){
					//FIXME currently no logic here if the throw effect kills the hero
					Dungeon.observe();
				}
			}
		}), -1);
	}
	
	
	
	
	private static final String BEAM_TARGET     = "beamTarget";
	private static final String BEAM_COOLDOWN   = "beamCooldown";
	private static final String BEAM_CHARGED    = "beamCharged";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( BEAM_TARGET, beamTarget);
		bundle.put( BEAM_COOLDOWN, beamCooldown );
		bundle.put( BEAM_CHARGED, beamCharged );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(BEAM_TARGET))
			beamTarget = bundle.getInt(BEAM_TARGET);
		beamCooldown = bundle.getInt(BEAM_COOLDOWN);
		beamCharged = bundle.getBoolean(BEAM_CHARGED);
	}

	{
		resistances.add( WandOfDisintegration.class );
		resistances.add( Grim.class );
	}
	
	{
		immunities.add( Terror.class );
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			//even if enemy isn't seen, attack them if the beam is charged
			if (beamCharged && enemy != null && canAttack(enemy)) {
				enemySeen = enemyInFOV;
				return doAttack(enemy);
			}
			return super.act(enemyInFOV, justAlerted);
		}
	}
}
