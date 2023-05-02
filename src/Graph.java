/* weighted, sparse, and undirected */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Collections;


// God, I hate object oriented programming
class Edge<T> {
    T first, second;
    int weight;

    public Edge(T first, T second) { this(first, second, 0); }

    public Edge(T first, T second, int weight) {
        this.first = first;
        this.second = second;
        this.weight = weight;
}}

class Point<T> {
    T id;
    int x, y;

    public Point(T id, int x, int y) { 
        this.id = id;
        this.x = x; 
        this.y = y; 
}}

/** only remember where it is, where it was, and how long it took to get there */
class ForgetfulMemory<T> implements Comparable<ForgetfulMemory<T>> {
    T current, previous;
    int distance;

    public ForgetfulMemory(T current, T previous, int distance) {
        this.current = current;
        this.previous = previous;
        this.distance = distance;
    }

    @Override
    public int compareTo(ForgetfulMemory<T> other) { return distance - other.distance; }
}

@SuppressWarnings("unchecked")
class Graph<T> {

    final static String FILENAME = "grid25x25.txt";

    private Map<T, Map<T, Integer>> graph = new HashMap<>();

    public Graph(Edge<T>[] edges) {
        for (Edge<T> edge : edges) {
            if (!graph.containsKey(edge.first)) graph.put(edge.first, new HashMap<>());
            if (!graph.containsKey(edge.second)) graph.put(edge.second, new HashMap<>());
            
            graph.get(edge.first).put(edge.second, edge.weight);
            graph.get(edge.second).put(edge.first, edge.weight);
    }}

    Set<T> getNeighbours(T vertex) { return graph.get(vertex).keySet(); }

    Map<T, ForgetfulMemory<T>> getDijkstrasTable(T from) {
        Map<T, ForgetfulMemory<T>> map = new HashMap<>();
        Queue<ForgetfulMemory<T>> queue = new PriorityQueue<>();

        ForgetfulMemory<T> start = new ForgetfulMemory<>(from, null, 0);
        map.put(from, start);
        queue.add(start);

        while (!queue.isEmpty()) {
            ForgetfulMemory<T> memory = queue.poll();
            for (T neighbour : getNeighbours(memory.current)) {
                int distance = memory.distance + graph.get(memory.current).get(neighbour);
                if (!map.containsKey(neighbour) || distance < map.get(neighbour).distance) {
                    ForgetfulMemory<T> newMemory = 
                        new ForgetfulMemory<>(neighbour, memory.current, distance);
                    
                    map.put(neighbour, newMemory);
                    if (!queue.contains(newMemory)) queue.add(newMemory);
        }}}
        return map;
    }

    T[] getShortestPath(T from, T to) {
        Map<T, ForgetfulMemory<T>> table = getDijkstrasTable(from);
        List<T> path = new ArrayList<>();
        
        T current = to;
        while (current != from) {
            path.add(current);
            current = table.get(current).previous;
        }
        path.add(from);
        Collections.reverse(path);
        return path.toArray((T[]) new Object[path.size()]);
    }


    
    static int getDistance(Point first, Point second) { return (int) Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2)); }

    public static void main(String[] args) {
        Scanner in;
        try { in = new Scanner(new File(FILENAME)); }
        catch (Exception e) { 
            System.out.println(FILENAME + " not found"); 
            return; 
        }

        Scanner firstLine = new Scanner(in.nextLine());
        int v = firstLine.nextInt(), e = firstLine.nextInt();

        List<Point<Integer>> vertices = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            String line = in.nextLine();
            while (line.equals("")) line = in.nextLine();
            Scanner lineIn = new Scanner(line);
            vertices.add(new Point<Integer>(lineIn.nextInt(), lineIn.nextInt(), lineIn.nextInt()));
            lineIn.close();
        }
  
        List<Edge<Integer>> unweightedEdges = new ArrayList<>();
        for (int i = 0; i < e; i++) {
            String line = in.nextLine();
            while (line.equals("")) line = in.nextLine();
            Scanner lineIn = new Scanner(line);
            unweightedEdges.add(new Edge<Integer>(lineIn.nextInt(), lineIn.nextInt()));
        }

        Edge<Integer>[] weightedEdges = new Edge[unweightedEdges.size()];
        for (int i = 0; i < unweightedEdges.size(); i++) {
            Edge<Integer> edge = unweightedEdges.get(i);
            
            weightedEdges[i] = new Edge<>( 
                edge.first, edge.second, getDistance(vertices.get(edge.first), vertices.get(edge.second)) 
        );}

        Graph<Integer> graph = new Graph<>(weightedEdges);

        String line = in.nextLine();
        while (line.equals("")) line = in.nextLine();
        Scanner lastLine = new Scanner(line);
        System.out.println(Arrays.toString(graph.getShortestPath(lastLine.nextInt(), lastLine.nextInt())));
}}