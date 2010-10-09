import org.duckapter.annotation.Alias;


public interface TestMovement1 extends TestMovement{
    @Alias("testPosuny") void test() throws Exception;
}
