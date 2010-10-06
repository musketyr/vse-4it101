

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*******************************************************************************
 * Třída {@code Mnohotvar} slouží k vytváření složitějších geometrických tvarů
 * složených z několika tvarů jednodušších, které jsou instancemi rozhraní.
 * <p>
 * Mnohotvar je postupně skládán z řady jednodušších tvarů, které musejí být
 * instancemi rozhraní {@link ITvar}. Jiné požadavky na ně kladeny nejsou.
 * Při sestavování mnohotvar automaticky upravuje interní informaci o své
 * pozici a rozměru tak, aby pozice byla neustále v levém rohu opsaného
 * obdélníku a rozměr mnohotvaru odpovídal rozměru tohoto obdélníku.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class Mnohotvar implements ITvar
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Kolekce všech vytvořených a dosud nezrušených mnohotvarů
     *  indexovaná jejich názvy. */
    private static final Map<String,Mnohotvar> název2mnohotvar =
                                               new HashMap<String, Mnohotvar>();




//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Název zadaný při konstrukci objektu.
     *  Tento název umožnuje blíže specifikovat příslušný mnohotvar. */
    private final String název;

    /** Seznam prvků, z nichž se mnohotvar skládá. */
    private final List<Část> seznam = new ArrayList<Část>();



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Dokud je atribut false, je možné do mnohotvaru
     *  přidávat další součásti. */
    private boolean tvorbaUkončena = false;

    /** Bodová x-ová souřadnice instance. */
    private int xPos;

    /** Bodová y-ová souřadnice instance. */
    private int yPos;

    /** Šířka v bodech. */
    protected int šířka;

    /** Výška v bodech. */
    protected int výška;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Vrátí pole názvů všech evidovaných mnohotvarů.
     *
     * @return Pole názvů evidovaných mnohotvarů
     */
    public static String[] názvyDostupných()
    {
        int počet = název2mnohotvar.size();
        String[] ret = new String[počet];
        int i = 0;
        for (Map.Entry<String,Mnohotvar> me : název2mnohotvar.entrySet()) {
            ret[i] = me.getKey();
        }
        return ret;
    }



