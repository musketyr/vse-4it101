
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;

import java.awt.geom.Rectangle2D;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;


/*******************************************************************************
 * Třída {@code Plátno} slouží k jednoduchému kreslení na virtuální plátno.
 *
 * <p>
 * Třída neposkytuje veřejný konstruktor, protože chce, aby její instance
 * byla jedináček, tj. aby se všechno kreslilo na jedno a to samé plátno.
 * Jediným způsobem, jak získat odkaz na instanci třídy Plátno,
 * je volaní statické metody {@link getPlátno()}.</p>.
 *
 * <p>
 * Aby bylo možno na plátno obyčejné kreslit a nebylo nutno kreslené objekty
 * přihlašovat, odmazané časti obrazců se automaticky neobnovují.
 * Je-li proto při smazání některého obrazce odmazána část jiného obrazce,
 * je třeba příslušný obrazec explicitně překreslit.</p>
 *
 * @author   Rudolf PECINOVSKÝ
 * @version  3.00.002
 */
public final class Plátno
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Titulek v záhlaví okna plátna. */
    private static final String TITULEK  = "Jednoduché plátno";

    /** Počáteční šířka plátna v bodech. */
    private static final int ŠÍŘKA_0 = 300;

    /** Počáteční výška plátna v bodech. */
    private static final int VÝŠKA_0 = 300;

    /** Počáteční barva pozadí plátna. */
    private static final Barva POZADÍ_0 = Barva.KRÉMOVÁ;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Jediná instance třídy Plátno. */
    private static Plátno jedináček = new Plátno();     //Jediná instance



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //Z venku neovlivnitelné Atributy pro zobrazeni plátna v aplikačním okně

        /** Aplikační okno animacniho plátna. */
        private JFrame okno;

        /** Instance lokální třídy, která je zřízena proto, aby odstínila
         *  metody svého rodiče JPanel. */
        private JPanel vlastníPlátno;

        /** Vše se kreslí na obraz - ten se snadněji překreslí. */
        private Image obrazPlátna;

        /** Kreslítko získané od obrazu plátna, na nějž se vlastně kreslí. */
        private Graphics2D kreslítko;


    //Přímo ovlivnitelné atributy
        private Barva barvaPozadí;
        private int šířka;
        private int výška;

        /** Pozice plátna na obrazovace - při používání více obrazovek
         *  je občas třeba ji po zviditelnění obnovit. */
        Point pozice = new Point(0, 0);



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Smaže plátno, přesněji smaže všechny obrazce na plátně.
     * Tato metoda by měla býr definována jako metodoa instance,
     * protože je instance jedináček,
     * byla metoda pro snazší dostupnost definovaná jako metoda třídy.
     * Jinak by totiž bylo potřeba vytvořit před smazáním plátna jeho instanci.
     */
    public static void smažPlátno()
    {
        jedináček.smaž();
    }



