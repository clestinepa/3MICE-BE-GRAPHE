package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    
    /*permet d'adapter AStar*/
    protected Label createLabel(Node node, ShortestPathData data) {
    	LabelStar Loulou = new LabelStar(node);
    	
    	double valeur = node.getPoint().distanceTo(data.getDestination().getPoint()); //distance (en m) entre mon point et le point de destination
    	
    	if (data.getMode() == Mode.TIME) { //il faut transformer la distance en temps
    		
    		int vitesse = data.getMaximumSpeed(); // en km/h
    		if (vitesse == -1) { //si AbstractInputData n'a pas d'informations sur la Max speed
    			vitesse = data.getGraph().getGraphInformation().getMaximumSpeed(); //il faut aller la chercher dans les infos du graph
    		}
    		
    		valeur = 3.6 * valeur/ (double)vitesse ; //distance/vitesse = temps (en s) entre mon point et le point de destination (3.6 pour calibrer les unités)
    	}
    	
    	Loulou.setCostEstime(valeur);
    	return Loulou;
    }

}
