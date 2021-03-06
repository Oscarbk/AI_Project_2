import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.*;
import java.util.*;

/*
* Write a CSP algorithm to solve this coloring problem.
* The CSP algorithm should have the following components:
*   x Search algorithm to solve the CSP
*   - Heuristics (min remaining values, least constraining value)
*   - Constraint propagation using AC3
* */
class V {
    String vertex;
    int color;
    int mrv;
    int edges;
    HashSet<Integer> domain;
    V ptr;
    public V(String vertex, int color)
    {
        this.vertex = vertex;
        this.color = color;
        this.edges = 0;
    }
    public String getID() {
        return this.vertex;
    }
    public void setDomain(int colors)
    {
        domain = new HashSet<>();
        for (int i = 0; i < colors; i++)
        {
            domain.add(i);
        }
    }
}
class arc {
    String x;
    String y;
    public arc (String x, String y)
    {
        this.x = x;
        this.y = y;
    }

}

public class CSP {
    static int numAgenda = 0;
    public static void main(String[] args) throws IOException
    {
        Graph<V, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

        int colors = getGraph(g);
        HashMap<String, Integer> coloring = new HashMap<>();

        getColoring2(g, colors, coloring);
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
                v1.mrv = colors;
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
                v2.mrv = colors;
                g.addVertex(v2);
                vertices.add(tokens[1]);
            }
            else
            {
                v2 = g.vertexSet().stream().filter(uri -> uri.getID().equals(tokens[1])).findAny().orElse(null);
            }

