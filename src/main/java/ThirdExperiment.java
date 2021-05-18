import clusterers.*;
import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThirdExperiment {

    public static void main(String[] args) throws IOException {

        List<Double> pList = Arrays.asList(0.33, 0.5, 0.66);

        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new GreedClusterer());
        clusterers.add(new GreedLocalEndClusterer());
        clusterers.add(new GreedSortedAscClusterer());
        clusterers.add(new GreedSortedDescClusterer());

        IClusterer etalon = new BruteClusterer();

        for (Double p : pList) {
            doExperiment(p, 20, 100, clusterers, etalon);
        }

    }


    public static void doExperiment(double p, int maxN, int iterationsPerN, List<IClusterer> clusterers, IClusterer etalonClusterer) throws IOException {
        String directory = "results/" + maxN + "_" + iterationsPerN + "/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(directory + "thirdExperiment_" + p + ".csv"))));

        GraphFactory factory = new GraphFactory();

        Graph cluster, diff;

        double etalonResult, result, accuracy;

        pw.print("Vertexes,");
        for(IClusterer c : clusterers) {
            pw.print(c + ",");
        }
        pw.println();

        List<Graph> graphs = new ArrayList<Graph>();
        List<Double> etalonResults = new ArrayList<>();

        for (int n = 1; n <= maxN; n++) {
            graphs.clear();
            for (int i = 0; i < iterationsPerN; i++) {
                graphs.add(factory.generateRandom(n, p));
            }
            etalonResults.clear();

            for (int i = 0; i < iterationsPerN; i++) {
                cluster = etalonClusterer.handle(graphs.get(i));
                diff = GraphFunctions.symmetricDifference(graphs.get(i), cluster);
                etalonResults.add((double)diff.countEdges());
            }

            pw.print("" + n + ",");

            for (IClusterer c : clusterers) {
                accuracy = 0.;
                for (int i = 0; i < iterationsPerN; i++) {
                    cluster = c.handle(graphs.get(i));
                    diff = GraphFunctions.symmetricDifference(graphs.get(i), cluster);
                    result = diff.countEdges();
                    result = result == etalonResults.get(i) ? 1 : result / etalonResults.get(i);
                    accuracy = Math.max(accuracy, result);
                }
                pw.print("" + accuracy + ",");
            }

            pw.println();
            pw.flush();


        }

    }

}
