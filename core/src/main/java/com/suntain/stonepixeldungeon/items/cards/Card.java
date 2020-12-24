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

import com.suntain.stonepixeldungeon.Dungeon;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.hero.Belongings;
import com.suntain.stonepixeldungeon.actors.hero.Hero;
import com.suntain.stonepixeldungeon.actors.hero.HeroClass;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.bags.Deck;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.sprites.ItemSpriteSheet;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Card extends Item {

    protected float TIME_TO_CARD = 2f;
    public static final String AC_Card	= "Card";
    public int Cost = 2;
    public String message = Messages.get(this, "card_msg");
    public int used = quantity;
    public String Type;
    public boolean disposable = false;


    public int TargetPos;


    {
        curUser = Dungeon.hero;
        stackable = true;
        image = ItemSpriteSheet.SOMETHING;
        bones = false;
        defaultAction = AC_Card;
        //usesTargeting
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put("used", used);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        used = bundle.getInt("used");

    }
    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_Card);
        return actions;
    }

    @Override
    public String info() {
        return Messages.get(this, "desc");
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals( AC_Card )) {
            System.out.println("카드사용중");
            if(used > 0) {
                if (curUser.MP >= Cost) {
                    if(!usesTargeting) {
                        used --;
                        Dungeon.hero.MP -= Cost;
                        //if(usesTargeting) GameScene.cancelCellSelector();
                        //SpellSprite.show(hero, SpellSprite.FOOD);
                        hero.sprite.operate(hero.pos);
                        hero.busy();
                        hero.spend(TIME_TO_CARD);
                    }
                    active(hero);
                } else{
                    GLog.i(Messages.get(this, "mana"));
                }
            } else{
                GLog.i(Messages.get(this, "cool"));
            }
            updateQuickslot();
        }
    }

    public abstract void active(Char c);
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 100;
    }

    @Override
    public int visiblyUpgraded() {
        return 0;
    }

    @Override
    public String status() {
        if(used == quantity) return Cost +" Cos";
        else if(used == 0) return "cool";
        return used+"/"+quantity;
    }

    public static void CoolReset(){
        Belongings stuff = Dungeon.hero.belongings;
        for(Item item : stuff.backpack.items.toArray(new Item[0])) {
            if (item instanceof Card) {
                if(!((Card)item).disposable) ((Card)item).used = ((Card)item).quantity;
            }
            if (item instanceof Deck){
                for(Item i : stuff.getItem( Deck.class ).items) {
                    if (i instanceof Card) {
                        if(!((Card)item).disposable) ((Card) i).used = ((Card)item).quantity;
                    }
                }
            }
        }
    }


    public static Class<?>[] WarriorCards() {
        return new Class[]{  Inner_Rage.class};
    }
    public static float[] WarriorChances() {
        return new float[]{ 1 };
    }
    public static Class<?>[] ShamanCards() {
        return new Class[]{  Shaman_Ability.class, Ancestral_Healing.class, Tidal_Surge.class};
    }
    public static float[] ShamanChances() {
        return new float[]{ 0, 1, 1 };
    }
    public static Class<?>[] RogueCards() {
        return new Class[]{  Rogue_Ability.class, Blade_Flurry.class};
    }
    public static float[] RogueChances() {
        return new float[]{ 0, 1 };
    }
    public static Class<?>[] RegularCards() {
        return new Class[]{ /*Laza.class*/ };
    }
    public static float[] RegularChances() {
        return new float[]{ /**/};
    }

    public static Object lootCard(){
        float[] Chances = null;
        Class<?>[] Cards = null;
        float[] RegularChances = Card.RegularChances();
        Class<?>[] RegularCards = Card.RegularCards();
        if(Dungeon.hero.heroClass == HeroClass.WARRIOR){
            Chances = Card.WarriorChances();
            Cards = Card.WarriorCards();
        }
        else if(Dungeon.hero.heroClass == HeroClass.MAGE){
            Chances = Card.ShamanChances();
            Cards = Card.ShamanCards();
        }
        else if(Dungeon.hero.heroClass == HeroClass.ROGUE){
            Chances = Card.RogueChances();
            Cards = Card.RogueCards();
        } else if(Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
            //Chances = Card.HuntressChances();
            //Cards = Card.HuntressCards();
            Chances = Card.RogueChances();
            Cards = Card.RogueCards();
        }
        float[] ResultChances = new float[Chances.length + RegularChances.length];
        Class<?>[] ResultCards = new Class<?>[Cards.length + RegularCards.length];
        System.arraycopy(Cards, 0, ResultCards, 0, Cards.length);
        System.arraycopy(RegularCards, 0, ResultCards, Cards.length, RegularCards.length);
        System.arraycopy(Chances, 0, ResultChances, 0, Chances.length);
        System.arraycopy(RegularChances, 0, ResultChances, Chances.length, RegularChances.length);
        return ResultCards[Random.chances( ResultChances )];
    }


    @Override
    public Item split( int amount ) {
        int q = quantity;
        Item result = super.split(amount);
        if(q != quantity && used > quantity) used = quantity;
        return result;
    }
}
