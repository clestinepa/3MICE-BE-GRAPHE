package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {

	// Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath,
            invalidPath;
    
    
    @BeforeClass
    public static void initAll() throws IOException {
    	
    	final String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\belgium.mapgr";
    	final String pathName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\path_be_1026359_835620.path";

    	// Create a graph reader.
    	final GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

    	// Read the graph.
    	final Graph graph = reader.read();

    	// Create a PathReader.
    	final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));

    	// Read the path.
    	final Path path = pathReader.readPath(graph);

    }
     
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
