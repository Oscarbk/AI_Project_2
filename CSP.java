import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.*;
import java.util.*;

/*
* Write a CSP algorithm to solve this coloring problem.
* The CSP algorithm should have the following components:
*   - Search algorithm to solve the CSP
*   - Heuristics (min remaining values, least constraining value)
*   - Constrain propagation using AC3
* */
class V {
    String vertex;
    int color;
    public V(String vertex, int color)
    {
        this.vertex = vertex;
        this.color = color;
    }
    public String getID() {
        return this.vertex;
    }
}

public class CSP {
    public static void main(String[] args) throws IOException
    {
        Graph<V, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        int colors = getGraph(g);
        HashMap<String, Integer> coloring = new HashMap<>();

        getColoring(g, colors, coloring);
    }
    public static int getGraph(Graph<V, DefaultEdge> g) throws IOException
    {
        int colors = -1;

        String test = "gc_basic.txt";
        File file = new File(test);
        System.out.println(test);
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;

        // Get to the point in the .txt file where color is defined
        while ((line = in.readLine()) != null) {
            if (line.contains("colors = ")) {
                String[] tokens = line.split(" ");
                colors = Integer.parseInt(tokens[2]);
            }
            if ((line.contains("# Graph:"))) break;
        }

        HashSet<String> vertices = new HashSet<>();
        while ((line = in.readLine()) != null) {
            if (line.equals("")) break;
            String[] tokens = line.split(",");

            V v1, v2;
            // Check if vertex is already in the graph
            if (!vertices.contains(tokens[0]))
            {
                v1 = new V(tokens[0], -1);
                g.addVertex(v1);
                vertices.add(tokens[0]);
            }
            else    // Get the vertex which already exists
            {
                v1 = g.vertexSet().stream().filter(uri -> uri.getID().equals(tokens[0])).findAny().orElse(null);
            }
            if (!vertices.contains(tokens[1]))
            {
                v2 = new V(tokens[1], -1);
                g.addVertex(v2);
                vertices.add(tokens[1]);
            }
            else
            {
                v2 = g.vertexSet().stream().filter(uri -> uri.getID().equals(tokens[1])).findAny().orElse(null);
            }

            // Always a new edge to add
            g.addEdge(v1, v2);
        }
        return colors;
    }

    public static void getColoring(Graph<V, DefaultEdge> g, int colors, HashMap<String, Integer> coloring)
    {
        V start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        //System.out.println("null?" + start);
        Iterator<V> iterator = new DepthFirstIterator<>(g, start);
        Stack<V> stack = new Stack<>();
        while (iterator.hasNext()) {
            V v = iterator.next();
            stack.push(v);
        }
        V lastVertex = stack.lastElement();

        while (!stack.isEmpty())
        {
            V currentVertex = stack.pop();

            // Return true if all vertices have been assigned a color
            if ((currentVertex.getID().equals(lastVertex.getID())) && (currentVertex.color != -1))
            {
                System.out.println("We ever get here?");
                return;
            }
            for (int i = 0; i < colors; i++)
            {
                currentVertex.color = i;
                if (verifyColor(g, currentVertex))
                {
                    coloring.put(currentVertex.getID(), i);
                    break;
                }
                else
                {
                    //System.out.println("Trying next color");
                    // Try the next color
                    currentVertex.color = -1;
                }
            }
        }
        int max = -1;
        for (Integer color : coloring.values())
        {
            if (color > max) max = color;
        }
        max++;
        System.out.println("Colors used: " + max + "\nColoring: " + coloring.toString());

        System.out.println("Verifying solution is valid: " + verifyGraph(g));
    }

    // Ensures a vertex does not share the same color with any neighboring vertices
    public static boolean verifyColor(Graph<V, DefaultEdge> g, V v)
    {
        List<V> neighbors = Graphs.neighborListOf(g, v);
        for (V neighbor : neighbors)
        {
            if (v.color == neighbor.color) return false;
        }
        return true;
    }

    // Iterates through each vertex in the graph and ensures no nodes have the same neighbor
    public static boolean verifyGraph(Graph<V, DefaultEdge> g)
    {
        V start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        Iterator<V> iterator = new DepthFirstIterator<>(g, start);
        while (iterator.hasNext()) {
            V v = iterator.next();
            if (!verifyColor(g, v)) return false;
        }
        return true;
    }

}
