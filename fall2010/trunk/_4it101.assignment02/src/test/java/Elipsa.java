


/*******************************************************************************
 * Instance třídy {@code Elipsa} představují elipsy určené
 * pro práci na virtuálním plátně při prvním seznámení s třídami a objekty.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Elipsa implements ITvar
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro elipsu Barva.MODRÁ. */
    public static final Barva IMPLICITNÍ_BARVA = Barva.MODRÁ;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet vytvořených instancí */
    private static int počet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Pořadí vytvoření dané instance v rámci třídy. */
    private final int pořadí = ++počet;

    /** Název sestávající z názvu třídy a pořadí instance */
    private final String název = "Elipsa_" + pořadí;



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
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku plátna.
     */
    public Elipsa()
    {
        this( 0, 0, 2*SprávcePlátna.getInstance().getKrok(), SprávcePlátna.getInstance().getKrok() );
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
    public Elipsa( int x, int y, int šířka, int výška )
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
    public Elipsa( int x, int y, int šířka, int výška, Barva barva )
    {
        //Test platnosti parametru
        if ((šířka<=0) || (výška<=0)) {
            throw new IllegalArgumentException(
                "\nnew Elipsa - Parametry nemají povolené hodnoty: x="
                + x + ", y=" + y + ", šířka=" + šířka + ", výška=" + výška );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.šířka = šířka;
        this.výška = výška;
        this.barva = barva;
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru,
     * tj. stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Elipsa kopie()
    {
        return new Elipsa(xPos, yPos, šířka, výška, barva);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance.
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    @Override
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
    @Override
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
    @Override
    public void setPozice(int x, int y)
    {
        xPos = x;
        yPos = y;
        SprávcePlátna.getInstance().překresli();
    }


    /***************************************************************************
     * Vrátí šířku instance v bodech.
     *
     * @return  Aktuální šířka instance v bodech
     */
    @Override
     public int getŠířka()
     {
         return šířka;
     }


    /***************************************************************************
     * Vrátí výšku instance v bodech.
     *
     * @return  Aktuální výška instance v bodech
     */
    @Override
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
    @Override
    public void setRozměr(int šířka, int výška)
    {
        if( (šířka < 0) || (výška < 0) ) {
            throw new IllegalArgumentException(
            "Rozměry musí byt nezáporné: šířka=" + šířka + ", výška=" + výška);
        }
        this.šířka = Math.max(1, šířka);
        this.výška = Math.max(1, výška);
        SprávcePlátna.getInstance().překresli();
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
        barva = nová;
        SprávcePlátna.getInstance().překresli();
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
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslítko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli( Kreslítko kreslítko )
    {
        kreslítko.vyplňOvál( xPos, yPos, šířka, výška, barva );
    }


    /***************************************************************************
     * Přihlásí se u správce plátna.
     */
    public void nakresli()
    {
        SprávcePlátna.getInstance().přidej(this);
    }


    /***************************************************************************
     * Odhlásí se u správce plátna.
     */
    public void smaž()
    {
        SprávcePlátna.getInstance().odstraň(this);
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
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@link getKrok()}
     * a nastavit zavoláním jeho metody {@link setKrok(int)},
     * resp. {@link setKrokRozměr(int,int,int)}.
     */
    public void posunVpravo()
    {
        posunVpravo( SprávcePlátna.getInstance().getKrok() );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vlevo.
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@link getKrok()}
     * a nastavit zavoláním jeho metody {@link setKrok(int)},
     * resp. {@link setKrokRozměr(int,int,int)}.
     */
    public void posunVlevo()
    {
        posunVpravo( -SprávcePlátna.getInstance().getKrok() );
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
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@link getKrok()}
     * a nastavit zavoláním jeho metody {@link setKrok(int)},
     * resp. {@link setKrokRozměr(int,int,int)}.
     */
    public void posunDolů()
    {
        posunDolů( SprávcePlátna.getInstance().getKrok() );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů nahoru.
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@link getKrok()}
     * a nastavit zavoláním jeho metody {@link setKrok(int)},
     * resp. {@link setKrokRozměr(int,int,int)}.
     */
    public void posunVzhůru()
    {
        posunDolů( -SprávcePlátna.getInstance().getKrok() );
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
