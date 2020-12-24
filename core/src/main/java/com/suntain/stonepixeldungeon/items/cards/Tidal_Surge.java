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

package com.suntain.stonepixeldungeon.items.cards;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.MagicMissile;
import com.suntain.stonepixeldungeon.effects.Speck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.scenes.CellSelector;
import com.suntain.stonepixeldungeon.scenes.GameScene;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.ui.QuickSlotButton;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Tidal_Surge extends Card {

	{
		Cost = 2;
		image = ItemSpriteSheet.TIDAL_SURGE;
		usesTargeting = true;
		curItem = this;
	}

	@Override
	public void active(final Char c) {
		curUser = Dungeon.hero;
		if(c instanceof Hero) GameScene.selectCell( act );
		else{
			MagicMissile.boltFromChar(c.sprite.parent, MagicMissile.FROST, c.sprite, TargetPos,
					new Callback() {
						@Override
						public void call() {
							use(TargetPos, true, c);
						}
					});
			c.spend(1f);
		}
	}


	@Override
	public int price() {
		return 100 * quantity;
	}

	protected static CellSelector.Listener act = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				((Tidal_Surge)curItem).cast2( curUser, target );
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Tidal_Surge.class, "prompt");
		}
	};


	public void use(final int cell, boolean isEnd, Char c){
		if(!isEnd) {
			GLog.w(Messages.get(Tidal_Surge.class, "message"));
			MagicMissile.boltFromChar(curUser.sprite.parent, MagicMissile.FROST, curUser.sprite, cell,
					new Callback() {
						@Override
						public void call() {
							use(cell, true, curUser);
						}
					});
		} else {
			Char attacker = c;
			Char victim = Actor.findChar(cell);
			if(victim != null) {
				victim.damage(attacker.damageRoll() * 3, this);
				attacker.HP *= 1.5;
				if(attacker.HP > attacker.HT) attacker.HP = attacker.HT;
				CellEmitter.get(attacker.pos).burst(Speck.factory(Speck.HEALING), 2);
			}
			if(attacker == curUser && curUser == Dungeon.hero) curUser.spendAndNext(1f);
		}
	}


	public void cast2(final Hero user, int dst) {

		final int cell = throwPos( user, dst );
		user.sprite.zap( cell );
		user.busy();

		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );
		QuickSlotButton.target(enemy);

		// FIXME!!!
		float delay = TIME_TO_THROW;
		final float finalDelay = delay;/*
		if(enemy != null)
			((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							enemy.sprite,
							this,
							new Callback() {
								@Override
								public void call() {
									//Item.this.detach(user.belongings.backpack).onThrow(cell);
									use(cell, false);
									//user.spendAndNext(finalDelay);
								}
							});
		else *//*((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
				reset(user.sprite,
						curUser.sprite,
						this,
						new Callback() {
							@Override
							public void call() {
								//Item.this.detach(user.belongings.backpack).onThrow(cell);
								//user.spendAndNext(finalDelay);
							}
						});*/

		use(cell, false, curUser);
		used ++;
		Dungeon.hero.MP -= Cost;
		updateQuickslot();
	}
}
