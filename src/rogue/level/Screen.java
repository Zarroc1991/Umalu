
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// TODO Move this File to rogue.system

package rogue.level;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import rogue.system.SystemHelper;
import java.util.jar.JarFile;

import lib.JarName;

/**
 * 
 * Used to print Stuff in Terminal.
 * 
 * @author
 */

public class Screen {
	/**
	 * Last used World
	 */
	public static World lastWorld;
	/**
	 * Last used Terminal
	 */
	public static TiledTermPanel lastTerminal;
	/**
	 * Last Eventmessages
	 */
	public static ArrayList<String> eventLog;

	/**
	 * Creates an empty EventLog
	 */
	public static void initialiseScreen() {
		eventLog = new ArrayList<String>();
	}
	
	/**
	 * Reads a file at filePath and shows its content intro Style, include Players
	 *
	 * @param name Character Name, will be insterted in Text at @-Signs
	 * @param filePath Path to File, which Contents will be added
	 * @param term Currently Used Terminal
	 * @param world Currently Used World
	 */
	public static void intro(String name, String filePath, TiledTermPanel term,World world) {
		term.clearBuffer();
		try {
			// Prepare File for reading (open it)


			//FileReader fr = new FileReader(filePath);
			BufferedReader br;
			// Prepare File for reading (open it)
			if (SystemHelper.isJar) {
				JarFile jar = new JarFile(JarName.getJarName());
				//JarEntry entry = jar.getEntry(filePath);
				br = new BufferedReader(new InputStreamReader(jar.getInputStream(jar.getEntry(filePath)),"UTF-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			}
			// int lineNumber = MAXHEIGHT; // TODO Delete this line, when it is
			// not needed anymore
			// Read first line
			String line = br.readLine();
			// Count read Lines
			int lineNumber = 0;
			// Go through whole file
			while (line != null) {
				int i;
				for (i = 0; i < line.length(); i++) {
					// Read Character at Position i in current line
					char c = line.charAt(i);
					if(c== '@'){
					for(int j= 0; j < name.length(); j++){
						term.bufferChar(i+j, lineNumber, ColoredChar.create(name.charAt(j)));
						Thread.sleep(5);
						term.refreshScreen();
					}
					
					
					// System.out.println(c); // TODO Delete this line, when it
					// is not needed anymore
					// Put Character on Screen
					i= i+name.length();}
					else{
					term.bufferChar(i, lineNumber, ColoredChar.create(c));
					}
					//Thread.sleep(5);
					term.refreshScreen();}

				// Fill rest of Line with Whitespaces
				while (i < world.width()) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
					i++;
				}
				// Read next line
				line = br.readLine();
				// Increment Linecounter
				lineNumber++;
			}
			// Fill rest of Lines with Whitespaces
			while (lineNumber < world.height()) {
				// Put Whitespaces in Columns
				for (int i = 0; i < world.width(); i++) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				}
				// Go to next Line
				lineNumber++;
			}
			// Redraw Window
			term.refreshScreen();

		} catch (IOException e) { // Something went wrong while reading the File
			System.out.println("!IoException");
			e.printStackTrace();
			System.exit(1);
		}
		// catch (FileNotFoundException ex) { TODO Delete this Block if it is
		// not needed anymore
		// System.out.println("file not found");
		// System.exit(1);
		// }
 catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Shows Content of File by printing it on Screen
	 *
	 * @param filePath Path to File
	 * @param term Currently used Term
	 */
	public static void showFile(String filePath, TiledTermPanel term,
			World world) {
		term.clearBuffer();
		try {
			BufferedReader br;
			// Prepare File for reading (open it)
			//FileReader fr = new FileReader(filePath);
			if (SystemHelper.isJar) {
				JarFile jar = new JarFile(JarName.getJarName());
				//JarEntry entry = jar.getEntry(filePath);
				br = new BufferedReader(new InputStreamReader(jar.getInputStream(jar.getEntry(filePath)),"UTF-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			}
			
			// int lineNumber = MAXHEIGHT; // TODO Delete this line, when it is
			// not needed anymore

			// Read first line
			String line = br.readLine();
			// Count read Lines
			int lineNumber = 0;
			// Go through whole file
			while (line != null) {
				int i;
				for (i = 0; i < line.length(); i++) {
					// Read Character at Position i in current line
					char c = line.charAt(i);
					
					// System.out.println(c); // TODO Delete this line, when it
					// is not needed anymore
					// Put Character on Screen
					term.bufferChar(i, lineNumber, ColoredChar.create(c));
				}
				// Fill rest of Line with Whitespaces
				while (i < world.width()) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
					i++;
				}
				// Read next line
				line = br.readLine();
				// Increment Linecounter
				lineNumber++;
			}
			// Fill rest of Lines with Whitespaces
			while (lineNumber < world.height()) {
				// Put Whitespaces in Columns
				for (int i = 0; i < world.width(); i++) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				}
				// Go to next Line
				lineNumber++;
			}
			// Redraw Window
			term.refreshScreen();

		} catch (IOException e) { // Something went wrong while reading the File
			System.out.println("!IoException");
			e.printStackTrace();
			System.exit(1);
		}
		// catch (FileNotFoundException ex) { TODO Delete this Block if it is
		// not needed anymore
		// System.out.println("file not found");
		// System.exit(1);
		// }
	}

	/**
	 * Puts a fullscreen Message on Screen. Deletes all former content.
	 * 
	 * @param line
	 *            Line to be put on Screen
	 */
	public static void printLine(String line, TiledTermPanel term, World world) {
		term.clearBuffer();
		int i;
		for (i = 0; i < line.length(); i++) {
			term.bufferChar(i, 0, ColoredChar.create(line.charAt(i)));
		}
		while (i < world.width()) {
			term.bufferChar(i, 0, ColoredChar.create(' '));
			i++;
		}
		int lineNumber = 1;
		while (lineNumber < world.height()) {
			i = 0;
			while (i < world.width()) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				i++;
			}
			lineNumber++;

		}
		term.refreshScreen();
	}

	/**
	 * Prints a Block of Lines on Screen. Deletes all former content.
	 * 
	 * @param line
	 *            Array of Lines
	 */
	public static void printBlock(ArrayList<String> lines, TiledTermPanel term,
			World world) {
		term.clearBuffer();
		int lineNumber = 0;
		for (lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
			String currentLine = lines.get(lineNumber);
			int i;
			for (i = 0; i < currentLine.length(); i++) {
				term.bufferChar(i, lineNumber,
						ColoredChar.create(currentLine.charAt(i)));
			}
			while (i < world.width()) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				i++;
			}
		}
		while (lineNumber < world.height()) {
			for (int i = 0; i < world.width(); i++) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
			}
			lineNumber++;
		}
		term.refreshScreen();
	}

