import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStaticNice;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.resetAll;
import static org.powermock.api.easymock.PowerMock.verify;

import javax.swing.JFrame;

import junit.framework.TestSuite;

import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.duckapter.annotation.Static;
import org.easymock.EasyMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit3.PowerMockSuite;

@PrepareForTest({
    Obdélník.class, 
    Elipsa.class, 
    Kompresor.class,
    Přesouvač.class,
    Mnohotvar.class,
    IO.class,
    Plátno.class,
    Trojúhelník.class,
    XORAV00_Orany3Test.class
})
@SuppressStaticInitializationFor({"IO", "Plátno"})
@PowerMockIgnore({"org.codehaus.groovy.*", "groovy.lang.*", "eu.ebdit.eau.*"})
public class Defense2b extends DuckapterTest {

    public static TestSuite suite() throws Exception{
        System.out.println("Using power mock suite!");
        return new PowerMockSuite(Defense2b.class);
    }
    
    private Plátno mock = null;
    private Plátno mockCanvas;
    
    public static interface CanvasStatic {
        @Private @Static @Field @Alias("getJedináček") Plátno getSingleton();
        @Private @Static @Field @Alias("setJedináček") void setSingleton(Plátno canvas);
    }
    
    public static interface Canvas {
        @Private @Field @Alias("getOkno") JFrame getFrame();
        @Private @Field @Alias("setOkno") void setFrame(JFrame frame);
    }
    
    public void /*test*/MultishapeHalf() throws Exception {
        Mnohotvar multishapeMock = createMock(Mnohotvar.class);
        expectNew(Mnohotvar.class, new Class[]{String.class, ITvar[].class}, anyObject(String.class), anyObject(ITvar[].class)).andReturn(multishapeMock);
        
        Kompresor mockCompressor = createMock(Kompresor.class);
        expectNew(Kompresor.class).andReturn(mockCompressor);
        
        mockCompressor.nafoukniKrát(anyInt(),anyObject(Mnohotvar.class));
        EasyMock.expectLastCall().once();
        replay(multishapeMock, Mnohotvar.class, mockCompressor, Kompresor.class);
        
        as(BasicTestCase.class).setUp();
        as(HasMultishapeHalfTest.class).testMultishapeHalf();
        verify(multishapeMock, Mnohotvar.class, mockCompressor, Kompresor.class);
        as(BasicTestCase.class).tearDown();
    }
    
    @Override
    protected void setUp() throws Exception {
        mockCanvas = createNiceMock(Plátno.class);
        expect(mockCanvas.getBarvaPozadí()).andReturn(Barva.KHAKI).anyTimes();
        replay(mockCanvas);
        
        mockStaticNice(IO.class);
        replay(IO.class);
        
        mockStaticNice(Plátno.class);
        expect(Plátno.getPlátno()).andReturn(mockCanvas).anyTimes();
        replay(Plátno.class);
        
        super.setUp();
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        verify(mockCanvas, IO.class, Plátno.class);
        resetAll();
        super.tearDown();
    }
    
}
