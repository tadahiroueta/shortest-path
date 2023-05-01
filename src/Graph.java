/* weighted, sparse, and undirected */

import java.util.HashMap;
import java.util.Map;

class Graph<T> {

    final static String FILENAME = 

    class Triplet<A, B, C> {
        A first;
        B second;
        C third;

        Triplet(A first, B second, C third) {
            this.first = first;
            this.second = second;
            this.third = third;
    }}

    private Map<T, Map<T, Integer>> graph = new HashMap<>();

    public Graph(Triplet<T, T, Integer>[] edges) {
        for (Triplet<T, T, Integer> edge : edges) {
            int weight = edge.third;

            if (!graph.containsKey(edge.first)) graph.put(edge.first, new HashMap<>());
            if (!graph.containsKey(edge.second)) graph.put(edge.second, new HashMap<>());
            
            graph.get(edge.first).put(edge.second, weight);
            graph.get(edge.second).put(edge.first, weight);
    }}

    public static void main(String[] args) {
        
    }
}