package ag;

import featureselectionwraper.BaseDeDados;
import featureselectionwraper.FeatureSelection;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Principal {
    static final double TAXADEMUTACAO = 0.5;
    static final double TAXADECROSSOVER = 0.9;
    static final boolean ELITISMO = true;
    static final boolean ESTAGNA = true; //se controla ou nao a estagnacao
    static final double VALORESTAGNA = 100; //valor da estagnacao maxima
    
    static final int TAMPOPULACAO = 100;
    static final int NUMMAXGERACOES = 500;

    static final BaseDeDados BASE = BaseDeDados.KEYSTROKE;
    static final String MODELO = "rf";

     public static void main(String[] args) {
         int cont30x = 0;
         util.Time tempoMetah = new util.Time();
        do{          
            Genetico ag = new Genetico();
            ag.executarAG();
            FeatureSelection fs = new FeatureSelection(Principal.BASE.NOMEBASE, Principal.MODELO);
            fs.removerAtributos(ag.getMelhor().getAtributos(), true, "AG");
            
            cont30x++;
            System.out.println("------PROCESSO CONCLUIDO  "+ cont30x + "  ------");
        } while (cont30x <= 30);
        System.out.println("Modelo: " + MODELO+"");
        System.out.println("Duracao: "+ tempoMetah);
        Runtime rt = Runtime.getRuntime();
        System.out.println("Uso de memória  = " +(rt.totalMemory()-rt.freeMemory())/(1000*1000)+"M");
        System.out.println("------------------------------");
        
    }
     
}
