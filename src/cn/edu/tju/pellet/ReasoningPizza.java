package cn.edu.tju.pellet;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.io.*;

import org.mindswap.pellet.RBox;
import org.mindswap.pellet.taxonomy.Taxonomy;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DLExpressivityChecker;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import aterm.ATermAppl;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class ReasoningPizza {

	public static final String PHYSICAL_URI = "http://www.co-ode.org/ontologies/pizza/2007/02/12/Thesaurus-35.5m.owl";
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Pellet test......");
		
		try {
			//org.semanticweb.owlapi.apibinding.OWLManager
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					
			File file = new File("lubm-ex-10.nt");
			//File file = new File("dbpedia_2015-10.nt");
			OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + localPizza);
	        IRI documentIRI = manager.getOntologyDocumentIRI(localPizza);
	        System.out.println("    from: " + documentIRI);
	        
	        PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        // Create a console progress monitor.  This will print the reasoner progress out to the console.
	        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	        // Specify the progress monitor via a configuration.  We could also specify other setup parameters in
	        // the configuration, and different reasoners may accept their own defined parameters this way.
	        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	        // Create a reasoner that will reason over our ontology and its imports closure.  Pass in the configuration.
			PelletReasoner reasoner = reasonerFactory.createReasoner(localPizza, config);
			System.out.println("done.");
		
			// Ask the reasoner to do all the necessary work now
			// reasoner.precomputeInferences();
            // We can examine the expressivity of our ontology (some reasoners do not support
            // the full expressivity of OWL)
			Set<OWLOntology> ontologies = Collections.singleton(reasoner.getRootOntology());
            DLExpressivityChecker checker = new DLExpressivityChecker(ontologies);
            System.out.println("Expressivity: " + checker.getDescriptionLogicName());
            
            // We can determine if the pizza ontology is actually consistent.  (If an ontology is
            // inconsistent then owl:Thing is equivalent to owl:Nothing - i.e. there can't be any
            // models of the ontology)
            long t1,t2,total;
            t1=System.currentTimeMillis();
			boolean consistent = reasoner.isConsistent();
			t2=System.currentTimeMillis();
			System.out.println("Consistent: " + consistent);
			total=t2-t1;
			OWLDataFactory fac = manager.getOWLDataFactory();
			System.out.println("Consistent check time:"+total+"ms");
			/*OWLClass publication = fac.getOWLClass(IRI.create("http://tju.edu.cn#ResearchAssistant"));
			NodeSet<OWLClass> dis = reasoner.getDisjointClasses(publication);
			Set<OWLClass> disSet = dis.getFlattened();
			System.out.println("The disjoint classes of Publication are: ");
            for(OWLClass cls : disSet) {
            	System.out.println("    " + cls);
            }*/
			
            // We can easily get a list of inconsistent classes.  (A class is inconsistent if it
            // can't possibly have any instances).  Note that the getInconsistentClasses method
            // is really just a convenience method for obtaining the classes that are equivalent
            // to owl:Nothing.
		
           /* Node<OWLClass> unsatisfiableClasses = reasoner.getUnsatisfiableClasses();
            System.out.println("unsatisfiableClasses  " + unsatisfiableClasses);
            //if (!(unsatisfiableClasses.)) {
                System.out.println("The following classes are unsatisfiable: ");
                for(OWLClass cls : unsatisfiableClasses) {
                    System.out.println("    " + cls);
                }*/
            //}
            /*else {
                System.out.println("There are no inconsistent classes");
            }*/
           
            t1=System.currentTimeMillis();
			 reasoner.getKB().realize();
			 reasoner.getKB().printClassTree();
			
		//生成类树
			 File result = new File("ClassTree.txt");
			 File result1 = new File("ClassTree1.txt");
				if(file.exists()){
					System.out.println("该文件已存在");
					//System.exit(0);//文件存在时，将自动覆盖当前内容
				}
				//在机器上产生文件，并且程序需要抛出异常
				PrintWriter output = new PrintWriter(result);
				reasoner.getKB().printClassTree(output);
				output.close();
				
		//获取Object Properties
			Set<ATermAppl> values = reasoner.getKB().getObjectProperties();
			System.out.println(values);
				
			RBox t=reasoner.getKB().getRBox();	
			System.out.println(t);
			
			
            t2=System.currentTimeMillis();
            total=t2-t1;
            System.out.println("classifying time:"+total+"ms");
            
           
            
            
           	
             //OWLClass vegPizza = fac.getOWLClass(IRI.create("http://www.pizza.com/ontologies/pizza.owl#VegetarianPizza"));
            //OWLClass vegPizza=fac.getOWLClass(IRI.create("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#Research_Techniques"));
            // Now use the reasoner to obtain the subclasses of vegetarian.
            // We can ask for the direct subclasses of vegetarian or all of the (proper) subclasses of vegetarian.
            // In this case we just want the direct ones (which we specify by the "true" flag).
            
            /*t1=System.currentTimeMillis();
            NodeSet<OWLClass> subClses = reasoner.getSubClasses(vegPizza, true);
            t2=System.currentTimeMillis();
            total=t2-t1;
            System.out.println("subClass time:"+total+"ms");*/
            
            // The reasoner returns a NodeSet, which represents a set of Nodes.
            // Each node in the set represents a subclass of vegetarian pizza.  A node of classes contains classes,
            // where each class in the node is equivalent. For example, if we asked for the
            // subclasses of some class A and got back a NodeSet containing two nodes {B, C} and {D}, then A would have
            // two proper subclasses.  One of these subclasses would be equivalent to the class D, and the other would
            // be the class that is equivalent to class B and class C.
            
            // In this case, we don't particularly care about the equivalences, so we will flatten this
            // set of sets and print the result
           /* Set<OWLClass> clses = subClses.getFlattened();
            System.out.println("The Subclasses of Research_Techniques are: ");
            for(OWLClass cls : clses) {
            	System.out.println("    " + cls);
            }*/
            System.out.println();
        }
        catch(UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        }
		
       // System.out.print( System.currentTimeMillis() - timer );
        //System.out.print(" ms\n");
	}

}
