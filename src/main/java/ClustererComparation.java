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
import java.util.List;

public class ClustererComparation {

    public static void doExperiment(String mainDirectory, double p, int maxN, int iterationsPerN,
                                    List<IClusterer> clusterers, IClusterer etalonClusterer) throws IOException {
        String directory = mainDirectory + "/" + maxN + "_" + iterationsPerN + "/";

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(
                new File(directory + "result_" + p + ".csv"))));

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


            pw.print("" + n + ",");
            for(IClusterer c : clusterers) {
                result = 0.;
                etalonResult = 0.;
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
