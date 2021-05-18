import clusterers.BaBClusterer;
import clusterers.GreedClusterer;
import clusterers.GreedLocalClusterer;
import clusterers.GreedLocalEndClusterer;
import clusterers.GreedSortedDescClusterer;
import clusterers.GreedSortedTriangleDescClusterer;
import clusterers.IClusterer;
import graph.Graph;
import graph.GraphFunctions;

import java.util.concurrent.ThreadLocalRandom;

public class WorstCaseExperiment {

    public static void main(String[] args) {
        IClusterer greedy = new GreedLocalClusterer();
        IClusterer etalon = new BaBClusterer();
        worstCaseExperiment(7, greedy, etalon);
    }

    private static double worstCaseExperiment(int n, IClusterer clusterer, IClusterer etalonClusterer) {
        int edgeCount = n*(n-1)/2;
        long maxSeed = 1;
        for (int i = 0; i < edgeCount; i++) {
            maxSeed *= 2;
        }
        double maxError = 0;
        for (long seed = 0; seed < maxSeed; seed++) {
            double error = experiment(seed, n, clusterer, etalonClusterer);
            if (error > maxError) {
                maxError = error;
                System.out.println(error);
            }
        }
        return maxError;
    }

    private static void worstCaseExperimentRandomized(int n, IClusterer clusterer, IClusterer etalonClusterer) {
        int edgeCount = n*(n-1)/2;
        long maxSeed = 1;
        for (int i = 0; i < edgeCount; i++) {
            maxSeed *= 2;
        }
        long seed;
        double maxError = 0;
        while (true) {
            seed = ThreadLocalRandom.current().nextLong(maxSeed);
            double error = experiment(seed, n, clusterer, etalonClusterer);
            if (error > maxError) {
                maxError = error;
                System.out.println(error);
            }
        }
        //return maxError;
    }

    private static double experiment(long seed, int n, IClusterer clusterer, IClusterer etalonClusterer) {

        long currentSeed = seed;
        Graph graph = new Graph(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (currentSeed % 2 == 1) {
                    graph.setEdge(i, j, true);
                }
                currentSeed /= 2;
            }
        }

        Graph clustered = clusterer.handle(graph);
        Graph etalon = etalonClusterer.handle(graph);
        double clusteredDistance = GraphFunctions.symmetricDifference(graph, clustered).countEdges();
        double etalonDistance = GraphFunctions.symmetricDifference(graph, etalon).countEdges();
        double ret = clusteredDistance / etalonDistance;
        if (ret == 3.0) {
            System.out.println(graph);
            System.out.println("-----------------------------");
            System.out.println(clustered);
            System.out.println("=============================");
        }
        return ret;

    }

}
