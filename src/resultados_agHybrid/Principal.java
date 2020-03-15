package resultados_agHybrid;

/**
 *
 * @author tuanymariah
 */
public class Principal {

    static final String MODELO = "svm";
    static final String METAH = "AG";
    static final String BASE = "greyc";
    static final String FILTRO = "correlation";
    static final int PORCENTAGEM = 90;

    public static void main(String[] args) {
        PercorreBase pb = new PercorreBase(BASE, MODELO, METAH, FILTRO, PORCENTAGEM);
        pb.percorredatabase();
    }

}
