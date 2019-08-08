package pso;

import java.util.ArrayList;
import java.util.Collections;
import featureselectionwraper.Classificador;
import featureselectionwraper.Modelos;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class Nuvem {

    private final ArrayList<Particula> particulas;

    static int[] melhorPosicaoNuvem;

    private double valorMelhorPosicaoNuvem;

    public Nuvem(int qtdParticulas) {
        particulas = new ArrayList<>();
        for (int i = 0; i < Principal.QTDPARTICULAS; i++) {
            particulas.add(new Particula());
        }

        melhorPosicaoNuvem = new int[Principal.BASE.QTDATRIBUTOS];
    }

    private void avaliarNuvem() {
        Thread[] pool = new Thread[particulas.size()];
        Classificador[] modelo = new Classificador[particulas.size()];
        int cont = 0;
        for (Particula p : particulas) {
            modelo[cont] = new Classificador(Modelos.getClassificador(Principal.MODELO), p.getAtributos(),
                    Principal.BASE.NOMEBASE, Principal.MODELO, "PSO");
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
        for (Particula p : particulas) {
            p.setValorPosicaoAtual(modelo[cont].getAcuracia());
            cont++;
        }

    }

    public void executarPSO() {
        System.out.println("------EXECUTANDO PSO------");
        for (int i = 0; i < Principal.QTDITERACOES; i++) {
            avaliarNuvem();

            //verificar qual a melhor solucao da nuvem
            for (Particula p : particulas) {
                p.avaliarSolucao();//Cada particula verifica se seu melhor mudou
            }

            // ordenar nuvem (ordem decrescente)
            Collections.sort(particulas, Collections.reverseOrder());

            //Verifica se a particula com melhor solucao individual eh melhor que a da nuvem
            if (particulas.get(0).getValorMelhorPosicao() > valorMelhorPosicaoNuvem) {
                valorMelhorPosicaoNuvem = particulas.get(0).getValorMelhorPosicao();
                System.arraycopy(particulas.get(0).getMelhorPosicao(), 0, melhorPosicaoNuvem, 0, melhorPosicaoNuvem.length);
            }

            for (Particula p : particulas) {
                p.atualizarVelocidade();
                p.atualizarPosicao();
            }
            resumoIteracao(i);
        }
    }

    private void resumoIteracao(int iteracao) {
        System.out.println("Iteracao " + iteracao + "| Melhor " + valorMelhorPosicaoNuvem);
    }

    public ArrayList<Integer> getMelhorSolucaoNuvem() {
        ArrayList<Integer> selecionados = new ArrayList<>();
        for (int i = 0; i < melhorPosicaoNuvem.length; i++) {
            if (melhorPosicaoNuvem[i] == 0) {
                selecionados.add(i + 1); //[1;qtdAtributos]
            }
        }
        return selecionados;
    }

}
