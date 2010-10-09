

/*******************************************************************************
 * Instance třídy {@code Elipsa} představují elipsy určené
 * pro práci na virtuálním plátně při prvním seznámení s třídami a objekty.
 *
 * Výchozí podoba třídy určená pro první seznámení s třídami a objekty.
 *
 * @author   Rudolf PECINOVSKÝ
 * @version  3.00.002
 */
public class Elipsa
       implements ITvar, IPosuvný, IHýbací, INafukovací
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro elipsu Barva.MODRÁ. */
    public static final Barva IMPLICITNÍ_BARVA = Barva.MODRÁ;

    /** Maximální povolená velikost kroku. */
    public static final int MAX_KROK = 100;

    /** Plátno, na které se bude instance kreslit. */
    private static final Plátno PLÁTNO = Plátno.getPlátno();



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet pixelů, o něž se instance posune
     *  po bezparametrickém posunovém povelu */
    private static int krok = 50;

    /** Počet vytvořených instancí */
    private static int počet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Pořadí vytvoření dané instance v rámci třídy. */
    private final int pořadí = ++počet;

    /** Název sestávající z názvu třídy a pořadí instance */
    private final String název = "Elipsa_" + pořadí;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    private int    xPos;    //Bodová x-ová souřadnice počátku
    private int    yPos;    //Bodová y-ová souřadnice počátku
    private int    šířka;   //šířka v bodech
    private int    výška;   //Výška v bodech
    private Barva  barva;   //Barva instance



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================

    /***************************************************************************
     * Vrátí velikost implicitního kroku, o který se instance přesune
     * při volaní bezparametrickych metod přesunu.
     *
     * @return Velikost implicitního kroku v bodech
     */
     public static int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastaví velikost implicitního kroku, o který se instance přesune
     * při volaní bezparametrickych metod přesunu.
     *
     * @param velikost  Velikost implicitního kroku v bodech;<br/>
     *                  musí platit:  0 &lt;= velikost &lt;= {@link #MAX_KROK}
     */
    public static void setKrok( int velikost )
    {
        if( (velikost < 0)  || (velikost > MAX_KROK) ) {
            throw new IllegalArgumentException(
                "Krok musí byt z intervalu <0;" + MAX_KROK + ">." );
        }
        krok = velikost;
    }



//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitními rozměry, umístěním a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku (tj. implicitně 50x100 bodů).
     */
    public Elipsa()
    {
        this( 0, 0, 2*krok, krok );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou polohou a rozměry
     * a implicitní barvou.
     *
     * @param x       x-ová souřadnice instance, x>=0, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y>=0, y=0 má horní okraj plátna
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     */
    public Elipsa( int x, int y, int šířka, int výška )
    {
        this( x, y, šířka, výška, IMPLICITNÍ_BARVA );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanými rozměry, polohou a barvou.
     *
     * @param x       x-ová souřadnice instance, x>=0, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y>=0, y=0 má horní okraj plátna
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     * @param barva   Barva vytvářené instance
     */
    public Elipsa( int x, int y, int šířka, int výška, Barva barva )
    {
        //Test platnosti parametru
        if( (x<0) || (y<0) || (šířka<=0) || (výška<=0) ) {
            throw new IllegalArgumentException(
                "\nParametry nemají povolené hodnoty: x="
                + x + ", y=" + y + ", šířka=" + šířka + ", výška=" + výška );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.šířka = šířka;
        this.výška = výška;
        this.barva = barva;
        nakresli();
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru,
     * tj. stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
    public ITvar kopie()
    {
        if (this instanceof ITvar) {
            return (ITvar)new Elipsa(xPos, yPos, šířka, výška, barva);
        } else {
            //Pomocná třída zabezpečující správnou funkci i v případě,
            //kdy třída nebude implementovat rozhraní ITvar
            class TE extends Elipsa implements ITvar {
                TE(int x, int y, int s, int v, Barva b) {
                    super(x, y, s, v, b);
                }
            }
            return new TE(xPos, yPos, šířka, výška, barva);
        }
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou souřadnici pozice instance.
     *
     * @return  x-ová souřadnice.
     */
    public int getX()
    {
        return xPos;
    }


    /***************************************************************************
     * Vrátí y-ovou souřadnici pozice instance.
     *
     * @return  y-ová souřadnice.
     */
    public int getY()
    {
        return yPos;
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x   Nová x-ová pozice instance
     * @param y   Nová y-ová pozice instance
     */
    public void setPozice(int x, int y)
    {
        smaž();
        xPos = x;
        yPos = y;
        nakresli();
    }


    /***************************************************************************
     * Vrátí šířku instance.
     *
     * @return  Šířka instance v bodech
     */
     public int getŠířka()
     {
         return šířka;
     }


    /***************************************************************************
     * Vrátí výšku instance.
     *
     * @return  Výška instance v bodech
     */
     public int getVýška()
     {
         return výška;
     }


    /***************************************************************************
     * Nastaví nový "čtvercový" rozměr instance -
     * na zadaný rozměr se nastaví výška i šířka.
     *
     * @param rozměr  Nově nastavovaný rozměr v obou směrech; rozměr>0
     */
    public void setRozměr(int rozměr)
    {
        setRozměr( rozměr, rozměr );
    }


    /***************************************************************************
     * Nastaví nové rozměry instance. Nastavované rozměry musí být nezáporné,
     * avšak místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param šířka    Nově nastavovaná šířka; šířka >= 0
     * @param výška    Nově nastavovaná výška; výška >= 0
     */
    public void setRozměr(int šířka, int výška)
    {
        if( (šířka < 0) || (výška < 0) ) {
            throw new IllegalArgumentException(
            "Rozměry musí byt nezáporné: šířka=" + šířka + ", výška=" + výška);
        }
        smaž();
        this.šířka = Math.max(1, šířka);
        this.výška = Math.max(1, výška);
        nakresli();
    }


    /***************************************************************************
     * Vrátí barvu instance.
     *
     * @return  Instance třídy Barva definující nastavenou barvu.
     */
    public Barva getBarva()
    {
        return barva;
    }


    /***************************************************************************
     * Nastaví novou barvu instance.
     *
     * @param nová  Požadovaná nová barva.
     */
    public void setBarva(Barva nová)
    {
        if ( nová != Barva.ŽÁDNÁ) {
            barva = nová;
            nakresli();
        }
    }


    /***************************************************************************
     * Vrátí název instance, tj. název její třídy následovaný pořadím.
     *
     * @return  Řetězec s názvem instance.
     */
    public String getNázev()
    {
        return název;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Převede instanci na řetězec. Používá se především při ladění.
     *
     * @return Řetězcová reprezentace dané instance.
     */
    @Override
    public String toString()
    {
        return název + "_(x=" + xPos + ",y=" + yPos  +
               ",šířka=" + šířka + ",výška=" + výška +
               ",barva=" + barva + ")";
    }


    /***************************************************************************
     * Vykreslí obraz své instance na plátno.
     */
    public void nakresli()
    {
        PLÁTNO.setBarvaPopředí( barva );
        PLÁTNO.zaplň(new java.awt.geom.Ellipse2D.Double
                         (xPos, yPos, šířka, výška));
    }


    /***************************************************************************
     * Smaže obraz své instance z plátna (nakreslí ji barvou pozadí plátna).
     */
    public void smaž()
    {
        PLÁTNO.setBarvaPopředí( PLÁTNO.getBarvaPozadí() );
        PLÁTNO.zaplň(new java.awt.geom.Ellipse2D.Double
                         (xPos, yPos, šířka, výška));
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů vpravo,
     * při záporné hodnotě parametru vlevo.
     *
     * @param vzdálenost Vzdálenost, o kterou se instance přesune.
     */
    public void posunVpravo(int vzdálenost)
    {
        setPozice( xPos+vzdálenost, yPos );
    }


    /***************************************************************************
     * Přesune instanci o krok bodů vpravo.
     */
    public void posunVpravo()
    {
        posunVpravo( krok );
    }


    /***************************************************************************
     * Přesune instanci o krok bodů vlevo.
     */
    public void posunVlevo()
    {
        posunVpravo( -krok );
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů dolů,
     * při záporné hodnotě parametru nahoru.
     *
     * @param vzdálenost   Počet bodů, o které se instance přesune.
     */
    public void posunDolů(int vzdálenost)
    {
        setPozice( xPos, yPos+vzdálenost );
    }


    /***************************************************************************
     * Přesune instanci o krok bodů dolů.
     */
    public void posunDolů()
    {
        posunDolů( krok );
    }


    /***************************************************************************
     * Přesune instanci o krok bodů nahoru.
     */
    public void posunVzhůru()
    {
        posunDolů( -krok );
    }


//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
