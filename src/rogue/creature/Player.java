package rogue.creature;

import jade.core.Actor;
import java.util.Collection;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import rogue.system.HelpScreen;
import rogue.level.Screen;
import rogue.creature.util.Inventory;
import rogue.creature.util.Item;
import rogue.creature.util.NotEnoughSpaceException;
import java.util.Random;
import java.awt.Color;
import java.lang.InterruptedException;
import jade.gen.map.ItemGenerator;
import java.util.ArrayList;
import rogue.system.SystemHelper;

/**
 * Represents Player
 */
public class Player extends Creature implements Camera {

	private Terminal term;
	private ViewField fov;
    private static double maxHitpointsWithoutArmor = 15;
    private static final int strengthConstantWithoutArmor = 1;
    private static final int strengthRandomWithoutArmor = 4;
    private int full;
	private static double maxHitpoints;
    private String name;
	private Inventory inventory;
	public Boolean worldchangedown = false;   // standardmäßig ist keine Mapänderung erfolgt
	public Boolean worldchangeup = false; 
	private int hpCycle=10;
	public int round=0;

	/**
	 * Creates a new Player Object
	 * 
	 * @param term
	 *            Currently used Terminalobject
	 */
	public Player(Terminal term) {
		// Put Charactersymbol on Screen
		super(ColoredChar.create('@'));
		// Save Terminal
		this.term = term;
		fov = new RayCaster();
		// Initialise Hitpoints on Max
                if (SystemHelper.debug) {
                	maxHitpointsWithoutArmor = maxHitpointsWithoutArmor * 1000000;
                }
        maxHitpoints = maxHitpointsWithoutArmor;
		hitpoints = maxHitpoints;
		strength_constant=strengthConstantWithoutArmor;
		strength_random = strengthRandomWithoutArmor;
		full=100;
        inventory = new Inventory(5,10,50);
                

		

	}

	/**
	 * Sets Charactername. Should be only called on character Creation.
	 * 
	 * @param name
	 *            New Name of Character
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns Charactername.
	 * 
	 * @return Name of Character
	 */
	public String getName() {
		return name;
	}
    public Inventory getInventory(){
            return inventory;
        }
    
    public int getFull(){
        return full;
    }
	@Override
	/**
	 * Ask Player to do some action (passing him the baton). Reads input and moves Character accordingly.
	 */
	public void act() {
		try {
			round++;
			regainHitpoints();
			if (full<=20){
				reduceHitpoints();
			}
			//System.out.println();
			// Get pressed char
			char key;
			key = term.getKey();
			switch (key) {
			case 'b': // User wants to quit//beenden spÃ¤ter auf esc 
				confirmQuit(); // Leave let player die, so this application quits
				break;
			case 'i': // Show Inventory
				showInventoryScreen();
                                updateHP();
                                updateStrength();

				break;
			case 'o':
				HelpScreen.printMainHelpScreen();
				break;
			case 'h':
				Screen.showEventLog();
				term.getKey();
				break;
			default: // User pressed something else
				Direction dir = Direction.keyToDir(key); // Get direction
				
				// Something useful pressed?
				if (dir != null) { // Yes
					// Get list of all monsters on target Coordinates
					Collection<Monster> actorlist = world().getActorsAt(Monster.class, x() + dir.dx(), y() + dir.dy());
					// Is there a monster on TargetL
					if (!actorlist.isEmpty()) { // Yes
						// Fight first monster on coordinate.
						fight((Monster) actorlist.toArray()[0]);
						inventory.decreaseStability();
                                                updateHP();
                                                updateStrength();
					} else {
						if (world().tileAt(x() + dir.dx(), y() + dir.dy()) == ColoredChar.create('\u00a9')) {  
							Screen.redrawEventLine("M\u00f6chtest du diesen Raum verlassen? Dr\u00fccke j f\u00fcr Ja, ansonsten verweilst du hier.", false);//Stellt fest, dass eine Tür gefunden wurde und somit eine Mapänderung erfolgt
							for(Coordinate coord: getViewField()){
								world().viewable(coord.x(), coord.y());}
							if (term.getKey()=='j'){
								worldchangeup= true;
								move(dir);}
							else{
								move(0,0); 
								}}
							else if(world().tileAt(x() + dir.dx(), y() + dir.dy()) == ColoredChar.create('\u00ae')) {  
									Screen.redrawEventLine("M\u00f6chtest du diesen Raum verlassen? Dr\u00fccke j für Ja, ansonsten verweilst du hier.", false);//Stellt fest, dass eine Tür gefunden wurde und somit eine Mapänderung erfolgt
									for(Coordinate coord: getViewField()){
										world().viewable(coord.x(), coord.y());}
									if (term.getKey()=='j'){
										worldchangedown= true;
										move(dir);}
									else{
										move(0,0); 
										}
						} else {// No monster there
							for(Coordinate coord: getViewField()){				//macht alles sichtbar, was im Field of View ist
								world().viewable(coord.x(), coord.y());}
								
                                                        move(dir);
                                                        if(round%5==0){
                                                        	reduceFull();
                                                        }
                                                        Actor itemGen= world().getActorAt(ItemGenerator.class, pos());
                                                        if(itemGen!=null){
                                                           itemGen.act();
                                                        }
						
							
							
							break;
						}
					}

				}
			}
		} catch (InterruptedException e) { // Something has happened here
			System.out.println("!Interrupted Exception");
			e.printStackTrace();
		}
	}

