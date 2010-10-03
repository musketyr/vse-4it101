


/*******************************************************************************
 * Instance rozhraní {@code INafukovací} představují geometrické tvary,
 * které umějí prozradit a nastavit svoje rozměry.
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public interface INafukovací extends IKreslený
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí šířku instance v bodech.
     *
     * @return  Aktuální šířka instance v bodech
     */
//     @Override
     public int getŠířka();


    /***************************************************************************
     * Vrátí výšku instance v bodech.
     *
     * @return  Aktuální výška instance v bodech
     */
//     @Override
     public int getVýška();


    /***************************************************************************
     * Nastaví nové rozměry instance. Nastavované rozměry musí být nezáporné,
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param šířka    Nově nastavovaná šířka; šířka >= 0
     * @param výška    Nově nastavovaná výška; výška >= 0
     */
//     @Override
    public void setRozměr(int šířka, int výška);



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
