/*
 * CharacterCreation.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
package rogue.system;

import jade.ui.TiledTermPanel;
import jade.core.World;
import rogue.level.Screen;
import java.lang.InterruptedException;

public class CharacterCreation {
	/**
	 * Asks user for a Name for his or her character. Name be is limited to 8 Characters.
	 *
	 * @param term Terminal used to print Messages
	 * @param world World used
	 * @return Chosen Name of Character
	 */
	public static String getCharacterName(TiledTermPanel term, World world) {
		// Set to some character
		char c = ' ';
		// Set Standardinputstring
		String printName = new String("Wie ist dein Name, mutiger Krieger? _"); // Used to Store String to be printed
		String name = new String(); // Used to Store name given by user
		while (c != '\n') { // Stop when user presses Return (Newline)
			Screen.printLine(printName, term, world); // Print Users Input
			try {
				// Read next Character
				c = term.getKey();
				switch (c) {
					// User pressed Backspace
					case '\b':
						// Name has already some Letters?
						if (name.length() != 0) { // Yes, remove last one
							// Remove last letter from Name
							name = name.substring(0,name.length()-1);
							// Remove last letter from Output
							printName = printName.substring(0,printName.length()-2).concat("_");
						}
						break;
					case '\n':
						// Just Jump to start of while loop
						break;
					default:
						// Has Maximumlength of Name been reached?
						if (name.length() < 8) { // No
							// Check what kind of Character has been pressed
							if (valueIsInIntervall((int)'0',(int)'9',(int)c) ||
								valueIsInIntervall((int)'a',(int)'z',(int)c) ||
								valueIsInIntervall((int)'A',(int)'Z',(int)c) ||
								c == ' ') {
								// Add to Name
								name = name.concat(new Character(c).toString());
								// Delete Input Character and last letter, dann append Input Character again
								printName = printName.substring(0,printName.length()-1).concat(new Character(c).toString()).concat("_");
							}
						}
						break;
				}

			} catch(InterruptedException e) { // Something went wrong 
				System.out.println("!Interrupted Exception");
				e.printStackTrace();
			}
		}
		return name;
	}

	/**
	 * Checks if a Value is between two other Values
	 *
	 * @param lowestValue lowest value which is allowed
	 * @param highestValue highest allowed value
	 * @param value checked value
	 * @return Boolean which tells if value is between highest and lowest Value 
	 */
	public static boolean valueIsInIntervall(int lowestValue, int highestValue, int value) {
		return ((value >= lowestValue) && (value <= highestValue));
	}
}
