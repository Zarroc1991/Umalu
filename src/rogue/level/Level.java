package rogue.level;

import jade.core.Actor;
import java.awt.Color;
import jade.core.World;
import jade.gen.Generator;
import jade.gen.map.*;
import jade.ui.Terminal;
import jade.ui.TiledTermPanel;
import java.util.logging.Logger;
import rogue.creature.*;
import jade.util.datatype.ColoredChar;
import java.lang.reflect.InvocationTargetException;
import rogue.creature.util.Item;

import rogue.system.SystemHelper;
public class Level extends World {
    private static Generator gen; /* = getLevelGenerator();*/
    public Level(int width, int height, Player player) {
        // Create a new Map, but make it 3 Rows less higher than window is, so we have some Space for
        // Statusmessages and stuff.
        super(width, height - 2);
        gen.generate(this);
        addActor(player);
    }
    // Zweiter Konstruktor, um ein neues Level aufzurufen

    public Level(int width, int height, Player player,int levelorder, int level,boolean aufwaerts, TiledTermPanel term) {
        super(width, height - 2);
        switch (levelorder) {											//Liste der Maps in Abhängikeit vom Level
            case 0: {
                gen = new World1(level,aufwaerts);
                break;
            }
            case 1: {
                gen = new World2(level,aufwaerts);
                break;
            }
	    case 2: {
		gen = new World3(level,aufwaerts);
		break;
	    }
	    case 3: {
		gen = new World4(level,aufwaerts);
		break;
	    }
	    case 4: {
		gen = new World5(level,aufwaerts);
		break;
	    }
	    case 5: {
		gen = new Arena();
		break;
	    }
            default: {
                gen = new World1(level,aufwaerts);
                break;
            }
        }
        gen.generate(this);
        //player starts at Playerstart
        this.addActor(player, playerstart);
        //insert Monster. We need new case-separation, because the Monsters should be added
        //after the tile, so they do not land on unpassable tiles
			switch (level) {											//Liste der Maps in Abhaengikeit vom Level  
			case 0: {
					//addActor(new Troll(term,level));
					addActors(Rat.class, term, 15);
					addActors(Slug_fat.class, term, 4);
					addActor(new Frog_poisonous(term));
					addActor(new ItemGenerator(ColoredChar.create('R', Color.yellow), new Item("Leichte R\u00fcstung", 0, Item.ITEMTYPE_HEAD, 0,0, 10,1), term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen",Item.ITEMTYPE_FOOD,31),term));
					
					
					
					break;
			}
			case 1: {
					//addActor(new Troll(term,level));
					addActors(Rat.class, term, 5);
					addActors(Slug_fat.class, term, 6);
					addActors(Frog_poisonous.class,term,5);
					addActors(Zombie.class,term,2);
					addActor(new ItemGenerator(ColoredChar.create('R', Color.yellow), new Item("Lederr\u00fcstung", 0, Item.ITEMTYPE_HEAD,0, 0, 30,1), term));
					break;
			}
			case 2: {
					//addActor(new Troll(term,level));
					addActors(Slug_fat.class, term, 7);
					addActors(Frog_poisonous.class, term, 7);
					addActors(Zombie.class,term,5);
					addActors(Unbeliever.class,term,5);
					addActor(new ItemGenerator(ColoredChar.create('R', Color.yellow), new Item("Kettenpanzer", 0, Item.ITEMTYPE_HEAD, 0,0, 50,1), term));
					break;
			}
			case 3:{
				addActors(Frog_poisonous.class, term, 7);
				addActors(Zombie.class,term,6);
				addActors(Unbeliever.class,term,6);
				addActors(Orc.class,term,2);
				addActor(new ItemGenerator(ColoredChar.create('R', Color.yellow), new Item("Eisenr\u00fcstung", 0, Item.ITEMTYPE_HEAD,0, 0, 80,1), term));       
				break;        
			}
			case 4:{
				addActors(Zombie.class,term,8);
				addActors(Unbeliever.class,term,7);
				addActors(Orc.class,term,6);
				addActors(Shadow.class,term,5);
				addActor(new ItemGenerator(ColoredChar.create('R', Color.yellow), new Item("Heilige R\u00fcstung", 0, Item.ITEMTYPE_HEAD,0, 0, 110,1), term));       
				break;        
			}
			case 5:{

				       addActor(new Dragon(term));
				       break;
			}
			default: {
					 break;
			}
		}
	}
    

    /*private static Generator getLevelGenerator() {
        return new World1();
    }*/
    // Delete following function?
    /*
     * adds {@ anzahl} actors of the class  {@cls}
     */
    public void addActors(Class<? extends Actor> cls,Terminal term, int anzahl){
        try {
            for (int i = 0; i < anzahl; i++) {
                //create a new Actor of the class with the constructor that requires term
                addActor(cls.getConstructor(Terminal.class).newInstance(term));
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex){
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex){
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

}
