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

package com.suntain.stonepixeldungeon.scenes;

import com.suntain.stonepixeldungeon.*;
import com.suntain.stonepixeldungeon.StonePixelDungeon;
import com.suntain.stonepixeldungeon.actors.Actor;
import com.suntain.stonepixeldungeon.actors.Char;
import com.suntain.stonepixeldungeon.actors.blobs.Blob;
import com.suntain.stonepixeldungeon.actors.mobs.Mob;
import com.suntain.stonepixeldungeon.effects.BannerSprites;
import com.suntain.stonepixeldungeon.effects.BlobEmitter;
import com.suntain.stonepixeldungeon.effects.CircleArc;
import com.suntain.stonepixeldungeon.effects.EmoIcon;
import com.suntain.stonepixeldungeon.effects.Flare;
import com.suntain.stonepixeldungeon.effects.FloatingText;
import com.suntain.stonepixeldungeon.effects.Ripple;
import com.suntain.stonepixeldungeon.effects.SpellSprite;
import com.suntain.stonepixeldungeon.items.Heap;
import com.suntain.stonepixeldungeon.items.Honeypot;
import com.suntain.stonepixeldungeon.items.Item;
import com.suntain.stonepixeldungeon.items.artifacts.DriedRose;
import com.suntain.stonepixeldungeon.items.bags.*;
import com.suntain.stonepixeldungeon.items.potions.Potion;
import com.suntain.stonepixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.suntain.stonepixeldungeon.journal.Journal;
import com.suntain.stonepixeldungeon.levels.RegularLevel;
import com.suntain.stonepixeldungeon.levels.traps.Trap;
import com.suntain.stonepixeldungeon.messages.Messages;
import com.suntain.stonepixeldungeon.plants.Plant;
import com.suntain.stonepixeldungeon.sprites.CharSprite;
import com.suntain.stonepixeldungeon.sprites.DiscardedItemSprite;
import com.suntain.stonepixeldungeon.sprites.HeroSprite;
import com.suntain.stonepixeldungeon.sprites.ItemSprite;
import com.suntain.stonepixeldungeon.tiles.CustomTilemap;
import com.suntain.stonepixeldungeon.tiles.DungeonTerrainTilemap;
import com.suntain.stonepixeldungeon.tiles.DungeonTileSheet;
import com.suntain.stonepixeldungeon.tiles.DungeonTilemap;
import com.suntain.stonepixeldungeon.tiles.DungeonWallsTilemap;
import com.suntain.stonepixeldungeon.tiles.FogOfWar;
import com.suntain.stonepixeldungeon.tiles.GridTileMap;
import com.suntain.stonepixeldungeon.tiles.RaisedTerrainTilemap;
import com.suntain.stonepixeldungeon.tiles.TerrainFeaturesTilemap;
import com.suntain.stonepixeldungeon.tiles.WallBlockingTilemap;
import com.suntain.stonepixeldungeon.ui.ActionIndicator;
import com.suntain.stonepixeldungeon.ui.AttackIndicator;
import com.suntain.stonepixeldungeon.ui.Banner;
import com.suntain.stonepixeldungeon.ui.BusyIndicator;
import com.suntain.stonepixeldungeon.ui.CharHealthIndicator;
import com.suntain.stonepixeldungeon.ui.GameLog;
import com.suntain.stonepixeldungeon.ui.LootIndicator;
import com.suntain.stonepixeldungeon.ui.QuickSlotButton;
import com.suntain.stonepixeldungeon.ui.ResumeIndicator;
import com.suntain.stonepixeldungeon.ui.StatusPane;
import com.suntain.stonepixeldungeon.ui.TargetHealthIndicator;
import com.suntain.stonepixeldungeon.ui.Toast;
import com.suntain.stonepixeldungeon.ui.Toolbar;
import com.suntain.stonepixeldungeon.ui.Window;
import com.suntain.stonepixeldungeon.utils.GLog;
import com.suntain.stonepixeldungeon.windows.WndBag;
import com.suntain.stonepixeldungeon.windows.WndBag.Mode;
import com.suntain.stonepixeldungeon.windows.WndGame;
import com.suntain.stonepixeldungeon.windows.WndHero;
import com.suntain.stonepixeldungeon.windows.WndInfoCell;
import com.suntain.stonepixeldungeon.windows.WndInfoItem;
import com.suntain.stonepixeldungeon.windows.WndInfoMob;
import com.suntain.stonepixeldungeon.windows.WndInfoPlant;
import com.suntain.stonepixeldungeon.windows.WndInfoTrap;
import com.suntain.stonepixeldungeon.windows.WndMessage;
import com.suntain.stonepixeldungeon.windows.WndOptions;
import com.suntain.stonepixeldungeon.windows.WndStory;
import com.suntain.stonepixeldungeon.windows.WndTradeItem;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class GameScene extends PixelScene {

	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTerrainTilemap tiles;
	private GridTileMap visualGrid;
	private TerrainFeaturesTilemap terrainFeatures;
	private RaisedTerrainTilemap raisedTerrain;
	private DungeonWallsTilemap walls;
	private WallBlockingTilemap wallBlocking;
	private FogOfWar fog;
	private HeroSprite hero;

	private StatusPane pane;
	
	private GameLog log;
	
	private BusyIndicator busy;
	private CircleArc counter;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group customTiles;
	private Group levelVisuals;
	private Group customWalls;
	private Group ripples;
	private Group plants;
	private Group traps;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	private Group healthIndicators;
	
	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ActionIndicator action;
	private ResumeIndicator resume;
	
	@Override
	public void create() {
		
		if (Dungeon.hero == null){
			StonePixelDungeon.switchNoFade(TitleScene.class);
			return;
		}
		
		if(Dungeon.depth > 0 && Dungeon.depth < 5) Music.INSTANCE.play( Assets.GAME_1, true );
		else if(Dungeon.depth == 5) Music.INSTANCE.play( Assets.GAME_2, true );
		else if(Dungeon.depth > 5 && Dungeon.depth < 10) Music.INSTANCE.play(Assets.GAME_3, true);
		else if(Dungeon.depth == 10) Music.INSTANCE.play( Assets.GAME_4, true );
		else Music.INSTANCE.play(Assets.GAME, true);

		SPDSettings.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + SPDSettings.zoom(), maxZoom));

		scene = this;

		terrain = new Group();
		add( terrain );

		water = new SkinnedBlock(
			Dungeon.level.width() * DungeonTilemap.SIZE,
			Dungeon.level.height() * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() ){

			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		terrain.add( water );

		ripples = new Group();
		terrain.add( ripples );

		DungeonTileSheet.setupVariance(Dungeon.level.map.length, Dungeon.seedCurDepth());
		
		tiles = new DungeonTerrainTilemap();
		terrain.add( tiles );

		customTiles = new Group();
		terrain.add(customTiles);

		for( CustomTilemap visual : Dungeon.level.customTiles){
			addCustomTile(visual);
		}

		visualGrid = new GridTileMap();
		terrain.add( visualGrid );

		terrainFeatures = new TerrainFeaturesTilemap(Dungeon.level.plants, Dungeon.level.traps);
		terrain.add(terrainFeatures);
		
		levelVisuals = Dungeon.level.addVisuals();
		add(levelVisuals);
		
		heaps = new Group();
		add( heaps );
		
		int size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite( Dungeon.level.heaps.valueAt( i ) );
		}
		
		emitters = new Group();
		effects = new Group();
		healthIndicators = new Group();
		emoicons = new Group();
		
		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		
		raisedTerrain = new RaisedTerrainTilemap();
		add( raisedTerrain );

		walls = new DungeonWallsTilemap();
		add(walls);

		customWalls = new Group();
		add(customWalls);

		for( CustomTilemap visual : Dungeon.level.customWalls){
			addCustomWall(visual);
		}

		wallBlocking = new WallBlockingTilemap();
		add (wallBlocking);

		add( emitters );
		add( effects );

		gases = new Group();
		add( gases );

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}


		fog = new FogOfWar( Dungeon.level.width(), Dungeon.level.height() );
		add( fog );

		spells = new Group();
		add( spells );
		
		statuses = new Group();
		add( statuses );
		
		add( healthIndicators );
		//always appears ontop of other health indicators
		add( new TargetHealthIndicator() );
		
		add( emoicons );
		
		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );
		
		add( cellSelector = new CellSelector( tiles ) );

		pane = new StatusPane();
		pane.camera = uiCamera;
		pane.setSize( uiCamera.width, 0 );
		add( pane );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		action = new ActionIndicator();
		action.camera = uiCamera;
		add( action );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		log = new GameLog();
		log.camera = uiCamera;
		log.newLine();
		add( log );

		layoutTags();

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = pane.bottom() + 1;
		add( busy );
		
		counter = new CircleArc(18, 4.25f);
		counter.color( 0x808080, true );
		counter.camera = uiCamera;
		counter.show(this, busy.center(), 0f);
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			ScrollOfTeleportation.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			ScrollOfTeleportation.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				break;
			case 6:
				WndStory.showChapter( WndStory.ID_PRISON );
				break;
			case 11:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 16:
				WndStory.showChapter( WndStory.ID_CITY );
				break;
			case 22:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
			if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
				Badges.validateNoKilling();
			}
			break;
		default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}
		
		ArrayList<Item> ported = Dungeon.portedItems.get( Dungeon.depth );
		if (ported != null){
			//TODO currently items are only ported to boss rooms, so this works well
			//might want to have a 'near entrance' function if items can be ported elsewhere
			int pos;
			//try to find a tile with no heap, otherwise just stick items onto a heap.
			int tries = 100;
			do {
				pos = Dungeon.level.randomRespawnCell();
				tries--;
			} while (tries > 0 && Dungeon.level.heaps.get(pos) != null);
			for (Item item : ported) {
				Dungeon.level.drop( item, pos ).type = Heap.Type.CHEST;
			}
			Dungeon.level.heaps.get(pos).type = Heap.Type.CHEST;
			Dungeon.level.heaps.get(pos).sprite.link(); //sprite reset to show chest
			Dungeon.portedItems.remove( Dungeon.depth );
		}

		Dungeon.hero.next();

		Camera.main.target = hero;

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
			if (Dungeon.depth == Statistics.deepestFloor
					&& (InterlevelScene.mode == InterlevelScene.Mode.DESCEND || InterlevelScene.mode == InterlevelScene.Mode.FALL)) {
				GLog.h(Messages.get(this, "descend"), Dungeon.depth);
				Sample.INSTANCE.play(Assets.SND_DESCEND);
				
				for (Char ch : Actor.chars()){
					if (ch instanceof DriedRose.GhostHero){
						((DriedRose.GhostHero) ch).sayAppeared();
					}
				}
				
			} else if (InterlevelScene.mode == InterlevelScene.Mode.RESET) {
				GLog.h(Messages.get(this, "warp"));
			} else {
				GLog.h(Messages.get(this, "return"), Dungeon.depth);
			}

			switch (Dungeon.level.feeling) {
				case CHASM:
					GLog.w(Messages.get(this, "chasm"));
					break;
				case WATER:
					GLog.w(Messages.get(this, "water"));
					break;
				case GRASS:
					GLog.w(Messages.get(this, "grass"));
					break;
				case DARK:
					GLog.w(Messages.get(this, "dark"));
					break;
				default:
			}
			if (Dungeon.level instanceof RegularLevel &&
					((RegularLevel) Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
				GLog.w(Messages.get(this, "secrets"));
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			fadeIn();
		}

	}
	
	public void destroy() {
		
		//tell the actor thread to finish, then wait for it to complete any actions it may be doing.
		if (actorThread.isAlive()){
			synchronized (GameScene.class){
				synchronized (actorThread) {
					actorThread.interrupt();
				}
				try {
					GameScene.class.wait(5000);
				} catch (InterruptedException e) {
					StonePixelDungeon.reportException(e);
				}
				synchronized (actorThread) {
					if (Actor.processing()) {
						Throwable t = new Throwable();
						t.setStackTrace(actorThread.getStackTrace());
						throw new RuntimeException("timeout waiting for actor thread! ", t);
					}
				}
			}
		}
		
		freezeEmitters = false;
		
		scene = null;
		Badges.saveGlobal();
		Journal.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void onPause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			StonePixelDungeon.reportException(e);
		}
	}

	private static final Thread actorThread = new Thread() {
		@Override
		public void run() {
			Actor.process();
		}
	};

	@Override
	public synchronized void update() {
		if (Dungeon.hero == null || scene == null) {
			return;
		}

		super.update();
		
		if (!freezeEmitters) water.offset( 0, -5 * Game.elapsed );

		if (!Actor.processing() && Dungeon.hero.isAlive()) {
			if (!actorThread.isAlive()) {
				//if cpu cores are limited, game should prefer drawing the current frame
				if (Runtime.getRuntime().availableProcessors() == 1) {
					actorThread.setPriority(Thread.NORM_PRIORITY - 1);
				}
				actorThread.start();
			} else {
				synchronized (actorThread) {
					actorThread.notify();
				}
			}
		}

		counter.setSweep((1f - Actor.now()%1f)%1f);
		
		if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
			log.newLine();
		}

		if (tagAttack != attack.active ||
				tagLoot != loot.visible ||
				tagAction != action.visible ||
				tagResume != resume.visible) {

			//we only want to change the layout when new tags pop in, not when existing ones leave.
			boolean tagAppearing = (attack.active && !tagAttack) ||
									(loot.visible && !tagLoot) ||
									(action.visible && !tagAction) ||
									(resume.visible && !tagResume);

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagAction = action.visible;
			tagResume = resume.visible;

			if (tagAppearing) layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);
	}

	private boolean tagAttack    = false;
	private boolean tagLoot      = false;
	private boolean tagAction    = false;
	private boolean tagResume    = false;

	public static void layoutTags() {

		if (scene == null) return;

		float tagLeft = SPDSettings.flipTags() ? 0 : uiCamera.width - scene.attack.width();

		if (SPDSettings.flipTags()) {
			scene.log.setRect(scene.attack.width(), scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
		} else {
			scene.log.setRect(0, scene.toolbar.top(), uiCamera.width - scene.attack.width(),  0 );
		}

		float pos = scene.toolbar.top();

		if (scene.tagAttack){
			scene.attack.setPos( tagLeft, pos - scene.attack.height());
			scene.attack.flip(tagLeft == 0);
			pos = scene.attack.top();
		}

		if (scene.tagLoot) {
			scene.loot.setPos( tagLeft, pos - scene.loot.height() );
			scene.loot.flip(tagLeft == 0);
			pos = scene.loot.top();
		}

		if (scene.tagAction) {
			scene.action.setPos( tagLeft, pos - scene.action.height() );
			scene.action.flip(tagLeft == 0);
			pos = scene.action.top();
		}

		if (scene.tagResume) {
			scene.resume.setPos( tagLeft, pos - scene.resume.height() );
			scene.resume.flip(tagLeft == 0);
		}
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}

	public void addCustomTile( CustomTilemap visual){
		customTiles.add( visual.create() );
	}

	public void addCustomWall( CustomTilemap visual){
		customWalls.add( visual.create() );
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {

	}

	private void addTrapSprite( Trap trap ) {

	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		System.out.println("current1 : "+mob.getClass());
		CharSprite sprite = mob.sprite();
		System.out.println("current2 : "+sprite.getClass());
		sprite.visible = Dungeon.level.heroFOV[mob.pos];
		mobs.add( sprite );
		sprite.link( mob );
	}
	
	private synchronized void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt.destroy();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		addToFront( banner );
	}
	
	// -------------------------------------------------------

	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}

	public static void add( Trap trap ) {
		if (scene != null) {
			scene.addTrapSprite( trap );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void add( CharHealthIndicator indicator ){
		if (scene != null) scene.healthIndicators.add(indicator);
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		if (scene != null) {
			Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
			ripple.reset(pos);
			return ripple;
		} else {
			return null;
		}
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item, int pos ) {
		if (scene != null) scene.toolbar.pickup( item, pos );
	}

	public static void pickUpJournal( Item item, int pos ) {
		if (scene != null) scene.pane.pickup( item, pos );
	}
	
	public static void flashJournal(){
		if (scene != null) scene.pane.flash();
	}
	
	public static void updateKeyDisplay(){
		if (scene != null) scene.pane.updateKeys();
	}

	public static void resetMap() {
		if (scene != null) {
			scene.tiles.map(Dungeon.level.map, Dungeon.level.width() );
			scene.visualGrid.map(Dungeon.level.map, Dungeon.level.width() );
			scene.terrainFeatures.map(Dungeon.level.map, Dungeon.level.width() );
			scene.raisedTerrain.map(Dungeon.level.map, Dungeon.level.width() );
			scene.walls.map(Dungeon.level.map, Dungeon.level.width() );
		}
		updateFog();
	}

	//updates the whole map
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updateMap();
			scene.visualGrid.updateMap();
			scene.terrainFeatures.updateMap();
			scene.raisedTerrain.updateMap();
			scene.walls.updateMap();
			updateFog();
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updateMapCell( cell );
			scene.visualGrid.updateMapCell( cell );
			scene.terrainFeatures.updateMapCell( cell );
			scene.raisedTerrain.updateMapCell( cell );
			scene.walls.updateMapCell( cell );
			//update adjacent cells too
			updateFog( cell, 1 );
		}
	}

	public static void plantSeed( int cell ) {
		if (scene != null) {
			scene.terrainFeatures.growPlant( cell );
		}
	}
	
	//todo this doesn't account for walls right now
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		if (scene != null) {
			cancelCellSelector();
			scene.addToFront(wnd);
		}
	}

	public static void updateFog(){
		if (scene != null) {
			scene.fog.updateFog();
			scene.wallBlocking.updateMap();
		}
	}

	public static void updateFog(int x, int y, int w, int h){
		if (scene != null) {
			scene.fog.updateFogArea(x, y, w, h);
			scene.wallBlocking.updateArea(x, y, w, h);
		}
	}
	
	public static void updateFog( int cell, int radius ){
		if (scene != null) {
			scene.fog.updateFog( cell, radius );
			scene.wallBlocking.updateArea( cell, radius );
		}
	}
	
	public static void afterObserve() {
		if (scene != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null)
					mob.sprite.visible = Dungeon.level.heroFOV[mob.pos];
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}

	public static void QuestComplete() {
		if (Dungeon.hero.isAlive()) {
			Banner Quest = new Banner( BannerSprites.get( BannerSprites.Type.QUEST ) );
			Quest.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( Quest );

			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		if (scene != null)
			scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd =
				mode == Mode.SEED ?
					WndBag.getBag( VelvetPouch.class, listener, mode, title ) :
				mode == Mode.SCROLL ?
					WndBag.getBag( ScrollHolder.class, listener, mode, title ) :
				mode == Mode.POTION ?
					WndBag.getBag( PotionBandolier.class, listener, mode, title ) :
				mode == Mode.WAND ?
					WndBag.getBag( MagicalHolster.class, listener, mode, title ) :
						mode == Mode.DECK ?
								WndBag.getBag( Deck.class, listener, mode, title ) :
				WndBag.lastBag( listener, mode, title );
		
		if (scene != null) scene.addToFront( wnd );
		
		return wnd;
	}
	
	static boolean cancel() {
		if (Dungeon.hero != null && (Dungeon.hero.curAction != null || Dungeon.hero.resting)) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
		if (scene != null && scene.toolbar != null) scene.toolbar.examining = false;
	}

	public static void examineCell( Integer cell ) {
		if (cell == null
				|| cell < 0
				|| cell > Dungeon.level.length()
				|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
			return;
		}

		ArrayList<String> names = new ArrayList<>();
		final ArrayList<Object> objects = new ArrayList<>();

		if (cell == Dungeon.hero.pos) {
			objects.add(Dungeon.hero);
			names.add(Dungeon.hero.className().toUpperCase(Locale.ENGLISH));
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Mob mob = (Mob) Actor.findChar(cell);
				if (mob != null) {
					objects.add(mob);
					names.add(Messages.titleCase( mob.name ));
				}
			}
		}

		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null && heap.seen) {
			objects.add(heap);
			names.add(Messages.titleCase( heap.toString() ));
		}

		Plant plant = Dungeon.level.plants.get( cell );
		if (plant != null) {
			objects.add(plant);
			names.add(Messages.titleCase( plant.plantName ));
		}

		Trap trap = Dungeon.level.traps.get( cell );
		if (trap != null && trap.visible) {
			objects.add(trap);
			names.add(Messages.titleCase( trap.name ));
		}

		if (objects.isEmpty()) {
			GameScene.show(new WndInfoCell(cell));
		} else if (objects.size() == 1){
			examineObject(objects.get(0));
		} else {
			GameScene.show(new WndOptions(Messages.get(GameScene.class, "choose_examine"),
					Messages.get(GameScene.class, "multiple_examine"), names.toArray(new String[names.size()])){
				@Override
				protected void onSelect(int index) {
					examineObject(objects.get(index));
				}
			});

		}
	}

	public static void examineObject(Object o){
		if (o == Dungeon.hero){
			GameScene.show( new WndHero() );
		} else if ( o instanceof Mob ){
			GameScene.show(new WndInfoMob((Mob) o));
		} else if ( o instanceof Heap ){
			Heap heap = (Heap)o;
			if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
				GameScene.show(new WndTradeItem(heap, false));
			} else {
				GameScene.show(new WndInfoItem(heap));
			}
		} else if ( o instanceof Plant ){
			GameScene.show( new WndInfoPlant((Plant) o) );
		} else if ( o instanceof Trap ){
			GameScene.show( new WndInfoTrap((Trap) o));
		} else {
			GameScene.show( new WndMessage( Messages.get(GameScene.class, "dont_know") ) ) ;
		}
	}

	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
