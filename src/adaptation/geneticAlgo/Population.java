package adaptation.geneticAlgo;

import java.util.Random;

//import org.apache.commons.math3.distribution.NormalDistribution;




import adaptation.geneticAlgo.Individual;
import adaptation.utils.Combination;
import adaptation.utils.Config;
import adaptation.utils.Printer;

public class Population {

    private Individual[] individuals;
 //   private int[] seed = new int[Individual.defaultGeneLength]; // the long-term subplan
    private int populationSize;
    private Config config;
    private double amp;

    /*
     * Constructors
     */
    // Create a population
    public Population(double amp, Config config, boolean initialise) {
    	this.setAmp(amp);
    	this.setConfig(config);
    	setPopulationSize((int) Math.ceil((double) size() * amp));
    	////////System.out.println(size() + " - " + this.amp + " - " + getPopulationSize());
        setIndividuals(new Individual[getPopulationSize()]);

        // Initialise population
        if (initialise) {
            // Loop and create individuals
        	 //Individual.generateInit();      
        	////////System.out.println("size: " + getPopulationSize());
            for (int i = 0; i < getPopulationSize(); i++) {
            //////////System.out.println("pop-i: " + i + " *****************************");
                Individual newIndividual = new Individual(config);
                // get the netnumber
                Random r = new Random();
                int genePoolSize = newIndividual.getGenePool().size();
                int x = 0;
                if (genePoolSize == 0) {
                    x = 0;
                } else {
                    x = r.nextInt(genePoolSize * 2);
                }
                int net = 0;
                if (genePoolSize != 0) {
                    net = x > genePoolSize? 0: newIndividual.getCertainGene(i % newIndividual.getGenePool().size());
                	//net = Individual.getRandomGene();
                }
               // ////////System.out.println("try get individuals" + "x: " + x + "net: " + net);
                //////////System.out.println("gene pool: " + Individual.getGenePool().toString());

                newIndividual.generateIndividual(net); //TODO not simply getrandomGene
                saveIndividual(i, newIndividual);
            }
        }
    }    
    
    public Population(int size, Config config, boolean initialise) {
    	this.setAmp(amp);
    	setPopulationSize(size);
    	////////System.out.println(size() + " - " + this.amp + " - " + getPopulationSize());
        setIndividuals(new Individual[getPopulationSize()]);
        
        // Initialise population
        if (initialise) {
            // Loop and create individuals: therefore
            for (int i = 0; i < getPopulationSize(); i++) {
            //////////System.out.println("pop-i: " + i + " *****************************");
                Individual newIndividual = new Individual(config);
                // get the net number
                Random r = new Random();
                int genePoolSize = newIndividual.getGenePool().size();
                int x = r.nextInt(genePoolSize * 2);
                int net = 0;
                if (genePoolSize != 0) {
                    net = x > genePoolSize? 0: newIndividual.getCertainGene(i % newIndividual.getGenePool().size());
                	//net = Individual.getRandomGene();
                }
               // ////////System.out.println("try get individuals" + "x: " + x + "net: " + net);
                //////////System.out.println("gene pool: " + Individual.getGenePool().toString());

                newIndividual.generateIndividual(net); //TODO not simply getrandomGene
                saveIndividual(i, newIndividual);
            }
        }
    }  

    /* Getters */
    public Individual getIndividual(int index) {
        return getIndividuals()[index];
    }

    public Individual getFittest() {
        Individual fittest = getIndividuals()[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < getPopulationSize(); i++) {
            if (fittest.getFitness() >= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            	//fittest.setIndivId(i);
            	//fittest.run();
            }
            //////////System.out.println("fittest: " + fittest.getFitness());
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return getConfig().getActiveFlowNum();//getIndividuals().length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        getIndividuals()[index] = indiv;
    }
    
    public void print() {
    	for (int i = 0; i < getIndividuals().length; i++) {
    		System.out.print("i: " + i + " - ");
    		//Printer.printInt(getIndividuals()[i].getComb().getComb());
    		////////System.out.println("i: " + i + " - " + Fitness.getFitness(getIndividuals()[i]));
    		// //Printer.printInt(individuals[i].getGenes());
    	}
    }

	public Individual[] getIndividuals() {
		return individuals;
	}
	
	public boolean containsIndividual(Individual indiv){
		for(Individual i:getIndividuals()){
			if(indiv==i) return true;
		}
		return false;
	}

	public void setIndividuals(Individual[] individuals) {
		this.individuals = individuals;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public double getAmp() {
		return amp;
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
