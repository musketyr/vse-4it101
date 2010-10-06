

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Utility class for handling object which are supposed to be or contain
 * classes.
 * 
 * @author Vladimir Orany
 * 
 */
public final class Classes {
    
    private Classes() {
	// prevents instance creation and subtyping
    }

    /**
     * Converts input to class if exists. Returns <code>null</code> if input is
     * <code>null</code>. Returns input itself if input is already class object.
     * For non-string and non-class inputs {@link Object#toString()} method is
     * used to find out class name.
     * 
     * @param input
     *            object describing class
     * @return class object described by input if exist or <code>null</code>
     *         otherwise
     */
    @SuppressWarnings("unchecked")
    public static Class<?> existingClass(final Object input) {
	if (input == null) {
	    return null;
	}
	if (input instanceof Class) {
	    return (Class<?>) input;
	}
	try {
	    return Class.forName(input.toString(), false, Thread
		    .currentThread().getContextClassLoader());
	} catch (ClassNotFoundException e) {
	    return null;
	}
    }

    /**
     * Converts object to iterable of class objects. Returns empty iterable if
     * there is no way how to convert object to class iterable.
     * 
     * @param object
     *            object representing zero or more classes
     * @return iterable of classes described by object input or empty iterable
     *         if there is no way how to derive class objects from input object
     */
    @SuppressWarnings("unchecked")
    public static Iterable<Class<?>> asClassIterable(final Object object) {
	if (object == null) {
	    return Collections.emptyList();
	}
	if (object instanceof Class) {
	    return Arrays.asList(new Class<?>[] { (Class<?>) object });
	}
	final Class<?> classForName = Classes.existingClass(object);
	if (classForName != null) {
	    return Arrays.asList(new Class<?>[] { classForName });
	}

	Iterable<Object> objList = null;
	if (object.getClass().isArray()) {
	    objList = Arrays.asList((Object[]) object);
	}
	if (object instanceof Iterable) {
	    objList = (Iterable<Object>) object;
	}
	if (objList == null) {
	    return Collections.emptyList();
	}
	final List<Class<?>> ret = Lists.newArrayList();
	for (Object obj : objList) {
	    final Class<?> clazz = Classes.existingClass(obj);
	    if (clazz != null) {
		ret.add(clazz);
	    }
	}
	return ImmutableList.copyOf(ret);
    }

    /**
     * Converts object to array of class objects. Returns empty array if there
     * is no way how to convert the input.
     * 
     * @param input
     *            object representing zero or more class objects
     * @return array of class objects derived from input object or empty array
     */
    public static Class<?>[] asClassArray(final Object input) {
	return ImmutableList.copyOf(asClassIterable(input)).toArray(
		new Class<?>[] {});
    }
    
    /**
     * Test whether input object is similar to footprint object.
     * <p>
     * The term similar for this purpose means:
     * <ul>
     * <li>has or is of the same class
     * <li>class of footprint is superclass of class of input
     * <li>superclass of footprint is class or superclass of input and class of
     * input implements all interfaces with same generic parameters as the
     * footprint class
     * </ul>
     * </p>
     * <p>
     * <code>null</code> input is similar to any footprint.
     * </p>
     * <p>
     * <code>null</code> footprint is only similar to <code>null</code> input
     * </p>
     * <p>
     * Instances of {@link Class} are used directly. To match similarity of
     * {@link Class} class use {@link Class#getClass()} directly as parameter.
     * </p>
     * 
     * @param input
     *            class or instance to be match to be similar to footprint
     * @param footprint
     *            class or instance to be input matched to using rules above,
     *            footprint is usually anonymous inner class to prevent bugs
     *            when referral class changes
     * @return whether is input similar to footprint using rules above
     */
    public static boolean isSimilarTo(final Object input, 
	    final Object footprint) {
	if (input == null) {
	    return true;
	}
	if (footprint == null) {
	    return false;
	}
	final Class<?> inputClass = classOf(input);
	final Class<?> footprintClass = classOf(footprint);

	if (footprintClass.equals(inputClass)) {
	    return true;
	}
	if (getSuperClasses(inputClass).contains(footprintClass)) {
	    return true;
	}
	if (getGenericInterfaces(inputClass).containsAll(
		getGenericInterfaces(footprintClass))) {
	    return true;
	}
	return false;
    }

    /**
     * Returns function which creates new instance of given class or
     * <code>null</code> when the class cannot create new instance.
     * 
     * @param <T>
     *            type of new object created
     * @return function which creates new instance of given class or
     *         <code>null</code> when the class cannot create new instance
     */
    public static <T> Function<Class<T>, T> toInstance() {
	return new Function<Class<T>, T>() {
	    public T apply(final Class<T> from) {
	        try {
		    return from.newInstance();
		} catch (InstantiationException e) {
		    logCannotInstantialize(from, e);
		    return null;
		} catch (IllegalAccessException e) {
		    logCannotInstantialize(from, e);
		    return null;
		}
	    }

	    private <TT> void logCannotInstantialize(final Class<TT> from,
		    final Throwable exception) {
	    }
	};
    }
    
    /**
     * Creates predicate which returns same results as
     * {@link #isSimilarTo(Object, Object)} method.
     * 
     * @param footprint
     *            object to match similarity
     * @return predicate returning same result as
     *         {@link #isSimilarTo(Object, Object)} method
     */
    public static Predicate<Object> isSimilarTo(final Object footprint){
	return new Predicate<Object>() {
	    public boolean apply(final Object input) {
	        return isSimilarTo(input, footprint);
	    }
	};
    }

    @SuppressWarnings("unchecked")
    private static Collection<Type> getGenericInterfaces(final Class<?> clazz) {
	final Collection<Type> interfaces = Sets.newHashSet();
	for (Type superClass : getSuperClasses(clazz)) {
	    if (superClass instanceof Class) {
		interfaces.addAll(Arrays.asList(((Class<?>) superClass)
			.getGenericInterfaces()));

	    }
	}
	return interfaces;
    }

    @SuppressWarnings("unchecked")
    private static Collection<Type> getSuperClasses(final Class<?> inputClass) {
	final Collection<Type> supers = Sets.newHashSet();
	Type starter = inputClass;
	while (starter != null) {
	    supers.add(starter);
	    if (starter instanceof Class) {
		starter = ((Class<?>) starter).getGenericSuperclass();
	    }
	}
	return supers;
    }

    @SuppressWarnings("unchecked")
    private static Class<?> classOf(final Object object) {
	if (object == null) {
	    return null;
	}
	if (object instanceof Class) {
	    return (Class<?>) object;
	}
	return object.getClass();
    }

}
