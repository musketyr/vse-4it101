

/*******************************************************************************
 * Rozhraní {@code IKreslený} musí implementovat všechny třídy, které chtějí,
 * aby jejich instance byly přijaty do správy instance {@link SprávcePlátna},
 * která vyžaduje, aby se na požádání uměly nakreslit prostřednictvím
 * dodaného kreslítka.
 * <p>
 * Implementací tohoto rozhraní třída současně slibuje, že bude správce plátna
 * okamžitě informovat o jakýchkoliv změnách ve svém umístění a vzhledu,
 * které che promítnout do své podoby na plántě. O tom, že se něco změnilo
 * informuje správce plátna zavoláním jeho metody
 * {@link SprávcePlátna#překresli()}
 *
 * @author Rudolf PECINOVSKÝ
 * @version 6.00 - 2010-07-10
 */
public interface IKreslený
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslítko Kreslítko, které nakreslí instanci
     */
//     @Override
    public void nakresli(Kreslítko kreslítko);


//== ZDĚDĚNÉ METODY ============================================================
//== VNOŘENÉ TŘÍDY =============================================================
}