	public void confirmQuit() {
		Screen.redrawEventLine("Sicher dass Adventures in Umalu beendet werden soll? <J>a/<N>ein",false);
		try {
			if (term.getKey() == 'j') {
				expire();
			}
		} catch (InterruptedException e) {
			System.out.println("!Interrupted Exception");
			e.printStackTrace();
		}
	}

	//@Override
	/**
	 * Get what is visible
	 *
	 * @return A collection of visible Items
	 */
	public Collection<Coordinate> getViewField() {
		return fov.getViewFieldplayer(world(), pos().x(),pos().y(), 2); //hab mal den Sichtbarkeitsradius verkleinert, damit es spannender ist
	} 

    /**
     * Player fights the opponent. Causes random damage between 1 and strength	 *
     * @param opponent
     *            The opponent Monster
     */

    private void fight(Monster opponent) {
        System.out.println("Du kämpfst gegen " + opponent.name());
        // Get random Damage for Attack
        reduceFull();
        int damage =makeDamage(strength_constant, strength_random);//random.nextInt(strength) + 1;
        // Print result

        System.out.println("Du hast " + damage + " Schaden verursacht");
        System.out.println(opponent.name() + " hat noch " + opponent.hitpoints
                + " HP");
        Screen.redrawEventLine("Du verursachst " + damage + " Schaden");
       
        // Do Damage to Opponent
        boolean opponentDied = opponent.loseHitpoints(damage);
        
        try {
            term.getKey();
            if (opponentDied) {
                //wait for key to continue on Status message
                randomlyDropItem(opponent);
            }       
        } catch (InterruptedException e) {
            System.out.println("!InterruptedException");
            e.printStackTrace();
        }
    }

    /**
     * Player regains 10% Hitpoints. 
     */
    public void regainHitpoints() {
    	//alle Cycle viele Runden oder Kirche
    	if(((round % hpCycle)==0&&full>=70)||world().tileAt(pos())==ColoredChar.create('\u2020', new Color(199,21,133))	){
    	// Is there something to heal
    	if (hitpoints < maxHitpoints) {
            // Gain Healthpoint back
            hitpoints=hitpoints+0.1*maxHitpoints;
            if (hitpoints>maxHitpoints){
            	hitpoints=maxHitpoints;
            }
            // Print Eventline
            Screen.redrawEventLine("Du regenerierst Hitpoints.");

        }
    }
    }
    public void reduceHitpoints() {
    	if (hitpoints > 0) {
            // Gain Healthpoint back
            hitpoints=hitpoints-0.01*maxHitpoints;
            if (hitpoints<0){
            	hitpoints=0;
            }
			Screen.redrawEventLine("Du bist sehr hungrig und verlierst Hitpoints.");       
    	}
    	checkHitpoints();
    }
    
    public void reduceFull(){
    	if(full>0){
    		full--;
    	}
    }
    public void increaseFull(int foodValue){
    	full=full+foodValue;
    	if(full>100){
    		full=100;
    	}
    }

    public double getHitpoints() {
        return hitpoints;
    }

    public double getMaxHitpoints() {
        return maxHitpoints;
    }

