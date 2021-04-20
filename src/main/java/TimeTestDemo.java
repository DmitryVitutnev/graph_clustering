import clusterers.BaBClusterer;
import clusterers.IClusterer;
import graph.Graph;
import graph.GraphFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TimeTestDemo {


    public static PrintWriter pw;
    public static List<Graph> graphs;

    public static void main(String[] args) throws IOException {



        GraphFactory factory = new GraphFactory();

        Graph graph = factory.generateRandom(30, 0.33, (int)(Math.random()*42));

        IClusterer clusterer = new BaBClusterer();

        long startTime = System.currentTimeMillis();

        Graph result = clusterer.handle(graph);

        long timeSpent = System.currentTimeMillis() - startTime;

        System.out.println(timeSpent * 0.001);
        System.out.println();
        //System.out.println(result);

    }

}
