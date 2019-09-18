package calculoAcuracia;

/**
 *
 * @author tuanymariah
 */
public class Principal {
    static final String MODELO = "knn";
    static final String METAH = "AG";
    static final String BASE = "keystroke";
    
    public static void main(String[] args) {
        PercorreBase pb = new PercorreBase(BASE,MODELO, METAH);
        pb.percorredatabase();
    }
    

    
}
