import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;
import org.duckapter.Duck;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;


public class WrapperHelper {
    
    public static interface TheMethodAdapter{
        @Private @Field Method getMethod();
    }
    
    
    public WrapperHelper() {
        // library class
    }
    
    public static AdaptedClass<?, ?> tryGetAdapter(Object o){
        if (o == null) {
            return null;
        }
        if (!Proxy.isProxyClass(o.getClass())) {
            return null;
        }
        InvocationHandler handler = Proxy.getInvocationHandler(o);
        if (handler instanceof AdaptedClass<?, ?>) {
            return (AdaptedClass<?, ?>) handler;
        }
        if (handler instanceof Adapted<?, ?>) {
            return ((Adapted<?, ?>)handler).getAdaptedClass();
        }
        return null;
    }
    
    public static Method tryGetMethod(Object o){
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
            if (field instanceof MethodAdapter) {
                MethodAdapter ma = (MethodAdapter)field;
                return Duck.type(ma, TheMethodAdapter.class).getMethod();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    

}
