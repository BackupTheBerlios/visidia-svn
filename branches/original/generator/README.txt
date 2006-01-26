Generateur de graphe au format GML.
Le graphe est ecrit sur la sortie standard pour etre redirige 
vers un fichier.

utilisation:
- graphe cyclique
java GraphGenerator cyclic rayon nombre_sommet

- grille
java GraphGenerator grid nombre_colonnes nombre_lignes espacement


exemples:
java GraphGenerator cyclic 300 20 > circle20.gml
java GraphGenerator grid 10 10 50 > grid_10x10.gml

