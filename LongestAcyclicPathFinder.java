import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LongestAcyclicPathFinder {

    public static void main(String[] args) {
        String inputFile = "graph_adjacency_list.csv";
        int startNode = 83;

        System.out.println("Start node is "+startNode);

        try {
            Map<Integer, Map<Integer, Integer>> adjacencyList = readGraph(inputFile);
            int endNode = findEndNodeOfLongestPath(adjacencyList, startNode);
            System.out.println("The end node of the longest acyclic path is: " + endNode);
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
                }

                adjacencyList.put(vertexId, neighbors);
            }
        }

        return adjacencyList;
    }

    private static int findEndNodeOfLongestPath(Map<Integer, Map<Integer, Integer>> graph, int startNode) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> distance = new HashMap<>();
        queue.add(startNode);
        distance.put(startNode, 0);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : graph.getOrDefault(current, Collections.emptyMap()).keySet()) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, distance.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }

        // Find the node with the maximum distance
        int endNode = startNode;
        int maxDistance = 0;
        for (int node : distance.keySet()) {
            if (distance.get(node) > maxDistance) {
                maxDistance = distance.get(node);
                endNode = node;
            }
        }

        return endNode;
    }
}
