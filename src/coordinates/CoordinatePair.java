package coordinates;

/**
 * Created by huonf on 3/4/2018.
 */
public class CoordinatePair {
    private Coordinate one;
    private Coordinate two;


    public CoordinatePair(Coordinate one, Coordinate two){
        this.one=one;
        this.two=two;
    }

    @Override
    public boolean equals(Object o) throws ClassCastException{
        CoordinatePair other = (CoordinatePair)o;
        return(other.getOne().equals(one) && other.getTwo().equals(two))||(other.getOne().equals(two) && other.getTwo().equals(one));
    }

    @Override
    public int hashCode(){
        return one.hashCode()+two.hashCode();
    }

    public Coordinate getOne(){
        return one;
    }
    public Coordinate getTwo(){
        return two;
    }

}
