package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.* ;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData(); //On a l'origin et la destination et le graph (avec des get)
        ShortestPathSolution solution = null;
        
        Graph g = data.getGraph() ;
        List<Label> labels = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        for (Node node : g.getNodes()) {
        	labels.add(node.getId(),new Label(node));//tous les noeuds enregistrÃ©s dans un tableau de Label, facilement accessible par leur id !
        }
        
        Label Origin = labels.get(data.getOrigin().getId()) ; //on enregistre le Label de l'origin
        Origin.setCost(0); //Met le cout du dÃ©part Ã  0
    	tas.insert(Origin); //l'ajoute dans le tas
    	Origin.setDansTas(true); //on previens le Label que le noeud est dans le tas
        
    	Label Objectif = labels.get(data.getDestination().getId()) ;
    	Label x = null ;
    	Label y = null ;
    	while (!tas.isEmpty()) {
    		
    		x = tas.deleteMin(); //on supprime le noeud avec le cout le plus faible
    		x.setMarque(true); //on le dit marquÃ©
    		x.setDansTas(false); //on prÃ©sice qu'il ne fait plus parti du tas
    		
    		if (x.getSommet() == Objectif.getSommet()) { //si on a fini avec le noeud objectif, on sort de la boucle
    			break ;
    		}
    		
    		for (Arc successeur : x.getSommet().getSuccessors()) { //on parcours tous les chemins possibles partant de ce noeud
    			y = labels.get(successeur.getDestination().getId()); //on enregistre le label de ce successeur
    			
    			if (!y.getMarque()) { //on ne considÃ¨re que les successeurs non marquÃ©s
    				
    				if ( y.getCost() > x.getCost() + (double)successeur.getLength() ) { //si le cout actuel est plus grand que le nouveau cout
    					y.setCost(x.getCost() + (double)successeur.getLength()); //alors on met Ã  jour le cout
    					y.setPere(successeur); //et on met Ã  jour le pere aussi
    					if (y.getDansTas()) { //si y est dÃ©jÃ  dans le tas
    						tas.remove(y); //on l'enlÃ¨ve du tas
    						tas.insert(y); //on le remet => il est Ã  la bonne place
    					} else { //sinon
    						tas.insert(y); //on l'ajoute Ã  la bonne place
    						y.setDansTas(true); //on indique au label qu'il a Ã©tait mis dans le tas
    					}
    					
    				}
    				
    			}
    			
    		}
    			
    	}
    	
    	List<Arc> arcs = new ArrayList<Arc>();
    	
    	Label k = Objectif; //j'initialise k Ã  Objectif
        while (k.getSommet() != Origin.getSommet()) { //je sors de la boucle quand j'atteins mon noeud d'origine
        	arcs.add(k.getPere());
        	k = labels.get(k.getPere().getOrigin().getId()) ; //je remonte de label en label en suivant le chemin que j'ai calculÃ©
        }
        Collections.reverse(arcs);
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(g, arcs));
        return solution;
    }
    


}
