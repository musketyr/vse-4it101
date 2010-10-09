import java.lang.reflect.Method;

import org.duckapter.Duck;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.annotation.All;
import org.duckapter.annotation.Any;
import org.duckapter.annotation.Declared;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Matching;
import org.duckapter.annotation.Private;

import junit.framework.TestCase;


public class WrapperHelperTest extends TestCase {

    private static final String THE_METHOD = "theMethod";

    public static class ToBeTested {

        public String theMethod(){
            return THE_METHOD;
        }
    }
    
    public static interface IToBeTestedMethod {
        String call();
    }
    
    public static interface IToBeTested{
        @All @Declared @Matching(".*") IToBeTestedMethod[] methods();
    }
    
    public void testGetInvocationAdapter() throws Exception {
        assertTrue(Duck.test(new ToBeTested(), IToBeTested.class));
        IToBeTestedMethod[] methods = Duck.type(new ToBeTested(), IToBeTested.class).methods();
        assertEquals(1, methods.length);
        IToBeTestedMethod method = methods[0];
        Method theMethod = WrapperHelper.tryGetMethod(method);
        assertEquals(THE_METHOD, theMethod.getName());
    }
    
}
