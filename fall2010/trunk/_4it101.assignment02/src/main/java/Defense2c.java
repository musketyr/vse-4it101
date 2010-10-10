import java.util.HashSet;
import java.util.Set;

import org.duckapter.Duck;

import com.google.common.collect.Sets;

public class Defense2c extends Assignment2b implements Kompresor.ITester {
    
    private Set<Object> fixtures = null;
    private Set<Object> usedInMultishape = null;
    
    private ITvar compressed = null;
    private double dx;
    private double dy;
    
    public void testAllFixturesUsed() throws Exception {
        as(BasicTestCase.class).setUp();
        copyFixtures();
        as(HasMultishapeHalfTest.class).testMultishapeHalf();
        assertAllFixturesUsed();
        as(BasicTestCase.class).tearDown();
    }
    
    private void copyFixtures() throws Exception {
        fixtures = new HashSet<Object>();
        for (ShapeFixture shape : as(HasShapeFixtures.class).allFixture()) {
            fixtures.add(WrapperHelper.getField(shape).get(getTest()));
        }
    }

    private void assertAllFixturesUsed() {
        assertEquals(fixtures.size(), usedInMultishape.size());
        for (Object obj : fixtures) {
            Shape shape = Duck.type(obj, Shape.class);
            boolean wasUsed = false;
            for (Object used : usedInMultishape) {
                Shape usedShape = Duck.type(used, Shape.class);
                wasUsed |= Shape.Helper.equals(shape, usedShape);
                if (wasUsed) {
                    break;
                }
            }
            assertTrue(shape.toString(), wasUsed);
        }
    }

    public void testShrinkedToHalf() throws Exception {
        as(BasicTestCase.class).setUp();
        as(HasMultishapeHalfTest.class).testMultishapeHalf();
        assertShrinkToHalf();
        as(BasicTestCase.class).tearDown();
    }
    
    private void assertShrinkToHalf() {
        assertNotNull(compressed);
        assertEquals(0.5d, dx, 0.05);
        assertEquals(0.5d, dy, 0.05);
    }

    public void testCompressorUsed() throws Exception {
        as(BasicTestCase.class).setUp();
        as(HasMultishapeHalfTest.class).testMultishapeHalf();
        assertCompressorUsed();
        as(BasicTestCase.class).tearDown();
    }

    private void assertCompressorUsed() {
        assertNotNull(compressed);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Kompresor.zpravodaj.přihlaš(this);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Kompresor.zpravodaj.odhlaš(this);
        compressed = null;
        fixtures = null;
        usedInMultishape = null;
        dx = 0;
        dy = 0;
    }
    
    
    @Override
    public void foukej(Kompresor kdo, INafukovací koho, int šťouchů, 
            double dx, double dy, Směr8 pevný){
        compressed =  ((ITvar)koho).kopie();
        Multishape multishape = Duck.type(koho, Multishape.class);
        usedInMultishape = Sets.newHashSet();
        for(Object o: multishape.getParts()){
            usedInMultishape.add(Duck.type(o, Part.class).getPart().kopie());
        }
        this.dx = Math.abs(dx * šťouchů) / compressed.getŠířka();
        this.dy = Math.abs(dy * šťouchů) / compressed.getVýška();
    }
    
}