    /**
     * Creates and prints an Inventory Screen
     */
    public void showInventoryScreen() {
        boolean loop = true;
        // Inventar geschlossen?
        while (loop) { // Nein.
            // Erstelle eine ArrayList von Strings um dort unser Inventarinterface zu puffern
            ArrayList<String> lines = new ArrayList<String>();
            // Erstelle eine Titelzeile
            lines.add("Inventar");
            lines.add(" ");
            // Zeige was der Nutzer gerade angelegt hat
            lines.add("Du traegst: ");
            lines.add(" ");
            // Lade die Liste
            Item[] wornItems = inventory.getWornItems();
            // Generiere den Output fuer den aktuellen Helm
			if (wornItems[Item.ITEMTYPE_HEAD] != null) {
				lines.add(makeRightString("<K>\u00f6rper:  "
						+wornItems[Item.ITEMTYPE_HEAD].getName(),40) 
						+ "+HP: " + wornItems[Item.ITEMTYPE_HEAD].getHealthBonus());
			} else {
				lines.add("<K>\u00f6rper:  Nichts.");
			}//else
            // Generiere den Output fuer das aktuelle Schwert
			if (wornItems[Item.ITEMTYPE_SWORD] != null) {
				lines.add(makeRightString("<S>chwert: " 
			+ wornItems[Item.ITEMTYPE_SWORD].getName(),40) 
			+ "+DMG: " 		+ wornItems[Item.ITEMTYPE_SWORD].getDamageConstantBonus() 
			+ "-" 			+ (wornItems[Item.ITEMTYPE_SWORD].getDamageRandomBonus()+wornItems[Item.ITEMTYPE_SWORD].getDamageConstantBonus()) 
			+ ", Dura: "	+ wornItems[Item.ITEMTYPE_SWORD].getDurability()
			+"/"			+ wornItems[Item.ITEMTYPE_SWORD].getMaxDurability());
			} else {
				lines.add("<S>chwert: Keines");
			}//else
            
            // Zeige an, was sonst noch im Inventar liegt, aber nicht angelegt wurde (und somit keinen Bonus bringt)
            ArrayList<Item> backpack = inventory.listBackpack();
            lines.add(" ");
            lines.add("Du hast im Rucksack: ");
            lines.add(" ");
            for (int i = 0; i < backpack.size(); i++) {
                // Zeige das Item an Stelle an i an
                lines.add(makeRightString("(" + i + ") " + backpack.get(i).getName(),30) 
                		+ "[+DMG: " + backpack.get(i).getDamageConstantBonus()
                		+"-"		+ (backpack.get(i).getDamageRandomBonus()+backpack.get(i).getDamageConstantBonus())
                		+ ", +HP: " + backpack.get(i).getHealthBonus()
                		+ ", Dura: "+ backpack.get(i).getDurability()
                		+ "/"		+ backpack.get(i).getMaxDurability()+"]");
            }//for
			lines.add("");
			lines.add("<l> Lunch Box aufrufen");
			lines.add(" ");
			lines.add("<q>/<i> Inventar verlassen, <o> Hilfe aufrufen");
            Screen.putText(lines);
            try {
                // Erwarte eine Eingabe vom Nutzer.
                char key = term.getKey();
                switch (key) {
					case 'i':
                    case 'q':
                        loop = false;
                        break;
                    case 'k':
                        //wornItems[Item.ITEMTYPE_HEAD].showInfo(term,inventory);
						if (wornItems[Item.ITEMTYPE_HEAD] != null) {
							inventory.showWorn(Item.ITEMTYPE_HEAD, term);
						}
                        break;
                    case 's':
                        //wornItems[Item.ITEMTYPE_SWORD].showInfo();
						if (wornItems[Item.ITEMTYPE_SWORD] != null) {
							inventory.showWorn(Item.ITEMTYPE_SWORD, term);
						}
                        break;
                    case '0':
                        inventory.showInfo(0, term);
                        break;
                    case '1':
                        inventory.showInfo(1, term);
                        break;
                    case '2':
                        inventory.showInfo(2, term);
                        break;
                    case '3':
                        inventory.showInfo(3, term);
                        break;
                    case '4':
                        inventory.showInfo(4, term);
                        break;
                    case 'o':
                        HelpScreen.printInventoryHelpScreen();
                        break;
                    case 'l':
                    	showLunchbox();
                    	loop=false;
                    	break;

                }//switch
            } catch (InterruptedException e) {
                System.out.println("!Exeception");
                e.printStackTrace();
            }catch (IndexOutOfBoundsException e){
            	System.out.println("IndexOutOfBounds");
            }//catch
        }//while(loop)
        // Inventar verlassen, zeichne wieder die Karte.
        Screen.redrawMap();
    }//showInventoryScreen
    
