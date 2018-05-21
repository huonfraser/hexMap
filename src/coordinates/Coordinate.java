package coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huonf on 2/15/2018.
 *
 * Interface that all coordinate classes in HexMap must follow
 *
 * The contract is that all coordinates can be used interchangably. At the moment this is done by the toAxial/offset/
 * cubic methods. In the future an adapter pattern should be used to reach this functionality.
 */
public interface Coordinate {

    public double distance(Coordinate c);

    public Axial toAxial();
    public Offset toOffset();
    public Cubic toCubic();

    public List<Coordinate> getNeighbours();
    public Coordinate getNeighbour(int i);

    /**
     * Return coordinates that neighbour both one and two.
     * @param c1
     * @param c2
     * @return
     */
    public static List<Coordinate> getNeighbours(Coordinate c1, Coordinate c2){

        List<Coordinate> neighbours1 = c1.getNeighbours();
        List<Coordinate> neighbours2 = c2.getNeighbours();
        List<Coordinate> returnThis = new ArrayList<Coordinate>();

        for(Coordinate c: neighbours1){
            if(neighbours2.contains(c)){
                returnThis.add(c);
            }
        }

        return returnThis;
    }

}
