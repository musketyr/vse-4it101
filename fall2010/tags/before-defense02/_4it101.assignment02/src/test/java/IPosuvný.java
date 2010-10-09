

/*******************************************************************************
 * Instance rozhraní {@code IPosuvný} představují geometrické tvary,
 * které umějí prozradit a nastavit svoji pozici.
 *
 * @author    Rudolf PECINOVSKÝ
 * @version   0.00.000
 */
public interface IPosuvný
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance.
     *
     * @return  x-ová souřadnice.
     */
//     @Override
    public int getX();


    /***************************************************************************
     * Vrátí y-ovou (svislou) souřadnici pozice instance.
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



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