//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Jediná metoda umožnující získat odkaz na instanci plátna.
     * Vrací vsak pokaždé odkaz na stejnou instanci, protože tato instance
     * je jedináček. Pokud instance při volaní metody ještě neexistuje,
     * metoda instanci vytvoří.
     *
     * @return Odkaz na instanci třídy Plátno.
     */
    public static Plátno getPlátno()
    {
        jedináček.setViditelné(true);
        return jedináček;
    }


    /***************************************************************************
     * Implicitní (a jediný) konstruktor - je volán pouze jednou.
     */
    @SuppressWarnings("serial")
    private Plátno()
    {
        konfiguraceZeSouboru();
        okno  = new JFrame();          //Vytvoří nové aplikační okno
        okno.setLocation( pozice );
        okno.setTitle(TITULEK);

        //Zavřením okna se zavře celá aplikace
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        okno.setResizable( false );    //Není možné měnit rozměr myši
        vlastníPlátno = new JPanel()
        {   /** Povinně překrývaná abstraktní metoda třídy JPanel. */
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(obrazPlátna, 0, 0, null);
            }
        };
        okno.setContentPane(vlastníPlátno);
        barvaPozadí = POZADÍ_0;
        setRozměrSoukr(ŠÍŘKA_0, VÝŠKA_0);  //Připraví a vykreslí prázdné plátno
        připravObrázek();
        smaž();

        IO.oknaNa(pozice.x, pozice.y + okno.getSize().height);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Poskytuje informaci o aktuální viditelnosti okna.
     *
     * @return Je-li okno viditelné, vrací <b>true</b>, jinak vrací <b>false</b>
     */
    public boolean isViditelné()
    {
        return okno.isVisible();
    }


    /***************************************************************************
     * Nastaví viditelnost plátna.
     *
     * @param viditelné {@code true} má-li být plátno viditelné,
     *                  {@code false} má-li naopak přestat být viditelné
     */
    public void setViditelné(boolean viditelné)
    {
        boolean změna = (isViditelné() != viditelné);
        if( změna ) {
            pozice = okno.getLocation();
            okno.setVisible(viditelné);
            if( viditelné )
            {
                //Při více obrazovkách po zviáditelnění blbne =>
                okno.setLocation(pozice);   //je třeba znovu nastavit pozici
                okno.setAlwaysOnTop(true);
                okno.toFront();
                okno.setAlwaysOnTop(false);
            }
        }
    }


    /***************************************************************************
     * Vrátí aktuální barvu pozadí.
     *
     * @return   Nastavena barva pozadí
     */
    public Barva getBarvaPozadí()
    {
        return barvaPozadí;
    }


    /***************************************************************************
     * Nastaví pro plátno barvu pozadí.
     *
     * @param barva  Nastavovaná barva pozadí
     */
    public void setBarvaPozadí(Barva barva)
    {
        barvaPozadí = barva;
        kreslítko.setBackground( barvaPozadí.getColor() );
        smaž();
    }


    /***************************************************************************
     * Nastaví pro plátno barvu popředí.
     *
     * @param  barva  Nastavovaná barva popředí
     */
    public void setBarvaPopředí(Barva barva)
    {
        kreslítko.setColor( barva.getColor() );
    }


    /***************************************************************************
     * Vrátí šířku plátna.
     *
     * @return  Aktuální šířka plátna v bodech
     */
    public int getŠířka()
    {
        return šířka;
    }


    /***************************************************************************
     * Vrátí výšku plátna.
     *
     * @return  Aktuální výška plátna v bodech
     */
    public int getVýška()
    {
        return výška;
    }


    /***************************************************************************
     * Nastaví nový rozměr plátna zadáním jeho výsky a šířky.
     *
     * @param  šířka  Nova šířka plátna v bodech
     * @param  výška  Nová výška plátna v bodech
     */
    public void setRozměr(int šířka, int výška)
    {
        setRozměrSoukr(šířka, výška);
//        obrazPlátna = vlastníPlátno.createImage(šířka+2, výška+2);
//        kreslítko = (Graphics2D)obrazPlátna.getGraphics();
//        kreslítko.setBackground( barvaPozadí.getColor() );
        setViditelné(true);
        připravObrázek();
        smaž();
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
        return this.getClass().getName() +
            "(" + šířka + "×" + výška +
            " bodů, barvaPozadí=" + barvaPozadí + ")";
    }


    /***************************************************************************
     * Nakreslí zadaný obrazec a vybarví jej barvou popředí plátna.
     *
     * @param  obrazec  Kreslený obrazec
     */
    public void zaplň(Shape obrazec)
    {
        kreslítko.fill(obrazec);
        vlastníPlátno.repaint();
    }


    /***************************************************************************
     * Smaže plátno, přesněji smaže všechny obrazce na plátně.
     */
    public void smaž()
    {
        smaž( new Rectangle2D.Double(0, 0, šířka, výška) );
    }


    /***************************************************************************
     * Smaže zadaný obrazec na plátně; obrazec vsak stalé existuje,
     * jenom není vidět. Smaže se totiž tak, že se nakreslí barvou pozadí.
     *
     * @param  obrazec   Obrazec, který má byt smazán
     */
    public void smaž(Shape obrazec)
    {
        Color original = kreslítko.getColor();
        kreslítko.setColor(barvaPozadí.getColor());
        kreslítko.fill(obrazec);       //Smaže jej vyplněním barvou pozadí
        kreslítko.setColor(original);
        vlastníPlátno.repaint();
    }


    /***************************************************************************
     * Vypíše na plátno text aktuálním písmem a aktuální barvou popředí.
     *
     * @param text   Zobrazovaný text
     * @param x      x-ová souřadnice textu
     * @param y      y-ová souřadnice textu
     * @param barva  Barva, kterou se zadaný text vypíše
     */
    public void kresliString(String text, int x, int y, Barva barva)
    {
        setBarvaPopředí(barva);
        kreslítko.drawString(text, x, y);
        vlastníPlátno.repaint();
    }


    /***************************************************************************
     * Nakresli na plátno úsečku se zadanými krajními body.
     * Usedku vykreslí aktuální barvou popředí.
     *
     * @param  x1    x-ová souřadnice počátku
     * @param  y1    y-ová souřadnice počátku
     * @param  x2    x-ová souřadnice konce
     * @param  y2    x-ová souřadnice konce
     * @param  barva Barva úsečky
     */
    public void kresliČáru(int x1, int y1, int x2, int y2, Barva barva)
    {
        setBarvaPopředí(barva);
        kreslítko.drawLine(x1, y1, x2, y2);
        vlastníPlátno.repaint();
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Inicializuje některé parametry z konfiguračního souboru.
     * Tento soubor je umístěn v domovském adresáři uživatele
     * ve složce {@code .rup} v souboru {@code bluej.properties}.
     */
    private void konfiguraceZeSouboru() {
        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File( userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
            String sx = rupProp.getProperty("canvas.x");
            String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            pozice = new Point( x, y );
        }catch( Exception e )  {
            pozice = new Point( 0, 0 );
        }
    }


    /***************************************************************************
     * Připraví obrázek, do nějž se budou všechny tvary kreslit.
     */
    private void připravObrázek()
    {
        obrazPlátna = vlastníPlátno.createImage(šířka, výška);
        kreslítko = (Graphics2D)obrazPlátna.getGraphics();
        kreslítko.setColor(barvaPozadí.getColor());
        kreslítko.fillRect(0, 0, šířka, výška);
        kreslítko.setColor(Color.black);
    }


    /***************************************************************************
     * Nastaví zadaný rozměr plátna, ale pouze ten.
     * Soufkorá verze určená pro konstruktor.
     * Veřejná verze přidává ještě zviditelnění plátna a přípravu obrázku.
     *
     * @param šířka  Nastavovaná bodová šířka plátna
     * @param výška  Nastavovaná bodová výška plátna
     */
    private void setRozměrSoukr(int šířka, int výška)
    {
        boolean upravit;
        do {
            this.šířka = šířka;
            this.výška = výška;
            okno.setResizable(true);
            vlastníPlátno.setPreferredSize(new Dimension(šířka, výška));
//            okno.setMaximizedBounds(new Rectangle(šířka, výška));
            okno.pack();
            java.awt.Dimension dim = okno.getSize(null);
            java.awt.Insets    ins = okno.getInsets();
//            IO.zpráva(
//                   "Nastavuju: šířka=" + šířka + ", výška=" + výška +
//                 "\nMám: width=" + dim.width + ", height=" + dim.height +
//                 "\nleft=" + ins.left + ", right=" + ins.right +
//                 "\n top=" + ins.top + ", bottom=" + ins.bottom );
            upravit = false;
            if (šířka < (dim.width - ins.left - ins.right)) {
                šířka  = dim.width - ins.left - ins.right + 2;
                upravit= true;
            }
            if (výška < (dim.height - ins.top - ins.bottom)) {
                výška  = dim.height - ins.top - ins.bottom;
                upravit= true;
            }
        } while (upravit);
        okno.setResizable(false);    //Není možné měnit rozměr myši
    }



//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================
//== TESTOVACÍ TŘÍDY A METODY ==================================================
////+ main
//    /***************************************************************************
//     * Testing method.
//     */
//    public static void test()
//    {
////        Plátno instance =
//                Plátno.getPlátno();
//        IO.zpráva("Hotovo");
//    }
//    /** @param args Command line arguments - not used. */
//    public static void main( String[] args )  {  test();  }
////- main
}
