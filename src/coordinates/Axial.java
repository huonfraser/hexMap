package coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huonf on 1/27/2018.
 *
 * A class for coordinate objects in axial form
 */
public class Axial extends Coordinate2D {
    public final int q;
    public final int r;

    public Axial(int q, int r) {
        this.q = q;
        this.r = r;
    }

    /**
     * Convert this coordinate to the equivalent cubic coordinate
     * @return
     */
    public Cubic toCubic() {
        return new Cubic(q, r, -q - r);
    }

    /**
     * Convert this coordinate to the equivalent offset coordinate
     * @return
     */
    public Offset toOffset() {
        int x = q + r / 2;
        int y = r;
        return new Offset(x, y);
    }

    public String toString() {
        return "Axial: (" + q + "," + r + ")";
    }


    /**
     * Return the ring of coordinates that are distance n away from this coordinate
     * @param n
     * @return
     */
    public List<Coordinate> ringAround(int n) {

        List<Coordinate> returnThis = new ArrayList<>();

        if (n == 0) {
            returnThis.add(this);
            return returnThis;
        }

        Coordinate startingPoint = new Axial(q - n, r);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < n; j++) {
                returnThis.add(startingPoint);
                startingPoint = startingPoint.getNeighbour(i);
            }
        }

        return returnThis;
    }

    /**
     * Return the coordinate that neighbours this coordinate in the ith direction
     * @param i
     * @return
     */
    public Coordinate getNeighbour(int i) {
        Axial[] neighbours = new Axial[]{
                new Axial(q + 1, r - 1),
                new Axial(q + 1, r),
                new Axial(q, r + 1),
                new Axial(q - 1, r + 1),
                new Axial(q - 1, r),
                new Axial(q, r - 1)
        };

        return neighbours[i];
    }


    /**
     * Return coordinates that neighbour this
     * @return
     */
    public List<Coordinate> getNeighbours() {
        Axial[] neighbours = new Axial[]{
                new Axial(q + 1, r - 1),
                new Axial(q + 1, r),
                new Axial(q, r + 1),
                new Axial(q - 1, r + 1),
                new Axial(q - 1, r),
                new Axial(q, r - 1)
        };

        return Arrays.asList(neighbours);
    }



    @Override
    public Axial toAxial() {
        return this;
    }

    /**
     * Return the distance between this coordinate and c
     * @param c
     * @return
     */
    public double distance(Coordinate c) {
        return this.toCubic().distance(c);
    }

    /**
     * Return the diestance between this coordinate and c as a coordinate representing a vector
     * @param c
     * @return
     */
    public Coordinate distanceVector(Coordinate c){
        return new Axial(c.toAxial().q-this.q,c.toAxial().r-this.r);
    }

    @Override
    public boolean equals(Object o) throws ClassCastException {
       Coordinate c = (Coordinate)o;
       Axial a = c.toAxial();
       boolean b = ((a.r== r) && (a.q == q));
       return b;
    }

    @Override
    public int hashCode(){
        int tmp = ( q +  ((r+1)/2));
        return r +  ( tmp * tmp);
    }



    /**
     * Return the coordiante in the list that is closest to this coordinate
     * @param list
     * @return
     */
    public Coordinate closestTo(List<Coordinate> list){
        double distance = Double.MAX_VALUE;
        Coordinate closest = null;
        for(Coordinate c: list){
            if(c.distance(this)<distance){
                distance = c.distance(this);
                closest=c;
            }
        }

        return closest;
    }

}