	public void showLunchbox() {
        boolean loop = true;
        // Lunchbox geschlossen?
        String food="";
        while (loop) { // Nein.
            // Erstelle eine ArrayList von Strings um dort unser Lunchboxinterface zu puffern
            ArrayList<String> lines = new ArrayList<String>();
            lines.add("Lunchbox");
            lines.add("");
            lines.add("Nahrung:"+getFull()+"/100");
            lines.add("");
            // Zeige an, was sonst noch im Inventar liegt, aber nicht angelegt wurde (und somit keinen Bonus bringt)
            ArrayList<Item> lunchbox = inventory.listLunchbox();
            for (int i = 0; i < lunchbox.size(); i++) {
                // Zeige das Item an Stelle an i an
                lines.add(makeRightString("(" + i + ") " + lunchbox.get(i).getName(),30) 
                		+ "Nährwert: "+lunchbox.get(i).getFoodValue()
                		+ " Anzahl: "  +lunchbox.get(i).getNumber());
			}
            lines.add("");
            lines.add("<o> Lunchbox Hilfe aufrufen");
			lines.add("<q> Lunchbox verlassen");
			lines.add("");
			lines.add(food);
            Screen.putText(lines);
            try {
                // Erwarte eine Eingabe vom Nutzer.
                char key = term.getKey();
                switch (key) {
                    case 'q':
                        loop = false;
                        break;
                    case '0':
                    	food=eatFood(lunchbox,0);
                        break;
                    case '1':
                    	food=eatFood(lunchbox,1);
                        break;
                    case '2':
                    	food=eatFood(lunchbox,2);
                        break;
                    case '3':
                    	food=eatFood(lunchbox,3);
                        break;
                    case '4':
                    	food=eatFood(lunchbox,4);
                        break;
                    case '5':
                    	food=eatFood(lunchbox,5);
                        break;
                    case '6':
                    	food=eatFood(lunchbox,6);
                        break;
                    case '7':
                    	food=eatFood(lunchbox,7);
                        break;
                    case '8':
                    	food=eatFood(lunchbox,8);
                        break;
                    case '9':
                    	food=eatFood(lunchbox,9);
                    	break;
                    case 'o':
                        HelpScreen.printLunchboxHelpScreen();
                        break;
                }//switch
            } catch (InterruptedException e) {
                System.out.println("!Exeception");
                e.printStackTrace();
            }catch (IndexOutOfBoundsException e){
            	System.out.println("IndexOutOfBounds");
            }//catch
	}//while
    showInventoryScreen();
}//showLunchbox
	
	private String eatFood(ArrayList<Item> lunchbox,int i){
		String name=lunchbox.get(i).getName();
		increaseFull(lunchbox.get(i).getFoodValue());
		if(lunchbox.get(i).getNumber()>1){
        	lunchbox.get(i).setNumber(lunchbox.get(i).getNumber()-1);	
        	}else{
        	inventory.removeItem(lunchbox.get(i));}
		return("Du hast "+name+ " gegessen.");
	}
    
