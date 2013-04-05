package rogue.system;
/*
 * Path.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
import rogue.system.SystemHelper;

/**
 * Path Helperclass. Contains functions to create Several paths
 */
public class Path {

	/**
	 * Generates an absolute Pathstring from a given relative Path.
	 * Automatically adds src folder if not given in working directory.
	 *
	 * @param path Relative Path in Form "[folder]/[path]".
	 * @return Absolute Path in current Workingdirectory
	 */
	public static String generatePath(String path) {
		// Running from .jar ?
		if (SystemHelper.isJar) {
			return path;
		}
		// What OS is running?
		if (isWin()) {
			// Delete me later
			System.out.println("Windows Operating System detected");
			// We're running Windows, create an absolute Path, but replace all / with \
			// to make Paths Windows Compatible
			return generateAbsolutePath(path);
		} else {
			// Found any other Operating System, so we just need to generate an
			// absolute Path
			return generateAbsolutePath(path);
		}
	}
	
	/**
	 * Checks if current OS is Windows
	 *
	 * @return Windows System
	 */
	public static boolean isWin() {
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
	}

	/**
	 * Checks, if given Path already includes src/ Folder.
	 * Needed, because eclipse started instances, don't include src/ in user.dir while
	 * Command line started instances do
	 * 
	 * @param path Path to be checked
	 * @return True, if path already includes src Folder
	 */
	public static boolean pathEndsWithSourceFolder(String path) {
		return (path.endsWith("/src") || path.endsWith("\\src"));
	}

	/**
	 * Creates an Absolute Path to path
	 *
	 * @param path Path to which absolute path should be created
	 * @return Absolute Path
	 */
	public static String generateAbsolutePath(String path) {
		// Running from .jar?
		if (SystemHelper.isJar) {
			return path;
		}
		// Get current working directory
		String currentDirectory = System.getProperty("user.dir");
		// Are we running Windows?
		if (isWin()) {
			// Check if src is already included
			if (pathEndsWithSourceFolder(currentDirectory)) { 
				// Yes, so we just need to add our target Path
				return currentDirectory.concat("\\").concat(path);
			} else {
				// No, so include src/ Folder
				return currentDirectory.concat("\\src\\").concat(path);
			}

		} else {
			if (pathEndsWithSourceFolder(currentDirectory)) {
				// Yes, add target path only
				return currentDirectory.concat("/").concat(path);
			} else {
				// No, include src/ Folder
				return currentDirectory.concat("/src/").concat(path);
			}
		}
	}
	/**
	 * Checks if Application is running from a .jar File and sets SystemHelper.isJar accordingly
	 */
	public void runningFromJar() {
		String className = this.getClass().getName().replace('.', '/');
		String classJar = this.getClass().getResource("/" + className + ".class").toString();
		if (classJar.startsWith("jar:")) {
			SystemHelper.isJar = true;
		}
	}
}
