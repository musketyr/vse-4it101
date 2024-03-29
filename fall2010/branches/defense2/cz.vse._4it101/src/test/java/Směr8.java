
import java.util.HashMap;
import java.util.Map;


/*******************************************************************************
 * Třída {@code Směr8} slouží jako výčtový typ pro 8 hlavních a vedlejších
 * světových stran spolu se směrem ŽÁDNÝ.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public enum Směr8
{
//== HODNOTY VÝČTOVÉHO TYPU ====================================================

    /** Východ       = doprava.        */    VÝCHOD      ( "V" ,  1,  0 ),
    /** Severovýchod = doprava nahoru. */    SEVEROVÝCHOD( "SV",  1, -1 ),
    /** Sever        = nahoru.         */    SEVER       ( "S" ,  0, -1 ),
    /** Severozápad  = doleva nahoru.  */    SEVEROZÁPAD ( "SZ", -1, -1 ),
    /** Západ        = doleva.         */    ZÁPAD       ( "Z" , -1,  0 ),
    /** Jihozápad    = doleva nahoru.  */    JIHOZÁPAD   ( "JZ", -1,  1 ),
    /** Jih          = dolu.           */    JIH         ( "J" ,  0,  1 ),
    /** Jihovýchod   = doprava dolu.   */    JIHOVÝCHOD  ( "JV",  1,  1 ),
    /** Žádný        = nikam.          */    ŽÁDNÝ       ( "@" ,  0,  0 ),
    ;


//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Celkový počet definovaných směrů. */
    public  static final int SMĚRŮ = values().length;

    /** Maska pro dělení modulo
     *  Výpočet masky předpokládá, že počet směrů je mocninou dvou
     *  plus směr ŽÁDNÝ). */
    private static final int MASKA = SMĚRŮ-2;

    /** Odmocnina z jedné poloviny. */
    private static final double SQR = Math.sqrt( 0.5 );

    /** Všechny použitelné NÁZVY směrů. */
    private static final Map<String,Směr8> NÁZVY =
                                        new HashMap<String,Směr8>( SMĚRŮ*3 );



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Příznak povolení operací se směrem ŽÁDNÝ. */
    private static boolean žádnýZakázán = false;


//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================

    //Inicializace statických atributů je nerealizovatelná před
    //definicí jednotlivých hodnot ==> je ji proto potřeba realizovat dodatečně
    static {
        if (SMĚRŮ != 9) {
            throw new RuntimeException(
                "\nNabouraný zdrojový kód - špatný počet směrů");
        }
        for( Směr8 s : values() )  {
            NÁZVY.put( s.name(),  s );
            NÁZVY.put( s.zkratka, s );
        }
    }



//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Velikost změny příslušné složky souřadnic po přesunu
     *  na sousední políčko v daném směru. */
    private final int dx, dy;

    /** Jedno- či dvojpísmenná zkratka úplného názvu daného směru. */
    private final String zkratka;


//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Vrátí vektor se čtyřmi hlavními světovými stranami.
     *
     * @return  Požadovaný vektor.
     */
    public static Směr8[] values4()
    {
        return new Směr8[] { VÝCHOD, SEVER, ZÁPAD, JIH };
    }


    /***************************************************************************
     * Nastaví, zda budou povoleny operace se směrem žádný.
     * Nejsou-li operace povoleny, vyhazují u tohoto směru výjimku
     * {@link java.lang.iIllegalStateException}.
     * Jsou-li operace povoleny, pak objekt natočený do směru {@link #ŽÁDNÝ}
     * zůstává v romto "směru" po jakémkoliv otočení
     * a při jakékmkoliv přesunu zůstává na místě.
     *
     * @param zakázat {@code true} mají-li se operace zakázat,
     *                {@code false} mají-li se povolit
     * @return Původní nastavení tohoto příznaku
     */
    public static boolean zakázatŽádný(boolean zakázat) {
        boolean původní = žádnýZakázán;
        žádnýZakázán = zakázat;
        return původní;
    }


