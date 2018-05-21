package coordinates;

import java.util.List;

/**
 * Created by huonf on 1/27/2018.
 *
 * A class for coordinates represented in Cubic form
 */
public class Cubic extends Coordinate3D {
    public final int q;
    public final int r;
    public final int z;

    public Cubic(int q, int r, int z){
        this.q=q;
        this.r=r;
        this.z=z;
    }

    public Offset toOffset(){
        return toAxial().toOffset();
    }

    public Axial toAxial(){
        return new Axial(q,r);
    }

    @Override
    public List<Coordinate> getNeighbours() {
        return null;
    }

    @Override
    public Coordinate getNeighbour(int i) {
        return null;
    }

    @Override
    public double distance(Coordinate c) {
        Cubic other = c.toCubic();
        return (Math.abs(other.q-this.q)+Math.abs(other.r-this.r) + Math.abs(other.z-this.z))/2;
    }

    @Override
    public Cubic toCubic() {
        return null;
    }

    /**
     * https://www.redblobgames.com/grids/hexagons/#map-storage
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Cubic roundToCube(double x, double y, double z){
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
        return new Cubic(rx,ry,rz);
    }

    @Override
    public boolean equals(Object o) throws ClassCastException {
        Coordinate c = (Coordinate)o;
       Cubic a = c.toCubic();
        return (a.r == r && a.q == q && a.z==z);
    }

    @Override
    public int hashCode(){
        return this.toAxial().hashCode();
    }
}
