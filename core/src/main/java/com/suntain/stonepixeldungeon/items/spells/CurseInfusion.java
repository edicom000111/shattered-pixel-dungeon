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

package com.suntain.stonepixeldungeon.items.spells;

import com.suntain.stonepixeldungeon.Assets;
import com.suntain.stonepixeldungeon.effects.CellEmitter;
import com.suntain.stonepixeldungeon.effects.particles.ShadowParticle;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.armor.Armor;
import com.suntain.stonepixeldungeon.items.quest.MetalShard;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.suntain.stonepixeldungeon.items.wands.Wand;
import com.suntain.stonepixeldungeon.items.weapon.SpiritBow;
import com.suntain.stonepixeldungeon.items.weapon.Weapon;
import com.suntain.stonepixeldungeon.items.weapon.melee.MagesStaff;
import com.suntain.stonepixeldungeon.items.weapon.melee.MeleeWeapon;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class CurseInfusion extends InventorySpell {
	
	{
		image = ItemSpriteSheet.CURSE_INFUSE;
		mode = WndBag.Mode.CURSABLE;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		CellEmitter.get(curUser.pos).burst(ShadowParticle.UP, 5);
		Sample.INSTANCE.play(Assets.SND_CURSED);
		
		item.cursed = true;
		if (item instanceof MeleeWeapon || item instanceof SpiritBow) {
			Weapon w = (Weapon) item;
			if (w.enchantment != null) {
				w.enchant(Weapon.Enchantment.randomCurse(w.enchantment.getClass()));
			} else {
				w.enchant(Weapon.Enchantment.randomCurse());
			}
			w.curseInfusionBonus = true;
			if (w instanceof MagesStaff){
				((MagesStaff) w).updateWand(true);
			}
		} else if (item instanceof Armor){
			Armor a = (Armor) item;
			if (a.glyph != null){
				a.inscribe(Armor.Glyph.randomCurse(a.glyph.getClass()));
			} else {
				a.inscribe(Armor.Glyph.randomCurse());
			}
			a.curseInfusionBonus = true;
		} else if (item instanceof Wand){
			((Wand) item).curseInfusionBonus = true;
			((Wand) item).updateLevel();
		}
		updateQuickslot();
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 100) / 3f));
	}
	
	public static class Recipe extends com.suntain.stonepixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfRemoveCurse.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = CurseInfusion.class;
			outQuantity = 3;
		}
		
	}
}
