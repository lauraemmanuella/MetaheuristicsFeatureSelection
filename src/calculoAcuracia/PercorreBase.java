package calculoAcuracia;

import featureselectionwraper.Modelos;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author tuany
 */
public class PercorreBase  {
    private final String nomedabase;
    private final String modelo;
    private final String nomemeta;

    public PercorreBase(String nomedabase, String modelo, String nomemeta) {
        this.nomedabase = nomedabase;
        this.modelo = modelo;
        this.nomemeta = nomemeta;
    }
   
    public void percorredatabase() {
        try {
            ArffLoader loader = new ArffLoader();

            File arquivos[];

            File diretorio = new File("./basesreduzidas/" + nomemeta + "/" + nomedabase +"/arff/"+ modelo+ "/");

            arquivos = diretorio.listFiles();

            for (int i = 0; i < arquivos.length; i++) {

                String arq = arquivos[i].getPath();
                //System.out.println(arq); //mostra o diretório do arquivo
                loader.setSource(new File(arq));
                Instances data = loader.getDataSet();
                //System.out.println(data.numAttributes());//mostra a quantidade de atributos
                data.setClassIndex(data.numAttributes() - 1);
                Classifier model = Modelos.getClassificador(modelo);

                int seed = 1;
                Random rnd = new Random(seed);
                data.randomize(rnd);
                double percent = 66;
                //System.out.println("Performing " + percent + "% split evaluation.");

                int trainSize = (int) Math.round(data.numInstances() * percent / 100);
                int testSize = data.numInstances() - trainSize;
                Instances train = new Instances(data, 0, trainSize);
                Instances test = new Instances(data, trainSize, testSize);

                model.buildClassifier(train);
                Evaluation evaluation = new Evaluation(train);
                evaluation.evaluateModel(model, test);

                DecimalFormat df = new DecimalFormat("0,00");
                double acuracia = evaluation.pctCorrect()*100;
                int qtdAtributos = data.numAttributes() ;
                String valoracuracia = df.format(acuracia);
                


                System.out.println(qtdAtributos);//imprime a quantidade de atributos
                //System.out.println("::: ACURACIA :::\n"+valoracuracia.replace(".", ",")); //imprime a acuracia das bases
                //System.out.println(arquivos[i].getName() + "\t>> " +valoracuracia  + " | Quantidade de Atributos Removidos >> "+ qtdAtributos);//imprime tudo
            }   
        } catch (Exception e) {
            System.out.println(e.getMessage());    
        }

    }

}