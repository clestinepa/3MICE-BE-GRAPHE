package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    
    /*Permet d'adapter AStar : retourne un LabelStar et non un Label*/
    protected Label createLabel(Node node, ShortestPathData data) {
    	LabelStar Loulou = new LabelStar(node);
    	
    	/*Adaptation de la valeur de cout_estime selon le mode TIME ou LENGHT*/
    	double valeur = node.getPoint().distanceTo(data.getDestination().getPoint()); //distance à vol d'oiseau (en m) entre mon point et le point de destination
    	
    	if (data.getMode() == Mode.TIME) { //il faut transformer la distance en temps  		
    		int vitesse = data.getMaximumSpeed(); //en km/h
    		if (vitesse == -1) { //si AbstractInputData n'a pas d'informations sur la Max speed
    			vitesse = data.getGraph().getGraphInformation().getMaximumSpeed(); //il faut aller la chercher dans les infos du graph
    		}		
    		valeur = 3.6 * valeur/ (double)vitesse ; //distance/vitesse = temps à vol d'oiseau (en s) entre mon point et le point de destination (facteur 3.6 pour calibrer les unités)
    	}
    	
    	Loulou.setCostEstime(valeur); //initialise correctement cout_estime
    	return Loulou;
    }

}
