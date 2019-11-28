package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

    private int n;
    private boolean[][] edges;
    private List<Integer> vertexList;
    private int[] degrees;

    public Graph(int n) {
        this.n = n;
        if (n == 0) {
            return;
        }
        edges = new boolean[n][n];
        vertexList = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            vertexList.add(i);
        }
        degrees = new int[n];
        for(int i = 0; i < n; i++) {
            degrees[i] = 0;
        }
    }

    public int getN() {
        return n;
    }

    public List<Integer> getVertexList() {
        return vertexList;
    }

    public boolean getEdge(int v1, int v2) {
        return edges[v1][v2];
    }

    public void setEdge(int v1, int v2, boolean value) {
        if (v1 == v2) {
            return;
        }
        if(edges[v1][v2] == value) {
            return;
        }
        edges[v1][v2] = value;
        edges[v2][v1] = value;
        if(value) {
            degrees[v1]++;
            degrees[v2]++;
        } else {
            degrees[v1]--;
            degrees[v2]--;
        }
    }

    public int countEdges() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (edges[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    public int getVertexDegree(int v) {
        return degrees[v];
    }

    public Set<Integer> getVertexNeighbours(int v) {
        Set<Integer> result = new HashSet<Integer>();
        for (int i = 0; i < n; i++) {
            if (edges[i][v]) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (edges[i][j]) {
                    string.append(1);
                } else {
                    string.append(0);
                }
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}
