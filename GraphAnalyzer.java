import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class GraphAnalyzer {
    public static void main(String[] args) {
        String inputFileName = "graph_adjacency_list.csv";

        // Read the graph from CSV file
        Map<Integer, Map<Integer, Integer>> adjacencyList = readGraphFromCSV(inputFileName);

        // Randomly select a source node
        int sourceNode = getRandomSourceNode(adjacencyList.keySet());

        // Apply BFS to find the longest acyclic path
        List<Integer> longestPath = findLongestAcyclicPath(adjacencyList, sourceNode);

        // Define the end node of the longest path as the sink
        int sinkNode = longestPath.get(longestPath.size() - 1);

        // Print the results
        System.out.println("Source Node: " + sourceNode);
        System.out.println("Longest Acyclic Path: " + longestPath);
        System.out.println("Sink Node: " + sinkNode);
    }

    private static Map<Integer, Map<Integer, Integer>> readGraphFromCSV(String fileName) {
        Map<Integer, Map<Integer, Integer>> adjacencyList = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int vertex = Integer.parseInt(parts[0]);
                Map<Integer, Integer> neighbors = new HashMap<>();

                for (int i = 1; i < parts.length; i++) {
                    String[] neighborInfo = parts[i].split(":");
                    int neighbor = Integer.parseInt(neighborInfo[0]);
                    int capacity = Integer.parseInt(neighborInfo[1]);
                    neighbors.put(neighbor, capacity);
                }

                adjacencyList.put(vertex, neighbors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return adjacencyList;
    }

    private static int getRandomSourceNode(Set<Integer> vertices) {
        List<Integer> vertexList = new ArrayList<>(vertices);
        Random random = new Random();
        return vertexList.get(random.nextInt(vertexList.size()));
    }

    static ArrayList<Integer> longest=new ArrayList<>();

    private static List<Integer> findLongestAcyclicPath(Map<Integer, Map<Integer, Integer>> graph, int source) {
        List<Integer> longestPath = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parent = new HashMap<>();

        // for(Map.Entry<Integer, Map<Integer, Integer>> e: graph.entrySet()){
        //     System.out.println("root="+e.getKey());
        //     for(Map.Entry<Integer, Integer> f: e.getValue().entrySet()){
        //         System.out.println(f.getKey());
        //     }
        // }

        queue.add(source);
        visited.add(source);
        parent.put(source, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : graph.getOrDefault(current, Collections.emptyMap()).keySet()) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }

        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(source);
        helper(graph, source, temp);

        System.out.println(longest);

        int maxLen=0;

        for(int i=0;i<longest.size()-1;i++){
            maxLen+=graph.get(longest.get(i)).get(longest.get(i+1));
        }

        System.out.println("maxLen of path = "+maxLen);

        // Reconstruct the path
        Integer currentNode = Collections.max(visited); // Start from the furthest node
        while (currentNode != null) {
            longestPath.add(currentNode);
            currentNode = parent.get(currentNode);
        }

        Collections.reverse(longestPath);
        return longestPath;
    }

    static void helper(Map<Integer, Map<Integer, Integer>> graph, int source, ArrayList<Integer> cur){
        if(cur.size()>longest.size()){
            longest.clear();
            for(int i:cur)
                longest.add(i);
        }
        
        if(!graph.containsKey(source)){
            return;
        }

        else{
            Map<Integer, Integer> nodes=graph.get(source);

            for(Map.Entry<Integer, Integer> e: nodes.entrySet()){

                Integer curKey=e.getKey();

                if(!cur.contains(curKey)){
                    cur.add(curKey);
                    helper(graph, curKey, cur);
                    cur.remove(curKey);
                }

            }

        }

    }
}
