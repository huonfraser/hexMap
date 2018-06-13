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
    public final int z;

    public Axial(int q, int r) {
        this.q = q;
        this.r = r;
        this.z = -q-r;
    }
    public Axial(int q, int r, int z){
        this.q = q;
        this.r = r;
        this.z = z;
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
    public List<Coordinate> getRingAround(int n) {

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
        Axial other = c.toAxial();
        return(Math.abs(other.q-this.q)+Math.abs(other.r-this.r) + Math.abs(other.z-this.z))/2;
    }

    /**
     * Return the diestance between this coordinate and c as a coordinate representing a vector
     * @param c
     * @return
     */
    public Coordinate distanceVector(Coordinate c){
        return new Axial(c.toAxial().q-this.q,c.toAxial().r-this.r);
    }


    /**
     * Return the coordiante in the list that is closest to this coordinate
     * @param list
     * @return
     */
    public Coordinate closestOutOF(List<Coordinate> list){
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

    @Override
    public Coordinate rotate(int degrees) {
        return rotateAround(new Axial(0,0),degrees);
    }

    @Override
    public Coordinate rotateAround(Coordinate c, int degrees) {
        int numSteps= (degrees+30)%60;

        Coordinate difference = distanceVector(c).toAxial();
        if(numSteps<0){
            for(int i =0; i > numSteps; i--){
                difference = rotateOneStepAntiClockwise(difference);
            }
        }else{
            for(int i =0; i < numSteps; i++){
                difference = rotateOneStepClockwise(difference);
            }
        }
        Axial a = difference.toAxial();
        return(new Axial(q+a.q,r+a.r));
    }

    /*
     */
    private Coordinate rotateOneStepClockwise(Coordinate coordinate){
        return new Cubic(-q,-r,-z);
    }

    private Coordinate rotateOneStepAntiClockwise(Coordinate coordinate){

        return new Cubic(-r,-z,-q);
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
     * https://www.redblobgames.com/grids/hexagons/#map-storage
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Axial round(double x, double y, double z){
        int rx = (int)Math.round(x);
        int ry = (int)Math.round(y);
        int rz = (int)Math.round(z);

        double diffx = Math.abs(x-rx);
        double diffy = Math.abs(y-ry);
        double diffz = Math.abs(z-rz);

        if(diffx > diffy && diffx> diffz){
            rx = -ry-rz;
        }else if(diffy>diffz){
            ry = -rx-rz;
        }
        else{
            rz = -rx-ry;
        }
        return new Axial(rx,ry,rz);
    }

}
