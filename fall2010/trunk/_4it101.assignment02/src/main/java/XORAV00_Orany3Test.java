import java.util.Random;

/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */

/*******************************************************************************
 * Testovací třída {@code Test} slouží ke komplexnímu otestování třídy
 * {@link Test}.
 * 
 * @author jméno autora
 * @version 0.00.000
 */
public class XORAV00_Orany3Test extends junit.framework.TestCase {
    private Obdélník obdélník1;
    private Elipsa elipsa1;
    private Trojúhelník trojúhel1;
    private Elipsa eli;
    private Trojúhelník troj;

    // == KONSTANTNÍ ATRIBUTY TŘÍDY
    // =================================================
    // == PROMĚNNÉ ATRIBUTY TŘÍDY
    // ===================================================
    // == STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR
    // ========================
    // == KONSTANTNÍ ATRIBUTY INSTANCÍ
    // ==============================================
    // == PROMĚNNÉ ATRIBUTY INSTANCÍ
    // ================================================
    // == PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY
    // ========================================
    // == OSTATNÍ NESOUKROMÉ METODY TŘÍDY
    // ===========================================

    // ##############################################################################
    // == KONSTRUKTORY A TOVÁRNÍ METODY
    // =============================================

    /***************************************************************************
     * Vytvoří test se zadaným názvem.
     * 
     * @param název
     *            Název konstruovaného testu
     */
    public XORAV00_Orany3Test(String název) {
        super(název);
    }

    // == PŘÍPRAVA A ÚKLID PŘÍPRAVKU
    // ================================================

    /***************************************************************************
     * Inicializace předcházející spuštění každého testu a připravující tzv.
     * přípravek (fixture), což je sada objektů, s nimiž budou testy pracovat.
     */
    @Override
    protected void setUp() {
        obdélník1 = new Obdélník();
        elipsa1 = new Elipsa();
        trojúhel1 = new Trojúhelník();
        eli = new Elipsa();
        troj = new Trojúhelník();
        IO.zpráva("Kuk");
    }

    /***************************************************************************
     * Úklid po testu - tato metoda se spustí po vykonání každého testu.
     */
    @Override
    protected void tearDown() {
    }

    // == PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ
    // =====================================
    // == OSTATNÍ NESOUKROMÉ METODY INSTANCÍ
    // ========================================
    // == SOUKROMÉ A POMOCNÉ METODY TŘÍDY
    // ===========================================
    // == SOUKROMÉ A POMOCNÉ METODY INSTANCÍ
    // ========================================
    // == INTERNÍ DATOVÉ TYPY
    // =======================================================
    // == VLASTNÍ TESTY
    // =============================================================
    //
    // /***************************************************************************
    // *
    // */
    // public void testXXX()
    // {
    // }

    public void testPosuny() {
        eli.setPozice(100, 100);
    }

    public void testPosuny2() {
        eli.setPozice(100, 100);
    }

    public void testPosunyPlynule() {
        Přesouvač p = new Přesouvač(1);
        p.přesunNa(100, 100, eli);
    }

    public void testPosuny2Plynule() {
        Přesouvač p = new Přesouvač(1);
        p.přesunNa(100, 100, eli);
    }
    
    public void testMnohotvarPůl()
    {
        Mnohotvar mnohotva1 = new Mnohotvar("rur" + new Random().nextInt(), eli, obdélník1, troj, elipsa1, trojúhel1);
        mnohotva1.nakresli();
        Kompresor kompreso1 = new Kompresor();
        kompreso1.nafoukniKrát(0.5, mnohotva1);
    }
}
