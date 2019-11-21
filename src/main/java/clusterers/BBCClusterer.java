package clusterers;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BBCClusterer implements IClusterer {

    public Graph handle(Graph graph) {
        List<Graph> list = new LinkedList<Graph>();
        for (Integer v : graph.getVertexSet()) {
            list.add(subClustering(graph, v));
        }
        int min = graph.getN() * graph.getN();
        Graph result = null;
        for (Graph g : list) {
            int difference = GraphFunctions.symmetricDifference(graph, g).countEdges();
            if (difference < min) {
                min = difference;
                result = g;
            }
        }
        return result;
    }

    private Graph subClustering(Graph graph, int mainVertex) {
        Set<Integer> vertexSet, set1, set2;
        vertexSet = graph.getVertexSet();
        set1 = new HashSet<Integer>();
        set1.add(mainVertex);
        set1.addAll(graph.getVertexNeighbours(mainVertex));
        set2 = new HashSet<Integer>(vertexSet);
        set2.removeAll(set1);

        GraphFactory factory = new GraphFactory();
        return factory.generateFromCliques(graph.getN(), set1, set2);
    }

    @Override
    public String toString() {
        return "BBC";
    }
}
