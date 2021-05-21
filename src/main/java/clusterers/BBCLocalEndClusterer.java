package clusterers;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BBCLocalEndClusterer implements IClusterer {

    public Graph handle(Graph graph) {
        List<Graph> list = new LinkedList<Graph>();
        for (Integer v : graph.getVertexList()) {
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

        Set<Integer> vertexSet = new HashSet<Integer>(result.getVertexList());
        Set<Integer> set1 = new HashSet<Integer>();
        set1.add(0);
        set1.addAll(graph.getVertexNeighbours(0));
        Set<Integer> set2 = new HashSet<Integer>(vertexSet);
        set2.removeAll(set1);

        localSearch(result, set1, set2);

        GraphFactory factory = new GraphFactory();
        result = factory.generateFromCliques(result.getN(), set1, set2);
        return result;
    }

    private Graph subClustering(Graph graph, int mainVertex) {
        Set<Integer> vertexSet, set1, set2;
        vertexSet = new HashSet<Integer>(graph.getVertexList());
        set1 = new HashSet<Integer>();
        set1.add(mainVertex);
        set1.addAll(graph.getVertexNeighbours(mainVertex));
        set2 = new HashSet<Integer>(vertexSet);
        set2.removeAll(set1);

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
        return "BBCLE";
    }
}
