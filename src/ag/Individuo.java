package ag;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Individuo implements Comparable<Individuo> {

    private final Random random = new Random();

    private int[] genes;

    private Double aptidao;

    /**
     * Inicializa um Individuo aleatorio na primeira geracao
     *
     * @param qtdAtributos
     */
    public Individuo(int qtdAtributos) {
        genes = new int[qtdAtributos];
        iniciarGenes();
    }

    /**
     * Inicializa um Individuo a partir de genes definidos (pais)
     *
     * @param genes
     */
    public Individuo(int[] genes) {
        this.genes = genes;
        //testa se deve fazer mutacao
        if (random.nextDouble() <= Principal.TAXADEMUTACAO) {
            int posAleatoria = random.nextInt(genes.length); //define gene que sera mutado
            mutacao(posAleatoria);
        }
    }

    private void iniciarGenes() {
        for (int i = 0; i < genes.length; i++) {
            if (Math.random() < 0.5) {
                genes[i] = 0;
            } else {
                genes[i] = 1;
            }
        }
    }

    private void mutacao(int posicao) {
        if (genes[posicao] == 1) {
            genes[posicao] = 0;
        } else {
            genes[posicao] = 1;
        }
    }

    /**
     * @return lista com os atributos que deverao ser removidos
     */
    public ArrayList<Integer> getAtributos() {
        ArrayList<Integer> atributos = new ArrayList<>();
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] == 0) {
                atributos.add(i + 1); //[1;qtdAtributos]
            }
        }
        return atributos;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }
    
    public int[] getGenes() {
        return genes;
    }

    public void setAptidao(double aptidao) {
        this.aptidao = aptidao;
    }

    public double getAptidao() {
        return aptidao;
    }

    /**
     * caso a aptidao de dois individuos seja igual, ele prioriza o que selecionou mais atributos
     * @param i
     * @return 
     */
    @Override
    public int compareTo(Individuo i) {
        if (aptidao != i.getAptidao()) {
            return aptidao.compareTo(i.getAptidao());
        } else {
            Integer thistam = this.getAtributos().size();
            Integer anothertam = i.getAtributos().size();
            return thistam.compareTo(anothertam);
        }
    }
}
