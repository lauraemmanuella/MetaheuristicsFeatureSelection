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
    static final String MODELO = "svm";

    public static void main(String[] args) {
        int cont30x = 0;
        util.Time tempoMetah = new util.Time();
        do{
            Nuvem nuvemParticulas = new Nuvem(QTDPARTICULAS);
            nuvemParticulas.executarPSO();
            ArrayList<Integer> selecionados = nuvemParticulas.getMelhorSolucaoNuvem();
            FeatureSelection fs = new FeatureSelection(BASE.NOMEBASE, MODELO);
            fs.removerAtributos(selecionados, true, "PSO");
            
            cont30x++;
            
            System.out.println("------PROCESSO CONCLUIDO  "+ cont30x + "  ------");
            
            
        }while(cont30x <= 30);
        
        System.out.println("Modelo: " + MODELO+"");
        System.out.println("Duracao: "+ tempoMetah);
        Runtime rt = Runtime.getRuntime();
        System.out.println("Uso de memória  = " +(rt.totalMemory()-rt.freeMemory())/(1000*1000)+"M");
        System.out.println("------------------------------");
        
       
    }

}
