import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
* Write a CSP algorithm to solve this coloring problem.
* The CSP algorithm should have the following components:
*   - Search algorithm to solve the CSP
*   - Heuristics (min remaining values, least constraining value)
*   - Constrain propagation using AC3
* */
class V {
    String vertex;
    int color = -1;
    public V(String vertex, int color)
    {
        this.vertex = vertex;
        this.color = color;
    }
    public String getName() {
        return this.vertex;
    }
}
class URI {
    String url;
    public URI(String url)
    {
        this.url = url;
    }
    public String getHost()
    {
        return this.url;
    }


}
public class CSP {
    public static void main(String[] args) throws IOException
    {
       /* Graph<V, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);;
        int colors = getGraph(g);
        getColoring(g, colors);*/



        Graph<URI, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        getGraph2(g);

        URI start = g
                .vertexSet().stream().filter(uri -> uri.getHost().equals("0")).findAny()
                .get();

        /*Iterator<URI> iterator = new DepthFirstIterator<>(g, start);
        while (iterator.hasNext()) {
            URI uri = iterator.next();
            System.out.println(uri.getHost());
        }*/
        URI test = g.vertexSet().stream().filter(V -> V.getHost().equals("0")).findAny().get();
        List<URI> testprint = Graphs.neighborListOf(g, test);
        System.out.println("Size: " + testprint.size());
        for (URI s : testprint) {
            System.out.println(s.getHost());
        }

    }
    public static int getGraph2(Graph<URI, DefaultEdge> g) throws IOException {
        int colors = 0;

        String test = "gc_basic.txt";
        File file = new File(test);
        System.out.println(test);
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;
        String edgePtr;

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

            URI v1, v2;
            if (!vertices.contains(tokens[0]))
            {
                System.out.println("Adding vertex: " + tokens[0]);
                v1 = new URI(tokens[0]);
                g.addVertex(v1);
                vertices.add(tokens[0]);
            }
            else
            {
                v1 = g
                        .vertexSet().stream().filter(uri -> uri.getHost().equals(tokens[0])).findAny()
                        .get();
            }
            if (!vertices.contains(tokens[1]))
            {
                System.out.println("Adding vertex: " + tokens[1]);
                v2 = new URI(tokens[1]);
                g.addVertex(v2);
                vertices.add(tokens[1]);
            }
            else
            {
                v2 = g
                        .vertexSet().stream().filter(uri -> uri.getHost().equals(tokens[1])).findAny()
                        .get();
            }
            g.addEdge(v1, v2);
        }
        return colors;
    }

    public static void getColoring(Graph<V, DefaultEdge> g, int colors)
    {
        /*System.out.println(g.toString());
        GreedyColoring<String, DefaultEdge> CSP = new GreedyColoring<>(g);
        System.out.println(CSP.getColoring().toString());*/
        //System.out.println(colors);

        //System.out.println(Graphs.neighborListOf(g, "0"));
        /*List<V> test = Graphs.neighborListOf(g, new V(0, -1));
        for (V s : test)
        {
            System.out.println(s.vertex);
        }*/

        V test = g.vertexSet().stream().filter(V -> V.getName().equals("0")).findAny().get();
        List<V> testprint = Graphs.neighborListOf(g, test);
        System.out.println(test.color);
        System.out.println("Size: " + testprint.size());
        for (V s : testprint) {
            System.out.println(s.vertex);
        }

    }
    public static int getGraph(Graph<V, DefaultEdge> g) throws IOException
    {
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

            V v1 = new V(tokens[0], -1);
            V v2 = new V(tokens[1], -1);
            g.addVertex(v1);
            g.addVertex(v2);
            //System.out.println("Trying to add edge between: " + tokens[0] + " " + tokens[1]);
            if (g.addEdge(v1, v2) == null) System.out.println("Could not add edge");
        }
        return colors;

        /*System.out.println(g.toString());
        GreedyColoring<String, DefaultEdge> CSP = new GreedyColoring<>(g);
        System.out.println(CSP.getColoring().toString());

        try {
            File myObj = new File("output.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(CSP.getColoring().toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }*/
    }
}
