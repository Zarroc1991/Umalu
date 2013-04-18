package rogue.creature;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import rogue.Rogue;
import rogue.creature.util.Item;
import rogue.creature.util.NotEnoughSpaceException;
import rogue.level.Screen;
import rogue.system.Path;
import jade.gen.map.ItemGenerator;
import jade.util.datatype.ColoredChar;
import jade.ui.Terminal;
import jade.ui.TiledTermPanel;

public abstract class Monster extends Creature {

    protected String name;
    private int maxHitpoints;
    protected Terminal term;
    // Typenumber is used to recognize, wich Sort of Monster we have
    protected  int typenumber;
    public Monster(ColoredChar face) {
        super(face);
        strength_constant=5;
        strength_random=5;
        maxHitpoints = 10;
        hitpoints = maxHitpoints;
    }

    public Monster(ColoredChar face, String name,int maxHitpoints, int strength_constant,int strength_random, Terminal term) {
        super(face);
        this.strength_constant = strength_constant;
        this.strength_random = strength_random;
        this.maxHitpoints= maxHitpoints;
        hitpoints = maxHitpoints;
        this.name = name;
	this.term = term;
    }

    public String name() {
        return name;
    }


     /* fight of the Moster aganst the Player
     * causes random damage between 1 and 5
     */
    // TODO Clean up Messages in Console, to use just a single line
    abstract public void fight(Player opponent); 
    public void dropItem(Monster opponent,TiledTermPanel term) throws InterruptedException{
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
        
		switch (opponent.typenumber) {

                case 1: {
                    //Rat, doesnt drop Weapons according to balance
                    item =new Item("Rattenfleisch",Item.ITEMTYPE_FOOD,5);
                    world().addActor(new ItemGenerator(ColoredChar.create('F', Color.yellow), item ,term),x(),y());break;
                }
                case 2: {
                    //Fette Nacktschnecke
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(3);
                    if (zufallszahl == 0) {
                        //Axt drops 1/3 of the time
                        item = new Item("Axt", 0, Item.ITEMTYPE_SWORD, 0,1, 0,30);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
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
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                        } 
                    else if (zufallszahl <= 4) {
                        //Kurzschwert droppt zu 1/5
                        item = new Item("Kurzschwert", 0, Item.ITEMTYPE_SWORD, 1,0, 0,15);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                        }
                    else{
                    	 item =new Item("Froschschenkel",Item.ITEMTYPE_FOOD,3);
                         world().addActor(new ItemGenerator(ColoredChar.create('F', Color.yellow), item ,term),x(),y());
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
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                        } 
                    else if (zufallszahl < 9) {
                        //Riesenschwert droppt zu 1/5
                        item = new Item("Riesenschwert", 0, Item.ITEMTYPE_SWORD, 4,10, 0,8);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
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
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());

                    } else if (zufallszahl < 25) {
                        //GroÃŸschwert droppt zu 1/6
                        item = new Item("Gro\u00dfschwert", 0, Item.ITEMTYPE_SWORD, 9,0, 0,25);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                    }else if (zufallszahl < 31) {
                        //Akragons Relikt droppt zu 1/10
                        item = new Item("Akragons Relikt", 0, Item.ITEMTYPE_SWORD, 12,0, 0, akragonLines, 10);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                    }else if (zufallszahl < 36) {
                        //Dumbarons Kolossschwert droppt zu 1/12
                        item = new Item("Dumbarons Kolossschwert", 0, Item.ITEMTYPE_SWORD, 0,27, 0,dumbaronLines,10);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
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
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());

                    } else if (zufallszahl < 6) {
                        //GroÃŸschwert droppt zu 1/3
                        item = new Item("Gro\u00dfschwert", 0, Item.ITEMTYPE_SWORD, 9,0, 0,25);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                    }else if (zufallszahl < 9) {
                        //Akragons Relikt droppt zu 1/5
                        item = new Item("Akragons Relikt", 0, Item.ITEMTYPE_SWORD, 12,0, 0, akragonLines, 10);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                    }else if (zufallszahl < 12) {
                        //Dumbarons Kolossschwert droppt zu 1/5
                        item = new Item("Dumbarons Kolossschwert", 0, Item.ITEMTYPE_SWORD, 0,27, 0,dumbaronLines, 10);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                    }else{
                    	 item =new Item("gebratener Hobbit",Item.ITEMTYPE_FOOD,50);
                         world().addActor(new ItemGenerator(ColoredChar.create('F', Color.yellow), item ,term),x(),y());
                    }
                    break;
                }
                case 7: {
                    //Shadow
                	int zufallszahl = random.nextInt(5);
                	if (zufallszahl < 1) {
                        //Kunkrans Drachtentöter droppt zu 1/5
                        item = new Item("Kunkrans Drachtent\u00f6ter", 0, Item.ITEMTYPE_SWORD, 0,45, 0,kunkranLines,4);
                        world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                     }else if(zufallszahl < 2){
                            //Munkrans Drachtentöter droppt zu 1/5
                            item = new Item("Munkrans Drachtent\u00f6ter", 0, Item.ITEMTYPE_SWORD, 19,7, 0,kunkranLines,4);
                            world().addActor(new ItemGenerator(ColoredChar.create('W', Color.yellow), item ,term),x(),y());
                            }
                    break;
                 }
                case 99: {
                    //Dragon
                	Rogue.win(term, world());
                }
                default: {
                    break;
                }
            }



    }

} 
