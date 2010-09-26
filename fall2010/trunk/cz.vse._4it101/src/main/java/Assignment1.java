

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.duckapter.Duck;
import org.duckapter.adapted.AdaptedFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import cz.vse._4it101.BasicTestCase;
import cz.vse._4it101.HasFixtures;
import cz.vse._4it101.HasShapeFixtures;
import cz.vse._4it101.HasStringContructor;
import cz.vse._4it101.HasTests;
import cz.vse._4it101.Shape;
import cz.vse._4it101.ShapeFixture;
import cz.vse._4it101.SomeFixture;
import cz.vse._4it101.TestMethod;
import eu.ebdit.eau.Assignment;
import eu.ebdit.eau.Description;
import eu.ebdit.eau.Details;
import eu.ebdit.eau.Points;
import eu.ebdit.eau.Porter;

@Assignment("Domácí úkol 01")
public class Assignment1 extends TestCase implements IO.ITester{

    
    /** Očekává xname velkými následovaný podtržítkem a příjmením začínajícím
     *  velkým písmenem a obsahujícím pouze ASCII. Za příjmením smí následovat
     *  číslo a za ním případně text "Test". 
     * @author Rudolf Pecinovský 
     */
    public static final Pattern XNAME_VSE_PTRN =
        Pattern.compile("(X[A-Z_]{4}\\d\\d\\d?)_[A-Z][a-z]*_?\\d?(Test)?");
    
    private static final String TESTED_CLASS_KEY = "testedClass";
    private static final String PORTER_ID = "bluej";

    private int waitCount = 0;
    private int messageCount = 0;


    @Points(0.25)
    @Description("Třída je správně pojmenována")
    public void testName() throws Exception {
        assertTrue(XNAME_VSE_PTRN.matcher(getTestedClass().getName()).matches());
    }
    
    @Points(0.25)
    @Description("Testovací třída je vytvořena podle správné šablony")
    public void testStringConstructor() throws Exception {
        assertTrue(Duck.test(getTestedClass(), HasStringContructor.class));
    }

    @Points(0.25)
    @Description("Je vytvořen testovací přípravek" )
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

    @Points(1)
    @Description("Přípravek je tvořen nejméně pěti grafickými objekty")
    public void testFixturesAreShapes() throws Exception {
        Object test = getTest();
        HasShapeFixtures fixtures = Duck.type(test, HasShapeFixtures.class);
        assertTrue(fixtures.allFixture().length >= 5);
    }

    @Points(0.25)
    @Description("Vytvoření přípravku je ukončeno zobrazením dialogového okna s oznámením o jeho připravenosti")
    public void testTestsUsesMessage() throws Exception {
        Object test = getTest();
        BasicTestCase testCase = Duck.type(test, BasicTestCase.class);
        int counts = messageCount;
        testCase.setUp();
        assertTrue(counts < messageCount);
        testCase.tearDown();
    }

    @Points(1)
    @Description("Třída má definovány alespoň dvě testovací metody (animace)")
    public void testTestMethodsCount() throws Exception {
        HasTests fixtures = Duck.type(getTest(), HasTests.class);
        assertTrue(2 <= fixtures.allTests().length);
    }

    @Points(1)
    @Description("Testovací metody (animace) změní pozici a/nebo velikost nejméně jednoho tvaru")
    public void testTestMethodsMoves() throws Exception {
        final Object test = getTest();
        final HasTests tests = Duck.type(test, HasTests.class);
        final HasShapeFixtures fixtures = Duck.type(test, HasShapeFixtures.class);
        BasicTestCase testCase = Duck.type(test, BasicTestCase.class);
        for (TestMethod method : tests.allTests()) {
            testCase.setUp();
            final List<Shape> original = shapesCopy(fixtures);
            int count = waitCount;
            IO.ITester tester = new IO.ITester() {
                
                public void čekej(int ms) {
                    List<Shape> afterRun = shapesCopy(fixtures);
                    assertTrue(checkMoved(original, afterRun));
                }
                
                public void zpráva(Object zpráva) {}
            };
            IO.zpravodaj.přihlaš(tester);
            method.testIt();
            if (count == waitCount) {
                List<Shape> afterRun = shapesCopy(fixtures);
                assertTrue(checkMoved(original, afterRun));
            }
            testCase.tearDown();// nedela nic v bluej
            IO.zpravodaj.odhlaš(tester);
        }
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
        return Class.forName((String)Porter.leasePorter(PORTER_ID)
                .inspectBurden(TESTED_CLASS_KEY));
    }



    @Override
    protected void tearDown() throws Exception {
       IO.zpravodaj.odhlaš(this);
       Plátno.getPlátno().setViditelné(false);
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
