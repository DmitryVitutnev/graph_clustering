import clusterers.*;
import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static PrintWriter pw;
    public static List<Graph> graphs;

    public static void main(String[] args) throws IOException {

        pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File("results/comparation.txt"))));

        GraphFactory factory = new GraphFactory();

        graphs = new ArrayList<Graph>();
        for(int i = 0; i < 1800; i++) {
            graphs.add(factory.generateRandom(i, 0.33, 42));
        }

        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new BruteClusterer());
        clusterers.add(new BBCClusterer());
        clusterers.add(new GreedClusterer());
        clusterers.add(new BBCLocalClusterer());
        clusterers.add(new GreedLocalClusterer());
        clusterers.add(new GreedLocalEndClusterer());
        clusterers.add(new GreedSortedDescClusterer());
        clusterers.add(new GreedSortedAscClusterer());

        List<List<Integer>> results = new ArrayList<List<Integer>>();

        for(int i = 0; i < clusterers.size(); i++) {
            results.add(testClusterer(clusterers.get(i), 0.33, 100000));
        }

        for(int i = 0; i < clusterers.size(); i++) {
            for(int j = 1; j < clusterers.size(); j++) {
                if(results.get(j - 1).size() < results.get(j).size()) {
                    List<Integer> x = results.get(j - 1);
                    results.set(j - 1, results.get(j));
                    results.set(j, x);
                    IClusterer y = clusterers.get(j - 1);
                    clusterers.set(j - 1, clusterers.get(j));
                    clusterers.set(j, y);
                }
            }
        }

        pw.printf("%8s |", "Vertexes");

        for(int i = 0; i < clusterers.size(); i++) {
            pw.printf(" %20s", clusterers.get(i));
        }
        pw.println();

        for(int i = 0; i < results.get(0).size(); i++) {
            pw.printf("%8d |", i+1);
            for(int j = 0; j < results.size(); j++) {
                if(i < results.get(j).size()) {
                    pw.printf(" %20s", results.get(j).get(i));
                }
            }
            pw.println();
            pw.flush();
        }

        /*compareClusterers(clusterers.get(0), clusterers.get(1), 0.33, 100000);
        compareClusterers(clusterers.get(0), clusterers.get(2), 0.33, 100000);
        compareClusterers(clusterers.get(1), clusterers.get(2), 0.33, 100000);

        compareClusterers(clusterers.get(0), clusterers.get(1), 0.5, 100000);
        compareClusterers(clusterers.get(0), clusterers.get(2), 0.5, 100000);
        compareClusterers(clusterers.get(1), clusterers.get(2), 0.5, 100000);

        compareClusterers(clusterers.get(0), clusterers.get(1), 0.66, 100000);
        compareClusterers(clusterers.get(0), clusterers.get(2), 0.66, 100000);
        compareClusterers(clusterers.get(1), clusterers.get(2), 0.66, 100000);*/

    }

    public static void compareClusterers(IClusterer clusterer1, IClusterer clusterer2, double edgeKoef, long maxTime) throws FileNotFoundException {
        long curTime = System.currentTimeMillis();
        long endTime = curTime + maxTime;
        int i = 1;

        GraphFactory factory = new GraphFactory();
        int wins1 = 0;
        int wins2 = 0;

        String filename = "results/" + clusterer1 + "_" + clusterer2 + "_" + edgeKoef + ".txt";

        File file = new File(filename);
        PrintWriter printer = new PrintWriter(file);

        while(curTime < endTime) {
            StringBuilder sb = new StringBuilder();

            Graph graph = factory.generateRandom(i, edgeKoef, 42);
            Graph graph1 = clusterer1.handle(graph);
            Graph dGraph1 = GraphFunctions.symmetricDifference(graph, graph1);
            Graph graph2 = clusterer2.handle(graph);
            Graph dGraph2 = GraphFunctions.symmetricDifference(graph, graph2);
            sb.append("Vertex_number=").append(i).append(" Edge_number=").append(graph.countEdges())
                    .append(" ").append(clusterer1).append("_result=").append(dGraph1.countEdges())
                    .append(" ").append(clusterer2).append("_result=").append(dGraph2.countEdges());
            if (dGraph1.countEdges() < dGraph2.countEdges()) {
                wins1++;
                sb.append(" Winner=").append(clusterer1);
            } else if (dGraph1.countEdges() > dGraph2.countEdges()) {
                wins2++;
                sb.append(" Winner=").append(clusterer2);
            } else {
                sb.append(" Winner=").append("Both");
            }
            printer.println(sb.toString());
            printer.flush();

            curTime = System.currentTimeMillis();
            i++;
        }

        printer.println("" + clusterer1 + " wins = " + wins1);
        printer.println("" + clusterer2 + " wins = " + wins2);
        printer.flush();

        System.out.println("" + clusterer1 + " " + clusterer2 + " comparation finished");

    }

    public static List<Integer> testClusterer(IClusterer clusterer, double edgeKoef, long maxTime) {
        List<Integer> result = new ArrayList<Integer>();
        long curTime = System.currentTimeMillis();
        long endTime = curTime + maxTime;
        int i = 1;

        while(curTime < endTime && i < graphs.size()) {
            Graph graph = graphs.get(i);
            Graph graph1 = clusterer.handle(graph);
            Graph dGraph1 = GraphFunctions.symmetricDifference(graph, graph1);
            result.add(dGraph1.countEdges());

            curTime = System.currentTimeMillis();
            i++;
        }

        return result;
    }

}
