package clusterers;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.util.HashSet;
import java.util.Set;

public class BruteClusterer implements IClusterer {


    public Graph handle(Graph graph) {
        int k = 1;
        for (int i = 0; i < graph.getN(); i++) {
            k *= 2;
        }

        GraphFactory factory = new GraphFactory();

        int optDifference = graph.getN() * graph.getN();
        Graph optGraph = null;

        for (int i = 0; i < k; i++) {
            int m = i;
            Set<Integer> set1 = new HashSet<Integer>();
            Set<Integer> set2 = new HashSet<Integer>();
            for (int j = 0; j < graph.getN(); j++) {
                if (m % 2 == 0) {
                    set1.add(j);
                } else {
                    set2.add(j);
                }
                m /= 2;
            }
            Graph g = factory.generateFromCliques(graph.getN(), set1, set2);
            Graph diffG = GraphFunctions.symmetricDifference(graph, g);
            int diff = diffG.countEdges();
            if (diff < optDifference) {
                optDifference = diff;
                optGraph = g;
            }
        }

        return optGraph;
    }

    @Override
    public String toString() {
        return "Brute";
    }
}
