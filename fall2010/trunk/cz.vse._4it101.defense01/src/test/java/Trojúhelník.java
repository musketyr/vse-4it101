

/*******************************************************************************
 * Instance třídy {@code Trojúhelník} představují trojúhelníky určené
 * pro práci na virtuálním plátně při prvním seznámení s třídami a objekty.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Trojúhelník
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro trojúhelník Barva.ZELENÁ. */
    public static final Barva IMPLICITNÍ_BARVA = Barva.ZELENÁ;

    /** Směr, kam bude ukazovat vrcholt trojúhelníku v případě,
     *  kdy uživatel žádný preferovný směr nezadá.    */
    public static final Směr8 IMPLICITNÍ_SMĚR = Směr8.SEVER;

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
    private final String název = "Trojúhelník_" + pořadí;



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

    /** Směr, do nějž je otočen vrchol trojúhelníku. */
    private Směr8   směr;



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
     * Připraví novou instanci s implicitním umístěním, rozměry, barvou
     * a natočením.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku (tj. implicitně 50x100 bodů)
     * a bude natočena vrcholem na sever.
     */
    public Trojúhelník()
    {
        this( 0, 0, 2*krok, krok );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou a směrem natočení.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     */
    public Trojúhelník( int x, int y, int šířka, int výška )
    {
        this( x, y, šířka, výška, IMPLICITNÍ_BARVA, IMPLICITNÍ_SMĚR );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a směrem natočení
     * a s implicitní barvou.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka instance,   šířka > 0
     * @param výška   Výška instance,   výška > 0
     * @param směr    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojúhelník( int x, int y, int šířka, int výška, Směr8 směr )
    {
        this( x, y, šířka, výška, IMPLICITNÍ_BARVA, směr );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     * Směr natočení bude implicitní, tj. na sever.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     * @param barva   Barva vytvářené instance
     */
    public Trojúhelník( int x, int y, int šířka, int výška, Barva barva )
    {
        this( x, y, šířka, výška, barva, IMPLICITNÍ_SMĚR );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry, barvou,
     * i směrem natočení.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param šířka   Šířka vytvářené instance,  šířka > 0
     * @param výška   Výška vytvářené instance,  výška > 0
     * @param barva   Barva vytvářené instance
     * @param směr    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojúhelník( int x, int y, int šířka, int výška, Barva barva,
                        Směr8 směr )
    {
        //Test platnosti parametru
        if ((šířka<=0) || (výška<=0)) {
            throw new IllegalArgumentException(
                "\nnew Trojúhelník - Parametry nemají povolené hodnoty: x="
                + x + ", y=" + y + ", šířka=" + šířka + ", výška=" + výška );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.šířka = šířka;
        this.výška = výška;
        this.barva = barva;
        this.směr  = směr;
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
     * @return Instance třídy {@code Barva} definující aktuálně nastavenou barvu
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


    /***************************************************************************
     * Vrátí aktuální směr instance. tj. směr, do nějž je natočen vrchol.
     *
     * @return  Instance třídy {@code Směr8} definující aktuálně nastavený směr
     */
    public Směr8 getSměr()
    {
        return směr;
    }


    /***************************************************************************
     * Nastaví nový směr instance.
     *
     * @param nový  Požadovaný nový směr
     */
    public void setSměr(Směr8 nový)
    {
        if (nový != Směr8.ŽÁDNÝ) {
            smaž();
            směr = nový;
            nakresli();
        }
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
               ",barva=" + barva + ",směr="  + směr  + ")";
    }


    /***************************************************************************
     * Vykreslí obraz své instance na plátno.
     */
    public void nakresli()
    {
        PLÁTNO.setBarvaPopředí( barva );
        int[][] points = getVrcholy();
        PLÁTNO.zaplň(new java.awt.Polygon(points[0], points[1], 3));
    }


    /***************************************************************************
     * Smaže obraz své instance z plátna (nakreslí ji barvou pozadí plátna).
     */
    public void smaž()
    {
        PLÁTNO.setBarvaPopředí( PLÁTNO.getBarvaPozadí() );
        int[][] points = getVrcholy();
        PLÁTNO.zaplň(new java.awt.Polygon(points[0], points[1], 3));
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

    /***************************************************************************
     * Vrátí matici se souřadnicemi vrcholů daného trojúhelníku.
     *
     * @return Požadovaná matice
     */
    private int[][] getVrcholy()
    {
        int[] xpoints = null;
        int[] ypoints = null;

        switch( směr )
        {
            case VÝCHOD:
                xpoints = new int[]{ xPos,  xPos + (šířka),    xPos };
                ypoints = new int[]{ yPos,  yPos + (výška/2),  yPos + výška };
                break;

            case SEVEROVÝCHOD:
                xpoints = new int[]{ xPos,  xPos + šířka,  xPos + šířka };
                ypoints = new int[]{ yPos,  yPos,          yPos + výška };
                break;

            case SEVER:
                xpoints = new int[]{ xPos,         xPos + (šířka/2), xPos + šířka };
                ypoints = new int[]{ yPos + výška, yPos,             yPos + výška };
                break;

            case SEVEROZÁPAD:
                xpoints = new int[]{ xPos,          xPos,  xPos + šířka };
                ypoints = new int[]{ yPos + výška,  yPos,  yPos         };
                break;

            case ZÁPAD:
                xpoints = new int[]{ xPos,             xPos + šířka, xPos + šířka };
                ypoints = new int[]{ yPos + (výška/2), yPos,         yPos + výška };
                break;

            case JIHOZÁPAD:
                xpoints = new int[]{ xPos,  xPos,          xPos + šířka };
                ypoints = new int[]{ yPos,  yPos + výška,  yPos + výška };
                break;

            case JIH:
                xpoints = new int[]{ xPos,  xPos + (šířka/2),  xPos + šířka };
                ypoints = new int[]{ yPos,  yPos + výška,      yPos,        };
                break;

            case JIHOVÝCHOD:
                xpoints = new int[]{ xPos,          xPos +šířka,   xPos + šířka };
                ypoints = new int[]{ yPos + výška,  yPos + výška,  yPos         };
                break;

            default:
                throw new IllegalStateException(
                    "Instance ukazuje do nedefinovaného směru" );
        }
        return new int[][] { xpoints, ypoints };
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
