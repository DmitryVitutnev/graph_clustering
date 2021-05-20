package diploma_experiments;

import clusterers.BaBClusterer;
import clusterers.GreedClusterer;
import clusterers.GreedLocalClusterer;
import clusterers.GreedLocalEndClusterer;
import clusterers.GreedSortedAscClusterer;
import clusterers.GreedSortedDescClusterer;
import clusterers.IClusterer;
import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WorstCaseExperiment {

    public static void main(String[] args) throws IOException {
        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new GreedClusterer());
        clusterers.add(new GreedSortedDescClusterer());
        clusterers.add(new GreedSortedAscClusterer());
        clusterers.add(new GreedLocalClusterer());
        clusterers.add(new GreedLocalEndClusterer());

        IClusterer etalon = new BaBClusterer();
        doExperiment(9, clusterers, etalon);
    }

    public static void doExperiment(int maxN, List<IClusterer> clusterers, IClusterer etalonClusterer) throws IOException {
        String directory = "results_diploma/worst_case_at_least_4/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(
                directory + "experiment.csv"))));

        pw.print("Vertices,");
        for(IClusterer c : clusterers) {
            pw.print(c + ",");
        }
        pw.println();

        double accuracy;
        for (int n = 1; n <= maxN; n++) {
            pw.print("" + n + ",");
            for(IClusterer c : clusterers) {
                accuracy = worstCaseExperiment(n, c, etalonClusterer);
                pw.print("" + accuracy + ",");
                System.out.println("" + n + " " + c.toString() + " " + accuracy);
            }
            pw.println();
            pw.flush();
        }

    }

    public static void doExperimentRandomized(int maxN, List<IClusterer> clusterers, IClusterer etalonClusterer, float maxTime) throws IOException {
        String directory = "results_diploma/worst_case_randomized/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(
                directory + "experiment.csv"))));

        pw.print("Vertices,");
        for(IClusterer c : clusterers) {
            pw.print(c + ",");
        }
        pw.println();

        double accuracy;
        for (int n = 1; n <= maxN; n++) {
            pw.print("" + n + ",");
            for(IClusterer c : clusterers) {
                accuracy = worstCaseExperimentRandomized(n, c, etalonClusterer, maxTime * n * n);
                pw.print("" + accuracy + ",");
            }
            pw.println();
            pw.flush();
        }

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
            }
            if (4 - maxError < 0.00001) {
                return maxError;
            }
        }
        return maxError;
    }

    private static double worstCaseExperimentRandomized(int n, IClusterer clusterer, IClusterer etalonClusterer, float maxTime) {
        int edgeCount = n*(n-1)/2;
        long maxSeed = 1;
        for (int i = 0; i < edgeCount; i++) {
            maxSeed *= 2;
        }
        long seed;
        double maxError = 0;
        long startTime = System.currentTimeMillis();

        long timeSpent = 0;
        while (timeSpent < maxTime) {
            timeSpent = System.currentTimeMillis() - startTime;
            seed = ThreadLocalRandom.current().nextLong(maxSeed);
            double error = experiment(seed, n, clusterer, etalonClusterer);
            if (error > maxError) {
                maxError = error;
                System.out.println(error);
            }
        }
        return maxError;
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
        double ret = (clusteredDistance == etalonDistance) ? 1.0 : clusteredDistance / etalonDistance;
        return ret;

    }

}
