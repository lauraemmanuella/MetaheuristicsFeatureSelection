package featureselectionhybrid;

import java.io.File;
import java.util.ArrayList;

import ag_hybrid.Principal;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author Laura Emmanuella <lauraemmanuella at eaj.ufrn.br>
 */
public class FeatureSelectionHybrid {

    private Instances dadosReduzidos;
    private static Instances genesfiltrados, newPais = null;
    private static String nomeBase;

    private final String nomeClassificador;

    static int quantidadeAtributos;

    public FeatureSelectionHybrid(String nomeBase, String nomeClassificador) {
        FeatureSelectionHybrid.nomeBase = nomeBase;
        this.nomeClassificador = nomeClassificador;
    }

    public void removerAtributos(ArrayList<Integer> selecionados, boolean fim, String nomeMetah) {
        String s = selecionados.toString();

        try {
            //Instances data = carregaDatabase();
            //System.err.println("ENTROU NO REMOVER ATRIBUTOS"+data);
            // Load Arff
            String[] options = new String[2];
            options[0] = "-R"; // "range"
            options[1] = s.substring(1, s.length() - 1);

            Remove remove = new Remove(); // new instance of filter
            remove.setOptions(options); // set options
            remove.setInputFormat(genesfiltrados); // inform filter about dataset **AFTER** setting options

            dadosReduzidos = Filter.useFilter(genesfiltrados, remove); // apply filter
            //System.out.println(dadosReduzidos);

            if (fim) {
                ArffSaver saver = new ArffSaver();
                saver.setInstances(dadosReduzidos);
                saver.setFile(new File(System.getProperty("user.home") + "/basesreduzidasCORRETAMENTE/" + nomeMetah + "/"
                        + nomeBase + "/arff/" + nomeClassificador + "/" + Principal.FILTRO + Principal.PORCENTAGEM + "/selecionados" + nomeClassificador.toUpperCase()
                        + System.currentTimeMillis() + ".arff"));
                saver.writeBatch();

                //Salva em csv
                CSVSaver csvsaver = new CSVSaver();
                csvsaver.setInstances(dadosReduzidos);
                csvsaver.setFile(new File(System.getProperty("user.home") + "/basesreduzidasCORRETAMENTE/" + nomeMetah + "/"
                        + nomeBase + "/csv/" + nomeClassificador + "/" + Principal.FILTRO + Principal.PORCENTAGEM + "/selecionados" + nomeClassificador.toUpperCase()
                        + System.currentTimeMillis() + ".csv"));
                csvsaver.writeBatch();
            }

        } catch (Exception e) {
            System.out.println("Erro em remover atributo: " + e);
        }
    }

    public static Instances filtroCorrelation(Instances i) throws Exception {
        System.out.println("CORTOU");
        quantidadeAtributos = ((i.numAttributes() * Principal.PORCENTAGEM) / 100) + 1;
        //System.out.println("quantidade de atributos" + quantidadeAtributos);

        String a = String.valueOf(quantidadeAtributos);
        String[] options = new String[2];
        options[0] = "-N"; // "range"
        options[1] = a;
        //System.out.println("a"+a);

        //System.out.println("QUANTIDADE DE ATRIBUTOS A SEREM RANKEADOS:::   " + quantidadeAtributos);
        AttributeSelection as = new AttributeSelection();

        as.setEvaluator(getFiltro());
        Ranker r = new Ranker();
        r.setOptions(options);

        as.setSearch(r);

        as.SelectAttributes(i);
        genesfiltrados = as.reduceDimensionality(i);
        //System.out.println(as.toResultsString());
        //System.out.println("DADOS REDUZIDOS PELO FILTRO" + genesfiltrados.numAttributes());

        return genesfiltrados;
    }

    public static ASEvaluation getFiltro() {
        CorrelationAttributeEval correlationAttributeEval = new CorrelationAttributeEval();
        ReliefFAttributeEval reliefFAttributeEval = new ReliefFAttributeEval();
        InfoGainAttributeEval infogain = new InfoGainAttributeEval();
        switch (Principal.FILTRO) {
            case "relief":
                return reliefFAttributeEval;
            case "correlation":
                return correlationAttributeEval;
            case "infogain":
                return infogain;
            default:
                System.out.println("FILTRO NAO ENCONTRADO. VERIFIQUE SE O NOME DO FILTRO ESTA CORRETO");
                return correlationAttributeEval;
        }
    }

    public Instances getDadosReduzidos() {
        return dadosReduzidos;
    }

    public static Instances getNewPais() {
        return newPais;
    }

    public static int getQuantidadeAtributos() {
        return quantidadeAtributos;
    }

    public static Instances getGenesfiltrados() {
        return genesfiltrados;
    }

}
