package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.HashSet;
import java.util.Set;

public class BaBClusterer implements IClusterer {


    @Override
    public Graph handle(Graph graph) {
        Graph result;
        GraphFactory factory = new GraphFactory();

        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        boolean[] array = new boolean[graph.getN()];

        recur(graph, 0, graph.getN(), 0, set1, set2, Integer.MAX_VALUE, array);

        set1.clear();
        set2.clear();
        for (int i = 0; i < graph.getN(); i++) {
            if (array[i] == false) {
                set1.add(i);
            } else {
                set2.add(i);
            }
        }

        result = factory.generateFromCliques(graph.getN(), set1, set2);
        return result;
    }


    public int recur(Graph graph, int k, int n, int count, Set<Integer> cluster1, Set<Integer> cluster2, int record, boolean[] result) {
        if (count >= record) {
            return record;
        }
        if (k == n) {
            for (int i : cluster1) {
                result[i] = false;
            }
            for (int i : cluster2) {
                result[i] = true;
            }
            record = count;
        } else {
            int c = 0;
            for (int i : cluster1) {
                if (!graph.getEdge(i, k)) {
                    c++;
                }
            }
            for (int i : cluster2) {
                if (graph.getEdge(i, k)) {
                    c++;
                }
            }
            cluster1.add(k);
            record = recur(graph, k + 1, n, count + c, cluster1, cluster2, record, result);
            cluster1.remove(k);

            c = 0;
            for (int i : cluster2) {
                if (!graph.getEdge(i, k)) {
                    c++;
                }
            }
            for (int i : cluster1) {
                if (graph.getEdge(i, k)) {
                    c++;
                }
            }
            cluster2.add(k);
            record = recur(graph, k + 1, n, count + c, cluster1, cluster2, record, result);
            cluster2.remove(k);
        }
        return record;
    }

    @Override
    public String toString() {
        return "BaB";
    }

}
