package jade.fov;

import jade.core.World;
import jade.path.Bresenham;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;

/**
 * Uses a simple raycasting algorithm to compute field of view. This algorithm works by sending
 * digital line rays to each point on the outside of the view radius. This algorithm is very simple,
 * and therefore can often be very fast, especially if the radius is small, or the map is very
 * closed. However, on large and open maps the algorithm results in inefficiencies as tiles close
 * the origin of the view field are rechecked many times over.
 */
public class RayCaster extends ViewField
{
    private Bresenham raycaster;

    /**
     * Constructs a new {@code RayCaster}.
     */
    public RayCaster()
    {
        raycaster = new Bresenham();
    }

    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r)
    {
        Collection<Coordinate> fov = new HashSet<Coordinate>();
        fov.add(new Coordinate(x, y));
        for(int dx =-r ;dx <= r; dx++)
        {	
            fov.addAll(raycaster.getPartialPath(world, x, y, x + dx, y - r));
            fov.addAll(raycaster.getPartialPath(world, x, y, x + dx, y + r));
        }
        for(int dy = -r; dy <= r; dy++)
        {
            fov.addAll(raycaster.getPartialPath(world, x, y, x - r, y + dy));
            fov.addAll(raycaster.getPartialPath(world, x, y, x + r, y + dy));
        }
        return fov;
    	}
        //alle als sichtbar markierten Objekte werden im Screen angezeigt 

    	protected Collection<Coordinate> calcViewFieldplayer(World world, int x, int y, int r){
    	Collection<Coordinate> fov = calcViewField(world, x, y,r);
        for(int i = 0; i<world.width()-1; i++){
        	for(int j=0; j<world.height()-1; j++){
        	 if(world.isviewable(i, j)){
        	 fov.addAll(raycaster.getPartialPath(world, i+1,j+1,i,j));
        	 }
        }
        }
        for(int i =world.width()-1;i>0; i--){
        	for(int j=world.height()-1;j>0; j--){
        	 if(world.isviewable(i, j)){
        	 fov.addAll(raycaster.getPartialPath(world,i-1,j-1,i,j));
        	 }
        }
        }
        for(int i =world.width()-1;i>0; i--){
        	for(int j=0;j<world.height()-1; j++){
        	 if(world.isviewable(i, j)){
        	 fov.addAll(raycaster.getPartialPath(world,i-1,j+1,i,j));
        	 }
        }
        }
        for(int i=0;i<world.width()-1; i++){
        	for(int j=world.height()-1;j>0; j--){
        	 if(world.isviewable(i, j)){
        	 fov.addAll(raycaster.getPartialPath(world,i+1,j-1,i,j));
        	 }
        }
        }
        return fov;
    }
}
