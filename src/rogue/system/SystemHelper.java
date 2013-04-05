/*
 * System.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
package rogue.system;

/**
 * Contains and sets global Options and Variables.
 */
public class SystemHelper {
	/**
	 * Set to true if debug parameter has been set on startup
	 */
	public static boolean debug = false;
	/**
	 * Set to true, if speedrun parameter has been set on startup
	 */
	public static boolean speedrun = false;
	/**
	 * Set to true, if Application has been started as an Applet
	 *
	 * @deprecated Since this Application does not support being run as an Applet, this Variable will not be set.
	 */
	public static boolean isApplet = false;
	/**
	 * Set to true, if application is run frum a Jar-File.
	 */
	public static boolean isJar = false;

	/**
	 * Prints a message to Console, prefixed with '> Debug', when debug option has been set
	 *
	 * @param message Message to be printed
	 */
	public static void debugMessage(String message) {
		if (debug) {
			System.out.println("> Debug: "+message);
		}
	}

	/**
	 * Prints a message to Console, prefixed with '(!) Error:'
	 *
	 * @param message Message to be printed
	 */
	public static void errorMessage(String message) {
		System.out.println("(!) Error: "+message);
	}

	/**
	 * Reads a list of strings and sets Options in SystemHelper accordingly
	 *
	 * @param args List of Arguments
	 */
	public static void getArgs(String[] args) {
		SystemHelper.isApplet = true;
		for (int i=0;i<args.length;i++) {
			if (args[i].compareTo("debug") == 0) {
				// Debugmode selected
				SystemHelper.debug = true;
			} else if (args[i].compareTo("speedrun") == 0) {
				SystemHelper.speedrun = true;
			}
		}
	}
}
