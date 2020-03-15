package ag_hybrid;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public final class Genetico {

    private Populacao populacao;

    private Individuo melhor;

    private final Random r;

    private int contEstagnar;

    private double melhorAptidaoAnterior;

    public Genetico() {
        r = new Random();
        populacao = new Populacao();
        contEstagnar = 0;
        melhorAptidaoAnterior = -1;
    }

    public void executarAG() throws Exception {
        System.out.println("------EXECUTANDO AG------");
        int geracao = 0;

        populacao.iniciarPopulacao(Principal.TAMPOPULACAO, Principal.GENESINICIAIS);
        System.out.println("INICIOU");
        populacao.avaliarPopulacao();
        populacao.ordenarPopulacao();
        melhor = populacao.getIndividuo(0);

        resumoGeracao(geracao);

        do {
            geracao++;

            populacao = gerarPopulacao();
            populacao.avaliarPopulacao();
            populacao.ordenarPopulacao();

            if (populacao.getIndividuo(0).getAptidao() > melhor.getAptidao()) {
                melhor = populacao.getIndividuo(0);
            }

            resumoGeracao(geracao);

            if (Principal.ESTAGNA) {
                contaEstagnacao();
                if (contEstagnar >= Principal.VALORESTAGNA) {
                    break;
                }
            }

        } while (geracao < Principal.NUMMAXGERACOES);

    }

    private void resumoGeracao(int geracao) {
        System.out.println("Geracao " + geracao + "| Melhor " + melhor.getAptidao() + " |Cromossomo " + melhor.getAtributos());
    }

    public Populacao gerarPopulacao() {
        Populacao novaPopulacao = new Populacao();

        if (Principal.ELITISMO) {
            novaPopulacao.setIndividuo(melhor);
        }

        // insere novos individuos na nova populacao, ate atingir o tamanho maximo
        while (novaPopulacao.getNumIndividuos() <= Principal.TAMPOPULACAO) {
            novaPopulacao.setIndividuos(cruzamento(selecaoTorneioBinario()));
        }

        return novaPopulacao;
    }

    public ArrayList<Individuo> selecaoTorneioBinario() {
        ArrayList<Individuo> pais = new ArrayList<>();
        int a, b;
        //repete esse laco 2 vezes para pegar 2 pais
        for (int i = 0; i < 2; i++) {
            a = r.nextInt(Principal.TAMPOPULACAO);
            b = r.nextInt(Principal.TAMPOPULACAO);
            //considerando que a populacao esta ordenada, o individuo na posicao menor eh melhor
            if (a < b) {
                pais.add(populacao.getIndividuo(a));
            } else {
                pais.add(populacao.getIndividuo(b));
            }
        }

        return pais;
    }

    public ArrayList<Individuo> cruzamento(ArrayList<Individuo> pais) {
        int[] pai0 = pais.get(0).getGenes();
        int[] pai1 = pais.get(1).getGenes();

        int[] filho0, filho1;

        //Testa se deve fazer o cruzamento
        //Caso nao faca, os filhos serao iguais aos pais
        if (r.nextDouble() > Principal.TAXADECROSSOVER) {
            filho0 = pai0;
            filho1 = pai1;
        } else {
            int tam = pai0.length;

            filho0 = new int[tam];
            filho1 = new int[tam];

            //metade dos elementos do pai 0 ficam no filho 0 
            System.arraycopy(pai0, 0, filho0, 0, tam / 2);
            //outra metade fica no filho 1
            System.arraycopy(pai0, tam / 2, filho1, tam / 2, tam - tam / 2);

            //metade dos elementos do pai 1 ficam no filho 0
            System.arraycopy(pai1, 0, filho1, 0, tam / 2);
            //outra metade fica no filho1
            System.arraycopy(pai1, tam / 2, filho0, tam / 2, tam - tam / 2);
        }

        ArrayList<Individuo> filhos = new ArrayList<>();
        filhos.add(new Individuo(filho0));
        filhos.add(new Individuo(filho1));
        return filhos;
    }

    private void contaEstagnacao() {
        if (melhorAptidaoAnterior == -1 || populacao.getIndividuo(0).getAptidao() != melhorAptidaoAnterior) {
            melhorAptidaoAnterior = populacao.getIndividuo(0).getAptidao();
            contEstagnar = 1;
        } else {
            contEstagnar++;
        }
    }

    public Individuo getMelhor() {
        return melhor;
    }
}
