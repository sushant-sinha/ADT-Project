import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FordFulkersonMaxCap {

    static int maxLength=-1;
    static int totalEdgesInGraph=-1;
    static List<Integer> longestAugPath;

    public static void main(String[] args) {
        String inputFile = "graph_adjacency_list_100_0.2_50.csv";
        int source = 53; // Specify the source node
        int sink = 6;  // Specify the sink node

        try {
            Map<Integer, Map<Integer, Integer>> graph = readGraph(inputFile);
            int[] result = fordFulkersonSAP(graph, source, sink);
            int lengthOfMaxCapPath=longestAugPath.size()-1;
            int paths = result[0];
            // since we are selecting only one path, the one with the maximum capacity
            double meanLength = (double) lengthOfMaxCapPath / paths;
            double mpl = meanLength / result[2];
            int totalEdges = result[3];

            System.out.println("Total Paths: " + paths);
            System.out.println("Augmenting path with maximum capacity is: "+longestAugPath +" and has "+result[4]+" capacity");
            System.out.println("Mean Length: " + meanLength);
            System.out.println("Mean Proportional Length: " + mpl);
            System.out.println("Total Edges: " + totalEdges);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, Map<Integer, Integer>> readGraph(String fileName) throws IOException {
        Map<Integer, Map<Integer, Integer>> adjacencyList = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int vertexId = Integer.parseInt(parts[0]);
                Map<Integer, Integer> neighbors = new HashMap<>();

                for (int i = 1; i < parts.length; i++) {
                    String[] neighborInfo = parts[i].split(":");
                    int neighborId = Integer.parseInt(neighborInfo[0]);
                    int capacity = Integer.parseInt(neighborInfo[1]);
                    neighbors.put(neighborId, capacity);
                    totalEdgesInGraph++;
                }

                adjacencyList.put(vertexId, neighbors);
            }
        }
        System.out.println(totalEdgesInGraph);
        return adjacencyList;
    }

    private static int[] fordFulkersonSAP(Map<Integer, Map<Integer, Integer>> graph, int source, int sink) {
        int paths = 0;
        int totalLength = 0;
        int maxCapacity=0;

        while (true) {
            List<Integer> augmentingPath = dijkstra(graph, source, sink);
            if (augmentingPath.isEmpty()) {
                break;
            }

            paths++;
            totalLength += augmentingPath.size();

            // Update capacities along the augmenting path
            int bottleneck = findBottleneck(graph, augmentingPath);
            if(maxCapacity<bottleneck){
                longestAugPath=augmentingPath;
                longestAugPath.add(0,source);
                maxCapacity=bottleneck;
            }

            for (int i = 0; i < augmentingPath.size() - 1; i++) {
                int u = augmentingPath.get(i);
                int v = augmentingPath.get(i + 1);

                // Forward edge
                int forwardCapacity = graph.get(u).get(v);
                graph.get(u).put(v, forwardCapacity - bottleneck);

                // Backward edge
                int backwardCapacity = graph.get(v).getOrDefault(u, 0);
                graph.get(v).put(u, backwardCapacity + bottleneck);
            }
        }

        return new int[]{paths, totalLength, maxLength, totalEdgesInGraph, maxCapacity};
    }

    private static List<Integer> dijkstra(Map<Integer, Map<Integer, Integer>> graph, int source, int sink) {
        Map<Integer, Integer> distance = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        PriorityQueue<Pair<Integer, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Pair::getSecond));
        Set<Integer> visited = new HashSet<>();

        distance.put(source, 0);
        minHeap.offer(new Pair<>(source, 0));

        while (!minHeap.isEmpty()) {
            int u = minHeap.poll().getFirst();

            if (u == sink) {
                return reconstructPath(parent, sink);
            }

            if (visited.contains(u)) {
                continue;
            }

            visited.add(u);

            for (int v : graph.getOrDefault(u, Collections.emptyMap()).keySet()) {
                int capacity = graph.get(u).get(v);
                if (capacity > 0) {  // Check if the edge has positive capacity
                    int weight = 1; // Treat the edge lengths as unit distances for SAP
                    int alt = distance.get(u) + weight;

                    if (!distance.containsKey(v) || alt < distance.get(v)) {
                        distance.put(v, alt);
                        parent.put(v, u);
                        minHeap.offer(new Pair<>(v, alt));
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private static int findBottleneck(Map<Integer, Map<Integer, Integer>> graph, List<Integer> path) {
        int bottleneck = Integer.MAX_VALUE;

        for (int i = 0; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);
            int capacity = graph.get(u).get(v);
            bottleneck = Math.min(bottleneck, capacity);
        }

        return bottleneck;
    }

    private static List<Integer> reconstructPath(Map<Integer, Integer> parent, int endNode) {
        List<Integer> path = new ArrayList<>();

        int curLength=0;

        int current = endNode;
        while (parent.containsKey(current)) {
            path.add(current);
            current = parent.get(current);
            curLength++;
        }
        Collections.reverse(path);
        maxLength = Math.max(maxLength, curLength);
        return path;
    }

    static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}
