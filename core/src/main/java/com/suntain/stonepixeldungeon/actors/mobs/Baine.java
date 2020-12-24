package com.suntain.stonepixeldungeon.actors.mobs;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Badges;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.items.keys.SkeletonKey;
import com.suntain.stonepixeldungeon.levels.PrisonBossLevel;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.BaineSprite;
import com.suntain.stonepixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
/*
public class Baine extends Mob {
	private int paze = 0;
	{

		HP = HT = 120;
		EXP = 20;
		defenseSkill = 20;
		spriteClass = BaineSprite.class;
		properties.add(Property.BOSS);
		state = HUNTING;
		lootChance = 0.4f;
	}


	@Override
	public boolean act() {
		if(paze != 2 && ((PrisonBossLevel)Dungeon.level).phaze == 2){
			HUNTING = new Rage();
			paze = 2;
			sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
			Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		}
		if(Dungeon.hero.invisible > 0) state = WANDERING;
		else state = HUNTING;
		return super.act();
	}


	@Override
	public void damage(int dmg, Object src) {
		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;
		if(HP < HT / 2){
			BossHealthBar.bleed(true);
		}
		BossHealthBar.assignBoss(this);
	}
	@Override
	public void die( Object cause ) {
		if(((PrisonBossLevel)Dungeon.level).open) lootChance = 0.6f;
		super.die(cause);

		if(((PrisonBossLevel)Dungeon.level).phaze == 1) ((PrisonBossLevel)Dungeon.level).phaze = 3;
		if(((PrisonBossLevel)Dungeon.level).phaze == 2) {
			((PrisonBossLevel)Dungeon.level).phaze = 4;
			//Dungeon.level.unseal();
			GameScene.bossSlain();
			Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
			Badges.validateBossSlain();
			yell(Messages.get(this, "defeated"));
		}
	}

	@Override
	public void notice() {
		if(((PrisonBossLevel)Dungeon.level).phaze == 2) HUNTING = new Rage();
		state = HUNTING;
		super.notice();
	}


	@Override
	public void storeInBundle( Bundle bundle ) {
		if(((PrisonBossLevel)Dungeon.level).phaze == 2){
			HUNTING = new Rage();
			paze = 2;
			state = HUNTING;
		}
		super.storeInBundle(bundle);

	}
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 8, 25 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 7);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );
	}

	protected class Rage implements AiState {

		public static final String TAG	= "HUNTING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
				boolean[] passable = Dungeon.level.passable;
				int[] neighbours = {pos * 2 - Dungeon.hero.pos, pos * 3 - Dungeon.hero.pos * 2};
				int ran1 = (Dungeon.hero.pos / 32 - pos / 32);
				int ran2 = (Dungeon.hero.pos % 32 - pos % 32);
				for (int n : neighbours) { //ran1 >= 0 && ran2 >= 0 && ran1 <= 32 && ran2 <= 32 &&
					if (passable[n] && Actor.findChar( n ) == null &&  Math.random() < 0.5 && passable[0]) {
						spend(1 / speed());
						moveSprite(pos, n);
						move( n );
						System.out.println("정보 : 백스텝 "+(n == neighbours[0] ? 1 : 2)+"칸, ran1 : "+ran1+", ran2 : "+ran2);
						return true;
					}
				}
				System.out.println("정보 : 근접공격, ran1 : "+ran1+", ran2 : "+ran2);
				return doAttack( enemy );
			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				}
				int oldPos = pos;
				if(distance(enemy) <= 3 && getCloser( target )) {
					getCloser(target);
					getCloser(target);
					getCloser(target);
					getCloser(target);
					getCloser(target);
					spend(1 / speed());
					moveSprite(oldPos, pos);
					//move( pos );
					System.out.println("정보 : 돌진공격");
					return doAttack( enemy );
				}
				if (target != -1 && getCloser( target )) {
					System.out.println("정보 : 다가가기");

					spend( 1 / speed() );
					move( pos );
					return moveSprite( oldPos,  pos );

				} else {

					spend( TICK );
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}
			}
		}
	}
}*/