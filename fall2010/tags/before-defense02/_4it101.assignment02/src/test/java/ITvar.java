

/*******************************************************************************
 * Instance rozhraní {@code ITvar} představují obecné geometrické tvary
 * určené pro práci na virtuálním plátně při seznamování s třídami a objekty.
 *
 * @author    Rudolf PECINOVSKÝ
 * @version   0.00.000
 */
public interface ITvar extends Cloneable
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí x-ovou souřadnici pozice instance.
     *
     * @return  x-ová souřadnice.
     */
//     @Override
    public int getX();


    /***************************************************************************
     * Vrátí y-ovou souřadnici pozice instance.
     *
     * @return  y-ová souřadnice.
     */
//     @Override
    public int getY();


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x   Nová x-ová pozice instance
     * @param y   Nová y-ová pozice instance
     */
//     @Override
    public void setPozice(int x, int y);


    /***************************************************************************
     * Vrátí šířku instance.
     *
     * @return  Šířka instance v bodech
     */
//     @Override
     public int getŠířka();


    /***************************************************************************
     * Vrátí výšku instance.
     *
     * @return  Výška instance v bodech
     */
//     @Override
     public int getVýška();


    /***************************************************************************
     * Nastaví nové rozměry instance.
     *
     * @param šířka    Nově nastavovaná šířka; šířka>0
     * @param výška    Nově nastavovaná výška; výška>0
     */
//     @Override
    public void setRozměr(int šířka, int výška);


    /***************************************************************************
     * Vykreslí obraz své instance na plátno.
     */
//     @Override
    public void nakresli();


    /***************************************************************************
     * Smaže obraz své instance z plátna (nakreslí ji barvou pozadí plátna).
     */
//     @Override
    public void smaž();


    /***************************************************************************
     * Vrátí kopii daného tvaru, tj. stejný tvar, stejně velký, stejně umístěný
     * a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
//     @Override
    public ITvar kopie();



//== ZDĚDĚNÉ METODY ============================================================
//
//    /***************************************************************************
//     * Vrátí kopii daného tvaru, tj. stejný tvar, stejně velký, stejně umístěný
//     * a se stejnou barvou.
//     *
//     * @return Požadovaná kopie
//     */
//    public ITvar clone();
//
//== INTERNÍ DATOVÉ TYPY =======================================================
}
