package featureselectionwraper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class FeatureSelection {

    private Instances dadosReduzidos;
    private final String nomeBase;
    private final String nomeClassificador;

    public FeatureSelection(String nomeBase, String nomeClassificador) {
        this.nomeBase = nomeBase;
        this.nomeClassificador = nomeClassificador;
    }

    private Instances carregaDatabase() throws IOException {
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File("./bases/" + nomeBase + ".arff"));
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        return data;
    }

    public void removerAtributos(ArrayList<Integer> selecionados, boolean fim, String nomeMetah) {
        String s = selecionados.toString();

        try {
            Instances data = carregaDatabase();
            // Load Arff
            String[] options = new String[2];
            options[0] = "-R"; // "range"
            options[1] = s.substring(1, s.length() - 1);

            Remove remove = new Remove(); // new instance of filter
            remove.setOptions(options); // set options
            remove.setInputFormat(data); // inform filter about dataset **AFTER** setting options

            dadosReduzidos = Filter.useFilter(data, remove); // apply filter

            if (fim) {
                ArffSaver saver = new ArffSaver();
                saver.setInstances(dadosReduzidos);
                saver.setFile(new File("./basesreduzidas/" + nomeMetah + "/" + nomeBase
                        + "/selecionados" + nomeClassificador
                        + System.currentTimeMillis() + ".arff"));
                saver.writeBatch();
            }

        } catch (Exception e) {
            System.out.println("Erro em remover atributo: " + e);
        }
    }

    public Instances getDadosReduzidos() {
        return dadosReduzidos;
    }

}
