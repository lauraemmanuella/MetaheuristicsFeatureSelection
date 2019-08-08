package featureselectionwraper;

import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class Classificador implements Runnable {

    private double acuracia;
    private final Classifier model;
    private final FeatureSelection fs;

    public Classificador(Classifier model, ArrayList<Integer> selecionados, String nomeBase,
            String nomeClassificador, String nomeMetah) {
        this.model = model;
        fs = new FeatureSelection(nomeBase, nomeClassificador);
        fs.removerAtributos(selecionados, false, nomeMetah);
    }

    @Override
    public void run() {
        try {
            Instances data = fs.getDadosReduzidos();
            data.setClassIndex(data.numAttributes() - 1); // Indice do atributo de classe

            int seed = 1;
            Random rnd = new Random(seed);
            data.randomize(rnd);
            double percent = 66;

            int trainSize = (int) Math.round(data.numInstances() * percent / 100);
            int testSize = data.numInstances() - trainSize;
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);

            model.buildClassifier(train);
            Evaluation ev = new Evaluation(train);
            ev.evaluateModel(model, test);
            //ev.crossValidateModel(model, data, 10, new Random(1));
            acuracia = ev.pctCorrect();
        } catch (Exception e) {
            System.out.println("Erro em construir classificador: " + e);
            acuracia = -1;
        }
    }

    public double getAcuracia() {
        return acuracia;
    }

}
