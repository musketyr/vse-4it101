
/**
 * Třída {@code Tester} slouží k otestování řešení domácího úkolu.
 *
 * @author Vladimír Oraný
 */
public final class Tester
{

    /**
     * Jedná se o knihovní třídu, takže nemá smysl vytvářet instance
     * této třídy.
     */
    private Tester() {}
    
    /**
     * Otestuje třídu daného jména, zda spluňuju požadavky domácího úkolu.
     * @param název     název třídy domácího úkolu
     */
    public static void otestuj(String název)
    {
        try {
            new AssignmentsTester(
                    Defense1.class, 
                    Class.forName(název),
                    "obhaj01.score.xml"
            ).showReport();
        } catch (ClassNotFoundException e) {
            IO.zpráva("Třída " + název + " nebyla nalezena!");
        }
    }
    
    
}
