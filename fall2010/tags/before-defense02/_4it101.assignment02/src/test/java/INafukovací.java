

/*******************************************************************************
 * Instance rozhraní {@code INafukovací} představují geometrické tvary,
 * které umějí prozradit a nastavit svoje rozměry.
 *
 * @author    Rudolf PECINOVSKÝ
 * @version   0.00.000
 */
public interface INafukovací
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

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



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
