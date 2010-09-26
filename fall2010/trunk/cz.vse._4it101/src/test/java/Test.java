/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */





/*******************************************************************************
 * Testovací třída {@code Test} slouží ke komplexnímu otestování
 * třídy {@link Test}.
 *
 * @author    jméno autora
 * @version   0.00.000
 */
public class Test extends junit.framework.TestCase
{
	private Obdélník obdélník1;
	private Elipsa elipsa1;
	private Trojúhelník trojúhel1;

//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří test se zadaným názvem.
     *
     * @param název  Název konstruovaného testu
     */
    public Test(String název)
    {
        super( název );
    }



//== PŘÍPRAVA A ÚKLID PŘÍPRAVKU ================================================

    /***************************************************************************
     * Inicializace předcházející spuštění každého testu a připravující tzv.
     * přípravek (fixture), což je sada objektů, s nimiž budou testy pracovat.
     */
    @Override
    protected void setUp()
    {
		obdélník1 = new Obdélník();
		elipsa1 = new Elipsa();
		trojúhel1 = new Trojúhelník();
		IO.zpráva("Hotovo");
	}


    /***************************************************************************
     * Úklid po testu - tato metoda se spustí po vykonání každého testu.
     */
    @Override
    protected void tearDown()
    {
    }



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================
//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== VLASTNÍ TESTY =============================================================
//
//     /***************************************************************************
//      *
//      */
//     public void testXXX()
//     {
//     }

	public void testPosuny()
	{
		trojúhel1.posunDolů();
		elipsa1.posunVpravo();
		obdélník1.nakresli();
	}
}

