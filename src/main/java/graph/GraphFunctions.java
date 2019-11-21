package graph;

public class GraphFunctions {

    public static Graph symmetricDifference(Graph g1, Graph g2) {
        int n = Math.min(g1.getN(), g2.getN());
        Graph result = new Graph(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean value = g1.getEdge(i, j) ^ g2.getEdge(i, j);
                result.setEdge(i, j, value);
            }
        }
        return result;
    }

}
