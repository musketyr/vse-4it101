

public class Prověrka implements IO.ITester
{
    public Prověrka()
    {
        přihlaš();
    }
    
    
    /***********************************************************************
     * Oznání zavolání metody {@link IO.čekej(int)}
     * a předá v parametru zadanou dobu čekání.
     *
     * @param ms Zadaná doba čekání v milisekundách
     */
    public void čekej(int ms)
    {
        System.out.println("Čekej: " + ms);
    }
    
    
    /***********************************************************************
     * Oznání zavolání metody {@link IO.zpráva(Object)}
     * a předá v parametru vypisovaný text.
     *
     * @param zpráva Zobrazovaný text
     */
    public void zpráva(Object zpráva)
    {
        System.out.println("Zpráva: " + zpráva);
    }


    /***********************************************************************
     * Oznání zavolání metody {@link IO.čekej(int)}
     * a předá v parametru zadanou dobu čekání.
     *
     * @param ms Zadaná doba čekání v milisekundách
     */
    public final void přihlaš()
    {
        IO.zpravodaj.přihlaš(this);
    }


    /***********************************************************************
     * Oznání zavolání metody {@link IO.zpráva(Object)}
     * a předá v parametru vypisovaný text.
     *
     * @param zpráva Zobrazovaný text
     */
    public final void odhlaš()
    {
        IO.zpravodaj.odhlaš(this);
    }

}
