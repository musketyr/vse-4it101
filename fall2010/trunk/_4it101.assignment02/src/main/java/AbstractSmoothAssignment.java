import static org.duckapter.Duck.test;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.AssertionFailedError;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class AbstractSmoothAssignment extends DuckapterTest implements IO.ITester{

    public static final Pattern XNAME_VSE_PTRN = Pattern
                .compile("(X[A-Z_]{4}\\d\\d\\d?)_[A-Z][a-z]*_?\\d?(Test)?");

    protected abstract void hideCanvas();

    private int waitAndMoveCount = 0;
    private int wait30 = 0;
    private int wait50 = 0;
    private int messageCount = 0;
    private Shape[] initialShapes = emptyFixtures();

    public AbstractSmoothAssignment() {
        super();
    }

    public AbstractSmoothAssignment(String name) {
        super(name);
    }

    private Shape[] emptyFixtures() {
        return new Shape[0];
    }

    public void testName() throws Exception {
        assertTrue(XNAME_VSE_PTRN.matcher(getTestClass().getName()).matches());
    }

    public void testStringConstructor() throws Exception {
        assertTrue(test(getTestClass(), HasStringContructor.class));
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

    public void testTestSmoothMethodsCount() throws Exception {
        assertTrue(2 <= as(HasSmoothTests.class).allTests().length);
        assertTrue(as(HasSmoothTests.class).allTests().length <= as(
                HasTests.class).allTests().length - 2);
    }

    // at least two
    public void testAllMethodsMoves() throws Exception {
        int count = 0;
        for (TestMethod method : as(HasTests.class).allTests()) {
            try{
                doTestMoved(method, false);
                count ++;
            } catch (AssertionFailedError e){
                // do nothing
            }
        }
        assertTrue(4 <= count);
    }

    public void testSmoothMoves2() throws Exception {
        int count = 0;
        for (TestMethod method : as(HasSmoothTests.class).allTests()) {
            try{
                doTestMoved(method, true);
                count ++;
            } catch (AssertionFailedError e){
                // do nothing
            }   
        }
        assertTrue(2 <= count);
    }
    
    public void testSmoothMoves() throws Exception {
        Map<String, TestMethod> methods = Maps.newHashMap();
        for (TestMethod method : as(HasTests.class).allTests()) {
            methods.put(WrapperHelper.getMethod(method).getName(), method);
        }
        Set<String> used = Sets.newHashSet();
        Pattern pattern = Pattern.compile(HasSmoothTests.SMOOTH_METHOD_REGEXP);
        int count = 0;
        for (Entry<String, TestMethod> entry : methods.entrySet()) {
            if (used.contains(entry.getKey())) {
                continue;
            }
            used.add(entry.getKey());
            Matcher match = pattern.matcher(entry.getKey());
            if (match.matches()) {
                String methodName = match.group(1);
                used.add(methodName);
                TestMethod baseMethod = methods.get(methodName);
                assertNotNull(baseMethod);
                as(BasicTestCase.class).setUp();
                beforeShapeChange();
                entry.getValue().testIt();
                assertMoved(true);
                as(BasicTestCase.class).tearDown();
                count ++;
            }
        }
        assertTrue(2 <= count);
    }

    public void testSameEnd() throws Exception {
        Map<String, TestMethod> methods = Maps.newHashMap();
        for (TestMethod method : as(HasTests.class).allTests()) {
            methods.put(WrapperHelper.getMethod(method).getName(), method);
        }
        Set<String> used = Sets.newHashSet();
        Pattern pattern = Pattern.compile(HasSmoothTests.SMOOTH_METHOD_REGEXP);
        boolean atLeastOneMatches = false;
        for (Entry<String, TestMethod> entry : methods.entrySet()) {
            if (used.contains(entry.getKey())) {
                continue;
            }
            used.add(entry.getKey());
            Matcher match = pattern.matcher(entry.getKey());
            if (match.matches()) {
                String methodName = match.group(1);
                used.add(methodName);
                TestMethod baseMethod = methods.get(methodName);
                if (baseMethod == null) {
                    continue;
                }
                assertSameEnd(baseMethod, entry.getValue());
                atLeastOneMatches |= true;
            }
        }
        for (String method : used) {
            methods.remove(method);
        }
        assertTrue(atLeastOneMatches);
    
    }

    private void assertSameEnd(TestMethod normal, TestMethod smooth) throws Exception {
        as(BasicTestCase.class).setUp();
        normal.testIt();
        Shape[] afterNormal = extractShapes();
        as(BasicTestCase.class).tearDown();
        as(BasicTestCase.class).setUp();
        smooth.testIt();
        assertTrue(Shape.Helper.equals(afterNormal, extractShapes()));
        as(BasicTestCase.class).tearDown();
    }

    private void doTestMoved(TestMethod method, boolean smooth) throws Exception {
        as(BasicTestCase.class).setUp();
        beforeShapeChange();
        method.testIt();
        assertMoved(smooth);
        as(BasicTestCase.class).tearDown();
    }

    private void assertMoved(boolean smooth) {
        assertTrue(moved(smooth));
    }

    private boolean moved(boolean smooth) {
        if (!Shape.Helper.equals(initialShapes, extractShapes())) {
            if (smooth) {
                return wait30 > 0 || wait50 > 0;
            }
            return true;
        }
        if (smooth) {
            return wait30 > 0 || wait50 > 0;
        }
        return waitAndMoveCount > 0;
    }

    private void beforeShapeChange() {
        waitAndMoveCount = 0;
        messageCount = 0;
        wait30 = 0;
        wait50 = 0;
        initialShapes = extractShapes();
    }

    private Shape[] extractShapes() {
        return Lists.transform(
                Arrays.asList(as(HasShapeFixtures.class).allFixture()),
                new Function<ShapeFixture, Shape>() {
    
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
        super.setUp();
        IO.zpravodaj.přihlaš(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        initialShapes = emptyFixtures();
        messageCount = 0;
        waitAndMoveCount = 0;
        wait30 = 0;
        wait50 = 0;
        IO.zpravodaj.odhlaš(this);
        hideCanvas();
    }

    public void čekej(int ms) {
        if (!Shape.Helper.equals(initialShapes, extractShapes())) {
            waitAndMoveCount++;
            if (ms == 30) {
                wait30++;
            }
            if (ms == 50) {
                wait50++;
            }
        }
    }

    public void zpráva(Object zpráva) {
        messageCount++;
    }

}