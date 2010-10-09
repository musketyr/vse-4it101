import static org.duckapter.Duck.test;

import java.util.Arrays;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.duckapter.Duck;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import eu.ebdit.eau.Porter;


public class Assignment2b extends TestCase implements IO.ITester{

    public static final Pattern XNAME_VSE_PTRN = Pattern
    .compile("(X[A-Z_]{4}\\d\\d\\d?)_[A-Z][a-z]*_?\\d?(Test)?");
    
    private static final int MIN_WAITS = 30;
    
    private Class<?> testClass;
    private Object test;
    
    private int waitAndMoveCount = 0;
    private int messageCount = 0;
    
    private Shape[] initialShapes = emptyFixtures();


    private Shape[] emptyFixtures() {
        return new Shape[0];
    }
    
    public void testName() throws Exception {
        assertTrue(XNAME_VSE_PTRN.matcher(this.testClass.getName()).matches());
    }

    public void testStringConstructor() throws Exception {
       assertTrue(test(testClass, HasStringContructor.class));
    }

    public void testInitBySetUp() throws Exception {
        beforeShapeChange();
        for (SomeFixture fix : as(HasFixtures.class).allFixture()) {
            assertNull(fix.fixture());
        }
        as(BasicTestCase.class).setUp();
        for (SomeFixture fix : as(HasFixtures.class).allFixture()) {
            assertNotNull(fix.fixture());
        }
    }

    public void testFixturesAreShapes() throws Exception {
        is(HasShapeFixtures.class);
        assertTrue(as(HasShapeFixtures.class).allFixture().length >= 5);
    }

    public void testSetUpUsesMessage() throws Exception {
        messageCount = 0;
        as(BasicTestCase.class).setUp();
        assertTrue(messageCount > 0);
        as(BasicTestCase.class).tearDown();
    }

    public void testTestMethodsCount() throws Exception {
        assertTrue(4 <= as(HasTests.class).allTests().length);
    }
    
    
    public void testHasMovement1() throws Exception {
        is(TestMovement1.class);
    }
    
    public void testHasMovement2() throws Exception {
        is(TestMovement2.class);
    }
    
    public void testHasSmoothMovement2() throws Exception {
        is(TestSmoothMovement2.class);
    }
    
    public void testHasSmoothMovement1() throws Exception {
        is(TestSmoothMovement1.class);
    }
    
    public void testMovement1Moves() throws Exception {
        doTestMoved(TestMovement1.class, false);
    }
    
    public void testMovement2Moves() throws Exception {
        doTestMoved(TestMovement2.class, false);
    }
    
    public void testSmoothMovement1Moves() throws Exception {
        doTestMoved(TestSmoothMovement1.class, false);
    }
    
    public void testSmoothMovement2Moves() throws Exception {
        doTestMoved(TestSmoothMovement2.class, false);
    }
    
    public void testSmoothEndsSameWay1() throws Exception {
        assertSameEnd(TestMovement1.class, TestSmoothMovement1.class);
    }
    
    public void testSmoothEndsSameWay2() throws Exception {
        assertSameEnd(TestMovement2.class, TestSmoothMovement2.class);
    }

    private void assertSameEnd(Class<? extends TestMovement> normalInterface,
            Class<? extends TestMovement> smoothInteface) throws Exception {
        as(BasicTestCase.class).setUp();
        as(normalInterface).test();
        Shape[] afterNormal = extractShapes();
        as(BasicTestCase.class).tearDown();
        as(BasicTestCase.class).setUp();
        as(smoothInteface).test();
        assertTrue(Shape.Helper.equals(afterNormal, extractShapes()));
        as(BasicTestCase.class).tearDown();
    }


    private void doTestMoved(Class<? extends TestMovement> duckInterface, boolean smooth)
            throws Exception {
        as(BasicTestCase.class).setUp();
        beforeShapeChange();
        as(duckInterface).test();
        assertMoved(smooth);
        as(BasicTestCase.class).tearDown();
    }
    
    
    private void assertMoved(boolean smooth){
        assertTrue(moved(smooth));
    }
    
    private boolean moved(boolean smooth) {
        if(!Shape.Helper.equals(initialShapes, extractShapes())){
            if (smooth) {
                return waitAndMoveCount > MIN_WAITS;
            }
            return true;
        }
        if (smooth) {
            return waitAndMoveCount > MIN_WAITS;
        }
        return waitAndMoveCount > 0;
    }
    
    

    private void beforeShapeChange() {
        waitAndMoveCount = 0;
        messageCount = 0;
        initialShapes = extractShapes();
    }


    private Shape[] extractShapes() {
        return Lists.transform(Arrays.asList(as(HasShapeFixtures.class).allFixture()), new Function<ShapeFixture, Shape>() {
        
              @Override
            public Shape apply(ShapeFixture from) {
                if (from == null) {
                    return null;
                }
                return Shape.Helper.copy(from.fixture());
            }
        
        }).toArray(new Shape[0]);
    }

    @Override
    protected void setUp() throws Exception {
        String testClassName = (String) Porter.leasePorter("bluej").inspectBurden("testedClass");
        if (testClassName == null) {
            testClassName = "XORAV00_Orany3Test";
        }
        testClass = Class.forName(testClassName);
        test = Duck.type(testClass, HasStringContructor.class).newInstance("Test");
        IO.zpravodaj.přihlaš(this);
    }
    

    @Override
    protected void tearDown() throws Exception {
        testClass = null;
        test = null;
        initialShapes = emptyFixtures();
        messageCount = 0;
        waitAndMoveCount = 0;
        IO.zpravodaj.odhlaš(this);
        Duck.type(SprávcePlátna.getInstance(), CanvasManager.class).setVisible(false);
    }
    
    private <T> T as(Class<T> duckInterface){
        return Duck.type(test, duckInterface);
    }
    
    private void is(Class<?> duckIterface){
        assertTrue(Duck.test(test, duckIterface));
    }

    public void čekej(int ms) {
        if (!Shape.Helper.equals(initialShapes, extractShapes())) {
            waitAndMoveCount++;
        }
    }

    public void zpráva(Object zpráva) {
        messageCount++;
    }
    
    
}
