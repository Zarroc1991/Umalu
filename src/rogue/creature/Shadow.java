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
 * Ein Schatten ist ein schwer sichtbares, starkes Monster (Kat 7/7)
 * Er hat 30 HP und macht zwischen 5 und 15 Schaden. 
 */
public class Shadow extends Monster {

    PathFinder pathfinder = new AStar();
    RayCaster fov;
    int attackRadius;

    public Shadow(Terminal term) {
        super(ColoredChar.create(':', new Color(51,51,51)), "Schatten", 30, 5,10, term);
        fov = new RayCaster();
        attackRadius = 5;
        //Typenumber 7 since cat 7
        typenumber =7;
    }

    @Override
  public void act() {
        
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
	        System.out.println("Ein " + name + "erhebt sich.");
		// Generate Damage
	        int abzug = makeDamage(strength_constant, strength_random);
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
	        Random generator = new Random();
            int ran = generator.nextInt( 5 );
            switch(ran){
            	case 0:Screen.showEventLineAndPutToConsole("Eine unerträgliche leere ergreift dich.", true, true);break;
            	case 1:Screen.showEventLineAndPutToConsole("Du kannst nichts sehen.", true, true);break;//optional hier alles sichtbare lï¿½schen
            	case 2:Screen.showEventLineAndPutToConsole("Dein gr\u00f6\u00fcter Albtraum spielt sich in deinem Kopf ab.", true, true);break;
            	case 3:Screen.showEventLineAndPutToConsole("Dein Herz fühlt sich so kalt an.", true, true);break;
            	case 4:Screen.showEventLineAndPutToConsole("Du hast schreckliche Angst.", true, true);break;
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