//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**************************************************************************
     * Vrátí směr se zadaným názvem nebo zkratkou.
     * Bohužel není možno použít definovat jako valueOf(),
     * protože tuto metodu definuje překladač v této třídě
     * takže ji není možno překrýt vlastní verzí.
     *
     * @param název Název požadovaného směru nebo jeho zkratka;
     *              při zadávání nezáleží na velikosti písmen
     * @return Požadovaný směr
     * @throws IllegalArgumentException  Neexistuje-li směr se zadaným
     *                                   názvem nebo zkratkou
     */
    public static Směr8 get( String název )
    {
       Směr8 ret = NÁZVY.get(název.toUpperCase() );
       if( ret == null ) {
            throw new IllegalArgumentException("\nNeznámý název směru");
        }
       return ret;
    }


    /**************************************************************************
     * Vytvoří nový směr a zapamatuje si zkratku jeho názvu
     * spolu se změnami souřadnic při pohybu v daném směru.
     *
     * @param zkratka   Jedno- či dvoj-písmenná zkratka označující daný směr.
     * @param dx        Změna vodorovné souřadnice
     *                  při přesunu na sousední políčko v daném směru.
     * @param dy        Změna svislé souřadnice
     *                  při přesunu na sousední políčko v daném směru.
     */
    private Směr8( String zkratka, int dx, int dy )
    {
        this.zkratka = zkratka;
        this.dx = dx;
        this.dy = dy;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí zkratku názvu daného směru.
     *
     * @return  Požadovaná zkratka
     */
    public String getZkratka()
    {
        return zkratka;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /**************************************************************************
     * Vrátí x-vou souřadnici sousedního políčka v daném směru.
     *
     * @param x x-ová souřadnice stávajícího políčka.
     * @return x-ová souřadnice políčka po přesunu o jedno pole v daném směru.
     */
    public int dalšíX( int x )
    {
        ověřPlatný();
        return x + dx;
    }


    /**************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici políčka
     * vzdáleného v daném směru o zadanou vzdálenost.
     *
     * @param x            x-ová souřadnice stávajícího políčka
     * @param vzdálenost   Vzdálenost políčka v daném směru
     * @return x-ová souřadnice vzdáleného políčka
     */
    public double dalšíX( int x, int vzdálenost )
    {
        ověřPlatný();
        if( (dx != 0)  &&  (dy != 0) ) {
            return x + SQR*dx*vzdálenost;
        } else {
            return x + dx*vzdálenost;
        }
    }


    /**************************************************************************
     * Vrátí y-vou souřadnici sousedního políčka v daném směru.
     *
     * @param y y-ová souřadnice stávajícího políčka
     *
     * @return y-ová souřadnice sousedního políčka v daném směru
     */
    public int dalšíY( int y )
    {
        ověřPlatný();
        return y + dy;
    }


    /**************************************************************************
     * Vrátí y-ovou (svislou) souřadnici políčka
     * vzdáleného v daném směru o zadanou vzdálenost.
     *
     * @param y            x-ová souřadnice stávajícího políčka
     * @param vzdálenost   Vzdálenost políčka v daném směru
     * @return y-ová souřadnice vzdáleného políčka
     */
    public double dalšíY( int y, int vzdálenost )
    {
        ověřPlatný();
        if( (dx != 0)  &&  (dy != 0) ) {
            return y + SQR*dy*vzdálenost;
        } else {
            return y + dy*vzdálenost;
        }
    }


    /**************************************************************************
     * Vrátí změnu vodorovné souřadnice při přesunu
     * na sousední pole v daném směru.
     *
     * @return Změna x-ové souřadnice při přesunu o jedno pole v daném směru
     */
    public int dx()
    {
        ověřPlatný();
        return dx;
    }


    /**************************************************************************
     * Vrátí změnu svislé souřadnice při přesunu
     * na sousední pole v daném směru.
     *
     * @return Změna y-ové souřadnice při přesunu o jedno pole v daném směru
     */
    public int dy()
    {
        ověřPlatný();
        return dy;
    }


    /**************************************************************************
     * Vráti směr otočený o 45° vlevo.
     *
     * @return Směr objektu po vyplnění příkazu nalevo vpříč.
     */
    public Směr8 nalevoVpříč()
    {
        ověřPlatný();
        return (this == ŽÁDNÝ)
               ?  ŽÁDNÝ
               : values()[MASKA & (1 + ordinal())];
    }


    /**************************************************************************
     * Vráti směr otočený o 45° vpravo.
     *
     * @return Směr objektu po vyplnění příkazu napravo vpříč.
     */
    public Směr8 napravoVpříč()
    {
        ověřPlatný();
        return (this == ŽÁDNÝ)
               ?  ŽÁDNÝ
               :  values()[MASKA & (-1 + ordinal())];
    }


    /**************************************************************************
     * Vráti směr otočený o 90° vlevo.
     *
     * @return Směr objektu po vyplněni příkazu vlevo v bok.
     */
    public Směr8 vlevoVbok()
    {
        ověřPlatný();
        return (this == ŽÁDNÝ)
               ?  ŽÁDNÝ
               :  values()[MASKA & (2+ordinal())];
    }


    /**************************************************************************
     * Vráti směr otočený o 90° vpravo.
     *
     * @return Směr objektu po vyplněni příkazu vpravo v bok
     */
    public Směr8 vpravoVbok()
    {
        ověřPlatný();
        return (this == ŽÁDNÝ)
               ?  ŽÁDNÝ
               :  values()[MASKA & (-2+ordinal())];
    }


    /**************************************************************************
     * Vráti směr otočený o 180°.
     *
     * @return Směr objektu po vyplněni příkazu čelem vzad.
     */
    public Směr8 čelemVzad()
    {
        ověřPlatný();
        return (this == ŽÁDNÝ)
               ?  ŽÁDNÝ
               :  values()[MASKA & (4+ordinal())];
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Ověří, že se nejedná o operaci zakázanou pro směr ŽÁDNÝ.
     *
     * @throws IllegalStateException - Operaci je zakázána pro směr ŽÁDNÝ
     */
    private void ověřPlatný()
    {
        if( žádnýZakázán  &&  (this == ŽÁDNÝ) ) {
            Throwable t = new Throwable();
            StackTraceElement[] aste = t.getStackTrace();
            StackTraceElement   ste  = aste[1];
            String metoda = ste.getMethodName();

            throw new IllegalStateException(
                "\nPro směr ŽÁDNÝ je tato operace zakázána: " + metoda );
        }
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
