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

    /*Juste pour être redéfinie par les héritiers (i.e AStarAlgorithm)*/
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
        	labels.add(node.getId(),createLabel(node,data));//noeuds enregistrés dans un tableau de Label, facilement accessibles avec leur id !
        }//chaque noeud est initialement non marqué, de coût infini, sans père et pas dans le tas
        
        Label Origin = labels.get(data.getOrigin().getId()) ; //on enregistre le Label de l'origine
    	Label Objectif = labels.get(data.getDestination().getId()) ; //et celui de l'objectif
    	
        Origin.setCost(0); //Met le coût du départ à 0
    	tas.insert(Origin); //l'ajoute dans le tas
    	Origin.setDansTas(true); //on previens le Label que le noeud est dans le tas
           	
    	
    	Label x = null ;
    	Label y = null ;
    	while (!tas.isEmpty()) { //tant qu'il reste des sommets dont on a pas étudié toutes les possibilités (i.e non marqués)
    		
    		x = tas.deleteMin(); //on supprime du tas le noeud avec le cout le plus faible
    		x.setDansTas(false); //on précise qu'il ne fait plus parti du tas
    		x.setMarque(true); //on le dit marqué
    		notifyNodeMarked(x.getSommet()); //et on l'indique au programme
    		
    		if (x.getSommet() == Objectif.getSommet()) { //si on a fini avec le noeud objectif (i.e on a le chemin souhaité), on sort de la boucle
    			break ;
    		}
    		
    		for (Arc successeur : x.getSommet().getSuccessors()) { //on parcours tous les chemins possibles (successeurs) partant de ce noeud
    			y = labels.get(successeur.getDestination().getId()); //on enregistre le label de ce successeur (=arc sortant)
        		notifyNodeReached(y.getSommet());//on indique au programme qu'on étudie ce point

    			if (!y.getMarque() && data.isAllowed(successeur)) { //on ne considère que les successeurs non marqués et les chemins qui respecte les contraintes (ex: si on est en voiture on évite les pistes cyclables)
					
    				/*(double)successeur.getLength() transformé en data.getCost(successeur) ainsi l'algorithme s'adapte au choix du mode : TIME ou LENGHT*/
    				if ( y.getCost() > x.getCost() + data.getCost(successeur) ) { //si le cout actuel est plus grand que le nouveau cout
    					y.setCost(x.getCost() + data.getCost(successeur)); //alors on met à jour le cout
    					y.setPere(successeur); //et on met à jour le père aussi
    					if (y.getDansTas()) { //si y est déjà  dans le tas
    						tas.remove(y); //on l'enlève du tas
    						tas.insert(y); //et on le remet => il est à la bonne place
    					} else { //sinon
    						tas.insert(y); //on l'ajoute à la bonne place
    						y.setDansTas(true); //on indique au label qu'il a été mis dans le tas
    					}
    					
    				}
    				
    			}
    			
    		}
    			
    	}
    	
    	
    	List<Arc> arcs = new ArrayList<Arc>();
    	Label k = Objectif; //j'initialise k à Objectif, il me permettra de naviguer ed sommet en sommet
        while (k.getSommet() != Origin.getSommet()) { //je sors de la boucle quand j'atteins mon noeud d'origine
        	
        	if (k.getPere() == null) { //si je ne peux plus remonter c'est que mon objectif n'est pas atteignable
        		return new ShortestPathSolution(data, Status.INFEASIBLE);
        	}
        	
        	arcs.add(k.getPere()); //j'enregistre chaque arc qui compose mon PCC
        	k = labels.get(k.getPere().getOrigin().getId()) ; //je remonte de label en label en suivant le chemin que j'ai calculé
        }
        Collections.reverse(arcs); //comme j'ai remonté de label en label, j'inverse l'ordre pour qu'on parte de mon origine
        
        
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(g, arcs));
        return solution;
    }
    


}
