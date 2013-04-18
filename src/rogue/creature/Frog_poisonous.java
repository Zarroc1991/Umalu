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
 * Der giftige Frosch ist ein mittelschwaches Monster. (Kat 3/7)
 * Er hat 10 HP und macht zwischen 2 und 4 Schaden.
 */
public class Frog_poisonous extends Monster {

    PathFinder pathfinder = new AStar();
    RayCaster fov;
    int attackRadius;

    public Frog_poisonous(Terminal term) {
        super(ColoredChar.create('F',	new Color(160,32,240)), "giftiger Frosch", 10, 2,2, term);
        fov = new RayCaster();
        attackRadius = 5;
        //Typenumber 3, sice Monster of Categorie 3
        typenumber = 3;
    }

    @Override
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
		// TODO Auto-generated method stub {
	        System.out.println("Der " + name + " greift dich an");
		// Generate Damage
	        int abzug =makeDamage(strength_constant, strength_random);
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
	           Random generator = new Random();
	            int ran = generator.nextInt( 4 );
	            switch(ran){
	            	case 0:Screen.showEventLineAndPutToConsole("Seine Gift brennt sich in deine Haut.", true, true);break;
	            	case 1:Screen.showEventLineAndPutToConsole("Aaarrhhh... Du hast sein Gift im Auge.", true, true);break;//optional hier alles sichtbare l�schen
	            	case 2:Screen.showEventLineAndPutToConsole("Du untersch\u00e4tzt dieses kleine Vieh.", true, true);break;
	            	case 3:Screen.showEventLineAndPutToConsole("NEIN... Deine Haut wird ver\u00e4tzt.", true, true);break;
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


