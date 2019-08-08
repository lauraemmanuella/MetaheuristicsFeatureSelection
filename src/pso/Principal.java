package pso;

import java.util.ArrayList;
import featureselectionwraper.BaseDeDados;
import featureselectionwraper.FeatureSelection;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Principal {
    static final double ALFA = 0.5; //inercia
    static final double BETA = 2.05; //memoria
    static final double GAMA = 2.05; //cooperacao
    static final double VMAX = 6; //velocidade maxima
    static final boolean VELCONTROL = true; //se controla ou nao a velocidade
    
    static final int QTDPARTICULAS = 100;
    static final int QTDITERACOES = 500;

    static final BaseDeDados BASE = BaseDeDados.KEYSTROKE;
    static final String MODELO = "naive";

    public static void main(String[] args) {
        Nuvem nuvemParticulas = new Nuvem(QTDPARTICULAS);
        nuvemParticulas.executarPSO();
        ArrayList<Integer> selecionados = nuvemParticulas.getMelhorSolucaoNuvem();
        FeatureSelection fs = new FeatureSelection(BASE.NOMEBASE, MODELO);
        fs.removerAtributos(selecionados, true, "PSO");
        System.out.println("------PROCESSO CONCLUIDO------");
    }

}
