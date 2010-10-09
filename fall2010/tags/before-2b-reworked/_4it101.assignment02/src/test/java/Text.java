
import java.awt.Font;


/*******************************************************************************
 * Třída pro Zobrazení textu na aktivním plátně.
 *
 * Jedná se o výchozí podobu třídy v souboru projektů.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Text implements IPosuvný, IKopírovatelný
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Konstanta označující text vysazený netučným, nekurzivním písmem. */
    public static final int OBYČEJNÝ = Font.PLAIN;

    /** Konstanta označující text vysazený tučným, nekurzivním písmem. */
    public static final int TUČNÝ    = Font.BOLD;

    /** Konstanta označující text vysazený netučným, kurzivním písmem. */
    public static final int KURZIVA  = Font.ITALIC;

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro text Barva.ČERNÁ. */
    public static final Barva IMPLICITNÍ_BARVA = Barva.ČERNÁ;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Text uchovávaný a zobrazovaný instancí. */
    private final String název;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Bodová x-ová souřadnice instance. */
    private int xPos;

    /** Bodová y-ová souřadnice instance. */
    private int yPos;

    /** Barva instance. */
    private Barva barva;

    /** Písmo, jímž se zobrazuje reprezentovaný text. */
    private Font   font; 

    /** Písmo nastavené pro kreslítko. */
    private Font   kfont;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * a vysazena implicitním písmem (tučným 12bodovým písmem Dialog).
     *
     * @param text  Vypisovaný text
     */
    public Text(String text)
    {
        this(text, 0, 0);
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * implicitní barvou
     * a vysazovanou implicitním písmem (tučným 12bodovým písmem Dialog).
     *
     * @param text  Vypisovaný text
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public Text(String text, int x, int y)
    {
        this(x, y, IMPLICITNÍ_BARVA, text);
    }


    /***************************************************************************
     * Vytvoří novou instanci se zadanou  pozicí, rozměry
     * a implicitní barvou
     * a vysazena implicitním písmem (tučným 12bodovým písmem Dialog).
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param text  Vypisovaný text
     */
    public Text(int x, int y, String text)
    {
        this(x, y, IMPLICITNÍ_BARVA, text);
    }


    /***************************************************************************
     * Vytvoří novou instanci se zadanou pozicí, rozměry a barvou.
     *
     * @param text    Vypisovaný text
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param barva   Barva vytvářené instance
     */
    public Text( int x, int y, Barva barva, String text )
    {
        this.xPos  = x;
        this.yPos  = y;
        this.název = text;
        this.barva = barva;
        this.font = new Font( "Dialog", Font.BOLD, 12 );
    }


    /***************************************************************************
     * Vrátí svoji kopii, tj. instanci s naprosto shodnými vlastnostmi
     * s výjimkou těch, které podle kontraktu shodné být nesmějí.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Text kopie()
    {
        Text ret = new Text(xPos, yPos, barva, název);
        ret.font = font;
        ret.font = kfont;
        return ret;
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
     * Tato metoda je tu jenom proto, aby se text sjednotil s ostatními
     * třídami - jinak text ji samozřejmě nepotřebuje, protože tištěný text
     * je sám názvem instance.
     *
     * @return  Uložený text.
     */
    public String getNázev()
    {
        return název;
    }


    /***************************************************************************
     * Vrátí font, kterým se bude daný text sázet.
     *
     * @return Požadovaný font
     */
    public Font getFont()
    {
        return font;
    }


    /***************************************************************************
     * Nastaví font, kterým se bude daný text sázet.
     *
     * @param název     Název fontu - je možno zadat jeden z názvů:
     *                   "Dialog", "DialogInput", "Monospaced",
     *                   "Serif",  "SansSerif".
     * @param řez       Je možno zadat některý z řezů:
     *                   Text.OBYČEJNÝ, Text.TUČNÝ, Text.KURZIVA,
     *                   případně Text.TUČNÝ|Text.KURZIVA
     * @param velikost  Velikost písma v bodech.
     */
    public void setFont( String název, int řez, int velikost )
    {
        font = new Font( název, řez, velikost );
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
        return název;
    }


    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslítko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli(Kreslítko kreslítko)
    {
        if( font != kfont )
        {
            kreslítko.setFont( font );
            kfont = font;
        }
        kreslítko.kresliText(název, xPos, yPos + font.getSize(), barva);
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



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
