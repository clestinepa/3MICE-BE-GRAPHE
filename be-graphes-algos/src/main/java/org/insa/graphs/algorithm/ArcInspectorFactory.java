package org.insa.graphs.algorithm;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.AccessRestrictions.AccessMode;
import org.insa.graphs.model.AccessRestrictions.AccessRestriction;

public class ArcInspectorFactory {

    /**
     * @return List of all arc filters in this factory.
     */
    public static List<ArcInspector> getAllFilters() {
        List<ArcInspector> filters = new ArrayList<>();

        // Common filters:

        // No filter (all arcs allowed):
        filters.add(new ArcInspector() {
            @Override
            public boolean isAllowed(Arc arc) {
                return true;
            }

            @Override
            public double getCost(Arc arc) {
                return arc.getLength();
            }

            @Override
            public int getMaximumSpeed() {
                return GraphStatistics.NO_MAXIMUM_SPEED;
            }

            @Override
            public Mode getMode() {
                return Mode.LENGTH;
            }

            @Override
            public String toString() {
                return "Shortest path, all roads allowed";
            }
        });

        // Only road allowed for cars and length:
        filters.add(new ArcInspector() {
            @Override
            public boolean isAllowed(Arc arc) {
                return arc.getRoadInformation().getAccessRestrictions()
                        .isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet
                                .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
            }

            @Override
            public double getCost(Arc arc) {
                return arc.getLength();
            }

            @Override
            public int getMaximumSpeed() {
                return GraphStatistics.NO_MAXIMUM_SPEED;
            }

            @Override
            public Mode getMode() {
                return Mode.LENGTH;
            }

            @Override
            public String toString() {
                return "Shortest path, only roads open for cars";
            }
        });

        // Only road allowed for cars and time:

        filters.add(new ArcInspector() {
            @Override
            public boolean isAllowed(Arc arc) {
                return true;
            }

            @Override
            public double getCost(Arc arc) {
                return arc.getMinimumTravelTime();
            }

            @Override
            public int getMaximumSpeed() {
                return GraphStatistics.NO_MAXIMUM_SPEED;
            }

            @Override
            public Mode getMode() {
                return Mode.TIME;
            }

            @Override
            public String toString() {
                return "Fastest path, all roads allowed";
            }
        });

        filters.add(new ArcInspector() {
            @Override
            public boolean isAllowed(Arc arc) {
                return arc.getRoadInformation().getAccessRestrictions()
                        .isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet
                                .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
            }

            @Override
            public double getCost(Arc arc) {
                return arc.getMinimumTravelTime();
            }

            @Override
            public int getMaximumSpeed() {
                return GraphStatistics.NO_MAXIMUM_SPEED;
            }

            @Override
            public Mode getMode() {
                return Mode.TIME;
            }

            @Override
            public String toString() {
                return "Fastest path, only roads open for cars";
            }
        });

        // Non-private roads for pedestrian :
        filters.add(new ArcInspector() {

            @Override
            public boolean isAllowed(Arc arc) {
                return arc.getRoadInformation().getAccessRestrictions()
                        .isAllowedForAny(AccessMode.FOOT, EnumSet.complementOf(EnumSet
                                .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
            }

            @Override
            public double getCost(Arc arc) {
                return arc.getTravelTime(
                        Math.min(getMaximumSpeed(), arc.getRoadInformation().getMaximumSpeed()));
            }

            @Override
            public String toString() {
                return "Fastest path for pedestrian";
            }

            @Override
            public int getMaximumSpeed() {
                return 5;
            }

            @Override
            public Mode getMode() {
                return Mode.TIME;
            }

        });
       
        
        // Add your own filters here (do not forget to implement toString()
        // to get an understandable output!):
        
        //Pour des cyclistes qui cherchent de la sécurité
        filters.add(new ArcInspector() {

            @Override
            public boolean isAllowed(Arc arc) {
                return arc.getRoadInformation().getAccessRestrictions()
                        .isAllowedForAny(AccessMode.BICYCLE, EnumSet.complementOf(EnumSet
                                .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
            }

            @Override
            public double getCost(Arc arc) {
            	double cout =  arc.getLength();
            	double m = 1 ;
            	if (arc.getRoadInformation().getAccessRestrictions().isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet.of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)))) {
                  /*	switch(arc.getRoadInformation().getType()) {
                  		case MOTORWAY : //autoroute
                  			cout = 1.0/0.0 ; //+inf, pas de vélo sur l'autoroute voyons !!
                  			break;
                  		case TRUNK : //route nationnale
            				cout = cout*80.0; 
            				break;
            			case PRIMARY :
            				break;
            			default :
            		} */
            		m = arc.getRoadInformation().getMaximumSpeed() ;
            	}
            	
               /*
                SECONDARY,
                MOTORWAY_LINK,
                TRUNK_LINK,
                PRIMARY_LINK,
                SECONDARY_LINK,
                TERTIARY,
                TRACK,
                RESIDENTIAL,
                UNCLASSIFIED,
                LIVING_STREET,
                SERVICE,
                ROUNDABOUT,
                PEDESTRIAN,
                CYCLEWAY,
                COASTLINE*/
                
            	
            	
                return cout * m ;
            }

            @Override
            public String toString() {
                return "Shortest path for careful cyclist";
            }

            @Override
            public int getMaximumSpeed() {
                return 19; //d'après google
            }

            @Override
            public Mode getMode() {
                return Mode.LENGTH;
            }

        });

        
        return filters;

    }

}
