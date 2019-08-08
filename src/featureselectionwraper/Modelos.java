package featureselectionwraper;

import java.io.File;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Puk;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */

public class Modelos {

    public static Classifier getClassificador(String classificador) {
        SMO svm = new SMO();
        svm.setC(10);
        svm.setKernel(new Puk());

        NaiveBayes nb = new NaiveBayes();
        nb.setUseKernelEstimator(true);

        IBk knn = new IBk(1);

        switch (classificador) {
            case "knn":
                return knn;
            case "naive":
                return nb;
            case "svm":
                return svm;
            default:
                System.out.println("CLASSIFICADOR NAO ENCONTRADO. "
                        + "VERIFIQUE SE O NOME ESTA CORRETO. KNN SERA CARREGADO");
                return knn;
        }
    }

    public static double testaClassificador(Classifier model, String database) {
        try {
            ArffLoader loader = new ArffLoader();
            loader.setSource(new File(database));
            Instances data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1); // Indice do atributo de classe
            System.out.println(">> Contruindo o classificador");

            int seed = 1;
            Random rnd = new Random(seed);
            data.randomize(rnd);
            double percent = 66;
            System.out.println(">>Performing " + percent + "% split evaluation.");

            int trainSize = (int) Math.round(data.numInstances() * percent / 100);
            int testSize = data.numInstances() - trainSize;
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);

            model.buildClassifier(train);
            Evaluation evaluation = new Evaluation(train);
            evaluation.evaluateModel(model, test);
            return evaluation.pctCorrect();
        } catch (Exception e) {
            System.out.println("Erro em construir classificador: " + e);
            return -1;
        }
    }
}
