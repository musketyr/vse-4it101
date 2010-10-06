

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



/*******************************************************************************
 * Třída <b><code>SprávcePlátna</code></b> slouží k jednoduchému kreslení
 * na virtuální plátno a připadné následné animaci nakreslených obrázků.
 * </p><p>
 * Třída neposkytuje veřejný konstruktor, protože chce, aby její instance
 * byla jedináček, tj. aby se všechno kreslilo na jedno a to samé plátno.
 * Jediným způsobem, jak získat odkaz na instanci třídy
 * <code>SprávcePlátna</code>,
 * je volaní její statické metody <code>getInstance()</code>.
 * </p><p>
 * Třída <code>SprávcePlátna</code> funguje jako manažer, který dohlíží
 * na to, aby se po změně zobrazení některého z tvarů všechny ostatní tvary
 * řádně překreslily, aby byly správně zachyceny všechny překryvy
 * a aby se při pohybu jednotlivé obrazce vzájemně neodmazavaly.
 * Aby vše správně fungovalo, je možno použít jeden dvou přístupů:</p>
 * <ul>
 *   <li>Manažer bude obsah plátna překreslovat
 *       <b>v pravidelných intervalech</b>
 *       bez ohledu na to, jestli se na něm udála nějaká změna či ne.
 *       <ul><li>
 *       <b>Výhodou</b> tohoto přístupu je, že se zobrazované objekty
 *       nemusí starat o to, aby se manažer dozvěděl, že se jejich stav změnil.
 *       </li><li>
 *       <b>Neýhodou</b> tohoto přístupu je naopak to, že manažer
 *       spotřebovává na neustálé překreslování jistou část výkonu
 *       procesoru, což může u pomalejších počítačů působit problémy.
 *       <br>&nbsp;</li></ul></li>
 *   <li>Manažer překresluje plátno <b>pouze na výslovné požádání</b>.
 *       <ul><li>
 *       <b>Výhodou</b> tohoto přístupu je úspora spotřebovaného výkonu
 *       počítače v období, kdy se na plátně nic neděje.
 *       </li><li>
 *       <b>Nevýhodou</b> tohoto přístupu je naopak to, že kreslené
 *       objekty musí na každou změnu svého stavu upozornit manažera,
 *       aby věděl, žed má plátno překreslit.
 *   </li>
 * </ul><p>
 * Třída <code>SprávcePlátna</code> požívá druhou z uvedených strategií,
 * tj. <b>překresluje plátno pouze na požádání</b>.
 * </p><p>
 * Obrazec, který chce být zobrazován na plátně, se musí nejprve přihlásit
 * u instance třídy <code>SprávcePlátna</code>, aby jej tato zařadila
 * mezi spravované obrazce (sada metod <code>přidej&hellip;</code>).
 * Přihlásit se však mohou pouze instance tříd, které implementují
 * rozhraní <code>IKreslený</code>.
 * </p><p>
 * Nepřihlášený obrazec nemá šanci býti zobrazen, protože na plátno
 * se může zobrazit pouze za pomoci kreslítka, jež může získat jedině od
 * instance třídy <code>SprávcePlátna</code>, ale ta je poskytuje pouze
 * instancím, které se přihlásily do její správy.
 * </p><p>
 * Obrazec, který již dále nemá byt kreslen, se muže odhlásit zavoláním
 * metody <code>odstraň(IKreslený)</code>.Zavoláním metody
 * <code>odstraňVše()</code> se ze seznamu spravovaných (a tím i z plátna)
 * odstraní všechny vykreslované obrazce.
 * </p><p>
 * Efektivitu vykreslování je možné ovlivnit voláním metody
 * <code>nekresli()</code>, která pozastaví překreslování plátna po nahlášených
 * změnách. Její volání je výhodné např. v situaci, kdy je třeba vykreslit
 * obrazec složený z řady menších obrazců a bylo by nevhodné překreslovat
 * plátno po vykreslení každého z nich.
 * </p><p>
 * Do původního stavu převedeme plátno voláním metody <code>vraťKresli()</code>,
 * která vrátí vykreslování do stavu před posledním voláním metody
 * <code>nekresli()</code>. Nemůžeč se tedy stát, že by se při zavolání metody
 * <code>nekresli()</code> v situaci, kdy je již vykreslování pozastaveno,
 * začalo po následém zavolání <code>vraťKresli()</code> hned vykreslovat.
 * Po dvou voláních <code>vraťKresli()</code> se začne vykreslovat až po
 * dvou zavoláních <code>vraťKresli()</code>.
 * </p><p>
 * Proto plátno pouze žádáme, aby se vrátilo do toho kreslícího stavu,
 * ve kterém bylo v okamžiku, kdy jsme je naposledy žádali o to,
 * aby se přestalo překreslovat. Nemůže se tedy stát, že by se při zavolání
 * metody <code>nekresli()</code> v situaci, kdy je již vykreslování
 * pozastaveno, začalo po následném zavolání <code>vraťKresli()</code> hned
 * vykreslovat.
 * </p><p>
 * Každé zavolání metody <code>nekresli()</code> musí být doplněno
 * odpovídajícím voláním <code>vraťKresli()</code>. Teprve když poslední
 * <code>vraťKresli()</code> odvolá první <code>nekresli()</code>, bude
 * překreslování opět obnoveno.
 * </p>
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public class SprávcePlátna
{
//    static { System.out.println("Inicializace třídy SprávcePlátna"); }
//    { System.out.println("Inicializace instance třídy SprávcePlátna"); }
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Titulek okna aktivního plátna. */
    private static final String TITULEK_0  = "Plátno ovládané správcem";

    /** Počáteční políčková šířka aktivní plochy plátna. */
    private static final int ŠÍŘKA_0 = 6;

    /** Počáteční políčková výška aktivní plochy plátna. */
    private static final int VÝŠKA_0 = 6;

    /** Počáteční barva pozadí plátna. */
    private static final Barva POZADÍ_0 = Barva.KRÉMOVÁ;

    /** Počáteční barva čar mřížky. */
    private static final Barva BARVA_ČAR_0 = Barva.ČERNÁ;

    /** Implicitní rozteč čtvercové sítě. */
    private static final int KROK_0 = 50;

    /** Maximální povolená velikost rozteče čtvercové sítě. */
    private static final int MAX_KROK = 200;

    /** Jediná instance třídy SprávcePlátna. */
    private static SprávcePlátna SP = new SprávcePlátna();

    //Při kreslení čar se ptá APosuvný po správci plánta. Proto se mohou čáry
    //kreslit až poté, co bude jednináček (konstanta SP) inicializován.
    static
    {
        //Připraví a vykreslí prázdné plátno
        SP.setKrokRozměr(KROK_0, ŠÍŘKA_0, VÝŠKA_0);
        //Nyní je znám rozměr plátna, tak můžeme umístit dialogová okna
        int x = SP.okno.getX();
        int y = SP.okno.getY() + SP.okno.getHeight();
        IO.oknaNa(x, y);
    }



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Aplikační okno animačního plátna. */
    private final JFrame okno;

    /** Instance lokální třídy, která je zřízena proto, aby odstínila
     *  metody svého rodiče JPanel. */
    private final JPanel plátno;

    /** Seznam zobrazovaných předmětů. */
    private final List<IKreslený> předměty = new ArrayList<IKreslený>();



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //Z venku neovlivnitelné Atributy pro zobrazeni plátna v aplikačním okně

        /** Vše se kreslí na obraz - ten se snadněji překreslí. */
        private Image obrazPlátna;

        /** Kreslítko získané od obrazu plátna, na nějž se vlastně kreslí. */
        private Kreslítko kreslítko;

        /** Semafor bránící příliš častému překreslování. Překresluje se pouze
         *  je-li ==0. Nesmi byt <0. */
        private int nekreslit = 0;

        /** Příznak toho, že kreslení právě probíhá,
         *  takže vypínání nefunguje. */
        private boolean kreslím = false;

        /** Čáry zobrazující na plántě mřížku. */
        private Čára[] vodorovná,   svislá;

    //Přímo ovlivnitelné atributy

        /** Rozteč čtvercové sítě. */
        private int krok = KROK_0;

        /** Zobrazuje-li se mřížka. */
        private boolean mřížka = true;

        /** Barva pozadí plátna. */
        private Barva barvaPozadí = POZADÍ_0;

        /** Barva čar mřížky. */
        private Barva barvaČar = BARVA_ČAR_0;

        /** Šířka aktivní plochy plátna v udávaná v polích. */
        private int sloupců = ŠÍŘKA_0;

        /** Výška aktivní plochy plátna v udávaná v polích. */
        private int řádků = VÝŠKA_0;

        /** Šířka aktivní plochy plátna v bodech. */
        private int šířkaBodů = ŠÍŘKA_0 * krok;

        /** Výška aktivní plochy plátna v bodech. */
        private int výškaBodů = VÝŠKA_0 * krok;

        /** Zda je možno měnit velikost kroku. */
        private Object vlastníkPovoleníZměnyKroku = null;

        /** Název v titulkové liště animačního plátna. */
        private String název  = TITULEK_0;

        /** Pozice plátna na obrazovace - při používání více obrazovek
         *  je občas třeba ji po zviditelnění obnovit. */
        Point pozice = new Point(0, 0);



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Jediná metoda umožnující získat odkaz na instanci plátna.
     * Vrací vsak pokaždé odkaz na stejnou instanci,
     * protože instance plátna je jedináček.
     * <p>
     * Pokud instance při volaní metody ještě neexistuje,
     * metoda instanci vytvoří.</p>
     *
     * @return Instance třídy SprávcePlátna
     */
    public static SprávcePlátna getInstance()
    {
        return SP;
    }


    /***************************************************************************
     * Vytvoří instanci třídy - je volaná pouze jednou.
     */
    @SuppressWarnings("serial")
    private SprávcePlátna()
    {
        okno  = new JFrame();          //Vytvoří nové aplikační okno
        okno.setTitle(název);
        konfiguraceZeSouboru();

        //Zavřením okna se zavře celá aplikace
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Vlastní plátno je definováno jako instance anonymní třídy
        plátno =
            new JPanel()
            {   /** Povinně překrývaná abstraktní metoda třídy JPanel. */
                @Override
                public synchronized void paintComponent(Graphics g) {
                    g.drawImage(obrazPlátna, 0, 0, null);
                }
            };//Konec definice třídy plátna
        okno.setContentPane(plátno);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Nastaví rozměr plátna zadáním bodové velikosti políčka a
     * počtu políček ve vodorovném a svislém směru.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param  krok    Nová bodová velikost políčka
     * @param  pŠířka  Nový počet políček vodorovně
     * @param  pVýška  Nový počet políček svisle
     */
    public synchronized void setKrokRozměr(int krok, int pŠířka, int pVýška)
    {
        setKrokRozměr(krok, pŠířka, pVýška, null);
    }


    /***************************************************************************
     * Nastaví rozměr plátna zadáním bodové velikosti políčka a
     * počtu políček ve vodorovném a svislém směru.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param  krok    Nová bodová velikost políčka
     * @param  pŠířka  Nový počet políček vodorovně
     * @param  pVýška  Nový počet políček svisle
     * @param  měnič   Objekt, který žádá o změnu rozměru. Jakmile je jednou
     *                 tento objekt nastaven, nesmí již rozměr plátna
     *                 měnit nikdo jiný.
     */
    private
    synchronized void setKrokRozměr(int krok, int pŠířka, int pVýška,
                                    Object měnič)
    {
        //Kontrola, jetli rozměry mění ten, kdo je měnit smí
        if ((měnič != null)  &&
            (měnič != vlastníkPovoleníZměnyKroku))
        {
            if (vlastníkPovoleníZměnyKroku == null) {
                vlastníkPovoleníZměnyKroku = měnič;
            } else {
                throw new IllegalStateException(
                    "Změna kroku a rozměru není danému objektu povolena");
            }
        }
        //Ověření korektnosti zadaných parametrů
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        if ((krok   < 1)  ||
            (pŠířka < 2)  ||  (obrazovka.width  < šířkaBodů) ||
            (pVýška < 2)  ||  (obrazovka.height < výškaBodů))
        {
            throw new IllegalArgumentException(
                "\nŠpatně zadané rozměry: " +
                "\n  krok =" + krok  + " bodů," +
                "\n  šířka=" + pŠířka + " polí = " + pŠířka*krok + " bodů," +
                "\n  výška=" + pVýška + " polí = " + pVýška*krok + " bodů," +
                "\n  obrazovka= " + obrazovka.width  + "×" +
                                    obrazovka.height + " bodů\n");
        }

        šířkaBodů = pŠířka * krok;
        výškaBodů = pVýška * krok;

        okno.setResizable(true);
        plátno.setPreferredSize(new Dimension(šířkaBodů, výškaBodů));
        okno.pack();
        okno.setResizable(false);

        obrazPlátna = plátno.createImage(šířkaBodů, výškaBodů);
        kreslítko   = new Kreslítko((Graphics2D)obrazPlátna.getGraphics());
        kreslítko.setPozadí(barvaPozadí);

        int starý    = this.krok;
        this.krok    = krok;
        this.sloupců = pŠířka;
        this.řádků   = pVýška;

        připravČáry();

        IO.Oprava.rozměrOkna(okno);
        IO.Oprava.poziceOkna(okno);
        setViditelné(true);
//        překresli();  //Překresluje již předchozí metoda
    }


    /***************************************************************************
     * Vrátí vzdálenost čar mřížky = bodovou velikost políčka.
     *
     * @return Bodová velikost políčka
     */
     public synchronized int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastaví vzdálenost čar mřížky = bodovou velikost políčka.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param velikost  Nová bodová velikost políčka
     */
    private
    synchronized void setKrok(int velikost)
    {
        setKrokRozměr(velikost, sloupců, řádků);
    }


    /***************************************************************************
     * Vrátí počet sloupců plátna, tj. jeho políčkovou šířku.
     *
     * @return  Aktuální políčková šířka plátna (počet políček vodorovně)
     */
    public synchronized int getSloupců()
    {
        return sloupců;
    }


    /***************************************************************************
     * Vrátí bodovou šířku plátna.
     *
     * @return  Aktuální bodová šířka plátna (počet bodů vodorovně)
     */
    private
    synchronized int getBšířka()
    {
        return šířkaBodů;
    }


    /***************************************************************************
     * Vrátí počet řádků plátna, tj. jeho políčkovou výšku.
     *
     * @return  Aktuální políčková výška plátna (počet políček svisle)
     */
    public synchronized int getŘádků()
    {
        return řádků;
    }


    /***************************************************************************
     * Vrátí bodovou výšku plátna.
     *
     * @return  Aktuální bodová výška plátna (počet bodů svisle)
     */
    private
    synchronized int getBVýška()
    {
        return výškaBodů;
    }


    /***************************************************************************
     * Nastaví rozměr plátna zadáním jeho políčkové výsky a šířky.
     *
     * @param  sloupců  Nový počet políček vodorovně
     * @param  řádků  Nový počet políček svisle
     */
    private
    synchronized void setRozměr(int sloupců, int řádků)
    {
        setKrokRozměr(krok, sloupců, řádků);
    }


    /***************************************************************************
     * Vrátí informaci o tom, je-li zobrazována mřížka.
     *
     * @return Mřížka je zobrazována = true, není zobrazována = false.
     */
    public synchronized boolean isMřížka()
    {
    	return mřížka;
    }


    /***************************************************************************
     * V závislosti na hodntě parametru nastaví nebo potlačí
     * zobrazování čar mřížky.
     *
     * @param zobrazit  Jestli mřížku zobrazovat.
     */
    public synchronized void setMřížka(boolean zobrazit)
    {
        mřížka = zobrazit;
        připravČáry();
        překresli();
    }


    /***************************************************************************
     * Poskytuje informaci o aktuální viditelnosti okna.
     *
     * @return Je-li okno viditelné, vrací <b>true</b>, jinak vrací <b>false</b>
     */
    private
    synchronized boolean isViditelné()
    {
        return okno.isVisible();
    }


    /***************************************************************************
     * V závislosti na hodntě parametru nastaví nebo potlačí viditelnost plátna.
     *
     * @param viditelné logická hodnota požadované viditelnost (true=viditelné)
     */
    private
    synchronized void setViditelné(boolean viditelné)
    {
        boolean změna = (isViditelné() != viditelné);
        if (změna)
        {
            pozice = okno.getLocation();
            okno.setVisible(viditelné);
            if (viditelné)
            {
                //Při více obrazovkách po zviáditelnění blbne =>
                okno.setLocation(pozice);   //je třeba znovu nastavit pozici
                okno.setAlwaysOnTop(true);
                okno.toFront();
                překresli();
                okno.setAlwaysOnTop(false);
            }
        }
    }


    /***************************************************************************
     * Vrátí aktuální barvu pozadí.
     *
     * @return  Nastavena barva pozadí
     */
    public synchronized Barva getBarvaPozadí()
    {
        return barvaPozadí;
    }


    /***************************************************************************
     * Nastaví pro plátno barvu pozadí.
     *
     * @param  barva  Nastavovaná barva pozadí
     */
    public synchronized void setBarvaPozadí(Barva barva)
    {
        barvaPozadí = barva;
        kreslítko.setPozadí(barvaPozadí);
        překresli();
    }


    /***************************************************************************
     * Pomocná metoda pro účely ladění aby bylo možno zkontrolovat,
     * ze na konci metody má semafor stejnou hodnotu, jako měl na počátku.
     *
     * @return  Stav vnitřního semaforu: >0  - nebude se kreslit,<br>
     *                                   ==0 - kreslí se,<br>
     *                                   <0  - chyba
     */
    private
    synchronized int getNekresli()
    {
        return nekreslit;
    }


    /***************************************************************************
     * Vrátí aktuální název v titulkové liště okna plátna.
     *
     * @return  Aktuální název okna
     */
    public String getNázev()
    {
        return okno.getTitle();
    }


    /***************************************************************************
     * Nastaví název v titulkové liště okna plátna.
     *
     * @param název  Nastavovaný název
     */
    public void setNázev(String název)
    {
        okno.setTitle(this.název = název);
    }


    /***************************************************************************
     * Nastaví pozici aplikačního okna aktivního plátna na obrazovce.
     *
     * @param x  Vodorovná souřadnice aplikačního okna plátna.
     * @param y  Svislá souřadnice aplikačního okna plátna.
     */
    public void setPozice(int x, int y)
    {
        okno.setLocation(x, y);
        pozice = new Point(x, y);
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
        return getClass().getName() + "(krok=" + krok +
                ", šířka=" + sloupců + ", výška=" + řádků +
                ", pozadí=" + barvaPozadí + ")";
    }


    /***************************************************************************
     * Vykreslí všechny elementy.
     */
    public synchronized void překresli()
    {
        if (kreslím) {    //Právě překresluji - volám nepřímo sám sebe
            return;
        }
        if ((nekreslit == 0)  &&  isViditelné())   //Mám kreslit a je proč
        {
            kreslím = true;
            synchronized(plátno)
            {
                kreslítko.vyplňRám(0, 0, šířkaBodů, výškaBodů,
                                    barvaPozadí);
                if (mřížka  &&  (barvaČar != barvaPozadí))
                {
                    //Budeme kreslit mřížku -- bude pod obrazci
                    for (int i=0;   i < sloupců;  ) {
                        svislá[i++].nakresli(kreslítko);
                    }
                    for (int i=0;   i < řádků;  ) {
                        vodorovná[i++].nakresli(kreslítko);
                    }
                }
                for (IKreslený předmět : předměty) {
                    předmět.nakresli(kreslítko);
                }
            }//synchronized(obrazPlátna)
            plátno.repaint();       //Překreslí se aktualizované plátno
            kreslím = false;        //Už nekreslím
        }
    }


    /***************************************************************************
     * Potlačí překreslování plátna, přesněji zvýší hladinu potlačení
     * překreslování o jedničku. Návratu do stavu před voláním této metody
     * se dosáhne zavoláním metody <code>vraťKresli()</code>.</p>
     * <p>
     * Metody <code>nekresli()</code> a <code>vraťKresli()</code>
     * se tak chovají obdobně jako závorky, mezi nimiž je vykreslování
     * potlačeno.</p>
     */
    public synchronized void nekresli()
    {
        nekreslit++;
    }


    /***************************************************************************
     * Vrátí překreslování do stavu před posledním voláním metody
     * <code>nekresli()</code>. Předcházelo-li proto více volání metody
     * <code>nekresli()</code>, začne se překreslovat až po odpovídajím počtu
     * zavolání metody <code>vraťKresli()</code>.
     *
     * @throws IllegalStateException
     *         Je-li metoda volána aniž by předcházelo odpovídající volání
     *         <code>nekresli()</code>.
     */
    public synchronized void vraťKresli()
    {
        if (nekreslit == 0) {
            throw new IllegalStateException(
                "Vrácení do stavu kreslení musí přecházet zákaz!");
        }
        nekreslit--;
        if (nekreslit == 0)  {
            překresli();
        }
    }


    /***************************************************************************
     * Odstraní zadaný obrazec ze seznamu malovaných.
     * Byl-li obrazec v seznamu, překreslí plátno.
     *
     * @param obrazec  Odstraňovaný obrazec
     *
     * @return  true v případě, když obrazec v seznamu byl,
     *          false v případě, když nebylo co odstraňovat
     */
    public synchronized boolean odstraň(IKreslený obrazec)
    {
        boolean ret = předměty.remove(obrazec);
        if (ret) {
            překresli();
        }
        return ret;
    }


    /***************************************************************************
     * Vyčisti plátno, tj. vyprázdní seznam malovaných
     * (odstraní z něj všechny obrazce).
     */
    public synchronized void odstraňVše()
    {
        nekresli(); {
            ListIterator<IKreslený> it = předměty.listIterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        } vraťKresli();
    }


    /***************************************************************************
     * Není-li zadaný obrazec v seznamu malovaných, přidá jej na konec
     * (bude se kreslit jako poslední, tj. na vrchu.
     * Byl-li obrazec opravdu přidán, překreslí plátno.
     * Objekty budou vždy kresleny v pořadí, v němž byly přidány do správy,
     * tj. v seznamu parametrů zleva doprava
     * a dříve zaregistrované objekty před objekty zaregistrovanými později.
     *
     * @param  obrazec  Přidávané obrazce
     * @return  Počet skutečně přidaných obrazců
     */
    public synchronized int přidej(IKreslený... obrazec)
    {
        int počet = 0;
        nekresli(); {
            for (IKreslený ik : obrazec)
            {
                if (! předměty.contains(ik)) {
                    předměty.add(ik);
                    počet++;
                }
            }
        } vraťKresli();
        return počet;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * nad zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param  současný  Obrazec, který má byt při kreslení pod
     *                    přidávaným obrazcem
     * @param  přidaný   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    public synchronized boolean přidejNad(IKreslený současný, IKreslený přidaný)
    {
        boolean nebyl = ! předměty.remove(přidaný);
        int kam = předměty.indexOf(současný);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referenční objekt není na plátně zobrazován!");
        }
        předměty.add(kam+1, přidaný);
        překresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * pod zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param  současný  Obrazec, který má byt při kreslení nad
     *                   přidávaným obrazcem
     * @param  přidaný   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    public synchronized boolean přidejPod(IKreslený současný, IKreslený přidaný)
    {
        boolean nebyl = ! předměty.remove(přidaný);
        int kam = předměty.indexOf(současný);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referenční objekt není na plátně zobrazován!");
        }
        předměty.add(kam, přidaný);
        překresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * nad všemi obrazci.
     * Pokud již v seznamu byl, jenom jej přesune do požadované pozice.
     *
     * @param  přidaný   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    private
    synchronized boolean přidejNavrch(IKreslený přidaný)
    {
        boolean nebyl = ! předměty.remove(přidaný);
        předměty.add(přidaný);
        překresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * pod zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param  přidaný   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    private
    synchronized boolean přidejDospod(IKreslený přidaný)
    {
        boolean nebyl = ! předměty.remove(přidaný);
        předměty.add(0, přidaný);
        překresli();
        return nebyl;
    }


    /***************************************************************************
     * Vrátí pořadí zadaného prvku v seznamu kreslených prvků.
     * Prvky se přitom kreslí v rostoucím pořadí, takže obrazec
     * s větším poradím je kreslen nad obrazcem s menším poradím.
     * Neni-li zadaný obrazec mezi kreslenými, vrátí -1.
     *
     * @param  obrazec  Objekt, na jehož kreslicí pořadí se dotazujeme
     *
     * @return  Pořadí obrazce; prvý kresleny obrazec má pořadí 0.
     *          Neni-li zadaný obrazec mezi kreslenými, vrátí -1.
     */
    private
    synchronized int pořadí(IKreslený obrazec)
    {
        return předměty.indexOf(obrazec);
    }


    /***************************************************************************
     * Vrátí nemodifikovatelný seznam všech spravovaných obrázků.
     *
     * @return  Požadovaný seznam
     */
    private
    List<IKreslený> seznamKreslených()
    {
        return Collections.unmodifiableList(předměty);
    }


    /***************************************************************************
     * Přihlásí posluchače událostí klávesnice.
     *
     * @param posluchač  Přihlašovaný posluchač
     */
    private
    void přihlašKlávesnici(KeyListener posluchač)
    {
        okno.addKeyListener(posluchač);
    }


    /***************************************************************************
     * Odhlásí posluchače klávesnice.
     *
     * @param posluchač  Odhlašovaný posluchač
     */
    private
    void odhlašKlávesnici(KeyListener posluchač)
    {
        okno.removeKeyListener(posluchač);
    }


    /***************************************************************************
     * Přihlásí posluchače událostí myši.
     *
     * @param posluchač  Přihlašovaný posluchač
     */
    private
    void přihlašMyš(MouseListener posluchač)
    {
        okno.addMouseListener(posluchač);
    }


    /***************************************************************************
     * Odhlásí posluchače myši.
     *
     * @param posluchač  Odhlašovaný posluchač
     */
    private
    void odhlašMyš(MouseListener posluchač)
    {
        okno.removeMouseListener(posluchač);
    }


    /***************************************************************************
     * Uloží obraz aktivního plátna do zadaného souboru.
     *
     * @param soubor Soubor, do nějž se má obraz plátna uložit
     */
    private
    void uložJakoObrázek(File soubor)
    {
        BufferedImage bim = getBufferedImage();
        try {
            ImageIO.write(bim, "PNG", soubor);
        } catch(IOException exc)  {
            throw new RuntimeException(
            	"\nObraz aktivního plátna se nepodařilo uložit do souboru " +
                soubor,  exc);
        }
    }


//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí obrázek na aktivním plátně.
     * @return Obsah plátna jako obrázek
     */
    private BufferedImage getBufferedImage()
    {
        if (obrazPlátna instanceof BufferedImage) {
            return (BufferedImage) obrazPlátna;         //==========>
        }
        else {
            return getBufferedImage(0, 0, šířkaBodů, výškaBodů);
        }
    }


    /***************************************************************************
     * Vrátí obrázek výřezu na aktivním plátně.
     * @return Výřez obsahu plátna jako obrázek
     */
    private BufferedImage getBufferedImage(int x, int y, int šířka, int výška)
    {
        BufferedImage ret = new BufferedImage(šířka, výška,
                                               BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)ret.getGraphics();
        g2d.drawImage(obrazPlátna, -x, -y, Kreslítko.OBRAZOR);
        return ret;
    }


    /***************************************************************************
     * Inicializuje některé parametry z konfiguračního souboru.
     * Tento soubor je umístěn v domovském adresáři uživatele
     * ve složce {@code .rup} v souboru {@code bluej.properties}.
     */
    private void konfiguraceZeSouboru() {
        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File(userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
            String sx = rupProp.getProperty("canvas.x");
            String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            pozice = new Point(x, y);
        }catch(Exception e)  {
            pozice = new Point(0, 0);
        }
        okno.setLocation(pozice);
    }


    /***************************************************************************
     * Připraví čáry vyznačující jednotlivá pole aktivního plátna.
     * Pokud se čáry kreslit nemají, vyprázdní odkazy na ně.
     */
    private void připravČáry()
    {
        if (mřížka  &&  (krok > 1))
        {
            if ((svislá == null)  ||  (svislá.length != sloupců)) {
                svislá = new Čára[sloupců];
            }
            if ((vodorovná == null)  ||  (vodorovná.length != řádků)) {
                vodorovná = new Čára[řádků];
            }
            for (int i=0, x=krok;   i < sloupců;      i++, x+=krok) {
                svislá[i] = new Čára(x, 0, x, výškaBodů, barvaČar);
            }
            for (int i=0, y=krok;   i < řádků;   i++, y+=krok) {
                vodorovná[i] = new Čára(0, y, šířkaBodů, y, barvaČar);
            }
        }
        else
        {
            //Uvolnění doposud používaných instancí
            svislá    = null;
            vodorovná = null;
            mřížka    = false;
        }
    }



//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================
//== TESTY A METODA MAIN =======================================================
}