	/**
	 * Redraws Content of Window
	 * 
	 * @param term
	 *            Canvas for Drawing
	 * @param world
	 *            Used World
	 */
	public static void redrawMap() {					
		lastTerminal.clearBuffer();
		/*for (int x = 0; x < lastWorld.width(); x++) {						//Karte wird nicht im Terminal geladen 
			for (int y = 0; y < lastWorld.height(); y++) {
				// lastTerminal.bufferChar(x+11,y,lastWorld.look(x,y));
				lastTerminal.bufferChar(x, y, lastWorld.look(x, y));
			}
		}*/
		lastTerminal.bufferCameras();
		lastTerminal.refreshScreen();
	}

	/**
	 * Redraw map, put Statusline below Map
	 * 
	 * @param statusLine
	 *            Statusline to be printed
	 */
	public static void redrawMap(String statusLine,String itemLine) {
		redrawMap();
		for (int x = 0; x < statusLine.length(); x++) {
			lastTerminal.bufferChar(x, (lastWorld.height()-1),
					ColoredChar.create(statusLine.charAt(x)));
		}
		for (int x = 0; x < itemLine.length(); x++) {
			lastTerminal.bufferChar(x, (lastWorld.height()),
					ColoredChar.create(itemLine.charAt(x)));
		}
		lastTerminal.refreshScreen();
	}

