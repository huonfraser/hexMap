package map;

import coordinates.*;
import edges.*;
import algorithms.*;
import tiles.*;
import java.util.*;

/**
 * Created by huonf on 12/30/2017.
 *
 * Data structure representing a Pointy Top Hexagon Grid,
 *
 * Hexagon tiles are stored in a dense 2d array while edges are stored in a map.
 *
 * @param <F> Type of faces of the grid
 * @param <E> Type of edges of the grid
 */
public class HexMap<F extends Hex, E extends Edge> implements Iterable {
    private int height;
    private int width;
    private F[][] hexs;
    HashMap<CoordinatePair,E> edges = new HashMap<>();


    /**
     * @return the width of the hex map, in offset representation
     */
    public int getWidth(){
        return width;
    }

    /**
     * @return the height of the hex map, in offset representation.
     */
    public int getHeight(){
        return height;
    }


    @Override
    public boolean equals(Object o) throws ClassCastException{
        HexMap<F,E> other = (HexMap<F,E>)o;

        //check maps of same dimension
        if( (this.height != other.getHeight()) || (this.width != other.getWidth()) ){
            return false;
        }

        //check hexes are equal
        Iterator<Coordinate> coordinateIterator = coordinateIterator();
        while(coordinateIterator.hasNext()){
            Coordinate coord = coordinateIterator.next();
            if(!getHex(coord).equals(other.getHex(coord))){
                return false;
            }
        }

        //check edges are equal
        if(!edges.keySet().equals(other.getEdges().keySet())){
            return false;
        }

        for(CoordinatePair edgeKey : edges.keySet()){
            if(!getEdge(edgeKey).equals(other.getEdge(edgeKey))){
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hexs,edges);
    }

    /**
     * Constructor that clones the arguments into a new HexMap object
     * @param map
     */
    public HexMap(F[][] map, HashMap<CoordinatePair,E> edges){
            this.height=map.length;
            this.width=map[0].length;

            this.hexs= (F[][])(new Hex[height][width]);
            this.edges=edges;
            for(int i = 0; i < height; i++){
                for(int j =0; j < width; j++){
                    this.hexs[i][j]=map[i][j];
                }
            }

            this.edges = new HashMap<CoordinatePair, E>();
            for(Map.Entry<CoordinatePair,E> entry : edges.entrySet()){
                this.edges.put(entry.getKey(),entry.getValue());
            }
        }

    /**
     * Constructor which creates an empty hex map of the given dimensions.
     * @param width
     * @param height
     */
    public  HexMap (int width, int height) {
        this.height = height;
        this.width = width;
        hexs = (F[][]) new Hex[height][width];
        edges = new HashMap<CoordinatePair,E>();
    }

    /**
     * Check that the axial coordinates provided lie within the box
     */
    private void rangeCheck(Coordinate c) {
        Offset off = c.toOffset();
        if (off.x >= width || off.y>=height || off.y<0 || off.x<0) {
            throw new IndexOutOfBoundsException(off.x + ", " + off.y); //TODO
        }
    }

    /**
     * Return a new instance of an iterator over tiles
     * @return
     */
    public Iterator<F> iterator() {
        Iterator<F> it= new TileIterator<F>();
        return it;
    }

    /**
     * Return a new instance of an iterator over coordinates
     * @return
     */
    public Iterator<Coordinate>  coordinateIterator(){
        return new Iterator<Coordinate>() {
            int x =0;
            int y =0;
            @Override
            public boolean hasNext() {
                return y<height;
            }

            @Override
            public Coordinate next() {
                Offset coord = new Offset(x,y);
                x++;
                if(x==width){
                    y++;
                    x=0;
                }
                return coord;
            }
        };
    }

    /**
     * Retun a new instance of an iterator over the edges
     * @return
     */
    public Iterator<E> edgeIterator(){
        return new EdgeIterator();
    }

    /**
     * Getter method for the tile at the given coordinate
     * @return
     */
    public F getHex(Coordinate coord){
        Offset offset = coord.toOffset();
        rangeCheck(offset);
        F item = hexs[offset.y][offset.x];
        return item;
    }

    /**
     * Set the element at axial x,y
     * @param element
     */
    public void setHex(F element, Coordinate coord){
        Offset offset = coord.toOffset();
        rangeCheck(offset);
        hexs[offset.y][offset.x]=element;
    }

    /**
     * Contains method for a given coordinate
     * @param coord
     * @return
     */
    public boolean contains(Coordinate coord) {
        Offset off = coord.toOffset();
        if (off.x < width && off.y < height && off.y >=0 && off.x >= 0){
            return true;
        }
        return false;
    }

    /**
     * Contains method for a given tile
     * @param tile
     * @return
     */
    public boolean contains(F tile) {
        TileIterator<F> it = new TileIterator<>();
        while(it.hasNext()){
            F hex = it.next();
            if( hex.equals(tile)){
                return true;
            }
        }
        return false;
    }



    /***
     * Custom iterator for iterating over a hex map
     * Goes left to right and top to bottom
     * @param
     */
    class TileIterator<F> implements Iterator<F>{
        int x =0;
        int y = 0;

        @Override
        public boolean hasNext() {
            return (y<height );
        }


        @Override
        public F next() {
            F returnThis = (F)hexs[y][x];
            x++;
            if(x==width){
                y++;
                x=0;

            }
            return returnThis;
        }
    }

    /**
     * Return a new instance of an iterator over the edges
     */
    class EdgeIterator implements Iterator<E>{
        Iterator<Map.Entry<CoordinatePair,E>> it;
        public EdgeIterator(){
            it = edges.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public E next() {
            return it.next().getValue();
        }
    }

    /**
     * @return The centre coordinate, biased top and left
     */
    public Coordinate getCentre(){
        return new Offset(width/2,height/2);
    }

    public List<Coordinate> pathFind(Coordinate start, Coordinate end){
        if(contains(start) && contains(end)){
            List<Coordinate> path = new AStar(start.toAxial(),end.toAxial(),this).getFoundPath();
            return path;
        }else{
            return null;
        }
    }

    /**
     * Calculate a path of edges between start and end
     *
     * This is currently broken
     * @param start
     * @param end
     * @return
     */
    public List<E> edgePathFind(Coordinate start, Coordinate end) {
        List<Coordinate> path = pathFind(start, end);
        if(path==null){
            return null;
        }
        List<E> edgePath = null;
        Queue<Coordinate> queue = new ArrayDeque<Coordinate>(path);
        Coordinate one = queue.poll();
        Coordinate two = queue.poll();

        edgePath = new ArrayList<E>();
        if (start != null) {

            while (!one.equals(end)) {
                List<Coordinate> csdfs = Axial.getNeighbours(one, two);
                Coordinate c = end.toAxial().closedTo(csdfs);

                E e1 = edges.get(new CoordinatePair(one,c));
                E e2 = edges.get(new CoordinatePair(two, c));
                edgePath.add(e1);
                edgePath.add(e2);

                one = two;
                two = queue.poll();

            }
        }
        //TODO a better algorithim should return looking at both neighbour coordinates and choose the one closer to end
        return edgePath;
    }


    /**
     * Generate a set of edges
     * @return
     */
    private Map<CoordinatePair,E> generateEdgeMap(){
        Map<CoordinatePair,E> edgeList = new HashMap<>();

        Iterator<Coordinate> it = coordinateIterator();
        while(it.hasNext()){
            Coordinate coord = it.next();
            List<Coordinate> neighbours = coord.getNeighbours();
            for(Coordinate neighbour: neighbours){
                if(contains(neighbour)){
                    edgeList.put(new CoordinatePair(coord,neighbour),(E)new Edge(coord,neighbour));
                }
            }
        }
        return edgeList;
    }

    /**
     * Getter method for the set of edges
     * @return
     */
    public Map<CoordinatePair,E> getEdges(){
        return edges;
    }

    /**
     * Setter method for a single edge
     * @param c
     * @param e
     */
    private void setEdge(CoordinatePair c,E e){
        //TODO out of bounds check
        edges.put(c,e);
    }

    /**
     * Getter method for a single edge
     * @param p
     * @return
     */
    public E getEdge(CoordinatePair p){
        if(edges.keySet().contains(p)){
            return edges.get(p);
        }
        return null;
    }



}
