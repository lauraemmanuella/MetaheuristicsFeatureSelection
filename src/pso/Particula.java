package pso;

import java.util.ArrayList;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class Particula implements Comparable<Particula> {

    private final int[] posicaoAtual;
    private double valorPosicaoAtual;

    private final int[] melhorPosicao;
    private Double valorMelhorPosicao;

    private final double[] velocidade;

    public Particula() {
        posicaoAtual = new int[Principal.BASE.QTDATRIBUTOS];
        melhorPosicao = new int[Principal.BASE.QTDATRIBUTOS];
        velocidade = new double[Principal.BASE.QTDATRIBUTOS];

        valorMelhorPosicao = new Double(0);

        inicializarPosicao();
        inicializarVelocidade();

    }

    //Inicia particula em posicao aleatoria
    private void inicializarPosicao() {
        for (int i = 0; i < posicaoAtual.length; i++) {
            if (Math.random() < 0.5) {
                posicaoAtual[i] = 0;
            } else {
                posicaoAtual[i] = 1;
            }
        }
    }

    private void inicializarVelocidade() {
        for (int i = 0; i < velocidade.length; i++) {
            velocidade[i] = 1;
        }
    }

    //Retorna uma lista com os atributos que deverao ser removidos
    public ArrayList<Integer> getAtributos() {
        ArrayList<Integer> atributos = new ArrayList<>();
        for (int i = 0; i < posicaoAtual.length; i++) {
            if (posicaoAtual[i] == 0) {
                atributos.add(i + 1); //[1;qtdAtributos]
            }
        }
        return atributos;
    }

    //Verifica se o valor da posicao atual eh melhor que o valor da melhor solucao ja encontrada
    //Se for, substitui
    public void avaliarSolucao() {
        if (valorPosicaoAtual > valorMelhorPosicao) {
            valorMelhorPosicao = valorPosicaoAtual;
            System.arraycopy(posicaoAtual, 0, melhorPosicao, 0, posicaoAtual.length);
        }
    }

    //Valor da posicao atual recebe a acuracia do classificador
    public void setValorPosicaoAtual(double v) {
        valorPosicaoAtual = v;
    }

    public void atualizarVelocidade() {
        for (int i = 0; i < velocidade.length; i++) {
            velocidade[i] = Principal.ALFA * velocidade[i]
                    + Principal.BETA * Math.random() * (melhorPosicao[i] - posicaoAtual[i])
                    + Principal.GAMA * Math.random() * (Nuvem.melhorPosicaoNuvem[i] - posicaoAtual[i]);

            if (Principal.VELCONTROL) {
                //limita a velocidade entre [-VMAX;VMAX]
                if (velocidade[i] > Principal.VMAX) {
                    velocidade[i] = Principal.VMAX;
                } else if (velocidade[i] < -Principal.VMAX) {
                    velocidade[i] = -Principal.VMAX;
                }
            }
        }
    }

    public void atualizarPosicao() {
        double s;
        for (int i = 0; i < posicaoAtual.length; i++) {
            //funcao sigmoid sobre a velocidade
            s = (1 / (1 + Math.exp(-velocidade[i])));

            if (Math.random() < s) {
                posicaoAtual[i] = 1;
            } else {
                posicaoAtual[i] = 0;
            }
        }
    }

    public double getValorPosicaoAtual() {
        return valorPosicaoAtual;
    }

    public double getValorMelhorPosicao() {
        return valorMelhorPosicao;
    }

    public int[] getMelhorPosicao() {
        return melhorPosicao;
    }

    //Compara as particulas pela melhor solucao que cada uma tem
    @Override
    public int compareTo(Particula o) {
        if (valorMelhorPosicao != o.getValorMelhorPosicao()) {
            return valorMelhorPosicao.compareTo(o.getValorMelhorPosicao());
        } else {
            Integer thistam = this.getAtributos().size();
            Integer anothertam = o.getAtributos().size();
            return thistam.compareTo(anothertam);
        }
    }

}
