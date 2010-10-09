

/*******************************************************************************
 * Instance rozhraní {@code IHýbací} představují geometrické tvary,
 * které umějí prozradit a nastavit svoji pozici a svoje rozměry.
 *
 * @author    Rudolf PECINOVSKÝ
 * @version   0.00.000
 */
public interface IHýbací
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================
//== ZDĚDĚNÉ METODY ============================================================

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



//== INTERNÍ DATOVÉ TYPY =======================================================
}
