

/*******************************************************************************
 * Třída Kompresor slouží ke změně velikosti objektu
 * implementujicich rozhraní INafukovací.
 * Oproti verzi z projektu _06_Rozhraní doplňuje možnost
 * definovat u na/vy-fukovaného objektu směr, v němž se předpokládá kotvící bod,
 * který se při změně rozměru nehýbá.
 * Třída NENÍ vláknově bezpečná (thread-safe). Nepředpokládá,
 * že její instance boudou volány simultánně z různých vláken.
 *
 * @author     Rudolf PECINOVSKÝ
 * @version    1.01, 10.3.2003
 */
public class Kompresor
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Doba mezi jednotlivými "šťouchy".*/
    private final static int ČEKÁNÍ = 30;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Specifikuje sílu "nafukování" objektu danou instanci kompresoru,
     * tj. míru jeho prifouknuti. */
    private int síla;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Konstruktor kompresorů se silou nafukování 1 (síla nafukování definuje
     * počet bodů, o něž se zvětší rozměr objektu po jednom "šťouchu").
     */
    public Kompresor()
    {
        this( 1 );
    }


    /***************************************************************************
     * Konstruktor kompresorů se zadanou silou nafukování (síla nafukování
     * definuje počet bodů, o něž se zvětší rozměr objektu po jednom "šťouchu").
     *
     * @param síla  Sila nafukování vytvářeného kompresoru
     */
    public Kompresor(int síla)
    {
        this.síla = síla;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Metoda zvětší zadanou instanci na zadaný násobek její velikosti.
     * Při změně velikosti se zachovává poměr stran.
     *
     * @param krát  Kolikrát se má zvětšit velikost daného objektu
     * @param koho  Objekt, jehož velikost je upravována
     */
    public void nafoukniKrát(double krát, INafukovací koho)
    {
        int šířka = (int)Math.round(koho.getŠířka() * krát);
        int výška = (int)Math.round(koho.getVýška() * krát);
        nafoukej(koho, šířka, výška, Směr8.SEVEROZÁPAD);
    }


    /***************************************************************************
     * Metoda zvětší zadanou instanci na zadaný násobek její velikosti.
     * Při změně velikosti se zachovává poměr stran.
     *
     * @param krát   Kolikrát se má zvětšit velikost daného objektu
     * @param koho   Objekt, jehož velikost je upravována
     * @param pevný  Směr od středu obrazce, v němž se nachází pevný bod,
     *               který při změně rozměru nemění svoji pozici.
     *               Směry {@link Směr8.ŽÁDNÝ} a {@code null} označují
     *               symetrickou změnu vůči středu obrazce.
     */
    public void nafoukniKrát(double krát, IHýbací koho, Směr8 pevný)
    {
        int šířka = (int)Math.round(koho.getŠířka() * krát);
        int výška = (int)Math.round(koho.getVýška() * krát);
        nafoukej((INafukovací)koho, šířka, výška, pevný );
    }


    /***************************************************************************
     * Nafoukne či vyfoukne zadaný objekt na požadovanou velikost.
     * Nejprve ale zabezpečí, aby byl objekt zobrazen na plátně.
     *
     * @param šířka   Požadovaná výsledná šířka objektu
     * @param výška   Požadovaná výsledná výška objektu
     * @param koho    Na(vy)fukovany objekt
     */
    public void nafoukniNa(int šířka, int výška, INafukovací koho)
    {
        nafoukej( koho, šířka, výška, Směr8.SEVEROZÁPAD);
    }


    /***************************************************************************
     * Nafoukne či vyfoukne zadaný objekt na požadovanou velikost.
     * Nejprve ale zabezpečí, aby byl objekt zobrazen na plátně.
     *
     * @param šířka   Požadovaná výsledná šířka objektu
     * @param výška   Požadovaná výsledná výška objektu
     * @param koho    Na(vy)fukovany objekt
     * @param pevný   Směr od středu obrazce, v němž se nachází pevný bod,
     *                který při změně rozměru nemění svoji pozici.
     *                Směry {@link Směr8.ŽÁDNÝ} a {@code null} označují
     *                symetrickou změnu vůči středu obrazce.
     */
    public void nafoukniNa(int šířka, int výška, IHýbací koho, Směr8 pevný )
    {
        nafoukej( (INafukovací)koho, šířka, výška, pevný);
    }


    /***************************************************************************
     * Metoda zvětší zadanou instanci o zadaný násobek síly svého kompresoru.
     * Při změně velikosti se zachovává poměr stran.
     * Nejprve ale zabezpečí, aby byl objekt zobrazen na plátně.
     *
     * @param dx    Změna rozměru ve vodorovném směru
     * @param dy    Změna rozměru ve svislém směru
     * @param koho  Objekt, jehož velikost je upravována
     */
    public void nafoukniO(int dx, int dy, INafukovací koho)
    {
        int šířka = koho.getŠířka() + dx;
        int výška = koho.getVýška() + dy;
        nafoukej(koho, šířka, výška, Směr8.SEVEROZÁPAD);
    }


    /***************************************************************************
     * Metoda zvětší zadanou instanci o zadaný násobek síly svého kompresoru.
     * Při změně velikosti se zachovává poměr stran.
     * Nejprve ale zabezpečí, aby byl objekt zobrazen na plátně.
     *
     * @param dx    Změna rozměru ve vodorovném směru
     * @param dy    Změna rozměru ve svislém směru
     * @param koho  Objekt, jehož velikost je upravována
     * @param pevný   Směr od středu obrazce, v němž se nachází pevný bod,
     *                který při změně rozměru nemění svoji pozici.
     *                Směry {@link Směr8.ŽÁDNÝ} a {@code null} označují
     *                symetrickou změnu vůči středu obrazce.
     */
    public void nafoukniO(int dx, int dy, IHýbací koho, Směr8 pevný)
    {
        int šířka = koho.getŠířka() + dx;
        int výška = koho.getVýška() + dy;
        nafoukej((INafukovací)koho, šířka, výška, pevný );
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Nafoukne či vyfoukne zadaný objekt na požadovanou velikost.
     * Nejprve ale zabezpečí, aby byl objekt zobrazen na plátně.
     *
     * @param koho    Na(vy)fukovany objekt
     * @param šířka   Požadovaná výsledná šířka objektu
     * @param výška   Požadovaná výsledná výška objektu
     * @param pevný   Směr od středu obrazce, v němž se nachází pevný bod,
     *                který při změně rozměru nemění svoji pozici.
     *                Směry {@link Směr8.ŽÁDNÝ} a {@code null} označují
     *                symetrickou změnu vůči středu obrazce.
     */
    private void nafoukej(INafukovací koho, int šířka, int výška, Směr8 pevný)
    {
        int    š = koho.getŠířka();
        int    v = koho.getVýška();
        int    vodorovne = šířka - š;
        int    svisle    = výška - v;
        int    kroku     = (int)(Math.hypot(vodorovne, svisle) / síla);
        double dx = (double)vodorovne / kroku;
        double dy = (double)svisle    / kroku;
        foukej( koho, kroku, dx, dy, pevný );
    }


    /***************************************************************************
     * Výkonná metoda, která zařídí vlastní nafouknutí, resp. vyfouknutí
     * zadaného objektu na základě přípravených parametrů.
     *
     * @param koho     Objekt, jehož velikost měníme.
     * @param šťouchů  Počet kroků, v nichž velikost objektu změníme.
     * @param dx       Zvětšení šířky objektu v jednom kroku.
     * @param dy       Zvětšení výšky objektu v jednom kroku.
     * @param pevný    Směr, kterým leží pevný bod.
     */
    private void foukej(INafukovací koho, int šťouchů, double dx, double dy,
                        Směr8 pevný)
    {
        if (pevný == null) {
            pevný = Směr8.ŽÁDNÝ;
        }
        IHýbací ih = null;
        double dxx = 0, dyy = 0;
        double x   = 0, y   = 0;
        if( pevný != Směr8.SEVEROZÁPAD )
        {
            if( ! (koho instanceof IHýbací) ) {
                throw new IllegalArgumentException("\nVe směru " + pevný +
                        " je možno nafukovat pouze " +
                        "objekty implementující rozhraní IHýbací");
            }
            ih = (IHýbací)koho;
            x  = ih.getX() + .4;
            y  = ih.getY() + .4;
            if(      (pevný == Směr8.JIHOVÝCHOD)   ||
                     (pevný == Směr8.VÝCHOD)       ||
                     (pevný == Směr8.SEVEROVÝCHOD) )
            {
                dxx = -dx;
            }
            else if( (pevný == Směr8.SEVER)  ||
                     (pevný == Směr8.JIH)    ||
                     (pevný == Směr8.ŽÁDNÝ) )
            {
                dxx = -dx/2;
            }
            if(      (pevný == Směr8.JIHOZÁPAD)  ||
                     (pevný == Směr8.JIH)        ||
                     (pevný == Směr8.JIHOVÝCHOD) )
            {
                dyy = -dy;
            }
            else if( (pevný == Směr8.VÝCHOD)  ||
                     (pevný == Směr8.ZÁPAD)   ||
                     (pevný == Směr8.ŽÁDNÝ) )
            {
                dyy = -dy/2;
            }
        }
        //Konstatnu připočítáváme proto, aby skoky byly vyrovnanější
        int    š = koho.getŠířka();
        int    v = koho.getVýška();
        double šířka = š + .4;
        double výška = v + .4;
        while( šťouchů-- > 0 )
        {
            IO.čekej(ČEKÁNÍ);
            šířka += dx;
            výška += dy;
            koho.setRozměr( (int)šířka, (int)výška );
            if( pevný != Směr8.SEVEROZÁPAD )
            {
                x += dxx;
                y += dyy;
                ih.setPozice( (int)x, (int)y );
            }
        }
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
