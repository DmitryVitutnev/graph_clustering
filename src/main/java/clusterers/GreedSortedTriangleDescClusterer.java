package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.*;

public class GreedSortedTriangleDescClusterer implements IClusterer {

    public Graph handle(final Graph graph) {
        Set<Integer> set1, set2;
        List<Integer> vertexList = graph.getVertexList();
        int n = graph.getN();
        final int[] trDegrees = new int[n];
        for(int i = 0; i < n; i++) {
            trDegrees[i] = 0;
        }
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                for(int k = j + 1; k < n; k++) {
                    if(graph.getEdge(i, j) && graph.getEdge(i, k) && graph.getEdge(k, j)) {
                        trDegrees[i]++;
                        trDegrees[j]++;
                        trDegrees[k]++;
                    }
                }
            }
        }

        Collections.sort(vertexList, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return trDegrees[o2] - trDegrees[o1];
            }
        });

        set1 = new HashSet<Integer>();
        set2 = new HashSet<Integer>();
        for (Integer v : vertexList) {
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
        return "GrSTD";
    }

}
