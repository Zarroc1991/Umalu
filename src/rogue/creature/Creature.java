package rogue.creature;

import java.util.Random;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;

/**
 * Defines an abstract Class for every Being on Map
 */
public abstract class Creature extends Actor {
	public double hitpoints;
	public int strength_random;
	public int strength_constant;

	/**
	 * Creates a new Creature Object
	 *
	 * @param face Symbol used ingame to Represent this creature
	 */
	public Creature(ColoredChar face)
	{
		super(face);
	}

	@Override
	/**
	 * Tries to move Creature to position (x,y), but checks if this is possible by checking if (x,y) is passable.
	 * If (x,y) is not passable (e.g. a wall), Creatures move is lost!
	 *
	 * @param x X Coordinate to which Creature should move
	 * @param y Y Coordinate to which Creatue should move
	 */
	public void setPos(int x, int y) {
		if(world().passableAt(x, y))
			super.setPos(x, y);
	}
	
	/**
	 * Reduces Creatures Hitpoints by damage.
	 *
	 * @param damage Amount of Hitpoints this creature should loose
	 */
	public boolean loseHitpoints(int damage) {
		hitpoints -= damage;
		return checkHitpoints();
	}
	
	/**
	 * Checks, if creature is dead (by having <= 0 hitpoints), if so, Creature dies
	 */
	public boolean checkHitpoints() {
		if(hitpoints<=0){
			expire();
                        return true;
		}
                return false;
	}
	
	public int makeDamage(int strength_constant,int strength_random){
		Random random = new Random();
		int ran=random.nextInt(strength_random+1);//Zufällige Zahl zwischen 0 und strength_random wobei strength_random auch erreicht werden kann.
		return (strength_constant+ran);
	}
}

