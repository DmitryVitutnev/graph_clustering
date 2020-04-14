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

public class SecondExperiment {

    public static void main(String[] args) throws IOException {

        List<Double> pList = Arrays.asList(0.33, 0.5, 0.66);

        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new GreedClusterer());
        clusterers.add(new GreedLocalEndClusterer());
        clusterers.add(new GreedLocalClusterer());

        IClusterer etalon = new BBCClusterer();

        for (Double p : pList) {
            doExperiment(p, 100, 1, clusterers, etalon);
        }

    }


    public static void doExperiment(double p, int maxN, int iterationsPerN, List<IClusterer> clusterers, IClusterer etalonClusterer) throws IOException {
        String directory = "results/" + maxN + "_" + iterationsPerN + "/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(directory + "secondExperiment_" + p + ".csv"))));

        GraphFactory factory = new GraphFactory();

        Graph cluster, diff;

        double etalonResult, result, accuracy;

        pw.print("Vertexes,");
        for(IClusterer c : clusterers) {
            pw.print(c + ",");
        }
        pw.println();

        List<Graph> graphs = new ArrayList<Graph>();;

        for (int n = 1; n <= maxN; n++) {
            graphs.clear();
            for (int i = 0; i < iterationsPerN; i++) {
                graphs.add(factory.generateRandom(n, p, 31));
            }

            etalonResult = 0.;
            for (Graph g : graphs) {
                cluster = etalonClusterer.handle(g);
                diff = GraphFunctions.symmetricDifference(g, cluster);
                etalonResult += diff.countEdges();
            }
            etalonResult /= iterationsPerN;


            pw.print("" + n + ",");
            for(IClusterer c : clusterers) {
                result = 0.;
                for (Graph g : graphs) {
                    cluster = c.handle(g);
                    diff = GraphFunctions.symmetricDifference(g, cluster);
                    result += diff.countEdges();
                }
                result /= iterationsPerN;
                accuracy = result == etalonResult ? 1 : result / etalonResult;
                pw.print("" + accuracy + ",");
            }
            pw.println();
            pw.flush();

        }

    }

}
