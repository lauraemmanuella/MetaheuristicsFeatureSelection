package ag;

import featureselectionwraper.Classificador;
import featureselectionwraper.Modelos;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Populacao {

    private final ArrayList<Individuo> individuos = new ArrayList<>();

    /**
     * Iniciar a populacao na primeira geracao
     * 
     * @param tamPop
     * @param qtdAtributos 
     */
    public void iniciarPopulacao(int tamPop, int qtdAtributos) {
        for (int i = 0; i < tamPop; i++) {
            individuos.add(new Individuo(qtdAtributos));
        }
    }

    public void avaliarPopulacao() {
        Thread[] pool = new Thread[individuos.size()];
        Classificador[] modelo = new Classificador[individuos.size()];
        int cont = 0;
        for (Individuo i : individuos) {
            modelo[cont] = new Classificador(Modelos.getClassificador(Principal.MODELO), i.getAtributos(),
                    Principal.BASE.NOMEBASE, Principal.MODELO, "AG");
            pool[cont] = new Thread(modelo[cont]);
            pool[cont].start();
            cont++;
        }
        for (Thread t : pool) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Erro em thread join");
            }
        }
        cont = 0;
        for (Individuo i : individuos) {
            i.setAptidao(modelo[cont].getAcuracia());
            cont++;
        }
    }

    // ordenar populacao em ordem decrescente
    public void ordenarPopulacao() {
        Collections.sort(individuos, Collections.reverseOrder());
    }

    public Individuo getIndividuo(int pos) {
        return individuos.get(pos);
    }

    public ArrayList<Individuo> getIndividuos() {
        return individuos;
    }

    // coloca um individuo na proxima posicao disponivel da populacao
    public void setIndividuo(Individuo individuo) {
        individuos.add(individuo);
    }

    public void setIndividuos(ArrayList<Individuo> filhos) {
        individuos.addAll(filhos);
    }

    // numero de individuos existentes na populacao
    public int getNumIndividuos() {
        return individuos.size();
    }

}
