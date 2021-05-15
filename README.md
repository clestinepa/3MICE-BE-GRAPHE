Une fois le projet importé dans Eclipse, il crée un nouveau package be-graphes-all
/!\ Eclipse est un handicapé : NE PAS OUVRIR les fichiers depuis be-graphes-all !! Bien que ce sont les mêmes car seulement des raccourcis, Eclipse n'arrive pas à les compiler.


Etape 2 :
-Diagramme UML très synthétique des classes Graph, Node, Path, Arcs, et RoadInformation (uniquement avec les attributs importants).
-Représentation choisie pour le graphe ?
-Création d'un programme from scratch

Etape 3 :
-Lors de l'agorithme de Dijkstra, l'étape de sélection d'un sommet de cout minimal doit se baser sur un tas. Savez-vous pourquoi ?
-Que contient le tas juste après l'étape d'initialisation de l'algorithme ? => Il ne contient que l'origine qu'on a choisie
-Quelle est la complexité théorique de l'algorithme de Dijkstra sans tas et avec tas ?

-Concernant les tests JUnit du tas : le programmeur expérimenté qui a écrit les tests JUnit du tas avait-il besoin de la classe BinaryHeap pour écrire les tests ?
-Cette question soulève un principe de conception important, cela vaut la peine d'y réfléchir 5 minutes... puis de chercher Test Driven Development sur le internet.
-Si vous ajoutez des méthodes publiques au tas, ajoutez-les aussi à l'interface PriorityQueue.

-Diagramme de classes UML avec les classes du package algo et algo.shortestpath.

-Quelle est la complexité de l'opération add sur le tas ? et de remove ?
-Et donc, quelle est la complexité théorique de votre Dijkstra ?
-Cela devrait vous sembler bizarre. La prochaine partie vous aidera à appréhender ce point.



PRINCIPES DES ALGOS dans Path.java :

getLenght() : on parcourt la liste des arcs du chemin et on somme la longueur de chaque arc (obtenu avec arc.getLenght() )
getTravelTime() : idem avec le temps mis pour chaque arc selon une vitesse speed donnée (obtenu avec arc.getTravelTime(speed) )
getMinimumTravelTime() : idem avec le temps minimum pour parcourir chaque arc (obtenu avec arc.getMinimumTravelTime() )

isValid() : vide => OK
            composé d'un seul sommet => OK
            la destination d'un arc n'est pas l'origine du prochain => NO
            le premier arc ne commence pas à l'origine => NO

createShortestPathFromNodes() : 0 sommet => path(graph)
                                1 sommet => path(graph, le sommet)
				plusieurs sommets => pour chaque sommet => sans successeur => erreur
                                                                        => avec successeurs (arcs sortants) => pour chaque arc => des arcs mènent au sommet voulu => le plus petit arc est enregistré dans listearcs
                                                                                                                               => aucun arc mène au sommet voulu => erreur
                                                  => path(graph, listearcs)
createFastestPathFromNodes() : idem mais c'est l'arc le plus rapide qui est enregistré


REMARQUES dans DijkstraAlgorithm.java :

Mon programme peut rencontrer deux cas particuliers lors de la contrainte "only cars" :
-l'origine se situe à un endroit non accessible par voitures => mon algo s'arrête assez tôt et indique un chemin impossible
-l'objectif n'est pas accessible en voiture => mon algo parcous TOUS les chemins de la carte avant d'en conclure qu'aucun chemin n'est possible
 Pour éviter une telle recherche inutile, on pourrait faire une vérification en amont des arcs menant à l'objectif => s'il n'est pas accessible, on stop l'algorithme avant !
 (dans notre contexte, ce cas est trop rare pour qu'on s'en préoccupe)


REMARQUE dans BinaryHeap.java :

remove() remplace l'élément que l'on souhaite enlever par le dernier élément, on réduit la taille du tas puis on place correctement le dernier élément dans le tas (avec percolateUp() et percolateDown()) => avec ce raisenement, les tests JUnits fonctionnent.
Mais /!\ que faisons nous si l'élément que l'on souhaite enlever EST le dernier élément ? => On réduit simplement la taille du tas !
Sans cette vérification, on remplace un élément par lui même (pas de pb, ça sert juste à rien), on réduit la taille du tas (parfait, c'est ce qu'on voulait), et on utilise percolateUp() et percolateDown() sur un élément qui ne fait plus parti du tas ! => le prgm plante