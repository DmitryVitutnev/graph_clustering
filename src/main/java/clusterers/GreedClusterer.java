package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.HashSet;
import java.util.Set;

public class GreedClusterer implements IClusterer {

    public Graph handle(Graph graph) {
        Set<Integer> vertexSet, set1, set2;
        vertexSet = graph.getVertexSet();
        set1 = new HashSet<Integer>();
        set2 = new HashSet<Integer>();
        for (Integer v : vertexSet) {
            int koef1 = 0, koef2 = 0;
            for (Integer v1 : set1) {
                if (graph.getEdge(v, v1)) {
                    koef1++;
                } else {
                    koef1--;
                }
            }
            for (Integer v2 : set2) {
                if (graph.getEdge(v, v2)) {
                    koef2++;
                } else {
                    koef2--;
                }
            }
            if (koef1 >= koef2) {
                set1.add(v);
            } else {
                set2.add(v);
            }
        }

        GraphFactory factory = new GraphFactory();
        return factory.generateFromCliques(graph.getN(), set1, set2);
    }

    @Override
    public String toString() {
        return "Greed";
    }

}
