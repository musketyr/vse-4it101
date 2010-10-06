

/*******************************************************************************
 * Třída {@code Přesouvač} slouží k plynulému přesouvání instancí tříd
 * implementujicich rozhraní {@link IPosuvný}.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Přesouvač
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Oočet milisekund mezi dvěma překresleními objektu. */
    private static final int PERIODA = 50;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet vytvořených instancí */
    private static int počet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Název sestávající z názvu třídy a pořadí instance */
    private final String název;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Specifikuje rychlost posunu objektu daným posunovačem. */
    private int rychlost;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří přesouvače, který bude přesouvat objekty rychlosti 1.
     */
    public Přesouvač()
    {
        this( 1 );
    }


    /***************************************************************************
     * Vytvoří přesouvače, který bude přesouvat objekty zadanou rychlostí.
     *
     * @param rychlost Rychlost, kterou bude přesouvač pohybovat
     *                 se svěřenými objekty.
     */
    public Přesouvač( int rychlost )
    {
        if( rychlost <= 0 ) {
            throw new IllegalArgumentException(
                "Zadaná rychlost musí byt kladná!" );
        }
        this.rychlost = rychlost;
        this.název    = getClass().getName() + "(ID=" + ++počet +
                        ",rychlost=" + rychlost + ")";
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Metoda převádí instanci na řetězec - používá se pro účely ladění.
     * Vrácený řetězec obsahuje název třídy následovaný identifikačním číslem
     * dané instance, které určuje pořadí dané instance mezi instancemi této
     * třídy, a nastavenou rychlostí přesunu.
     *
     * @return  Řetězec charakterizující instanci
     */
    @Override
    public String toString()
    {
        return název;
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt o zadaný počet obrazových bodů.
     *
     * @param doprava   Počet bodů, o než se objekt přesune doprava
     * @param dolů      Počet bodů, o než se objekt přesune dolů
     * @param objekt    Přesouvaný objekt
     */
    public void přesunO(int doprava, int dolů, IPosuvný objekt)
    {
        double vzdálenost = Math.sqrt(doprava*doprava + dolů*dolů);
        int    kroků      = (int)(vzdálenost / rychlost);
        double dx = (doprava+.4) / kroků;
        double dy = (dolů   +.4) / kroků;
        int px = objekt.getX();
        int py = objekt.getY();
        double x  = px + .4;
        double y  = py + .4;

        for (int i=kroků;   i > 0;   i--)  {
            x = x + dx;
            y = y + dy;
            objekt.setPozice( (int)x, (int)y );
            IO.čekej(PERIODA);
        }
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt do požadované pozice.
     *
     * @param x       x-ova souřadnice požadované cílové pozice
     * @param y       y-ova souřadnice požadované cílové pozice
     * @param objekt  Přesouvaný objekt
     */
    public void přesunNa(int x, int y, IPosuvný objekt)
    {
        int px = objekt.getX();
        int py = objekt.getY();
        přesunO(x-px, y-py, objekt);
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
