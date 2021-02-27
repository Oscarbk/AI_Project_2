# AI_Project_2
##Search algorithm to sovle CSP Graph Coloring using heuristics and constraint propagation

### How to run script:
In the project folder, run “javac -cp lib\\* src/Search.java” to compile.

In the project folder, run “java -cp lib\\* src/Search.java” to run.

To change graph, edit the contents of "gc_basic.txt"

### Write-up:

This algorithm solves the k-colorable graph problem using a heuristic of 
minimum remaining values by keeping track of how many possible colors each 
vertex can take on. The most contained vertex is then chosen to be colored 
in order to get the algorithm to "fail-fast" and search more optimal 
branches. The algorithm also uses AC3 by keeping track of and updating each 
vertex's domain when a vertex takes on a color and maintaining arc 
consistency between all neighboring nodes. This allows detection of failure 
much earlier than forward checking.