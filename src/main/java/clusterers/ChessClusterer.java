package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.*;

public class ChessClusterer implements IClusterer {


    public Graph handle(Graph graph) {
        //Create list with vertices and their degrees
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (Integer v : graph.getVertexSet()) {
            vertices.add(new Vertex(v, graph.getVertexDegree(v)));
        }

        //Sort list
        Collections.sort(vertices, new Comparator<Vertex>() {
            public int compare(Vertex o1, Vertex o2) {
                return o1.degree - o2.degree;
            }
        });

        //Put every 2nd into one cluster 1, others into cluster 2
        Set<Integer> set1 = new HashSet<Integer>();
        Set<Integer> set2 = new HashSet<Integer>();
        boolean flag = true;
        for (Vertex v : vertices) {
            if (flag) {
                set1.add(v.getIndex());
            } else {
                set2.add(v.getIndex());
            }
            flag = !flag;
        }

        //Create result graph
        GraphFactory factory = new GraphFactory();
        return factory.generateFromCliques(graph.getN(), set1, set2);
    }

    @Override
    public String toString() {
        return "Chess";
    }

    private class Vertex {
        int index;
        int degree;

        public Vertex(int index, int degree) {
            this.index = index;
            this.degree = degree;
        }

        public int getIndex() {
            return index;
        }

        public int getDegree() {
            return degree;
        }
    }
}
