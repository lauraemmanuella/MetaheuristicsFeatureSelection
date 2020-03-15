package ag_hybrid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import featureselectionhybrid.BaseDeDados;
import featureselectionhybrid.FeatureSelectionHybrid;
import util.Time;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class Principal {

    static final double TAXADEMUTACAO = 0.5;
    static final double TAXADECROSSOVER = 0.9;
    static final boolean ELITISMO = true;
    static final boolean ESTAGNA = true; // se controla ou nao a estagnacao
    static final double VALORESTAGNA = 75; // valor da estagnacao maxima

    static final int TAMPOPULACAO = 30;
    static final int NUMMAXGERACOES = 100;

    static final int QUANTIDADEATRIBUTOS = 60;

    public static final int PORCENTAGEM = 90;

    static final int GENESINICIAIS = (QUANTIDADEATRIBUTOS * PORCENTAGEM) / 100;

    static final BaseDeDados BASE = BaseDeDados.GREYC;

    public static final String MODELO = "knn";
    public static final String FILTRO = "infogain";

    static final String METAH = "AG";

    public static void main(String[] args) throws Exception {
        util.Time tempoMetah = new util.Time();
        int cont30x = 0;
        carregaDatabase();

        do {
            Genetico ag = new Genetico();

            System.out.println("MODELO: " + Principal.MODELO.toUpperCase() + "\nFILTRO: "
                    + Principal.FILTRO.toUpperCase() + "\nBASE: " + BASE + "\nPORCENTAGEM: " + PORCENTAGEM);

            ag.executarAG();

            FeatureSelectionHybrid fs = new FeatureSelectionHybrid(Principal.BASE.NOMEBASE, Principal.MODELO);
            fs.removerAtributos(ag.getMelhor().getAtributos(), true, METAH);
            System.out.println("------ PROCESSO CONCLUIDO ------");

            System.out
                    .println("MODELO: " + Principal.MODELO.toUpperCase() + "\nFILTRO: " + Principal.FILTRO.toUpperCase()
                            + "\nBASE: " + BASE + "\nPORCENTAGEM: " + PORCENTAGEM + "\nTEMPO: " + tempoMetah);
            cont30x++;
        } while (cont30x <= 30);
        escreveArquivo(tempoMetah);
    }

    static void carregaDatabase() throws Exception {
        System.out.println("CARREGOU BASE");
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(System.getProperty("user.home") + "/bases/" + BASE.NOMEBASE + ".arff"));
        Instances data = loader.getDataSet();

        FeatureSelectionHybrid.filtroCorrelation(data);

    }

    static void escreveArquivo(Time t) {
        try {
            FileWriter arquivo = new FileWriter(
                    System.getProperty("user.home") + "/basesreduzidasCORRETAMENTE/tempoExecucoes.txt", true);
            BufferedWriter buffer = new BufferedWriter(arquivo);
            buffer.write("## MODELO: " + Principal.MODELO.toUpperCase() + "\nFILTRO: " + Principal.FILTRO.toUpperCase()
                    + "\nBASE: " + BASE + "\nPORCENTAGEM: " + PORCENTAGEM + "\nMetaheuristica: " + METAH + "\nTEMPO: "
                    + t);
            buffer.newLine();
            buffer.newLine();
            buffer.close();
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