//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vrátí instanci se zadaným názvem.
     *
     * @param název Název požadované instance
     * @return Požadovaná instance
     * @throws IllegalArgumentException Není-li evidována instance
     *                                  se zadaným názvem
     */
    public Mnohotvar getInstance(String název)
    {
        Mnohotvar ret = název2mnohotvar.get(název);
        if (ret == null) {
            throw new IllegalArgumentException(
                "\nMnohotvar se zadaným názvem není evidován: " + název);
        }
        return ret;
    }


    /***************************************************************************
     * Definuje mnohotvar se zadaným názvem skládající se ze zadaných objektů;
     * do tohoto mnohotvaru již nebude možno přidávat další objekty.
     *
     * @param název  Název vytvářeného mnohotvaru. Je-li zadán prázdný řetězec
     *               nebo {@code null}, konstruktor mu přiřadí název ve tvaru
     *               {@code Mnohotvar#}<i>i</i>, kde <i>i</i> je celé číslo.
     * @param části  Jednotlivé tvary, z nichž je mnohotvar složen
     */
    public Mnohotvar(String název, ITvar... části)
    {
        this(false, název, části);
    }


    /***************************************************************************
     * Definuje prázdný mnohotvar se zadaným názvem očekávající,
     * že jeho jednotlivé části budou teprve dodány pomocí metody
     * {@link #přidej(ITvar...)}.
     * <p>
     * Ukončení sestavování mnohotvaru je třeba oznámit zavoláním metody
     * {@link hotovo()}. Dokud není sestavování ukončeno, není možno nastavovat
     * pozici ani rozměr vznikajícího mnohotvaru. Je však možno se na ně
     * zeptat a současně je možno rozdělaný mnohotvar nakreslit.
     *
     * @param název  Název vytvářeného mnohotvaru. Je-li zadán prázdný řetězec
     *               nebo {@code null}, konstruktor mu přiřadí název ve tvaru
     *               {@code Mnohotvar#}<i>i</i>, kde <i>i</i> je celé číslo.
     */
    public Mnohotvar(String název)
    {
        this(false, název, (ITvar[])null);
    }


    /***************************************************************************
     * Definuje prázdný mnohotvar se zadaným názvem očekávající,
     * že jeho jednotlivé části budou teprve dodány pomocí metody.
     *
     * @param interní Příznak tvorby interní kopie existujícího tvaru,
     *                která nebude mít název a nebude zařazena mezi evidované
     * @param název  Název vytvářeného mnohotvaru. Je-li zadán prázdný řetězec
     *               nebo {@code null}, konstruktor mu přiřadí název ve tvaru
     *               {@code Mnohotvar#}<i>i</i>, kde <i>i</i> je celé číslo.
     * @param části  Jednotlivé tvary, z nichž je mnohotvar složen
     */
    private Mnohotvar(boolean interní, String název, ITvar... části)
    {
        if (! interní) {
            //Není-li název zadán, vymyslí vlastní
            if ((název == null)  ||  (název.equals(""))) {
                název = názevProKopii("Mnohotvar");
            }
            else { //Je zadán => zkontroluje jeho jedinečnost
                ověřNázev(název);
            }
        }
        this.název = název;

        if (! interní) {
            název2mnohotvar.put(název, this);
        }

        //Byly-li zadány části, sestaví a uzavře vytvářený tvar
        if ((části != null)  &&  (části.length > 0)) {
            přidej(části);
            tvorbaUkončena = true;
        }
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru a přidělí jí vlastní název ve tvaru
     * {@code PůvodníNázev#?}, kde otazník zastupuje nejmenší celé kladné číslo,
     * pro něž není evidován mnohotvar s takto vytvořeným názvem.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Mnohotvar kopie()
    {
        String názevKopie = (název == null)
                          ? null
                          : názevProKopii(this.název);
        Mnohotvar kopie = kopie(false, názevKopie);
        return kopie;
    }


    /***************************************************************************
     * Vytvoří kopii daného mnohotvaru se zadaným názvem,
     * který se však nesmí shodovat s názvem žádného evidovaného tvaru.
     *
     * @param  názevKopie Název požadované kopie
     * @return Požadovaná kopie
     */
    public Mnohotvar kopie(String názevKopie)
    {
        return kopie(false, názevKopie);
    }


    /***************************************************************************
     * Vytvoří kopii daného mnohotvaru se zadaným názvem,
     * který se však nesmí shodovat s názvem žádného evidovaného tvaru.
     *
     * @param  názevKopie Název požadované kopie
     * @return Požadovaná kopie
     */
    private Mnohotvar kopie(final boolean interní, final String názevKopie)
    {
        if (! interní) {
            ověřNázev(názevKopie);
        }
        int počet  = seznam.size();
        final ITvar[] ait = new ITvar[počet];
        for (int i = 0;   i < počet;   i++) {
            ait[i] = seznam.get(i).tvar;
        }
        return new Mnohotvar(interní, názevKopie, ait);
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
     * Přemístí celý mnohotvar na zadanou pozici.
     * Všechny součásti instance se přemisťují jako celek
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
    @Override
    public void setPozice(int x, int y)
    {
        ověřDokončenost();
        int dx = x - getX();
        int dy = y - getY();
        SprávcePlátna.getInstance().nekresli(); {
            for( Část č : seznam )
            {
                ITvar tvar = č.tvar;
                tvar.setPozice( dx + tvar.getX(), dy + tvar.getY() );
            }
            this.xPos = x;    //Nastavuji hodnoty pro celý tvar
            this.yPos = y;
        } SprávcePlátna.getInstance().vraťKresli();
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
     * Nastaví nový rozměr mnohotvaru. Upraví rozměry a pozice všech jeho
     * součástí tak, aby výsledný mnohotvar měl i při novém rozměru
     * stále stejný celkový vzhled.
     * Nastavované rozměry musí být nezáporné,
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param šířka    Nově nastavovaná šířka; šířka >= 0
     * @param výška    Nově nastavovaná výška; výška >= 0
     */
    @Override
    public void setRozměr(int šířka, int výška)
    {
        ověřDokončenost();
        if( (šířka < 0) || (výška < 0) ) {
            throw new IllegalArgumentException(
            "Rozměry musí byt nezáporné: šířka=" + šířka + ", výška=" + výška);
        }
        SprávcePlátna.getInstance().nekresli(); {
            //Uprav velikosti a pozice jednotlivých částí
            for( Část č : seznam ) {
                č.poNafouknutí(šířka, výška);
            }
            //Nastavuji hodnoty pro celý tvar
//            this.šířka = šířka;
//            this.výška = výška;
            this.šířka = Math.max(1, šířka);
            this.výška = Math.max(1, výška);
        } SprávcePlátna.getInstance().vraťKresli();
    }


    /***************************************************************************
     * Vrátí název instance zadaný při konstrukci objektu.
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
               ",šířka=" + šířka + ",výška=" + výška + ")";
    }


    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslítko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli( Kreslítko kreslítko )
    {
        SprávcePlátna.getInstance().nekresli(); {
            for( Část č : seznam )
            {
                č.tvar.nakresli(kreslítko);
            }
        } SprávcePlátna.getInstance().vraťKresli();
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


    /***************************************************************************
     * Ukončí tvrobu mnohotvaru; od této chvíle již nebude možno přidat
     * žádný další objekt.
     */
    public void hotovo()
    {
        tvorbaUkončena = true;
    }


    /***************************************************************************
     * Přidá do mnohotvaru zadaný prvek a příslušně upraví novou
     * pozici a velikost mnohotvaru.
     *
     * @param tvar  Přidávaný hýbací tvar
     */
    public final void přidej(ITvar... tvar)
    {
        if (tvorbaUkončena) {
            throw new IllegalStateException("\nPokus o přidání dalšího prvku " +
                    "po ukončení tvorby mnohotvaru " + název);
        }
        for (ITvar t : tvar) {
            t =  (t instanceof Mnohotvar)
              ?  ((Mnohotvar)t).kopie(true, null)   //Interní kopie
              :  t.kopie();
            přidejTvar(t);
        }
        SprávcePlátna.getInstance().překresli();
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí doposud nepoužitý název pro vytvářenou kopii dané instance.
     * Má-li aktuální mnohotvar název {@code null}, jedná se o interní mnohotvar
     * složitějšího celku. Předpokládá se, že jeho kopie se opět vytváří pouze
     * pro zabudování do vyššího celku, a tak může mít opět název {@code null}.
     *
     * @return Dosud nepoužitý název
     */
    private String názevProKopii(String název)
    {
        if (název == null) {
            return null;
        }
        String podkladek = název + "#";
        String novýNázev;
        int i = 1;
        do {
            novýNázev = podkladek + i++;
        } while (název2mnohotvar.containsKey(novýNázev));
        return novýNázev;
    }


    /***************************************************************************
     * Zkontroluje, je-li název v kolekci dosud vytvořených názvů,
     * a pokud je, tak vyhodí výjimku.
     *
     * @param název Testovaný název
     * @throws IllegalArgumentException Název je již v kolekci
     */
    private void ověřNázev(String název)
            throws IllegalArgumentException
    {
        if (název2mnohotvar.containsKey(název)) {
            throw new IllegalArgumentException(
                "\nMnohotvar nemá zadán jedinečný název: " + název);
        }
    }


    /***************************************************************************
     * Zkontroluje dokončenost konstrucke objektu a není-li objekt dokončen,
     * vyhodí výjimku {@code IllegalStateException}.
     *
     * @throws IllegalStateException Není-li dokončena konstrukce objektu
     */
    private void ověřDokončenost()
    {
        if (tvorbaUkončena) {
            return;
        }
        Throwable ex = new Throwable();
        StackTraceElement[] aste = ex.getStackTrace();
        String metoda = aste[1].getMethodName();
        throw new IllegalStateException(
            "\nPNedokončený objekt nemůže provádět akce: " + metoda);
    }


    /***************************************************************************
     * Přidá do mnohotvaru zadaný prvek a příslušně upraví novou
     * pozici a velikost mnohotvaru.
     *
     * @param tvar  Přidávaný tvar
     */
    private void přidejTvar(ITvar tvar)
    {
        //tx, y, s, v = x, y, šířka výška přidávaného tvaru
        int tx = tvar.getX();
        int ty = tvar.getY();
        int ts = tvar.getŠířka();
        int tv = tvar.getVýška();

        if( seznam.isEmpty() )  //Přidávaný tvar je prvním tvarem v mnohotvaru
        {
            xPos  = tx;
            yPos  = ty;
            šířka = ts;
            výška = tv;
            seznam.add(new Část(tvar));
            return;                     //==========>
        }

        //Přídávaný tvar není prvním
        //Zapamatuji si původní parametry, aby je pak bylo možno porovnávat
        //s upravenými po zahrnutí nového tvaru
        int mx = xPos;
        int my = yPos;
        int ms = šířka;
        int mv = výška;
        boolean změna = false;

        if( tx < xPos )
        {   //Přidávaný tvar zasahuje víc vlevo
            šířka += getX() - tx;
            xPos   = tx;
            změna = true;
        }
        if( ty < yPos )
        {   //Přidávaný tvar zasahuje víc nahoru
            výška += yPos - ty;
            yPos   = ty;
            změna = true;
        }
        if( (xPos + šířka) < (tx + ts) )
        {   //Přidávaný tvar zasahuje víc vpravo
            šířka = tx + ts - xPos;
            změna = true;
        }
        if( (yPos + výška) < (ty + tv) )
        {   //Přidávaný tvar zasahuje víc dolů
            výška = ty + tv - yPos;
            změna = true;
        }
        //Nyní mají atributy xPos, yPos, šířka a výška hodnoty
        //odpovídající mnohotvaru zahrnujícímu přidaný tvar

        //Pokud se něco změnilo, musím přepočítat parametry všech součástí
        if (změna) {
            for( Část č : seznam ) {
                č.poPřidání( mx, my, ms, mv);
            }
        }
        seznam.add(new Část(tvar));
    }


    /***************************************************************************
     * Nastaví navý název svého tvaru. Nekontroluje jeho jedinečnost -
     * o tu se musí postarat volající metoda.
     */
    private void setNovýNázev(String název)
    {
        Class<?> cls = Mnohotvar.class;
        try {
            java.lang.reflect.Field fldNázev = cls.getDeclaredField("název");
            fldNázev.setAccessible(true);
            fldNázev.set(this, název);
        } catch(Exception ex) {
            throw new RuntimeException("\nReflexe má problémy", ex);
        }
    }



//== INTERNÍ DATOVÉ TYPY =======================================================

    /***************************************************************************
     * Třída slouží jako přepravka pro uchovávání pomocných informací
     * pro co nelepší změnu velikosti mnohotvaru.
     */
    private final class Část
    {
    //== KONSTANTNÍ ATRIBUTY TŘÍDY =============================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===============================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ====================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==========================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ============================================

        /** Odkaz na část mnohotvaru. */
        ITvar tvar;

        /** Podíl odstupu od levého kraje mnohotvaru na jeho celkové šířce. */
        double dx;

        /** Podíl odstupu od horního kraje mnohotvaru na jeho celkové výšce. */
        double dy;

        /** Podíl šířky části k celkové šířce mnohotvaru. */
        double dš;

        /** Podíl výšky části k celkové výšce mnohotvaru. */
        double dv;



    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ====================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY =======================================

    //##########################################################################
    //== KONSTRUKTORY A TOVÁRNÍ METODY =========================================

        /***********************************************************************
         * Vytvoří přepravku a zapamatuje si aktuální stav některých poměrů
         * vůči současné porobě mnohotvaru.
         */
        Část(ITvar tvar)
        {
            this.tvar = tvar;
            int tx = tvar.getX();
            int ty = tvar.getY();
            int ts = tvar.getŠířka();
            int tv = tvar.getVýška();
            dx = (tx - xPos) / (double)šířka;
            dy = (ty - yPos) / (double)výška;
            dš = ts          / (double)šířka;
            dv = tv          / (double)výška;
        }



    //== ABSTRAKTNÍ METODY =====================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =================================
    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ====================================

        /***********************************************************************
         * Aktiualizuje uchovávanou relativní pozici a rozměry dané součásti
         * v rámci celého mnohotvaru
         * po přidání nové součásti vedoucí ke změně rozměru mnohotvaru.
         *
         * @param sx  Původní (staré) x
         * @param sy  Původní (staré) y
         * @param sš  Původní (stará) šířka
         * @param sv  Původní (stará) výška
         */
        void poPřidání(int sx, int sy, int sš, int sv)
        {
            //Souřadnice se mohou pouze zmenšovat
            dx = (sx - xPos + dx*sš) / šířka;
            dy = (sy - yPos + dy*sv) / výška;

            dš = dš * sš / šířka;
            dv = dv * sv / výška;
        }


        /***********************************************************************
         * Aktiualizuje uchovávanou relativní pozici a rozměry dané součásti
         * v rámci celého mnohotvaru
         * po změně jeho veloikosti.
         *
         * @param šířka  Nastavovaná šířka celého mnohotvaru
         * @param výška  Nastavovaná výška celého mnohotvaru
         */
        void poNafouknutí(int šířka, int výška)
        {
            tvar.setPozice( (int)Math.round( Mnohotvar.this.xPos + dx*šířka ),
                            (int)Math.round( Mnohotvar.this.yPos + dy*výška ));
            tvar.setRozměr( (int)Math.round( šířka*dš ),
                            (int)Math.round( výška*dv ) );
        }


        /***********************************************************************
         * Vrátí textovou reprezentaci všech atributů.
         *
         * @return Textová reprezentace všech atributů
         */
        @Override
        public String toString()
        {
            return "tvar=" + tvar + "; dx=" + dx + "; dy=" + dy +
                   "; šířka=" + šířka + "; výška=" + výška;
        }



    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ====================================
    //== INTERNÍ DATOVÉ TYPY ===================================================
    //== TESTY A METODA MAIN ===================================================
    }



//== TESTOVACÍ METODY A TŘÍDY ==================================================

    /***************************************************************************
     * Interní třída určená k tomu, aby testovací metoda mohla spustit
     * posloupnost testů nad jedním přípravkem.
     * Její jedinou metodou je tedy metoda pro smazání všech mnohotvarů.
     * Třída by správně neměla být veřejná.
     * Umístování testů do stejného balíčku v testovacím stromu balíčků
     * je však probíráno až v pokračovacím kurzu, tak je vše definováno tak,
     * aby mohla být testovací metoda i v jiném balíčku.
     */
    public static class Test
    {
        /***********************************************************************
         * Odregistruje všechny zaregistrované mnohotvary a začíná vše znovu.
         */
        public static void odregistrujVše()
        {
            název2mnohotvar.clear();
        }
    }

}
