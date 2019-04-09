gOWL-1 is the  first version for materializing ABox.
You can use it to complete the expansion of the data and then use the query engine to complete the query answering.

Command is of the following form
java -jar gOWL-1.jar ONTOLOGY(TBox)_PATH DATA(ABox)_PATH ONTOLOGY-REWRITE_PATH

for example:
java -jar gOWL-1.jar lubm-ontology.nt LUBM1.nt lubm-rewrite.nt

Note that all files are triples in ".nt" format.

SPARQL Query Engine：
RDF3X:  https://github.com/gh-rdf3x/gh-rdf3x
gStore: https://github.com/Caesar11/gStore.git

Compared Engine：
PAGOdA: https://www.cs.ox.ac.uk/isg/tools/PAGOdA/
Ontop: https://github.com/ontop/vig
