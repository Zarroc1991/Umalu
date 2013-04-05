package jade.gen.map;

import rogue.system.Path;
import jade.core.World;
import jade.util.Dice;


// Beispiel Map World1 
public class Arena extends NewWorld {

	@Override
	//ruft generateStep aus NewWorld auf
	protected void generateStep(World world, Dice dice) {
		generateStep(world,dice,Path.generateAbsolutePath("maps/Arena.txt"),5, true);
                for (int i = 0; i < world.width(); i++) {
                    for (int j = 0; j < world.height(); j++) {
                        world.viewable(j, j);

                    }

            }
	}
}
