/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jade.gen.map;

import jade.core.Actor;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rogue.creature.Player;
import rogue.creature.util.Item;
import rogue.creature.util.NotEnoughSpaceException;
import rogue.level.Screen;

/**
 *
 * An actor, that doesnt do anything, but wait for the Player and let him find
 * an Item, when he passes him
 */
public class ItemGenerator extends Actor {

    // the Item to find here
    private Item item;
    private TiledTermPanel term;

    public ItemGenerator(ColoredChar face, Item item, TiledTermPanel term) {
        super(face);
        this.item = item;
        this.term = term;
    }

    @Override
    public void act() {
        Player player = world().getActorAt(Player.class, pos());
        if (player != null) {
            try {
                Screen.redrawEventLine(item.getName() + " gefunden");
                //wait for key presssed
                term.getKey();

            } catch (InterruptedException e) {
                Logger.getLogger(ItemGenerator.class.getName()).log(Level.SEVERE, null, e);
            }

            try {
                //Player gets the item
                player.getInventory().addItem(item);
            } catch (NotEnoughSpaceException ex) {
                if (item.getItemType()!=3){
            	player.getInventory().fullInventoryScreen(item);
                }else{
                player.getInventory().fullLunchboxScreen(item);
                }//else
                	
                // the item is gone
            }
                expire();
                //status message

            
        }

    }
}
