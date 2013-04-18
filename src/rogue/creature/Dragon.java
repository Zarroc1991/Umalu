/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rogue.creature;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import jade.fov.RayCaster;
import jade.path.AStar;
import jade.path.PathFinder;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import jade.ui.Terminal;

import rogue.level.Screen;

/**
 * Represents a Dragon
 */

public class Dragon extends Monster {
		
	    PathFinder pathfinder = new AStar();
	    RayCaster fov;
	    int attackRadius;

	    public Dragon(Terminal term) {
	        super(ColoredChar.create('D',Color.red), "roter Drache", 130, 0,50, term);
	        fov = new RayCaster();
	        attackRadius = 100;
	        //the Dragon is so special, he is number 99
	        typenumber =99;
	    }

	@Override
    public void act() {

		boolean fight = false;
		
		for (Direction dir : Arrays.asList(Direction.values())) {
			Player player = world().getActorAt(Player.class, x() + dir.dx(),
					y() + dir.dy());
			if (player != null) {
				fight(player);
				fight = true;
				break;
			}
		}

		if (!fight) {
			move(Dice.global.choose(Arrays.asList(Direction.values())));
		}
	}

	@Override
	/**
	 * Allows this monster to figth against the player
	 */
	public void fight(Player opponent) {
		// TODO Auto-generated method stub {
	        System.out.println("Ra's al Ghul greift dich an");
		// Generate Damage
	        int abzug =makeDamage(strength_constant, strength_random);
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
            Random generator = new Random();
            int ran = generator.nextInt( 4 );
            switch(ran){
            	case 0:Screen.showEventLineAndPutToConsole("Er verbrennt dir den Arsch!", true, true);break;
            	case 1:Screen.showEventLineAndPutToConsole("Eine Drachenkralle trifft dich.", true, true);break;
            	case 2:Screen.showEventLineAndPutToConsole("Iiiihh er hat dich angefurzt.", true, true);break;
            	case 3:Screen.showEventLineAndPutToConsole("Aua Drachenz\u00e4hne sind scharf", true, true);break;
	        }
	        System.out.println("Du hast "+ abzug + " HP verloren");
	        System.out.println("verbleibende HP: "+ opponent.hitpoints);
		Screen.redrawEventLine(name+" macht "+abzug+" Schaden (Rest: "+opponent.hitpoints+")");
		try {
			term.getKey();
		} catch(InterruptedException e) {
			System.out.println("!InterruptedException");
			e.printStackTrace();
		}
	    }


	}


