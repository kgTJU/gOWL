gOWL
==========

gOWL-1 is the  first version for materializing ABox.

Data
==========

We provide lubm-ontology.nt(TBox) and LUBM1.nt(ABox) for gOWL.
 
gOWL also supports other data in ".nt" format, such as UOBM and DBpedia. 

Query
==========

LUBM-query, UOBM-query, DBpedia-query

Code
==========

The codes are in the folder gOWL.

Running
==========

1. For materializing the source RDF data, you need use the follow command

java -jar gOWL-1.jar ONTOLOGY(TBox)_PATH DATA(ABox)_PATH ONTOLOGY-REWRITE_PATH

for example:

java -jar gOWL-1.jar lubm-ontology.nt LUBM1.nt lubm-rewrite.nt

2. For executing queries, you can use SPARQL query engine

such as:

RDF3X:  https://github.com/gh-rdf3x/gh-rdf3x

gStore: https://github.com/Caesar11/gStore.git

