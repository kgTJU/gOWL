package cn.edu.tju.pellet;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class TautologyVerify {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//System.out.println("Pellet test......");
		String prefix = "http://tju.edu.cn/emptyKB.owl";
		
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			//读取本体文件
			File file = new File("emptyKB.owl");
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + ontology);
	        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
	        System.out.println("    from: " + documentIRI);
	        
	        PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        // Create a console progress monitor.  This will print the reasoner progress out to the console.
	        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	        // Specify the progress monitor via a configuration.  We could also specify other setup parameters in
	        // the configuration, and different reasoners may accept their own defined parameters this way.
	        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	        // Create a reasoner that will reason over our ontology and its imports closure.  Pass in the configuration.
			PelletReasoner reasoner = reasonerFactory.createReasoner(ontology, config);    //创建reasoner
			System.out.println("done.\n");
			
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			OWLClass claA = factory.getOWLClass(IRI.create(prefix+"#A"));
			OWLClass claB = factory.getOWLClass(IRI.create(prefix+"#B"));
			OWLAxiom claASubClaUnionAB = factory.getOWLSubClassOfAxiom(claA, factory.getOWLObjectUnionOf(claA,claB));
			OWLAxiom claAScClaA = factory.getOWLSubClassOfAxiom(claA, claA);
			System.out.println("Assertion 1 : " + claAScClaA.toString());
			System.out.println("Assertion 2 : " + claASubClaUnionAB.toString());
			System.out.println("Assertion 1 is tautology? " + reasoner.isEntailed(claAScClaA));
			System.out.println("Assertion 2 is tautology? " + reasoner.isEntailed(claASubClaUnionAB));

        }
        catch(UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        }
	}

}
