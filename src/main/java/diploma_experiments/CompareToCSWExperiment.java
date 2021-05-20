package diploma_experiments;

import clusterers.BBCClusterer;
import clusterers.BBCLocalClusterer;
import clusterers.GreedClusterer;
import clusterers.GreedLocalClusterer;
import clusterers.GreedLocalEndClusterer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareToCSWExperiment {
    public static void main(String[] args) throws IOException {

        List<Double> pList = Arrays.asList(0.1, 0.33, 0.5, 0.66, 0.9);

        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new BBCLocalClusterer());
        clusterers.add(new GreedClusterer());
        clusterers.add(new GreedLocalEndClusterer());
        clusterers.add(new GreedLocalClusterer());
        clusterers.add(new GreedSortedDescClusterer());

        IClusterer etalon = new BBCLocalClusterer();

        for (Double p : pList) {
            doExperiment(p, 50, 100, clusterers, etalon);
        }

    }


    public static void doExperiment(double p, int maxN, int iterationsPerN, List<IClusterer> clusterers, IClusterer etalonClusterer) throws IOException {
        String directory = "results_diploma/compare_csw/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        directory = directory + maxN + "_" + iterationsPerN + "/";

        path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(
                directory + p + ".csv"))));

        GraphFactory factory = new GraphFactory();

        Graph cluster, diff;

        double etalonResult, result, accuracy;

        pw.print("Vertices,");
        for(IClusterer c : clusterers) {
            pw.print(c + ",");
        }
        pw.println();

        List<Graph> graphs = new ArrayList<Graph>();;

        for (int n = 1; n <= maxN; n++) {
            graphs.clear();
            for (int i = 0; i < iterationsPerN; i++) {
                graphs.add(factory.generateRandom(n, p));
            }

            pw.print("" + n + ",");
            for(IClusterer c : clusterers) {
                accuracy = 0.;
                for (Graph g : graphs) {
                    cluster = etalonClusterer.handle(g);
                    diff = GraphFunctions.symmetricDifference(g, cluster);
                    etalonResult = diff.countEdges();

                    cluster = c.handle(g);
                    diff = GraphFunctions.symmetricDifference(g, cluster);
                    result = diff.countEdges();

                    accuracy += result == etalonResult ? 1 : result / etalonResult;
                }
                accuracy /= iterationsPerN;
                pw.print("" + accuracy + ",");
            }
            pw.println();
            pw.flush();

        }

    }
}
