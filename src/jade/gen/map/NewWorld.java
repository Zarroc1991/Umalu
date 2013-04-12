package jade.gen.map;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import rogue.system.SystemHelper;
import java.util.jar.JarFile;

import lib.JarName;


public abstract class NewWorld extends MapGenerator {

	// neue generateStep Methode damit man eine Map als txt-Datei einlesen kann
	// - analog zu showfile
	protected void generateStep(World world, Dice dice, String filePath, int level, boolean aufwaerts ) {
            SystemHelper.debugMessage(""+aufwaerts);
		// TODO Auto-generated method stub
		try {
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
			ColoredChar c_color;// einige Zeichen werden durch andere bunte
								// Zeichen ersetzt.
			ArrayList<Character> notPassable = new ArrayList<Character>();
			notPassable.add('#');
			notPassable.add('~');
			notPassable.add('^');
			notPassable.add(';');
			while (line != null) {
				int i;

				for (i = 0; i < line.length(); i++) {
					// Read Character at Position i in current line
					char c = line.charAt(i);
					c_color = ColoredChar.create(c);
					switch (c) {
					case '@': {	c_color = ColoredChar.create('#');break;}
					case ':': {	c_color = ColoredChar.create('.', Color.yellow);	break;}
					case '~': {	c_color = ColoredChar.create('~', Color.blue);	break;}
					case '^': {	c_color = ColoredChar.create('^', new Color(210,105,30));break;}
					case '$': {	c_color = ColoredChar.create('$', Color.yellow);	break;}
					case ',': {	c_color = ColoredChar.create(',', Color.green);	break;}
					case ';': {	c_color = ColoredChar.create(';', new Color(69,139,0));	break;}
					case '\u2020': {	c_color = ColoredChar.create('\u2020', new Color(199,21,133));break;}
					case '=': {	c_color = ColoredChar.create('=', new Color(210,105,30));break;}
					case '#': {	c_color = ColoredChar.create('#', new Color(205,102,29));break;}
					case '\u00AC': {	c_color = ColoredChar.create('\u00AC', new Color(210,105,30));break;}
					case '.': {	c_color = ColoredChar.create('.', new Color(51,51,51));	break;}
                                        //player starts here
                                        case '\u00AE':{
                                            if(aufwaerts){world.playerstart = new Coordinate(i,lineNumber);}break;}
                                        //or here
                                        case '\u00A9':{                                            
                                            if(!aufwaerts){world.playerstart = new Coordinate(i,lineNumber);}break;}
					}
					//beim ersten Level soll es kein Leveldown geben 
					if((level==0||level==5) && c=='\u00AE'){
						c_color=ColoredChar.create('.', new Color(51,51,51));
					}
					// System.out.println(c); // TODO Delete this line, when it
					// is not needed anymore
					// Put Character on Screen

					world.setTile(c_color, !notPassable.contains(c), i,
							lineNumber);
				}
				// Fill rest of Line with Whitespaces
				while (i < world.width()) {
					world.setTile(ColoredChar.create('#'), false, i, lineNumber);
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
					world.setTile(ColoredChar.create('#'), false, i, lineNumber);
				}
				// Go to next Line
				lineNumber++;
			}

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

	@Override
	protected abstract void generateStep(World world, Dice dice);
	// Reicht abstrakte Methode weiter, um verschiedene Maps zu laden

}
