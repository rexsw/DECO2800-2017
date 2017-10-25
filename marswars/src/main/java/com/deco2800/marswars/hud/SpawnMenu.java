package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.BuildWallAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

public class SpawnMenu {

    //hover sound
    Sound hover = Gdx.audio.newSound(Gdx.files.internal("sounds/hover.mp3"));

    private static final int NUMBER_OF_MENU_OPTIONS = 6; // the maximum number of buttons in a row

    private static Stage stage; // the stage of the game
    private static Skin skin; // the skin for the menu
    // the game's texture manager
    private TextureManager textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);
    // the game's time manager
    private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);

    private static Window entitiesPicker; //window that selects available entities

    private BaseEntity selectedEntity;	//for differentiating the entity selected
    
    private String entityTypesString = "Entity Types\n (Back)";

    /**
     * The constructor of the spawn menu.
     *
     * @param stage the stage where the menu will seat.
     * @param skin the skin to be used on the menu.
     */
    SpawnMenu(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    /**
     *  Creates the basic structure of the Entities picker menu
     */
     static void setupEntitiesPickerMenu(){
        entitiesPicker = new Window("Spawn", skin);
        entitiesPicker.align(Align.topLeft);
        entitiesPicker.setPosition(220,0);
        entitiesPicker.setMovable(false);
        entitiesPicker.setVisible(false);
        entitiesPicker.setWidth(stage.getWidth()-220);
        entitiesPicker.setHeight(150);
    }

    /**
     * Add the customise window / entities picker.
     * This method shall only be called when the "Customize" button from the start menu is clicked.
     * If this method is call, it will cause that the actions window be set to not visible.
     *
     */
     void addEntitiesPickerMenu(){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText("Customize");

        Table table = new Table();
        TextButton unitsButton = new TextButton("Units",skin);
        unitsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addUnitsPickerMenu(false);
            }
        });
        TextButton buildingsButton = new TextButton("Buildings",skin);
        buildingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addBuildingsPickerMenu(true);
            }
        });
        TextButton resourcesButton = new TextButton("Resources",skin);
        resourcesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addResourcesPickerMenu();
            }
        });
        TextButton terrainsButton = new TextButton("Terrains",skin);
        terrainsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addTerrainsPickerMenu();
            }
        });

        TextButton mapButton = new TextButton("Maps",skin);
        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addMapTypesPickerMenu();
            }
        });
        TextButton sizeButton = new TextButton("World Size",skin);
        sizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addMapSizesPickerMenu();
            }
        });

        table.add(unitsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(buildingsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(resourcesButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(terrainsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(mapButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(sizeButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        entitiesPicker.add(table);
        stage.addActor(entitiesPicker);

    }

    /**
     * Creates the sub menu that displays all available units
     *
     * @param inGame whether is being used during a game play
     */
     private void addUnitsPickerMenu(boolean inGame){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText("Spawn Units");

        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
        table.align(Align.left);
        if(!inGame) {
            TextButton entitiesButton = new TextButton(entityTypesString, skin);
            entitiesButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    addEntitiesPickerMenu();
                    entitiesPicker.setVisible(true);
                }
            });
            table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
        }

        TextButton astronautButton = new TextButton("Astronaut",skin);
        astronautButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.get().getWorld().addEntity( new Astronaut(0,0,0,1));
            };
        });

        TextButton carrierButton = new TextButton("Carrier",skin);
        TextButton healerButton = new TextButton("Healer",skin);
        TextButton heroSpacmanButton = new TextButton("Hero Spacman",skin);
        TextButton soldierButton = new TextButton("Soldier",skin);
        TextButton spacmanButton = new TextButton("Spacman",skin);
        TextButton tankButton = new TextButton("Tank",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        table.add(astronautButton).width(buttonWidth).height(buttonHeight);
        table.add(carrierButton).width(buttonWidth).height(buttonHeight);
        table.add(healerButton).width(buttonWidth).height(buttonHeight);
        table.add(heroSpacmanButton).width(buttonWidth).height(buttonHeight);
        table.add(soldierButton).width(buttonWidth).height(buttonHeight);
        table.row();
        table.add(spacmanButton).width(buttonWidth).height(buttonHeight);
        table.add(tankButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
    }

    /**
     * Creates the sub menu that displays all available buildings
     * @param addBack allows to go back to menu options if true
     */
    private void addBuildingsPickerMenu(boolean addBack){
        entitiesPicker.clear();
        Table table = new Table();
        table.align(Align.left);
        TextButton entitiesButton = new TextButton(entityTypesString,skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();
        int columns = 0;
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);
        if(addBack) {
            table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
            columns = 1;
        }
        // Adds all build buttons and listeners for each available building
        for (BuildingType b : BuildingType.values()) {
            selectedEntity = GameManager.get().getGui().getSelectedEntity();
            Button formatPane = new Button(skin);
            Texture entity = textureManager.getTexture(b.getBuildTexture());
            TextureRegion entityRegion = new TextureRegion(entity);
            TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
            ImageButton addPane = new ImageButton(buildPreview);
            formatPane.add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
            formatPane.row().padBottom(20);
            formatPane.add(new Label(b.toString(),skin)).align(Align.left).padLeft(10);
            formatPane.add(new Label(String.valueOf(b.getCost()),skin)).align(Align.left);
            Texture rockTex = textureManager.getTexture("rock_HUD");
            Image rock = new Image(rockTex);
            formatPane.add(rock).width(40).height(40).padBottom(30).align(Align.left);
            formatPane.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    if(selectedEntity == null){
                        // needs to be changed. Shouldn't need an entity selected to be able  to place a building
                        return;
                    }
                    if(selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildWallAction) {
                        BuildWallAction cancelBuild = (BuildWallAction)selectedEntity.getAction().get();
                        cancelBuild.cancelBuild();
                        cancelBuild.doAction();
                    }else if(selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildAction) {
                        BuildAction cancelBuild = (BuildAction)selectedEntity.getAction().get();
                        cancelBuild.cancelBuild();
                        cancelBuild.doAction();
                    }
                    selectedEntity.setAction(new BuildAction(selectedEntity, b));
                }
            });
            table.add(formatPane).width(buttonWidth).height(buttonHeight).align(Align.center);
            columns++;
            if(columns % NUMBER_OF_MENU_OPTIONS == 0){
                table.row();
            }

        }
        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }


    /**
     * Creates the sub menu that displays all available resources
     *
     */
    private void addResourcesPickerMenu(){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText("Spawn Resources");

        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
        table.align(Align.left);
        TextButton entitiesButton = new TextButton(entityTypesString, skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton biomassButton = new TextButton("Biomass",skin);
        TextButton crystalButton = new TextButton("Crystal",skin);
        TextButton rockButton = new TextButton("Rock",skin);
        TextButton waterButton = new TextButton("Water",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(biomassButton).width(buttonWidth).height(buttonHeight);
        table.add(crystalButton).width(buttonWidth).height(buttonHeight);
        table.add(rockButton).width(buttonWidth).height(buttonHeight);
        table.add(waterButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Creates the sub menu that displays all available terrains
     *
     */
    private void addTerrainsPickerMenu(){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText(" Generate Terrains");

        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
        table.align(Align.left);
        TextButton entitiesButton = new TextButton(entityTypesString, skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton caveButton = new TextButton("Cave",skin);
        TextButton lakeButton = new TextButton("Lake",skin);
        TextButton pondButton = new TextButton("Pond",skin);
        TextButton quicksandButton = new TextButton("Quicksand",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(caveButton).width(buttonWidth).height(buttonHeight);
        table.add(lakeButton).width(buttonWidth).height(buttonHeight);
        table.add(pondButton).width(buttonWidth).height(buttonHeight);
        table.add(quicksandButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Populate the menu with map size options
     */
    private void addMapSizesPickerMenu(){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText(" Choose Map Size");

        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
        table.align(Align.left);
        TextButton entitiesButton = new TextButton(entityTypesString, skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton tinyButton = new TextButton("Tiny",skin);
        sizeButtonHandler(tinyButton, MapSizeTypes.TINY);
        TextButton smallButton = new TextButton("Small",skin);
        sizeButtonHandler(smallButton, MapSizeTypes.SMALL);
        TextButton mediumButton = new TextButton("Medium",skin);
        sizeButtonHandler(mediumButton, MapSizeTypes.MEDIUM);
        TextButton largeButton = new TextButton("Large",skin);
        sizeButtonHandler(largeButton, MapSizeTypes.LARGE);
        TextButton veryLargeButton = new TextButton("Very Large",skin);
        sizeButtonHandler(veryLargeButton, MapSizeTypes.VERY_LARGE);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(tinyButton).width(buttonWidth).height(buttonHeight);
        table.add(smallButton).width(buttonWidth).height(buttonHeight);
        table.add(mediumButton).width(buttonWidth).height(buttonHeight);
        table.add(largeButton).width(buttonWidth).height(buttonHeight);
        table.add(veryLargeButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
    }

    /**
     * Populate the menu with map types options
     */
    private void addMapTypesPickerMenu(){
        entitiesPicker.clear();
        entitiesPicker.getTitleLabel().setText(" Choose Map");

        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
        table.align(Align.left);
        TextButton entitiesButton = new TextButton(entityTypesString, skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton marsButton = new TextButton("Mars",skin);
        typesButtonHandler(marsButton, MapTypes.MARS);
        TextButton moonButton = new TextButton("Moon",skin);
        typesButtonHandler(moonButton, MapTypes.MOON);
        TextButton sunButton = new TextButton("Sun",skin);
        typesButtonHandler(sunButton, MapTypes.SUN);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(marsButton).width(buttonWidth).height(buttonHeight);
        table.add(moonButton).width(buttonWidth).height(buttonHeight);
        table.add(sunButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
    }

    /**
     * Handler for the size buttons of the customization menu
     *
     * @param button the button that will use the handler
     * @param mapSize the new map size
     */
    private void sizeButtonHandler(TextButton button, MapSizeTypes mapSize){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CustomizedWorld oldWorld = (CustomizedWorld) GameManager.get().getWorld();
                MapContainer map = new MapContainer(oldWorld.getMapType(), mapSize);
                CustomizedWorld world = new CustomizedWorld(map);
                GameManager.get().setWorld(world);
                world.loadMapContainer(map);
            }
        });
    }

    /**
     * Handler for the map types buttons of the customization menu
     *
     * @param button the button that will use the handler
     * @param mapType the new map type
     */
    private void typesButtonHandler(TextButton button, MapTypes mapType){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CustomizedWorld oldWorld = (CustomizedWorld) GameManager.get().getWorld();
                MapContainer map = new MapContainer(mapType, oldWorld.getMapSizeType());
                CustomizedWorld world = new CustomizedWorld(map);
                GameManager.get().setWorld(world);
                world.loadMapContainer(map);
            }
        });
    }

    /**
     * Displays the entities picker menu.
     * If picker is shown then fog is off and game is paused
     *
     * @param isVisible whether to display the picker or hide it.
     * @param isPlaying whether a game is being played.
     */
    public void showEntitiesPicker( boolean isVisible, boolean isPlaying){
        entitiesPicker.setVisible(isVisible);
        // this call allows the menu to reset instead of using its latest state
        addEntitiesPickerMenu();
        if(!isPlaying) {
            entitiesPicker.setVisible(true);
            FogManager.toggleFog(false);
            timeManager.pause();

        }
    }
}
