package rogue;

import jade.core.World;
import jade.ui.TiledTermPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import rogue.creature.Player;
import rogue.level.Level;
import rogue.level.Screen;
import rogue.system.Path;
import rogue.system.CharacterCreation;
import rogue.system.SystemHelper;

public class Rogue {
	int level ;
	int levelanzahl;
	private static boolean godmode=false;
	ArrayList<Integer> levelorder;

	
	public static void main(String[] args) throws InterruptedException {

   		int level = 0; 
		int stepSum = 0;
		// Set System options
   		int levelanzahl = 5;

   		// Set System options
		Screen.initialiseScreen();
		new Path().runningFromJar();
		SystemHelper.getArgs(args);
		TiledTermPanel term = TiledTermPanel.getFramedTerminal("Jade Rogue");


		// Create a new Player
		Player player = new Player(term);
		// erstellt zufällige Levelreihenfolge
		ArrayList<Integer> levelorder = term.levelorder(levelanzahl);
		levelorder.add(levelanzahl);
		
		
		
		// Generate a new World
		World world = new Level(83, 34, player, levelorder.get(level), level,true,
				term);
		//Show Splashscreen for Start
		Screen.showFile(Path.generateAbsolutePath("maps/start.txt"), term,
				world);
		term.getKey();
		player.setName(CharacterCreation.getCharacterName(term, world));
		//Screen.printLine(player.getName(),term,world);
		//term.getKey();
		
		
		
		//Screen.showFile(Path.generateAbsolutePath("maps/start.txt"),term,world);
		
		//Zeigt Intro 
		Screen.intro(player.getName(), Path.generateAbsolutePath("txt Dateien/Intro.txt"),term,world);
		term.getKey();
		Screen.showFile(Path.generateAbsolutePath("txt Dateien/Tutorial.txt"),term,world);
		term.getKey();
		Calendar cal = Calendar.getInstance();
		long startTime = cal.getTimeInMillis();
		

		while (!player.expired()) { // Player is still living?
			if (player.worldchangeup) { // Überprüft, ob einen Levelup erfolgt ist
				world.removeActor(player); // entfernt Spieler aus der alten
											// Welt
				world = new Level(80, 34, player, levelorder.get(++level),
						level,true, term); // lädt das nächste Level
				player.setWorld(world); // Spieler erkennt seine Welt
				player.worldchangeup = false;
			} else if (player.worldchangedown) {
				world.removeActor(player); // entfernt Spieler aus der alten						// Welt
				world = new Level(80, 34, player, levelorder.get(--level),
						level,false, term); // lädt das nächste Level
				player.setWorld(world); // Spieler erkennt seine Welt
				player.worldchangedown = false;
			}

			term.registerCamera(player, player.x(), player.y() + 1); // Kamera
																		// verfolgt
																		// den
																		// Spieler
			// TODO HPup Codeblock should move to Player.act(), since it is only
			// his stuff
			
			// Generate a List of Monsters still on Map
			Screen.lastWorld = world;
			Screen.lastTerminal = term;
			Screen.redrawMap(getHPLine(player),getHPColor(player),
					getDamageLine(player),
					getFullLine(player),getFullColor(player),
					getLevelLine(player,level),
					player.getItemLine());
			
			world.playertick();
			term.registerCamera(player, player.x(), player.y() + 1);
			term.bufferCameras(); 
			world.tick();
			stepSum++;
		}
		term.clearBuffer();
		// Screen.showFile(normalizePath("src\\rogue\\system\\end.txt","rogue/system/end.txt"),
		// term, world);
		Screen.showFile(Path.generatePath("maps/end.txt"), term, world);

		term.getKey();
		System.exit(0);

	}
		
	public static void win(TiledTermPanel term, World world) throws InterruptedException{
		
		Screen.showFile(Path.generatePath("maps/win.txt"), term, world);
		term.getKey();
		System.exit(0);
		
	}
	
	public static String getHPLine (Player player){
		String a="HP: "+Math.round(player.getHitpoints()*10)/10.0+"/"+Math.round(player.getMaxHitpoints());
		return(player.makeRightString(a,20));
	}
	public static Color getHPColor(Player player){
		if (player.getHitpoints()<=player.getMaxHitpoints()*0.2){
			return Color.red;
		}else if(player.getHitpoints()<=player.getMaxHitpoints()*0.5){
			return Color.yellow;
		}else{
			return Color.white;
		}
	}
	public static String getDamageLine (Player player){
		String b="Schaden: "+player.strength_constant +"-"+(player.strength_constant+player.strength_random);
		return(player.makeRightString(b,20));
	}
	public static String getFullLine (Player player){
		String c="Nahrung: "+player.getFull()+"/"+"100";
		return(player.makeRightString(c,20));
	}
	
	public static Color getFullColor(Player player){
		if (player.getFull()<=20){
			return Color.red;
		}else if(player.getFull()<=70){
			return Color.yellow;
		}else{
			return Color.white;
		}
	}
	
	public static String getLevelLine (Player player,int level){
		String d="Level: " + (level+1);
		return(player.makeRightString(d,20));
	}
    public static boolean getGodmode(){
    	return godmode;
    }
    
    public static void switchGodmode(){
    	if (godmode){
    		godmode=false;
    	}else{
    		godmode=true;
    	}
    }
    
}
