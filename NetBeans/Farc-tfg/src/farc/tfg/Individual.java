package farc.tfg;

/**
 * <p>
 * Title: Individual</p>
 *
 * <p>
 * Description: This class contains the representation of the individuals of the
 * population</p>
 *
 * <p>
 * Copyright: Copyright KEEL (c) 2007</p>
 *
 * <p>
 * Company: KEEL </p>
 *
 * @author Written by Jesus Alcal� (University of Granada) 09/02/2010
 * @version 1.0
 * @since JDK1.5
 */
import farc.org.core.Randomize;
import java.lang.*;
//import java.util.Random;

public class Individual implements Comparable {

    double[][] gene;
    int[] geneR;
    double fitness, accuracy, w1;
    int n_e, nGenes;
    RuleBase ruleBase;

    public Individual() {
    }

    public Individual(RuleBase ruleBase, DataBase dataBase, double w1) {
        this.ruleBase = ruleBase;
        this.w1 = w1;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.accuracy = 0.0;
        this.n_e = 0;
        this.nGenes = dataBase.getnLabelsReal(); //El nombre no es representativo

        if (this.nGenes > 0) {
            this.gene = new double[this.ruleBase.size()][this.nGenes];
        }
        this.geneR = new int[this.ruleBase.size()];
//    for (int i = 0; i < this.geneR.length; i++)   this.geneR[i] = 1;
    }

    public Individual clone() {
        Individual ind = new Individual();

        ind.ruleBase = this.ruleBase;
        ind.w1 = this.w1;
        ind.fitness = this.fitness;
        ind.accuracy = this.accuracy;
        ind.n_e = this.n_e;
        ind.nGenes = this.nGenes;

        if (this.nGenes > 0) {
            ind.gene = new double[this.ruleBase.size()][this.nGenes];
            for (int j = 0; j < this.ruleBase.size(); j++) {
                for (int k = 0; k < this.nGenes; k++){
                    ind.gene[j][k] = this.gene[j][k];
                }
            }
        }

        ind.geneR = new int[this.geneR.length];
        for (int j = 0; j < this.geneR.length; j++) {
            ind.geneR[j] = this.geneR[j];
        }

        return ind;
    }

    public void reset() {
        if (this.nGenes > 0) {
            for (int i = 0; i < this.ruleBase.size(); i++) {
                for (int j = 0; j < this.nGenes; j++){
                    this.gene[i][j] = 0.5;
                }
            }
        }
        for (int i = 0; i < this.geneR.length; i++) {
            this.geneR[i] = 1;
        }
    }

    public void randomValues() {
        if (this.nGenes > 0) {
            for (int i = 0; i < this.ruleBase.size(); i++) {
                for (int j = 0; j < this.nGenes; j++){
                    this.gene[i][j] = Randomize.Rand();
                }
            }
        }

        for (int i = 0; i < this.geneR.length; i++) {
            if (Randomize.Rand() < 0.5) {
                this.geneR[i] = 0;
            } else {
                this.geneR[i] = 1;
            }
        }
    }

    public int size() {
        return this.geneR.length;
    }

    public int getnSelected() {
        int i, count;

        count = 0;
        for (i = 0; i < this.geneR.length; i++) {
            if (this.geneR[i] > 0) {
                count++;
            }
        }

        return (count);
    }

    public boolean isNew() {
        return (this.n_e == 1);
    }

    public void onNew() {
        this.n_e = 1;
    }

    public void offNew() {
        this.n_e = 0;
    }

