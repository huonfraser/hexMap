package coordinates;

import java.util.List;

/**
 * Created by huonf on 1/27/2018.
 *
 * A class for coordinates represented in offset form
 */
public class Offset extends Coordinate2D {
    public final int x;
    public final int y;

    public Offset(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Axial toAxial(){
        int q = x-y/2;
        int r = y;
        return new Axial(q,r);
    }

    public Cubic toCubic(){
        return toAxial().toCubic();
    }

    @Override
    public List<Coordinate> getNeighbours() {
        return toAxial().getNeighbours();
    }

    @Override
    public Coordinate getNeighbour(int i) {
        return toAxial().getNeighbour(i);
    }

    @Override
    public double distance(Coordinate c) {
      return this.toCubic().distance(c);
    }

    public Offset toOffset(){
        return this;
    }

    @Override
    public Coordinate rotate(int degrees) {
        return toAxial().rotate(degrees);
    }

    @Override
    public Coordinate rotateAround(Coordinate c, int degrees) {
        return toAxial().rotateAround(c,degrees);
    }

    @Override
    public boolean equals(Object o) throws ClassCastException {
        Coordinate c = (Coordinate)o;
        Offset a = c.toOffset();
        return (a.x == x && a.y == y);
    }

    @Override
    public int hashCode(){
        return this.toAxial().hashCode();
    }

    public int getX() {
        return x;
    }

    @Override
    public String toString(){
        return "Offset: (" +x+","+y +")";
    }
}
