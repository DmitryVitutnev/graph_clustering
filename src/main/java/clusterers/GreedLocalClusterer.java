package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.HashSet;
import java.util.Set;

public class GreedLocalClusterer implements IClusterer {

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
            localSearch(graph, set1, set2);
        }


        GraphFactory factory = new GraphFactory();
        return factory.generateFromCliques(graph.getN(), set1, set2);
    }

    private void localSearch(Graph graph, Set<Integer> set1, Set<Integer> set2) {
        boolean flag = true;
        while (flag) {
            flag = false;
            int maxIncrease = 0;
            Set<Integer> currentSet = null;
            Integer currentVertex = null;

            for (Integer i : set1) {
                int increase = 0;
                for (Integer j : set1) {
                    if (i.equals(j)) {
                        continue;
                    }
                    if (graph.getEdge(i, j)) {
                        increase--;
                    } else {
                        increase++;
                    }
                }
                for (Integer j : set2) {
                    if (graph.getEdge(i, j)) {
                        increase++;
                    } else {
                        increase--;
                    }
                }
                if (increase > maxIncrease) {
                    maxIncrease = increase;
                    currentVertex = i;
                    currentSet = set1;
                }
            }
            for (Integer i : set2) {
                int increase = 0;
                for (Integer j : set2) {
                    if (i.equals(j)) {
                        continue;
                    }
                    if (graph.getEdge(i, j)) {
                        increase--;
                    } else {
                        increase++;
                    }
                }
                for (Integer j : set1) {
                    if (graph.getEdge(i, j)) {
                        increase++;
                    } else {
                        increase--;
                    }
                }
                if (increase > maxIncrease) {
                    maxIncrease = increase;
                    currentVertex = i;
                    currentSet = set2;
                }
            }

            if (maxIncrease > 0) {
                if (currentSet == set1) {
                    set1.remove(currentVertex);
                    set2.add(currentVertex);
                } else {
                    set2.remove(currentVertex);
                    set1.add(currentVertex);
                }
                flag = true;
            }

        }

    }

    @Override
    public String toString() {
        return "GreedLocal";
    }


}