	public static void redrawMap(String hp, Color hpColor, String dmg, String full,Color fullColor, String lvl,String itemLine) {
		redrawMap();
		int a=0;
		int b=0;
		
		while (a<hp.length()){
			lastTerminal.bufferChar(a, (lastWorld.height()),
			ColoredChar.create(hp.charAt(a),hpColor));
			a++;
		}
		while (a<hp.length()+dmg.length()){
			lastTerminal.bufferChar(a, (lastWorld.height()),
			ColoredChar.create(dmg.charAt(b)));
			a++;
			b++;
		}
		b=0;
		while (a<hp.length()+dmg.length()+full.length()){
			lastTerminal.bufferChar(a, (lastWorld.height()),
			ColoredChar.create(full.charAt(b),fullColor));
			a++;
			b++;
		}
		b=0;
		while (a<hp.length()+dmg.length()+full.length()+lvl.length()){
			lastTerminal.bufferChar(a, (lastWorld.height()),
			ColoredChar.create(lvl.charAt(b)));
			a++;
			b++;
		}
		
		for (int x = 0; x < itemLine.length(); x++) {
			lastTerminal.bufferChar(x, (lastWorld.height()+1),
					ColoredChar.create(itemLine.charAt(x)));
		}
		
		lastTerminal.refreshScreen();
	}
	
	
	/**
	 * Puts Text in Eventline. redrawEventLine does not delete content on its own!
	 *
	 * @param eventLine Line to be printed.
	 */
	public static void redrawEventLine(String eventLine) {
		int x;
		for (x = 0; x < eventLine.length(); x++) {
			lastTerminal.bufferChar(x, 0,
					ColoredChar.create(eventLine.charAt(x)));
		}
		
		while (x < lastWorld.width()) {
			lastTerminal.bufferChar(x, 0,
					ColoredChar.create(' '));
			x++;
		}

		if (eventLog.size() == 25) {
			eventLog.remove(0);
		}
		eventLog.add(eventLine);
		lastTerminal.refreshScreen();
	}

	/**
	 * Prints a Text in Eventline, but gives possibility to not save it in Eventlog (e.g. Levelswitch)
	 *
	 * @param eventLine Line to be printed
	 * @param saveInLog True, if this Message should be used in Eventlog, other should be set to false
	 */
	public static void redrawEventLine(String eventLine, boolean saveInLog) {
		int x;
		for (x = 0; x < eventLine.length(); x++) {
			lastTerminal.bufferChar(x,0,
					ColoredChar.create(eventLine.charAt(x)));
		}
		
		while (x < lastWorld.width()) {
			lastTerminal.bufferChar(x,0,
					ColoredChar.create(' '));
			x++;
		}
		if (saveInLog) {
			if (eventLog.size() == 25) {
				eventLog.remove(0);
			}
		}
		eventLog.add(eventLine);
		lastTerminal.refreshScreen();
	}
	
	/**
	 * Prints a Block of Text at Fullscreen
	 * 
	 * @param lines Lines to be printed
	 */
	public static void putText(ArrayList<String> lines) {
		Screen.printBlock(lines, Screen.lastTerminal, Screen.lastWorld);
	}
	
	/**
	 * Shows Eventlog
	 */
	public static void showEventLog() {
		Screen.putText(Screen.eventLog);
	}

	/**
	 * Prints a Text to Eventline and Console, gives possibility to not save it in Eventlog
	 *
	 * @param eventLine Line to be printed
	 * @param saveInLog True, if this Message should be logged in Eventlog
	 * @param waitForKey True, if Application should wait for Keyinput by User
	 */
	public static void showEventLineAndPutToConsole(String eventLine, boolean saveInLog, boolean waitForKey) {
		Screen.redrawEventLine(eventLine, saveInLog);
		System.out.println(eventLine);
		if (waitForKey) {
			try {
				Screen.lastTerminal.getKey();
			} catch (InterruptedException e) {
				System.out.println("!InterruptedException");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Waits for a pressed Key
	 */
	public static void anyKey() {
		try {
			lastTerminal.getKey();
		} catch (InterruptedException e) {
			System.out.println("!InterruptedException e");
			e.printStackTrace();
		}
	}
}

