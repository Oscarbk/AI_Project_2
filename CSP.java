import org.jgrapht.Graph;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
* Write a CSP algorithm to solve this coloring problem.
* The CSP algorithm should have the following components:
*   - Search algorithm to solve the CSP
*   - Heuristics (min remaining values, least constraining value)
*   - Constrain propagation using AC3
* */
public class CSP {
    public static void main(String[] args) throws IOException
    {
        getGraph();
    }
    public static void getGraph() throws IOException
    {
        Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        int colors = 0;

        String test = "gc_basic.txt";
        File file = new File(test);
        System.out.println(test);
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;

        // Get to the point in the .txt file where color is defined
        while ((line = in.readLine()) != null)
        {
            if (line.contains("colors = "))
            {
                String[] tokens = line.split(" ");
                colors = Integer.parseInt(tokens[2]);
            }
            if ((line.contains("# Graph:"))) break;
        }
        while ((line = in.readLine()) != null)
        {
            if (line.equals("")) break;
            String[] tokens = line.split(",");

            g.addVertex(tokens[0]);
            g.addVertex(tokens[1]);
            g.addEdge(tokens[0], tokens[1]);
        }
        System.out.println(g.toString());
        GreedyColoring<String, DefaultEdge> CSP = new GreedyColoring<>(g);
        System.out.println(CSP.getColoring().toString());
    }
}
