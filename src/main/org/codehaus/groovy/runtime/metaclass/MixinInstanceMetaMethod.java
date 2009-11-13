package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.MixinInMetaClass;
import org.codehaus.groovy.reflection.CachedClass;

/**
 * MetaMethod for mixed in classes
 */
public class MixinInstanceMetaMethod extends MetaMethod{
    private final MetaMethod method;
    private final MixinInMetaClass mixinInMetaClass;

    public MixinInstanceMetaMethod(MetaMethod method, MixinInMetaClass mixinInMetaClass) {
        this.method = method;
        this.mixinInMetaClass = mixinInMetaClass;
    }

    public int getModifiers() {
        return method.getModifiers();
    }

    public String getName() {
        return method.getName();
    }

    public Class getReturnType() {
        return method.getReturnType();
    }

    public CachedClass getDeclaringClass() {
        return mixinInMetaClass.getInstanceClass();
    }

    public Object invoke(Object object, Object[] arguments) {
        // make sure parameterTypes gets set
        method.getParameterTypes();
        return method.invoke(mixinInMetaClass.getMixinInstance(object), method.correctArguments(arguments));
    }

    protected Class[] getPT() {
        return method.getNativeParameterTypes();
    }
}
