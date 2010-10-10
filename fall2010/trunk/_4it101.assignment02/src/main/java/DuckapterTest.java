import org.duckapter.Duck;

import eu.ebdit.eau.Porter;

import junit.framework.TestCase;

public abstract class DuckapterTest extends TestCase {

    private Class<?> testClass;
    private Object test;

    public DuckapterTest() {
        super();
    }

    public DuckapterTest(String name) {
        super(name);
    }

    protected <T> T as(Class<T> duckInterface) {
        return Duck.type(test, duckInterface);
    }

    protected void is(Class<?> duckIterface) {
        assertTrue(Duck.test(test, duckIterface));
    }

    @Override
    protected void tearDown() throws Exception {
        testClass = null;
        test = null;
    }

    @Override
    protected void setUp() throws Exception {
        String testClassName = (String) Porter.leasePorter("bluej")
                .inspectBurden("testedClass");
        if (testClassName == null) {
            testClassName = "XORAV00_Orany3Test";
        }
        testClass = Class.forName(testClassName);
        test = Duck.type(testClass, HasStringContructor.class).newInstance(
                "Test");
    }
    
    public Object getTest() {
        return test;
    }
    
    public Class<?> getTestClass() {
        return testClass;
    }
    

}