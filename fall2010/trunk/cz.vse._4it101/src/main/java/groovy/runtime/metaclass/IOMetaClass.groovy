package groovy.runtime.metaclass

import groovy.lang.MetaClass;

class IOMetaClass extends groovy.lang.DelegatingMetaClass {
    
    IOMetaClass(final MetaClass aclass) {
        super(aclass)
        initialize()
    }
    
    @Override
    public Object invokeStaticMethod(Object object, String methodName, Object[] arguments){
        def ret =  super.invokeStaticMethod(object, methodName, arguments)
        println "changed ${ret}"
        return ret
    }
    
    public Object invokeMethod(Object a_object, String a_methodName, Object a_arguments) {
        def ret = super.invokeMethod(a_object, a_methodName, a_arguments)
        println "changed ${ret}"
        return ret
    }
}

