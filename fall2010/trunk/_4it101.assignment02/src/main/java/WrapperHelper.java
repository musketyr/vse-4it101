import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.duckapter.Duck;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.GetFieldAdapter;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;


public class WrapperHelper {
    
    public static interface TheMethodAdapter{
        @Private @Field Method getMethod();
    }
    
    public static interface TheGetFieldAdapter{
        @Private @Field java.lang.reflect.Field getField();
    }
    
    
    public WrapperHelper() {
        // library class
    }
    
    public static InvocationAdapter getAdapter(Object o){
        if (o == null) {
            return null;
        }
        if (!Proxy.isProxyClass(o.getClass())) {
            return null;
        }
        InvocationHandler handler = Proxy.getInvocationHandler(o);
        try {
            java.lang.reflect.Field f = handler.getClass().getDeclaredField("val$adapter");
            f.setAccessible(true);
            Object field = f.get(handler);
            if (field instanceof InvocationAdapter) {
               return (InvocationAdapter) field;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Method getMethod(Object o){
        Object field = getAdapter(o);
        if (field instanceof MethodAdapter) {
            MethodAdapter ma = (MethodAdapter)field;
            return Duck.type(ma, TheMethodAdapter.class).getMethod();
        }
        return null;
    }
    
    public static java.lang.reflect.Field getField(Object o){
        Object field = getAdapter(o);
        if (field instanceof GetFieldAdapter) {
            GetFieldAdapter ma = (GetFieldAdapter)field;
            return Duck.type(ma, TheGetFieldAdapter.class).getField();
        }
        return null;
    }
    
    

}
