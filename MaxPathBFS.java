import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MaxPathBFS {
    static class Vertex {
        int id;
        Map<Vertex, Integer> neighbors;

        Vertex(int id) {
            this.id = id;
            this.neighbors = new HashMap<>();
        }
    }

    public static void main(String[] args) {
        String graphFileName = "graph_adjacency_list.csv";

        // Load the graph from the file
        Map<Integer, Vertex> graph = loadGraph(graphFileName);

        // Randomly select a source node
        Vertex source = getRandomSource(graph);

        // Perform BFS to find the acyclic path with maximum path sum
        Pair<Vertex, Integer> result = findMaxPathBFS(source);

        Vertex sink = result.getKey();
        int maxPathSum = result.getValue();

        // Print the results
        System.out.println("Source Node: " + source.id);
        System.out.println("Sink Node: " + sink.id);
        System.out.println("Maximum Path Sum: " + maxPathSum);
    }

    static Map<Integer, Vertex> loadGraph(String fileName) {
        Map<Integer, Vertex> graph = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);

                Vertex vertex = graph.computeIfAbsent(id, Vertex::new);

                for (int i = 1; i < parts.length; i++) {
                    String[] neighborParts = parts[i].split(":");
                    int neighborId = Integer.parseInt(neighborParts[0]);
                    int capacity = Integer.parseInt(neighborParts[1]);

                    Vertex neighbor = graph.computeIfAbsent(neighborId, Vertex::new);
                    vertex.neighbors.put(neighbor, capacity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    static Vertex getRandomSource(Map<Integer, Vertex> graph) {
        List<Vertex> vertices = new ArrayList<>(graph.values());
        Random random = new Random();
        return vertices.get(random.nextInt(vertices.size()));
    }

    static Pair<Vertex, Integer> findMaxPathBFS(Vertex source) {
        Queue<Pair<Vertex, Integer>> queue = new LinkedList<>();
        Set<Vertex> visited = new HashSet<>();

        queue.offer(new Pair<>(source, 0));
        visited.add(source);

        Vertex maxPathEnd = source;
        int maxPathSum = 0;

        while (!queue.isEmpty()) {
            Pair<Vertex, Integer> current = queue.poll();
            Vertex currentVertex = current.getKey();
            int currentPathSum = current.getValue();

            if (currentPathSum > maxPathSum) {
                maxPathSum = currentPathSum;
                maxPathEnd = currentVertex;
            }

            for (Map.Entry<Vertex, Integer> neighborEntry : currentVertex.neighbors.entrySet()) {
                Vertex neighbor = neighborEntry.getKey();
                int capacity = neighborEntry.getValue();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(new Pair<>(neighbor, currentPathSum + capacity));
                }
            }
        }

        return new Pair<>(maxPathEnd, maxPathSum);
    }

    static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }
    }
}
