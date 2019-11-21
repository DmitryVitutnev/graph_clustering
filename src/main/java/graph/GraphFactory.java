package graph;

import java.util.Random;
import java.util.Set;

public class GraphFactory {


    public Graph generateRandom(int n, double edgeKoef, long seed) {
        Random random = new Random(seed);
        Graph result = new Graph(n);
        double randomValue;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                randomValue = random.nextDouble();
                if (randomValue < edgeKoef) {
                    result.setEdge(i, j, true);
                }
            }
        }
        return result;
    }

    public Graph generateFromCliques(int n, Set<Integer>... cliques) {
        Graph result = new Graph(n);
        for (Set<Integer> s : cliques) {
            for (Integer v1 : s) {
                for (Integer v2 : s) {
                    result.setEdge(v1, v2, true);
                }
            }
        }
        return result;
    }

}