            // Add edge
            if (g.addEdge(v1, v2) != null)
            {
                v1.edges++;
                v2.edges++;
            }
        }
        return colors;
    }

    public static void getColoring2(Graph<V, DefaultEdge> g, int colors, HashMap<String, Integer> coloring)
    {
        V start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        Iterator<V> iterator = new DepthFirstIterator<>(g, start);
        ArrayList<V> set = new ArrayList<>();
        ArrayList<arc> arcs = new ArrayList<>();
        while (iterator.hasNext())
        {
            V v = iterator.next();
            set.add(v);

            // Set domain of each vertex
            v.setDomain(colors);

            // Construct arcs
            List<V> neighbors = Graphs.neighborListOf(g, v);
            for (V neighbor : neighbors)
            {
                arc arc1 = new arc(v.getID(), neighbor.getID());
                //arc arc2 = new arc(neighbor.getID(), v.getID());
                arcs.add(arc1);
                //arcs.add(arc2);
            }
            //System.out.println("Domain of " + v.getID() + ": " + v.domain.toString());
        }
        Queue<arc> agenda = new ArrayDeque<>();
        for (int i = 0; i < arcs.size(); i++)
        {
            agenda.add(arcs.get(i));
        }

        System.out.println();
        System.out.println("result: " + Util(g, set, colors, coloring, 0, arcs, agenda));

        int max = -1;
        for (Integer color : coloring.values())
        {
            if (color > max) max = color;
        }
        max++;
        System.out.println("Colors used: " + max + "\nColoring: " + coloring.toString());

        System.out.println("Verifying solution is valid: " + verifyGraph(g));

        /*start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        iterator = new DepthFirstIterator<>(g, start);
        while (iterator.hasNext()) {
            V v = iterator.next();
            System.out.println(v.getID() + "=" + v.color);
        }*/
        GreedyColoring<V, DefaultEdge> CSP = new GreedyColoring<>(g);
        System.out.println("What does greedy say: " + CSP.getColoring().toString());
        System.out.println("Amount of times agenda was used: " + numAgenda);
        //System.out.println(colors);

        //System.out.println(Graphs.neighborListOf(g, "0"));
        /*List<V> test = Graphs.neighborListOf(g, new V(0, -1));
        for (V s : test)
        {
            System.out.println(s.vertex);
        }*/

    }

    // TODO: Verify sample sets can be solved with given coloring (See set 4)
    public static boolean Util(Graph<V, DefaultEdge> g, ArrayList<V> set, int colors, HashMap<String, Integer> coloring, int i, ArrayList<arc> arcs, Queue<arc> agenda)
    {
        /*if (i == set.size())
        {
            System.out.println("reached max size");
            return true;
        }*/
        if (set.isEmpty())
        {
            System.out.println("Set empty");
            return true;
        }
        //V currentVertex = set.get(i);

        /*System.out.println("vertices and mrv:");
        V start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        Iterator<V> iterator = new DepthFirstIterator<>(g, start);
        while (iterator.hasNext()) {
            V v = iterator.next();
            System.out.println("vertex " + v.getID() + " has mrv of " + v.mrv);
        }*/

        // Find vertex with mrv
        V currentVertex;
        int index = -1;
        int mrv = colors + 10;
        for (int it = 0; it < set.size(); it++)
        {
            if (set.get(it).mrv < mrv)
            {
                mrv = set.get(it).mrv;
                index = it;
            }
        }
        currentVertex = set.remove(index);

        //System.out.println("Got vertex: " + currentVertex.getID());


        // Try to assign that vertex a color
        for (int c = 0; c < colors; c++)
        {
            currentVertex.color = c;
            if (verifyColor(g, currentVertex))
            {

                // We can assign color c to vertex v
                // See if AC3 is satisfied
                /*currentVertex.domain.clear();
                currentVertex.domain.add(c);*/
                if (!agenda.isEmpty()) numAgenda++;
                while(!agenda.isEmpty())
                {
                    arc temp = agenda.remove();

                    // Make sure there exists v1, v2, in V1, V2, such that v1 != v2
                    V v1 = g.vertexSet().stream().filter(V -> V.getID().equals(temp.x)).findAny().orElse(null);
                    V v2 = g.vertexSet().stream().filter(V -> V.getID().equals(temp.y)).findAny().orElse(null);



                    // No solution exists in this branch if a domain is empty
                    //if ((v1.domain.isEmpty()) || (v2.domain.isEmpty())) return false;

                    if (v2.color == -1) continue;
                    else
                    {
                        Boolean[] toRemove = new Boolean[colors];
                        Boolean domainChanged = false;
                        for (Integer it : v1.domain)
                        {
                            if (it == v2.color)
                            {
                                toRemove[it] = true;
                            }
                        }
                        for (int j = 0; j < toRemove.length; j++)
                        {
                            if ((toRemove[j] != null))
                            {
                                v1.domain.remove(j);
                                domainChanged = true;
                            }
                        }
                        // Need to check constraints with v1 on the right hand side
                        if (domainChanged)
                        {
                            for (arc it : arcs)
                            {
                                // Add arc back to agenda to check constraints again
                                if (it.y.equals(v1.getID()))
                                {
                                    agenda.add(it);
                                }
                            }
                        }
                    }
                    if ((v1.domain.isEmpty()) || (v2.domain.isEmpty()))
                    {
                        System.out.println("returning gfalse here");
                        return false;
                    }
                }

                // Reset agenda to check arc consistency on next iteration
                for (int j = 0; j < arcs.size(); j++)
                {
                    agenda.add(arcs.get(i));
                }


                //System.out.println("Checking if vertex " + currentVertex.getID() + " can be assigned color " + c);
                coloring.put(currentVertex.getID(), c);
                updateMRV(g, currentVertex);
                if (Util(g, set, colors, coloring, i + 1, arcs, agenda))
                {

                    return true;
                }

                //System.out.println("Assigning vertex " + currentVertex.getID() + " to color " + currentVertex.color + " did not work");
                coloring.remove(currentVertex.getID());
                currentVertex.color = -1;
            }
        }
        return false;
    }

    public static void getColoring(Graph<V, DefaultEdge> g, int colors, HashMap<String, Integer> coloring)
    {
        V start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        Iterator<V> iterator = new DepthFirstIterator<>(g, start);
        ArrayList<V> set = new ArrayList<>();
        while (iterator.hasNext()) {
            V v = iterator.next();
            set.add(v);
        }

        for (int i = 0; i < set.size(); i++)
        {
            //System.out.println(set.get(i).mrv);
        }

        while (!set.isEmpty())
        {
            /*System.out.println("vertices and mrv:");
            start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
            iterator = new DepthFirstIterator<>(g, start);
            while (iterator.hasNext()) {
                V v = iterator.next();
                System.out.println("vertex " + v.getID() + " has mrv of " + v.mrv);
            }
            // Find vertex with mrv
            ArrayList<V> toChooseFrom = new ArrayList<>();
            V currentVertex;
            int index = -1;
            int mrv = colors + 10;
            for (int i = 0; i < set.size(); i++)
            {
                if (set.get(i).mrv < mrv)
                {
                    mrv = set.get(i).mrv;
                    index = i;
                }
            }*/
            V currentVertex = set.remove(set.size()-1);
            //System.out.println("About to color vertex " + currentVertex.getID());

            for (int i = 0; i < colors; i++)
            {
                currentVertex.color = i;
                if (verifyColor(g, currentVertex))
                {
                    coloring.put(currentVertex.getID(), i);
                    updateMRV(g, currentVertex);
                    break;
                }
                else
                {
                    // Try the next color

                    currentVertex.color = -1;
                }
            }
            if (currentVertex.color == -1)
            {
                System.out.println("SETTING VERTEX TO -1: " + currentVertex.getID());

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
        /*System.out.println("Verify edges and mrv: ");
        start = g.vertexSet().stream().filter(V -> V.getID().equals("0")).findAny().orElse(null);
        iterator = new DepthFirstIterator<>(g, start);
        while (iterator.hasNext()) {
            V v = iterator.next();
            System.out.println("vertex " + v.getID() + " has " + v.edges + " edges and mrv of " + v.mrv);
        }*/
    }

    // Updates mrv
    public static void updateMRV(Graph<V, DefaultEdge> g, V v)
    {
        List<V> neighbors = Graphs.neighborListOf(g, v);
        for (V neighbor : neighbors)
            neighbor.mrv--;
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
            if (v.color == -1)
            {
                System.out.println("Did not color vertex " + v.getID());
                System.out.println("check neighbors of " + v.getID());
                List<V> neighbors = Graphs.neighborListOf(g, v);
                System.out.println("Vertex " + v.getID() + " color: " + v.color);
                for (V neighbor : neighbors)
                {
                    System.out.println("neighbor " + neighbor.getID() + " color: " + neighbor.color);
                }
                return false;
            }
            if (!verifyColor(g, v))
            {
                System.out.println("check neighbors of " + v.getID());
                List<V> neighbors = Graphs.neighborListOf(g, v);
                for (V neighbor : neighbors)
                {
                    if (v.color == neighbor.color)
                    {
                        System.out.println("Vertex " + v.getID() + " color: " + v.color);
                        System.out.println("neighbor " + neighbor.getID() + " color: " + neighbor.color);
                    }
                }
                return false;
            }
        }
        return true;
    }

}