    private void randomlyDropItem(Monster opponent) {
        Random random = new Random();
		Screen.redrawEventLine(opponent.name+" stirbt");
		System.out.println(opponent.name+" stirbt");
        //This Item drops;
        Item item = null;
		ArrayList<String> akragonLines = new ArrayList<String>();
		akragonLines.add("Akragon war einst ein großer Krieger, der beim ersten Auftauchen des");
		akragonLines.add("Drachens durch die Zerst\u00f6rung der Stadt am Fuße des Berges Umalu");
		akragonLines.add("seine Familie verlor. Seit er losgezogen ist, um Rache zu nehmen, hat");
		akragonLines.add("niemand je wieder von ihm gehört.");
		akragonLines.add("Das Blut an dieser Waffe scheint sehr alt zu sein.");
		ArrayList<String> dumbaronLines = new ArrayList<String>();
		dumbaronLines.add("Dumbaron war Akragons Neffe, der, nachdem er viele K\u00e4mpfe in fremden");
		dumbaronLines.add("Landen bestanden hatte, zur\u00fcck nach Umalu kam nur um zu erfahren,");
		dumbaronLines.add("dass seine Familie tot und sein Onkel verschollen war. Er zog los,");
		dumbaronLines.add("um seinen Onkel zu finden und ward seid diesem Tag nicht mehr gesehen.");
		ArrayList<String> kunkranLines = new ArrayList<String>();
		kunkranLines.add("Der legendäre Waffenschmied Kunkran schmiedete einst ein Schwert im Auftrag des");
		kunkranLines.add("K\u00f6nigs, mit dem der Drache get\u00f6tet werden sollte, dem er diesen Namen gab. ");
		kunkranLines.add("Jedoch war die Belohnung so gro\u00df, dass sie bei dessen Lehrling die Gier weckte.");
		kunkranLines.add("Daher stahl dieser nicht nicht nur das Schwert bevor er seinen Meister t\u00f6tete,");
		kunkranLines.add("sondern stellte auch billige Kopien her, die er dann teuer verkaufte bevor er aus der");
		kunkranLines.add("Stadt verschwand.");
		kunkranLines.add("");
		kunkranLines.add("Sei daher vorsichtig mit diesem Schwert. Vielleicht ist es nur eine dieser Kopien,");
		kunkranLines.add("denen die Speziallakierung fehlt, deren Geheimnis der Schmiedemeister mit ins Grab");
		kunkranLines.add("nahm und die trotz ihrer Mächtigkeit sehr schnell bersten!");
        try {

            switch (opponent.typenumber) {

                case 1: {
                    //Rat, doesnt drop Weapons according to balance
                    System.out.println("Ratte lässt nichts fallen");
                    //addActor(new ItemGenerator(ColoredChar.create('F', Color.orange), new Item("TestEssen12",Item.ITEMTYPE_FOOD,12),term));
                    break;
                }
                case 2: {
                    //Fette Nacktschnecke
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(3);
                    if (zufallszahl == 0) {
                        //Axt drops 1/3 of the time
                        item = new Item("Axt", 0, Item.ITEMTYPE_SWORD, 0,1, 0,30);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast eine Axt bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();
                       
                    }

                    break;
                }
                case 3: {
                    //Giftiger Frosch
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(20);

                    if (zufallszahl == 0) {
                        //Langschwert droppt zu 1/20
                        item = new Item("Langschwert", 0, Item.ITEMTYPE_SWORD, 2,4, 0,10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Langschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl <= 4) {
                        //Kurzschwert droppt zu 1/5
                        item = new Item("Kurzschwert", 0, Item.ITEMTYPE_SWORD, 1,0, 0,15);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Kurzschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;

                }
                case 4: {
                    //Zombie
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(20);

                    if (zufallszahl < 5) {
                        //Langschwert droppt zu 1/4
                        item = new Item("Langschwert", 0, Item.ITEMTYPE_SWORD, 2,4, 0, 10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Langschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey(); 
                        

                    } else if (zufallszahl < 9) {
                        //Riesenschwert droppt zu 1/5
                        item = new Item("Riesenschwert", 0, Item.ITEMTYPE_SWORD, 4,10, 0,8);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Riesenschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;
                }
                case 5: {
                    //Unbeliever
                   //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(60);

                    if (zufallszahl < 15) {
                        //Riesenschwert droppt zu 1/4
                        item = new Item("Riesenschwert", 0, Item.ITEMTYPE_SWORD, 4,10, 0,8);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Riesenschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl < 25) {
                        //GroÃŸschwert droppt zu 1/6
                        item = new Item("Gro\u00dfschwert", 0, Item.ITEMTYPE_SWORD, 9,0, 0,25);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Gro\u00dfschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }else if (zufallszahl < 31) {
                        //Akragons Relikt droppt zu 1/10
                        item = new Item("Akragons Relikt", 0, Item.ITEMTYPE_SWORD, 12,0, 0, akragonLines, 10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Akragons Relikt bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }else if (zufallszahl < 36) {
                        //Dumbarons Kolossschwert droppt zu 1/12
                        item = new Item("Dumbarons Kolossschwert", 0, Item.ITEMTYPE_SWORD, 0,27, 0,dumbaronLines,10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Dumbarons Kolossschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;
                }
                case 6: {
                    //Orc
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(15);

                    if (zufallszahl < 1) {
                        //Kunkrans DrachtentÃ¶ter droppt zu 1/15
                        item = new Item("Kunkrans Drachtent\u00f6ter", 0, Item.ITEMTYPE_SWORD,0, 45, 0,kunkranLines,4);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Kunkranks Drachent\u00f6 bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl < 6) {
                        //GroÃŸschwert droppt zu 1/3
                        item = new Item("Gro\u00dfschwert", 0, Item.ITEMTYPE_SWORD, 9,0, 0,25);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Gro\u00dfschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }else if (zufallszahl < 9) {
                        //Akragons Relikt droppt zu 1/5
                        item = new Item("Akragons Relikt", 0, Item.ITEMTYPE_SWORD, 12,0, 0, akragonLines, 10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast Akragons Relikt bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }else if (zufallszahl < 12) {
                        //Dumbarons Kolossschwert droppt zu 1/5
                        item = new Item("Dumbarons Kolossschwert", 0, Item.ITEMTYPE_SWORD, 0,27, 0,dumbaronLines, 10);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Dumbarons Kolossschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;
                }
                case 7: {
                    //Shadow
                	int zufallszahl = random.nextInt(5);
                	if (zufallszahl < 1) {
                        //Kunkrans Drachtentöter droppt zu 1/5
                        item = new Item("Kunkrans Drachtent\u00f6ter", 0, Item.ITEMTYPE_SWORD, 0,45, 0,kunkranLines,4);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Kunkrans Drachtent\u00f6ter bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();
                     }else if(zufallszahl < 2){
                            //Munkrans Drachtentöter droppt zu 1/5
                            item = new Item("Munkrans Drachtent\u00f6ter", 0, Item.ITEMTYPE_SWORD, 19,7, 0,kunkranLines,4);
                            inventory.addItem(item);
                            //Status message
                            Screen.redrawEventLine("Du hast ein Munkrans Drachtent\u00f6ter bekommen, druecke i, um das Inventar zu oeffnen");
                            //Wait for pressed key
                            term.getKey();
                            }
                    break;
                 }
                case 99: {
                    //Dragon
                    //droppt nichts
                }
                default: {
                    break;
                }
            }
        } catch (NotEnoughSpaceException ex) {
            
                //Status message
                //Screen.redrawEventLine("Du konntest leider ein" + item.getName() + " nicht ins Inventar aufnehmen, da es voll war");
                //Wait for pressed key
               // term.getKey();
                inventory.fullInventoryScreen(item);
             

        } catch (InterruptedException e) {
            System.out.println("!IOException");
            e.printStackTrace();
        }


    }

    public void updateHP(int newBonusHP){
        double relativeHP = (hitpoints) /(maxHitpoints);
        maxHitpoints = maxHitpointsWithoutArmor+newBonusHP;
        double newHP = Math.round(relativeHP*maxHitpoints);
        hitpoints = newHP;
    }
    public void updateHP(){
        updateHP(inventory.getHealthBonus());
    }
    public void updateStrength(int newBonusStrengthConstant, int newBonusStrengthRandom){
        strength_constant= newBonusStrengthConstant+strengthConstantWithoutArmor;
        strength_random= newBonusStrengthRandom+strengthRandomWithoutArmor;
    }
    public void updateStrength(){
        updateStrength(inventory.getBonusDamageConstantOfWornItems(),inventory.getBonusDamageRandomOfWornItems());
    }

    public String makeRightString(String a,int len){
    	String longString=a;
    	while (longString.length()<len){
    		longString=longString+" ";
    	}//while
    	if (longString.length()>len){
    		longString=longString.substring(0,len);
    	}
    	return(longString);
    }//makeRightString
    
    public String getItemLine(){
    	Item[] wornItems = inventory.getWornItems();
    	String sword;
    	String b="";
    	String body;
    	String d="";
    	if (wornItems[Item.ITEMTYPE_SWORD] != null){
    		sword=wornItems[Item.ITEMTYPE_SWORD].getName();
    		b="(" +wornItems[Item.ITEMTYPE_SWORD].getDurability()+"/"+wornItems[Item.ITEMTYPE_SWORD].getMaxDurability()+")";
    	}else{
    		sword="Keine";
    	}
    	if (wornItems[Item.ITEMTYPE_HEAD] !=null){
    		body=wornItems[Item.ITEMTYPE_HEAD].getName();
    		d="(+" + wornItems[Item.ITEMTYPE_HEAD].getHealthBonus()+")";
    	}else{
    		body="Keine";
    	}
    	
    	String a="Waffe: "+sword;
    	String c="Rüstung: "+body;


    	return(makeRightString(a,32)+makeRightString(b,8)+makeRightString(c,32)+makeRightString(d,8));
    }
}



