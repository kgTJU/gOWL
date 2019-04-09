package fdx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.mindswap.pellet.RBox;
import org.mindswap.pellet.tbox.TBox;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DLExpressivityChecker;

import aterm.ATermAppl;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.rules.rete.Rule;

public class Reason {
//public static final String PHYSICAL_URI = "http://www.co-ode.org/ontologies/pizza/2007/02/12/Thesaurus-35.5m.owl";	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public void generateClassTree(String path) throws FileNotFoundException {		
		//System.out.println("Pellet test......");
		try {
			//org.semanticweb.owlapi.apibinding.OWLManager
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					
			File file = new File(path);
			//File file = new File("dbpedia_2015-10.nt");
			OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);
			/*
			 * LocalPizza------OWLOntology----Ontology
			 * (<http://swat.cse.lehigh.edu/onto/univ-bench.owl> [Axioms: 369] [Logical axioms: 172])
			 */
			//System.out.println("Loaded ontology: " + localPizza);
	        IRI documentIRI = manager.getOntologyDocumentIRI(localPizza);
	        /*
	         * /G:/fdx/Pellet/lubm-ex-10.nt----documentIRI
	         */
	        System.out.println("    from: " + documentIRI);
	        
	        PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        // Create a console progress monitor.  This will print the reasoner progress out to the console.
	        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	        // Specify the progress monitor via a configuration.  We could also specify other setup parameters in
	        // the configuration, and different reasoners may accept their own defined parameters this way.
	        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	        // Create a reasoner that will reason over our ontology and its imports closure.  Pass in the configuration.
			PelletReasoner reasoner = reasonerFactory.createReasoner(localPizza, config);
			//System.out.println("done.");
		
			// Ask the reasoner to do all the necessary work now
			// reasoner.precomputeInferences();
            // We can examine the expressivity of our ontology (some reasoners do not support
            // the full expressivity of OWL)
			
			/*Set<OWLOntology> ontologies = Collections.singleton(reasoner.getRootOntology());
            DLExpressivityChecker checker = new DLExpressivityChecker(ontologies);
            System.out.println("Expressivity: " + checker.getDescriptionLogicName());*/
            
            // We can determine if the pizza ontology is actually consistent.  (If an ontology is
            // inconsistent then owl:Thing is equivalent to owl:Nothing - i.e. there can't be any
            // models of the ontology)
            long t1,t2,total;
            
            /*t1=System.currentTimeMillis();
			boolean consistent = reasoner.isConsistent();
			t2=System.currentTimeMillis();
			System.out.println("Consistent: " + consistent);
			total=t2-t1;
			OWLDataFactory fac = manager.getOWLDataFactory();
			System.out.println("Consistent check time:"+total+"ms");*/
			
            t1=System.currentTimeMillis();
			 reasoner.getKB().realize();
			 reasoner.getKB().printClassTree();
			 
//			TBox r = reasoner.getKB().getTBox();
//			System.out.println(r);
			Collection<ATermAppl> c = reasoner.getKB().getTBox().getAxioms();
			System.out.println(c);
			
			Set<ATermAppl> s = reasoner.getKB().getTransitiveProperties();
			for (ATermAppl str : s) {  
	            System.out.println(str);  
	        }  

		//生成类树
			 File result = new File("ClassTree.txt");
				if(file.exists()){
					System.out.println("该文件已存在");
					//System.exit(0);//文件存在时，将自动覆盖当前内容
				}
				//在机器上产生文件，并且程序需要抛出异常
				PrintWriter output = new PrintWriter(result);
				reasoner.getKB().printClassTree(output);
				output.close();
				
		//获取Object Properties
			/*Set<ATermAppl> values = reasoner.getKB().getObjectProperties();
			System.out.println(values);
				
			RBox t=reasoner.getKB().getRBox();	
			System.out.println(t);*/
			
			
            t2=System.currentTimeMillis();
            total=t2-t1;
            System.out.println("classifying time:"+total+"ms");
   
        }
        catch(UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        }
		
	}
}
