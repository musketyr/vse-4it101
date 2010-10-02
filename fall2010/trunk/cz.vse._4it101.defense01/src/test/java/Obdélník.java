

/*******************************************************************************
 * Instance třídy {@code Obdélník} představují obdélníky určené
 * pro práci na virtuálním plátně při prvním seznámení s třídami a objekty.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Obdélník
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro obdélník Barva.ČERVENÁ. */
    public static final Barva IMPLICITNÍ_BARVA = Barva.ČERVENÁ;

    /** Maximální povolená velikost kroku. */
    public static final int MAX_KROK = 100;



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
    private final String název = "Obdélník_" + pořadí;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Bodová x-ová souřadnice instance. */
    private int xPos;

    /** Bodová y-ová souřadnice instance. */
    private int yPos;

    /** Šířka v bodech. */
    protected int šířka;

    /** Výška v bodech. */
    protected int výška;

    /** Barva instance. */
    private Barva barva;



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
                "\nKrok musí byt z intervalu <0;" + MAX_KROK + ">." );
        }
        krok = velikost;
    }



//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku (tj. implicitně 50x100 bodů).
     */
    public Obdélník()
    {
        this( 0, 0, 2*krok, krok );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     */
    public Obdélník( int x, int y, int šířka, int výška )
    {
        this( x, y, šířka, výška, IMPLICITNÍ_BARVA );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     * @param barva   Barva vytvářené instance
     */
    public Obdélník( int x, int y, int šířka, int výška, Barva barva )
    {
        //Test platnosti parametru
        if ((šířka<=0) || (výška<=0)) {
            throw new IllegalArgumentException(
                "\nnew Obdélník - Parametry nemají povolené hodnoty: x="
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



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance.
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    public int getX()
    {
        return xPos;
    }


    /***************************************************************************
     * Vrátí y-ovou (svislou) souřadnici pozice instance.
     *
     * @return  Aktuální svislá (y-ová) souřadnice instance,
     *          y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public int getY()
    {
        return yPos;
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public void setPozice(int x, int y)
    {
        smaž();
        xPos = x;
        yPos = y;
        nakresli();
    }


    /***************************************************************************
     * Vrátí šířku instance v bodech.
     *
     * @return  Aktuální šířka instance v bodech
     */
     public int getŠířka()
     {
         return šířka;
     }


    /***************************************************************************
     * Vrátí výšku instance v bodech.
     *
     * @return  Aktuální výška instance v bodech
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
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param šířka    Nově nastavovaná šířka; šířka >= 0
     * @param výška    Nově nastavovaná výška; výška >= 0
     */
    public void setRozměr(int šířka, int výška)
    {
        if ((šířka < 0) || (výška < 0)) {
            throw new IllegalArgumentException(
            "Rozměry musí byt nezáporné: šířka=" + šířka + ", výška=" + výška);
        }
        smaž();
        this.šířka = Math.max(1, šířka);
        this.výška = Math.max(1, výška);
        nakresli();
    }


    /***************************************************************************
     * Vrátí aktuální barvu instance.
     *
     * @return  Instance třídy Barva definující aktuálně nastavenou barvu
     */
    public Barva getBarva()
    {
        return barva;
    }


    /***************************************************************************
     * Nastaví novou barvu instance.
     *
     * @param nová  Požadovaná nová barva
     */
    public void setBarva(Barva nová)
    {
        if ( nová != Barva.ŽÁDNÁ) {
            barva = nová;
            nakresli();
        }
    }


    /***************************************************************************
     * Vrátí název instance, tj. název její třídy následovaný
     * pořadím vytvoření instance v rámci instancí této třídy.
     *
     * @return  Řetězec s názvem instance
     */
    public String getNázev()
    {
        return název;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí podpis instance, tj. její řetězcovou reprezentaci.
     * Používá se především při ladění.
     *
     * @return Řetězcová reprezentace (podpis) dané instance
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
        Plátno.getPlátno().setBarvaPopředí( barva );
        Plátno.getPlátno().zaplň(new java.awt.geom.Rectangle2D.Double
                         (xPos, yPos, šířka, výška));
    }


    /***************************************************************************
     * Smaže obraz své instance z plátna (nakreslí ji barvou pozadí plátna).
     */
    public void smaž()
    {
        Plátno.getPlátno().setBarvaPopředí(Plátno.getPlátno().getBarvaPozadí());
        Plátno.getPlátno().zaplň(new java.awt.geom.Rectangle2D.Double
                         (xPos, yPos, šířka, výška));
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů vpravo,
     * při záporné hodnotě parametru vlevo.
     *
     * @param vzdálenost Vzdálenost, o kterou se instance přesune
     */
    public void posunVpravo(int vzdálenost)
    {
        setPozice( xPos+vzdálenost, yPos );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vpravo.
     * Tento počet lze zjistit zavoláním statické metody {@link getKrok()}
     * a nastavit zavoláním statické metody {@link setKrok(int)}.
     */
    public void posunVpravo()
    {
        posunVpravo( krok );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vlevo.
     * Tento počet lze zjistit zavoláním statické metody {@link getKrok()}
     * a nastavit zavoláním statické metody {@link setKrok(int)}.
     */
    public void posunVlevo()
    {
        posunVpravo( -krok );
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů dolů,
     * při záporné hodnotě parametru nahoru.
     *
     * @param vzdálenost   Počet bodů, o které se instance přesune
     */
    public void posunDolů(int vzdálenost)
    {
        setPozice( xPos, yPos+vzdálenost );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů dolů.
     * Tento počet lze zjistit zavoláním statické metody {@link getKrok()}
     * a nastavit zavoláním statické metody {@link setKrok(int)}.
     */
    public void posunDolů()
    {
        posunDolů( krok );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů nahoru.
     * Tento počet lze zjistit zavoláním statické metody {@link getKrok()}
     * a nastavit zavoláním statické metody {@link setKrok(int)}.
     */
    public void posunVzhůru()
    {
        posunDolů( -krok );
    }


//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
