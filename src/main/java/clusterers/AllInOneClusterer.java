package clusterers;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphFunctions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AllInOneClusterer implements IClusterer {

    public Graph handle(Graph graph) {
        Graph result = null;
        GraphFactory factory = new GraphFactory();
        result = factory.generateFromCliques(graph.getN(), new HashSet<Integer>(graph.getVertexList()), new HashSet<Integer>());
        return result;
    }

    @Override
    public String toString() {
        return "One";
    }

}
