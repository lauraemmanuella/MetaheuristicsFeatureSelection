package pso;

import java.util.ArrayList;
import featureselectionwraper.BaseDeDados;
import featureselectionwraper.FeatureSelection;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Principal {
    static final double ALFA = 1; //inercia
    static final double BETA = 1.2; //memoria
    static final double GAMA = 1.4; //cooperacao
    static final double VMAX = 6; //velocidade maxima
    static final boolean VELCONTROL = true; //se controla ou nao a velocidade
    
    static final int QTDPARTICULAS = 30;
    static final int QTDITERACOES = 100;

    static final BaseDeDados BASE = BaseDeDados.KEYSTROKE;
    static final String MODELO = "knn";

    public static void main(String[] args) {
        Nuvem nuvemParticulas = new Nuvem(QTDPARTICULAS);
        nuvemParticulas.executarPSO();
        ArrayList<Integer> selecionados = nuvemParticulas.getMelhorSolucaoNuvem();
        FeatureSelection fs = new FeatureSelection(BASE.NOMEBASE, MODELO);
        fs.removerAtributos(selecionados, true, "PSO");
        System.out.println("------PROCESSO CONCLUIDO------");
        System.out.println("::MODELO: " + MODELO+"::");
    }

}
