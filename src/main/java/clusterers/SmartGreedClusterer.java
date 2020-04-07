package clusterers;

import graph.Graph;
import graph.GraphFactory;

import java.util.*;

public class SmartGreedClusterer implements IClusterer {
    public Graph handle(Graph graph) {
        Set<Integer> set1, set2;
        List<Integer> vertexList;
        vertexList = new ArrayList<Integer>(graph.getVertexList());
        set1 = new HashSet<Integer>();
        set2 = new HashSet<Integer>();
        List<Integer> removed = new ArrayList<Integer>();
        for(Integer v : vertexList) {
            Set<Integer> vertexSetT, set1T, set2T;
            vertexSetT = new HashSet<Integer>(vertexList);
            removed.add(v);
            vertexSetT.removeAll(removed);
            set1T = new HashSet<Integer>(set1);
            set2T = new HashSet<Integer>(set2);
            for(Integer vT : vertexSetT) {
                int koef1T = 0, koef2T = 0;
                for (Integer v1 : set1) {
                    if (graph.getEdge(vT, v1)) {
                        koef1T++;
                    } else {
                        koef1T--;
                    }
                }
                for (Integer v2 : set2) {
                    if (graph.getEdge(vT, v2)) {
                        koef2T++;
                    } else {
                        koef2T--;
                    }
                }
                if (koef1T >= koef2T) {
                    set1T.add(vT);
                } else {
                    set2T.add(vT);
                }
            }
            int koef1 = 0, koef2 = 0;
            for (Integer v1 : set1T) {
                if (graph.getEdge(v, v1)) {
                    koef1++;
                } else {
                    koef1--;
                }
            }
            for (Integer v2 : set2T) {
                if (graph.getEdge(v, v2)) {
                    koef2++;
                } else {
                    koef2--;
                }
            }
            if (koef1 >= koef2) {
                set1.add(v);
            } else {
                set2.add(v);
            }
        }

        GraphFactory factory = new GraphFactory();
        return factory.generateFromCliques(graph.getN(), set1, set2);
    }

    @Override
    public String toString() {
        return "SmGr";
    }
}