    public void setw1(double value) {
        this.w1 = value;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public double getFitness() {
        return this.fitness;
    }

    /**
     * **********************************************************************
     */
    /* Translations between string representation and floating point vectors */
    /**
     * **********************************************************************
     */
    private int StringRep(Individual indiv, int BITS_GEN) {
        int i, j, k, pos, length, count;
        long n;
        char last;
        double INCREMENTO;
        char[][] stringIndiv1;
        char[][] stringIndiv2;
        char[][] stringAux;

        length = this.nGenes * BITS_GEN;
        stringIndiv1 = new char[this.ruleBase.size()][length];
        stringIndiv2 = new char[this.ruleBase.size()][length];
        stringAux = new char[this.ruleBase.size()][length];

        INCREMENTO = 1.0 / (Math.pow(2.0, (double) BITS_GEN) - 1.0);

        pos = 0;
        for (k = 0; k < this.ruleBase.size(); k++){
            for (i = 0; i < this.nGenes; i++) {
                n = (int) (this.gene[k][i] / INCREMENTO + 0.5);

                for (j = BITS_GEN - 1; j >= 0; j--) {
                    stringAux[k][j] = (char) ('0' + (n & 1));
                    n >>= 1;
                }

                last = '0';
                for (j = 0; j < BITS_GEN; j++, pos++) {
                    if (stringAux[k][j] != last) {
                        stringIndiv1[k][pos] = (char) ('0' + 1);
                    } else {
                        stringIndiv1[k][pos] = (char) ('0' + 0);
                    }
                    last = stringAux[k][j];
                }
            }
            pos = 0;
        }

        pos = 0;
        for (k = 0; k < this.ruleBase.size(); k++){
            for (i = 0; i < this.nGenes; i++) {
                n = (int) (indiv.gene[k][i] / INCREMENTO + 0.5);

                for (j = BITS_GEN - 1; j >= 0; j--) {
                    stringAux[k][j] = (char) ('0' + (n & 1));
                    n >>= 1;
                }

                last = '0';
                for (j = 0; j < BITS_GEN; j++, pos++) {
                    if (stringAux[k][j] != last) {
                        stringIndiv2[k][pos] = (char) ('0' + 1);
                    } else {
                        stringIndiv2[k][pos] = (char) ('0' + 0);
                    }
                    last = stringAux[k][j];
                }
            }
            pos = 0;
        }

        count = 0;
        for (i = 0; i < length; i++) {
            for (k = 0; k < this.ruleBase.size(); k++){
                if (stringIndiv1[k][i] != stringIndiv2[k][i]) {
                    count++;
                }
            }
        }

        return (count);
    }

    public int distHamming(Individual ind, int BITS_GEN) {
        int i, count;

        count = 0;
        for (i = 0; i < this.geneR.length; i++) {
            if (this.geneR[i] != ind.geneR[i]) {
                count++;
            }
        }

        if (this.nGenes > 0) {
            count += StringRep(ind, BITS_GEN);
        }

        return (count);
    }

    public void Hux(Individual indiv) {
        int i, dist, random, aux, nPos;
        int[] position;

        position = new int[this.geneR.length];
        dist = 0;

        for (i = 0; i < this.geneR.length; i++) {
            if (this.geneR[i] != indiv.geneR[i]) {
                position[dist] = i;
                dist++;
            }
        }

        nPos = dist / 2;

        for (i = 0; i < nPos; i++) {
            random = Randomize.Randint(0, dist);

            aux = this.geneR[position[random]];
            this.geneR[position[random]] = indiv.geneR[position[random]];
            indiv.geneR[position[random]] = aux;

            dist--;

            aux = position[dist];
            position[dist] = position[random];
            position[random] = aux;
        }
    }

    public void xPC_BLX(Individual indiv, double d) {
        double I, A1, C1;
        int i, j;
        
        for (j = 0; j < this.ruleBase.size(); j++){
            for (i = 0; i < this.nGenes; i++) {
                I = d * Math.abs(gene[j][i] - indiv.gene[j][i]);

                A1 = gene[j][i] - I;
                if (A1 < 0.0) {
                    A1 = 0.0;
                }
                C1 = gene[j][i] + I;
                if (C1 > 1.0) {
                    C1 = 1.0;
                }
                gene[j][i] = A1 + Randomize.Rand() * (C1 - A1);

                A1 = indiv.gene[j][i] - I;
                if (A1 < 0.0) {
                    A1 = 0.0;
                }
                C1 = indiv.gene[j][i] + I;
                if (C1 > 1.0) {
                    C1 = 1.0;
                }
                indiv.gene[j][i] = A1 + Randomize.Rand() * (C1 - A1);
            }
        }
    }

    public void twoPoint(Individual indiv) {
        double aux;
        int i, p1, p2;

        p1 = Randomize.Randint(0, this.nGenes);
        p2 = Randomize.Randint(0, this.nGenes);

        if (p2 < p1) {
            i = p2;
            p2 = p1;
            p1 = p2;
        }
        
        for (int j = 0; j < this.ruleBase.size(); j++){
            for (i = 0; i < this.nGenes; i++) {
                if (i >= p1 && i <= p2) {
                    aux = gene[j][i];
                    gene[j][i] = indiv.gene[j][i];
                    indiv.gene[j][i] = aux;
                }
            }
        }
    }

    public RuleBase generateRB() {
        int i, bestRule;
        RuleBase ruleBase = this.ruleBase.clone();

        ruleBase.evaluate(this.gene, this.geneR);
        ruleBase.setDefaultRule();

        for (i = geneR.length - 1; i >= 0; i--) {
            if (geneR[i] < 1) {
                ruleBase.dataBase.dataBase3D.remove(i);
                ruleBase.remove(i);
            }
        }

        return ruleBase;
    }

    public void evaluate() {

//	  if (this.ruleBase.hasClassUncovered(this.geneR)) {
//		  this.fitness = Double.NEGATIVE_INFINITY;
//		  this.accuracy = 0.0;
//	  }
//	  else {
        this.ruleBase.evaluate(this.gene, this.geneR);
        this.accuracy = this.ruleBase.getAccuracy();

        this.fitness = this.accuracy;
        //this.fitness = this.accuracy - (this.w1 / (this.ruleBase.size() - this.getnSelected() + 1.0)) - (5.0 * this.ruleBase.getUncover()) - (5.0 * this.ruleBase.hasClassUncovered(this.geneR));
//		  this.fitness = this.accuracy - this.w1 * this.getnSelected();

//		  System.out.println ("Fitness: " + this.fitness);
//		  if (this.ruleBase.hasUncover())  this.fitness = Double.NEGATIVE_INFINITY;
//		  this.fitness = this.accuracy - this.w1 * this.getnSelected() - (5.0 * this.ruleBase.getUncover()) - (5.0 * this.ruleBase.hasClassUncovered(this.geneR));
//		  this.fitness = this.accuracy - this.w1 * this.getnSelected();
//	  }
    }

    public int compareTo(Object a) {
        if (((Individual) a).fitness < this.fitness) {
            return -1;
        }
        if (((Individual) a).fitness > this.fitness) {
            return 1;
        }
        return 0;
    }

}
