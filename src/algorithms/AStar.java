package algorithms;

import coordinates.Coordinate;
import map.HexMap;

import java.util.*;

/**
 * Created by huonf on 2/12/2018.
 *
 * Class for AStar pathfinding algorithm on a HexMap
 */
public class AStar {

    private List<Coordinate> foundPath;

    public AStar(Coordinate start, Coordinate end, HexMap map){


        if(start.equals(end) || map==null){
            return;
        }

        boolean pathFound = false;

        Map<Coordinate, Coordinate> path = new HashMap<Coordinate, Coordinate>();
        Set<Coordinate> visited = new HashSet<Coordinate>();

        Queue<AStarEntry> fringe =  new PriorityQueue<AStarEntry>(10,(a,b) -> (Double.compare(a.getTotalCost(), b.getTotalCost())));
        double estimate = start.distance(end);

        fringe.offer(new AStarEntry(start,null,0,estimate));

        while(!fringe.isEmpty()){
            AStarEntry entry = fringe.poll();
            Coordinate node = entry.node;
            if(!visited.contains(node)){
                visited.add(node);
                path.put(node.toAxial(),entry.from);

                if(node.equals(end)){
                    pathFound = true;
                    break;
                }

                for(Coordinate i: node.getNeighbours()){
                    if(map.contains(i)){
                        Coordinate from  = path.get(node);
                        if(visited.contains(i)){
                            break;
                        }
                    }

                    fringe.offer(new AStarEntry(i,node,entry.costFromStart+i.distance(node),i.distance(end)));
                }
            }
        }

        path=new HashMap<>(path);

        if(pathFound){
            Stack<Coordinate> stack = new Stack<Coordinate>();
            Coordinate node=end;
            while(!node.equals(start)){
                stack.push(node);
               // boolean b = path.keySet().contains(node);
                Coordinate next = path.get(node.toAxial());
                node = next;
            }
            stack.push(start);

            foundPath = new ArrayList<Coordinate>();
            while(!stack.isEmpty()){
                foundPath.add(stack.pop());
            }

        }
    }

    /**
     * @return The path from start to end, null if no path exists, empty if start equals end
     */
    public List<Coordinate> getFoundPath(){
        return foundPath;
    }


    /**
     * Internal class representing the data points that are passed along in the Astar search
     */
    class AStarEntry{
        Coordinate node;
        Coordinate from;
        double costFromStart;
        double costToGoal;

        double totalCost;

        AStarEntry(Coordinate node, Coordinate from, double costFromStart, double costToGoal){
            this.node=node;
            this.from=from;
            this.costFromStart=costFromStart;
            this.costToGoal=costToGoal;
            this.totalCost=costFromStart+costToGoal;
        }

        /**
         *
         * @return hearistic estimate of cost
         */
        double getTotalCost(){
            return totalCost;
       }


    }

}
