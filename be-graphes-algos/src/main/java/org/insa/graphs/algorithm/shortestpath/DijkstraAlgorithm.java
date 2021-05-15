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

    /*permet d'adapter AStar*/
    protected Label createLabel(Node node, ShortestPathData data) {
    	return new Label(node);
    }
   
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData(); //On a l'origin et la destination et le graph (avec des get)
        ShortestPathSolution solution = null;
        
        Graph g = data.getGraph() ;
        List<Label> labels = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        for (Node node : g.getNodes()) {
        	labels.add(node.getId(),createLabel(node,data));//tous les noeuds enregistrés dans un tableau de Label, facilement accessible par leur id !
        }
        
        Label Origin = labels.get(data.getOrigin().getId()) ; //on enregistre le Label de l'origine
    	Label Objectif = labels.get(data.getDestination().getId()) ; //et celui de l'objectif

        Origin.setCost(0); //Met le cout du départ à 0
    	tas.insert(Origin); //l'ajoute dans le tas
    	Origin.setDansTas(true); //on previens le Label que le noeud est dans le tas
        
    	Label x = null ;
    	Label y = null ;
    	while (!tas.isEmpty()) {
    		
    		x = tas.deleteMin(); //on supprime le noeud avec le cout le plus faible
    		x.setMarque(true); //on le dit marqué
    		x.setDansTas(false); //on présice qu'il ne fait plus parti du tas
    		notifyNodeMarked(x.getSommet()); //pour voir la progression
    		
    		if (x.getSommet() == Objectif.getSommet()) { //si on a fini avec le noeud objectif, on sort de la boucle
    			break ;
    		}
    		
    		for (Arc successeur : x.getSommet().getSuccessors()) { //on parcours tous les chemins possibles partant de ce noeud
    			y = labels.get(successeur.getDestination().getId()); //on enregistre le label de ce successeur
        		notifyNodeReached(y.getSommet());//pour voir la progression

    			if (!y.getMarque() && data.isAllowed(successeur)) { //on ne considère que les successeurs non marqués et les chemins qui respecte les contraintes (ex: si on est en voiture on évite les pistes cyclables)
    				
    				if ( y.getCost() > x.getCost() + data.getCost(successeur) ) { //si le cout actuel est plus grand que le nouveau cout
    					/*(double)successeur.getLength() transformé en data.getCost(successeur) ainsi l'algorithme s'adapte au choix du mode : TIME ou LENGHT*/
    					y.setCost(x.getCost() + data.getCost(successeur)); //alors on met à jour le cout
    					y.setPere(successeur); //et on met à jour le père aussi
    					if (y.getDansTas()) { //si y est déjà  dans le tas
    						tas.remove(y); //on l'enlève du tas
    						tas.insert(y); //on le remet => il est à la bonne place
    					} else { //sinon
    						tas.insert(y); //on l'ajoute à la bonne place
    						y.setDansTas(true); //on indique au label qu'il a été mis dans le tas
    					}
    					
    				}
    				
    			}
    			
    		}
    			
    	}
    	
    	List<Arc> arcs = new ArrayList<Arc>();
    	
    	Label k = Objectif; //j'initialise k à Objectif
        while (k.getSommet() != Origin.getSommet()) { //je sors de la boucle quand j'atteins mon noeud d'origine
        	
        	if (k.getPere() == null) { //si je ne peux plus remonter c'est que mon objectif n'est pas atteignable
        		return new ShortestPathSolution(data, Status.INFEASIBLE);
        	}
        	
        	arcs.add(k.getPere());
        	k = labels.get(k.getPere().getOrigin().getId()) ; //je remonte de label en label en suivant le chemin que j'ai calculé
        }
        Collections.reverse(arcs);
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(g, arcs));
        return solution;
        
        /*Mon programme marche et peut rencontrer deux cas particuliers lors de la contrainte "only cars" :
         * l'origine se situe à un endroit non accessible par voitures alors mon algo s'arrête assez tôt et indique un chemin impossible OU
         * c'est l'objectif qui n'est pas accessible alors mon algo parcous touut les chemins de la carte avant d'en conclure que ce n'est pas possible
         * Pour éviter une telle recherche inutile, on pourrait faire une vérification en amont des arcs menant à l'objectif voir s'il est isolé
         * mais ce cas est tellement rare qu'on s'en fiche un peu dans notre contexte*/
    }
    


}
