import org.duckapter.annotation.All;
import org.duckapter.annotation.Declared;
import org.duckapter.annotation.Matching;


public interface HasSmoothTests {
    
    String SMOOTH_METHOD_REGEXP = "(test.*?)Plynule";
    
    @All @Declared @Matching(SMOOTH_METHOD_REGEXP) TestMethod[] allTests();
}
