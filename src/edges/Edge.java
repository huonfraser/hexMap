package edges;

import coordinates.*;

/**
 * Created by huonf on 2/19/2018.
 */

public class Edge{
    private Coordinate face1;
    private Coordinate face2;

    public Edge(Coordinate face1, Coordinate face2){

        //set coordinates so that the top-left coord is face1 - that is correct for incorrect usage
        if(face1.distance(new Offset(0,0))<=face2.distance(new Offset(0,0))){
            this.face1=face1;
            this.face2=face2;
        }else{
            this.face1=face2;
            this.face2=face1;
        }
    }


    @Override
    /***
     * Edges are equal if the coordinates are the same
     */
    public boolean equals(Object o) throws ClassCastException{
        Edge e = (Edge)o;
        return(e.face1.equals(face1) && e.face2.equals(face2));
    }

    @Override
    public int hashCode(){
        return face1.hashCode() + face2.hashCode();
    }

    /***
     * Return which side of coordinate 1 this edge is
     * Where 1 is .., 2 is ..., etc.
     * @return
     */
    public int coord1(){
        Coordinate distance = face1.toAxial().distanceVector(face2);
        if(distance.toCubic().q==1 && distance.toCubic().r==0){//+x
            return 1;
        }else if(distance.toCubic().q==-1 && distance.toCubic().r==0){//-x
            return 4;
        }else if(distance.toCubic().q==0 && distance.toCubic().r==1){//y
            return 2;
        }else if(distance.toCubic().q==0 && distance.toCubic().r==-1){//-y ,working
            return 5;
        }else if(distance.toCubic().q==1 && distance.toCubic().r==-1){//+z
            return 0;
        }else if(distance.toCubic().q==-1 && distance.toCubic().r==1){//-z
            return 3;
        }else{
            return -1;
        }
    }

    /***
     * Return which side of coordinate 2 this edge is
     * @return
     */
    public int point2(){
        Coordinate distance = face1.toAxial().distanceVector(face2);
        if(distance.toCubic().q==1 && distance.toCubic().r==0){//+x
            return 4;
        }else if(distance.toCubic().q==-1 && distance.toCubic().r==0){//-x
            return 1;
        }else if(distance.toCubic().q==0 && distance.toCubic().r==1){//y
            return 5;
        }else if(distance.toCubic().q==0 && distance.toCubic().r==-1){//-y ,working
            return 2;
        }else if(distance.toCubic().q==1 && distance.toCubic().r==-1){//+z
            return 3;
        }else if(distance.toCubic().q==-1 && distance.toCubic().r==1){//-z
            return 0;
        }else{
            return -1;
        }
    }

    /**
     * Return the face closer to the origin
     * @return
     */
    public Coordinate getFace1() {
        return face1;
    }

    /**
     * Return the face further from the origin
     * @return
     */
    public Coordinate getFace2(){
        return face2;
    }

}