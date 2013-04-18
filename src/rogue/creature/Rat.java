/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rogue.creature;

import jade.fov.RayCaster;
import jade.path.*;
import jade.ui.Terminal;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import rogue.level.Screen;

/**
 * Die Ratte ist das schwächste Monster (Kat 1/7).
 * Sie hat 5 HP und macht genau 1 Schaden.
*/
public class Rat extends Monster {

    PathFinder pathfinder = new AStar();
    RayCaster fov;
    int attackRadius;

    public Rat(Terminal term) {
        super(ColoredChar.create('R', new Color(110, 110, 110)), "Ratte", 5, 1,0, term);
        fov = new RayCaster();
        attackRadius = 5;
        //a Rat is the weakest Monster, hence Typenumber 1
        typenumber = 1;
    }//public Rat

    @Override
	/**
	 * This Creatures has its move now
	 */
    public void act() {
    	world().setStepInt(x(), y());
        for (Direction dir : Arrays.asList(Direction.values())) {
            Player player = world().getActorAt(Player.class, x() + dir.dx(), y() + dir.dy());

            if (player != null && world().tileAt(pos().x()+dir.dx(),pos().y()+dir.dy())!=ColoredChar.create('\u2020', new Color(199,21,133))) {
                fight(player);
                return;
            }//if(player!=null)
        }//for(Direction dir... 
            Collection<Coordinate> viewField = fov.getViewField(this.world(), this.pos().x(), this.pos().y(), attackRadius);
            for (Coordinate coordinate : viewField) {
                if (this.world().getActorAt(Player.class, coordinate) != null) {

                	List<Coordinate> path = pathfinder.getPath(this.world(), this.pos(), coordinate);
                    if(path!=null){
                	Direction dir = this.pos().directionTo(path.get(0));
                	if(world().tileAt(pos().x()+dir.dx(),pos().y()+dir.dy())!=ColoredChar.create('\u2020', new Color(199,21,133))){
                    
                		move(dir);
                    return;
                	}//if(world()
                    }//if(path...                    
                }//if(this.world()...
            }//for(Coordinate ...

                Direction dir =Dice.global.choose(Arrays.asList(Direction.values()));
                if (world().tileAt(x() + dir.dx(), y() + dir.dy()) != ColoredChar.create('\u2020', new Color(199,21,133))){
                	move(dir);
                }//end of if

            }//act
        
    

	@Override
	/**
	 * Allows this monster to figth against the player
	 */
	public void fight(Player opponent) {
		
	        System.out.println("Die " + name + " greift dich an");
		// Generate Damage
	        int abzug = makeDamage(strength_constant, strength_random);
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
            Random generator = new Random();
            int ran = generator.nextInt( 4 );
            switch(ran){
            	case 0:Screen.showEventLineAndPutToConsole("Ihre Z\u00e4hne stecken in deinem Hintern.",true, true);break;
            	case 1:Screen.showEventLineAndPutToConsole("Sie bei\u00dft in deinen gro\u00dfen Zeh", true, true);break;
            	case 2:Screen.showEventLineAndPutToConsole("Tollwutalarm",true, true);break;
            	case 3:Screen.showEventLineAndPutToConsole("Sie quieckt so laut.", true, true);break;
	        }//switch
	        System.out.println("Du hast "+ abzug + " HP verloren");
	        System.out.println("verbleibende HP: "+ opponent.hitpoints);
		Screen.redrawEventLine(name+" macht "+abzug+" Schaden (Rest: "+opponent.hitpoints+")");
		try {
			term.getKey();
		} catch(InterruptedException e) {
			System.out.println("!InterruptedException");
			e.printStackTrace();
		}//catch
	    }//fight
	


	}//class



