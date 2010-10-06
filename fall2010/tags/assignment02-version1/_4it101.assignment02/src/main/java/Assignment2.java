
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.duckapter.Duck;
import org.duckapter.adapted.AdaptedFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import eu.ebdit.eau.Assignment;
import eu.ebdit.eau.Description;
import eu.ebdit.eau.Points;
import eu.ebdit.eau.Porter;

@Assignment("Domácí úkol 01")
public class Assignment2 extends TestCase implements IO.ITester {

    private static final int MIN_WAITS = 30;
    
    /**
     * Očekává xname velkými následovaný podtržítkem a příjmením začínajícím
     * velkým písmenem a obsahujícím pouze ASCII. Za příjmením smí následovat
     * číslo a za ním případně text "Test".
     * 
     * @author Rudolf Pecinovský
     */
    public static final Pattern XNAME_VSE_PTRN = Pattern
            .compile("(X[A-Z_]{4}\\d\\d\\d?)_[A-Z][a-z]*_?\\d?(Test)?");

    private static final String TESTED_CLASS_KEY = "testedClass";
    private static final String PORTER_ID = "bluej";

    private int waitCount = 0;
    private int messageCount = 0;

    @Points(0.25d)
    @Description("Třída je správně pojmenována")
    public void testName() throws Exception {
        assertTrue(XNAME_VSE_PTRN.matcher(getTestedClass().getName()).matches());
    }

    @Points(0.25d)
    @Description("Testovací třída je vytvořena podle správné šablony")
    public void testStringConstructor() throws Exception {
        assertTrue(Duck.test(getTestedClass(), HasStringContructor.class));
    }

    @Points(0.25d)
    @Description("Je vytvořen testovací přípravek")
    public void testInitBySetUp() throws Exception {
        Object test = getTest();
        HasFixtures fixtures = Duck.type(test, HasFixtures.class);
        for (SomeFixture fix : fixtures.allFixture()) {
            assertNull(fix.fixture());
        }
        Duck.type(test, BasicTestCase.class).setUp();
        for (SomeFixture fix : fixtures.allFixture()) {
            assertNotNull(fix.fixture());
        }
    }

    @Points(1d)
    @Description("Přípravek je tvořen nejméně pěti grafickými objekty")
    public void testFixturesAreShapes() throws Exception {
        Object test = getTest();
        HasShapeFixtures fixtures = Duck.type(test, HasShapeFixtures.class);
        assertTrue(fixtures.allFixture().length >= 5);
    }

    @Points(0.25d)
    @Description("Vytvoření přípravku je ukončeno zobrazením dialogového okna s oznámením o jeho připravenosti")
    public void testTestsUsesMessage() throws Exception {
        Object test = getTest();
        BasicTestCase testCase = Duck.type(test, BasicTestCase.class);
        int counts = messageCount;
        testCase.setUp();
        assertTrue(counts < messageCount);
        testCase.tearDown();
    }

    @Points(1d)
    @Description("Třída má definovány alespoň dvě testovací metody (animace)")
    public void testTestMethodsCount() throws Exception {
        HasTests fixtures = Duck.type(getTest(), HasTests.class);
        assertTrue(2 <= fixtures.allTests().length);
    }

    @Points(1d)
    @Description("Testovací metody (animace) změní pozici a/nebo velikost nejméně jednoho tvaru")
    public void testTestMethodsMoves() throws Exception {
        final Object test = getTest();
        final HasTests tests = Duck.type(test, HasTests.class);
        final HasShapeFixtures fixtures = Duck.type(test,
                HasShapeFixtures.class);
        BasicTestCase testCase = Duck.type(test, BasicTestCase.class);
        final Collection<String> moved = Lists.newArrayList();
        for (TestMethod method : tests.allTests()) {
            testCase.setUp();
            final List<Shape> original = shapesCopy(fixtures);
            int count = waitCount;
            IO.ITester tester = new IO.ITester() {

                public void čekej(int ms) {
                    List<Shape> afterRun = shapesCopy(fixtures);
                    if (checkMoved(original, afterRun)) {
                        moved.add("");
                    }
                }

                public void zpráva(Object zpráva) {
                }
            };
            IO.zpravodaj.přihlaš(tester);
            method.testIt();
            if (count == waitCount) {
                List<Shape> afterRun = shapesCopy(fixtures);
                if(checkMoved(original, afterRun)){
                    moved.add("");
                }
            }
            testCase.tearDown();// nedela nic v bluej
            IO.zpravodaj.odhlaš(tester);
        }
        
        assertTrue(moved.size() > 1);
    }
    
    @Points(1d)
    @Description("Alespoň jedna metoda používá kompresor nebo posouvač.")
    public void testSmoothMove() throws Exception {
        final Object test = getTest();
        final HasTests tests = Duck.type(test, HasTests.class);
        BasicTestCase testCase = Duck.type(test, BasicTestCase.class);
        
        boolean moverUsed = false;
        boolean compressorUsed = false;
        
        for (TestMethod method : tests.allTests()) {
            testCase.setUp();
            final int[] compressorAndMover = new int[]{ 0, 0};

            IO.ITester tester = new IO.ITester() {

                public void čekej(int ms) {
                    if (ms == 30 /* nejspis kompresor*/) {
                        compressorAndMover[0] = compressorAndMover[0] + 1;
                    }
                    if (ms == 50 /* nejspis presouvac*/) {
                        compressorAndMover[1] = compressorAndMover[1] + 1;
                    }
                }

                public void zpráva(Object zpráva) {
                }
            };
            IO.zpravodaj.přihlaš(tester);
            
            
            method.testIt();
            
            compressorUsed = compressorUsed || (compressorAndMover[0] >= MIN_WAITS);
            moverUsed = moverUsed || (compressorAndMover[1] >= MIN_WAITS);
            
            testCase.tearDown();// nedela nic v bluej
            IO.zpravodaj.odhlaš(tester);

        }
        
        assertTrue("Compressor or mover must be used at least once", 
                compressorUsed || moverUsed);
        
    }

    private boolean checkMoved(List<Shape> original, List<Shape> afterRun) {
        boolean moved = false;
        for (int i = 0; i < original.size(); i++) {
            moved |= !Shape.Helper.equals(original.get(i), afterRun.get(i));
        }
        return moved;
    }

    private List<Shape> shapesCopy(HasShapeFixtures test) {
        return Lists.newArrayList(Lists.transform(
                Arrays.asList(test.allFixture()),
                new Function<ShapeFixture, Shape>() {
                    public Shape apply(ShapeFixture from) {
                        return Shape.Helper.copy(from.fixture());
                    }
                }));
    }

    private Object getTest() throws ClassNotFoundException {
        return Duck.type(getTestedClass(), HasStringContructor.class)
                .newInstance("Test");
    }

    protected Class<?> getTestedClass() throws ClassNotFoundException {
        AdaptedFactory.clearCache();
        String className = (String) Porter.leasePorter(PORTER_ID)
                .inspectBurden(TESTED_CLASS_KEY);
        if (className == null) {
            className = "XORAV00_Orany2Test"; // for test purpose
        }
        return Thread.currentThread().getContextClassLoader()
                .loadClass(className);
    }

    @Override
    protected void tearDown() throws Exception {
        IO.zpravodaj.odhlaš(this);
        SprávcePlátna.getInstance();
    }

    @Override
    protected void setUp() throws Exception {
        IO.zpravodaj.přihlaš(this);

    }

    public void čekej(int ms) {
        waitCount++;
    }

    public void zpráva(Object zpráva) {
        messageCount++;
    }

}
