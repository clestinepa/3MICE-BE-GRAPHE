package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.utils.DijkstraTestWithMap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.AccessRestrictions;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {

	// Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static List<Node> nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e;   
    
    @BeforeClass
    public static void initAll() throws IOException {
    	
    	/*Définition du type d'arcs : type inconnu, accès inconnu, arc à sens unique,
    	vitesse max à 1, nom inconnu*/
    	RoadInformation RoadInfo = new RoadInformation(RoadType.UNCLASSIFIED, new AccessRestrictions(), true, 1, null);

     	/*Création de la liste des 6 nodes : ID définit, pas associé à un point de la map*/
    	nodes = new ArrayList<Node>();
		for (int i = 0; i < 6; i++) {
			nodes.add(new Node(i, null));
		}
		
		/*Ajout des arcs : relie les deux nodes avec une distance arbitraire, applique
		les infos de l'arc définit plus haut, n'est pas associé à des points sur une map*/
		a2b = Node.linkNodes(nodes.get(0), nodes.get(1), 4, RoadInfo, null);
		a2c = Node.linkNodes(nodes.get(0), nodes.get(2), 6, RoadInfo, null);
		b2d = Node.linkNodes(nodes.get(1), nodes.get(3), 11, RoadInfo, null);
		b2e = Node.linkNodes(nodes.get(1), nodes.get(4), 5, RoadInfo, null);
		b2f = Node.linkNodes(nodes.get(1), nodes.get(5), 9, RoadInfo, null);
		c2a = Node.linkNodes(nodes.get(2), nodes.get(0), 9, RoadInfo, null);
		c2b = Node.linkNodes(nodes.get(2), nodes.get(1), 5, RoadInfo, null);
		c2f = Node.linkNodes(nodes.get(2), nodes.get(5), 4, RoadInfo, null);
		e2c = Node.linkNodes(nodes.get(4), nodes.get(2), 6, RoadInfo, null);
		e2d = Node.linkNodes(nodes.get(4), nodes.get(3), 10, RoadInfo, null);
		e2f = Node.linkNodes(nodes.get(4), nodes.get(5), 6, RoadInfo, null);
		f2e = Node.linkNodes(nodes.get(5), nodes.get(4), 8, RoadInfo, null);
		
		/*Définition du graph : ID de la map = ID, sans nom, de noeuds créés, statistiques inconnues*/
		graph = new Graph("ID", "", nodes, null);
		
    }
     
	@Test
	public void testDoRun() {
		System.out.println("#####-----Test de validité avec oracle sur un exemple simplifié-----#####\n");
		
		new ArcInspectorFactory(); //création des filtres
		List<ArcInspector> filters = new ArrayList<>();
		filters = ArcInspectorFactory.getAllFilters() ;
		
		for (int k = 0; k < filters.size() ; k++) { //pour tout les filtres
			
			System.out.println(filters.get(k));
			
			for (int i=0;  i < nodes.size(); i++) { //pour les 6 noeuds
	
				/*Affichage du point de départ */
				System.out.print("x"+(nodes.get(i).getId()+1) + " :");
					
				for (int j=0;  j < nodes.size(); ++j) { 
	
					if(nodes.get(i)==nodes.get(j)) {
						System.out.print("     -    ");
					}
					else{
	
						ArcInspector arcInspectorDijkstra = filters.get(k); //filtre k
						ShortestPathData data = new ShortestPathData(graph, nodes.get(i), nodes.get(j), arcInspectorDijkstra);
	
						BellmanFordAlgorithm B = new BellmanFordAlgorithm(data);
						DijkstraAlgorithm D = new DijkstraAlgorithm(data);
	
						/*Récupération des solutions de Bellman et Dijkstra pour comparer*/
						ShortestPathSolution solution = D.run();
						ShortestPathSolution expected = B.run();
	
						/*Si aucun chemin n'est trouvé */
						if (solution.getPath() == null) {
							assertEquals(expected.getPath(), solution.getPath());
							System.out.print("  (infini)");
						}
						/*Si un plus court chemin est trouvé */
						else {
	
							/*Calcul du coût de la solution */
							float costSolution = solution.getPath().getLength();
							float costExpected = expected.getPath().getLength();
							assertEquals(costExpected, costSolution, 0);
	
							/*On récupère l'avant dernier sommet du chemin de la solution (=sommet père de la destination) */
							List<Arc> arcs = new ArrayList<Arc>();
							arcs = solution.getPath().getArcs();
							Node originOfLastArc = arcs.get(arcs.size()-1).getOrigin();
	
							/*Affiche le couple (coût, sommet père du Dest) */
							System.out.print("  ("+costSolution+ ", x" + (originOfLastArc.getId()+1) + ")");
						}
						
					}
	
				}
				System.out.println(); //saut de ligne
				}
		System.out.println();
		}
		System.out.println("Et oui, quelque soit le filtre, le résultat est le même... On pas pas défini les restrictions d'accès !");
	}
	
	@Test
	public void testDoScenarioDistanceHG() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\haute-garonne.mapgr";
		
		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testScenario(mapName, 1,origine,destination);    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		test.testScenario(mapName, 1,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		test.testScenario(mapName, 1,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		test.testScenario(mapName, 1,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000;
		test.testScenario(mapName, 1,origine,destination);    	
	}

	
	@Test
	public void testDoScenarioTempsHG() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\haute-garonne.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testScenario(mapName, 0,origine,destination);    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		test.testScenario(mapName, 0,origine,destination);    	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		test.testScenario(mapName, 0,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		test.testScenario(mapName, 0,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000;
		test.testScenario(mapName, 0,origine,destination);    	
	}

	@Test
	public void testDoScenarioDistanceINSA() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\insa.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : INSA ----------------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 300 ;
		destination = 300;
		test.testScenario(mapName, 1,origine,destination);    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 607;
		destination = 857;
		test.testScenario(mapName, 1,origine,destination);    	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = 2000;
		destination = 857;
		test.testScenario(mapName, 1,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 607;
		destination = 200000;
		test.testScenario(mapName, 1,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 2000;
		destination = 2000;
		test.testScenario(mapName, 1,origine,destination);   
	}

	@Test
	public void testDoScenarioTempsINSA() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\insa.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : INSA ----------------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 300 ;
		destination = 300;
		test.testScenario(mapName, 0,origine,destination);    
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 607;
		destination = 857;
		test.testScenario(mapName, 0,origine,destination);    	
	
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = 2000;
		destination = 857;
		test.testScenario(mapName, 0,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 607;
		destination = 200000;
		test.testScenario(mapName, 0,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 2000;
		destination = 2000;
		test.testScenario(mapName, 0,origine,destination);   
	}
	
	@Test
	public void testDoScenarioDistanceCarreDense() throws Exception {


		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\carre-dense.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 204097;
		test.testScenario(mapName, 1,origine,destination);    		
	}

	@Test
	public void testDoScenarioTempsCarreDense() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\carre-dense.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println("#####----- Mode : TEMPS -------------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 204097;
		test.testScenario(mapName, 0,origine,destination);    			
	}
	
	
	@Test
	public void testDoScenarioDistanceBelgique() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\belgium.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : BELGIQUE ------------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		test.testScenario(mapName, 1,origine,destination);    	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		test.testScenario(mapName, 1,origine,destination);    	

	}
	
	@Test
	public void testDoScenarioTempsBelgique() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\belgium.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : BELGIQUE ------------------------------######");
		System.out.println("#####----- Mode : TEMPS ----------------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		test.testScenario(mapName, 0,origine,destination);    	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		test.testScenario(mapName, 0,origine,destination);    	

	}

	@Test
	public void testDoScenarioMinTempsDistHG() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\haute-garonne.mapgr";
		
		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testScenarioSansOracle(mapName,origine,destination);   
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 38926;
		destination = 59015;
		test.testScenarioSansOracle(mapName,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 59015;
		test.testScenarioSansOracle(mapName,origine,destination);   	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 38926;
		destination = 200000;
		test.testScenarioSansOracle(mapName,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 200000; 
		test.testScenarioSansOracle(mapName,origine,destination);   
	}

	@Test
	public void testDoScenarioMinTempsDistCarreDense() throws Exception {
	
		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\carre-dense.mapgr";
		
		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : CARRE DENSE ---------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin simple ------");
		origine = 0;
		destination = 100052;
		test.testScenarioSansOracle(mapName,origine,destination);    
	}
	
	@Test
	public void testDoScenarioMinTempsDistBelgique() throws Exception {

		String mapName = "C:\\Users\\celes\\Documents\\INSA\\3MIC-E\\2eme_Semestre\\Graphes\\Map et Path\\belgium.mapgr";

		DijkstraTestWithMap test = new  DijkstraTestWithMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : BELGIQUE  -----------------------------######");
		System.out.println();
	
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 9922;
		destination = 34328;
		test.testScenarioSansOracle(mapName,origine,destination);    	
	
		System.out.println("----- Cas de sommets non connexes ------");
		origine = 9950;
		destination = 15860;
		test.testScenarioSansOracle(mapName,origine,destination);    
	}
}
	
