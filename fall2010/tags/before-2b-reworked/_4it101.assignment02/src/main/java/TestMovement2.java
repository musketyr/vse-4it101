import org.duckapter.annotation.Alias;


public interface TestMovement2 extends TestMovement{
    @Alias("testPosuny2") void test() throws Exception;
}
