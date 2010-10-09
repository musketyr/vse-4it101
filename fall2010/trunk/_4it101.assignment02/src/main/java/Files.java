

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Library class for handling files.
 * 
 * @author Vladimir Orany
 * 
 */
public final class Files {

    private Files() {
	// prevents instance creation and subtyping
    }

    /**
     * Returns existing file represented by path parameter or <code>null</code>
     * if no such file was found.
     * 
     * @param file
     *            object representing the file
     * @return file which exists in current context or <code>null</code> if
     *         there is no such file
     * @see #existingFile(Class, Object)
     */
    public static File existingFile(final Object file) {
	return existingFile(null, file);
    }

    /**
     * Returns existing file represented by path parameter or <code>null</code>
     * if no such file was found.
     * 
     * @param context
     *            context where to find existing files
     * @param file
     *            object representing the file
     * @return file which exists in scope of current context or
     *         <code>null</code> if there is no such file
     */
    public static File existingFile(final Class<?> context, final Object file) {
	if (file == null) {
	    return null;
	}
	final Class<?> theContext = context == null ? Files.class : context;
	File helper = null;
	try {
	    if (file instanceof File) {
		helper = (File) file;
	    } else if (file instanceof String) {
		final URL resource = theContext.getResource((String) file);
		if (resource != null) {
		    helper = new File(resource.toURI());
		}
	    } else if (file instanceof URL) {
		helper = new File(((URL) file).toURI());
	    } else if (file instanceof URI) {
		helper = new File((URI) file);
	    }
	} catch (URISyntaxException e) {
	    return null;
	}
	if (helper == null || !helper.exists()) {
	    return null;
	}
	return helper;
    }

    /**
     * Adapts object into iterable of existing files. In there is no way how to
     * convert object into files iterable then the method returns empty
     * iterable.
     * 
     * @param input
     *            object to be adapted to iterable of existing files
     * 
     * @return object converted to iterable of existing files or empty iterable
     *         when there is no way to adapt input object
     */
    public static Iterable<File> asFileIterable(final Object input) {
	return asFileIterable(null, input);
    }

    /**
     * Adapts object into iterable of existing files. In there is no way how to
     * convert object into files iterable then the method returns empty
     * iterable.
     * 
     * @param input
     *            object to be adapted to iterable of existing files
     * @param aContext
     *            context where to look for existing files
     * @return object converted to iterable of existing files or empty iterable
     *         when there is no way to adapt input object
     */
    public static Iterable<File> asFileIterable(final Class<?> aContext,
	    final Object input) {
	final Class<?> context = aContext == null ? Files.class : aContext;
	if (input == null) {
	    return Collections.emptyList();
	}
	if (input instanceof File) {
	    return Arrays.asList(new File[] { (File) input });
	}
	final File existingFile = existingFile(context, input);
	if (existingFile != null) {
	    return Arrays.asList(new File[] { (File) existingFile });
	}
	Object maybeIterable = input;
	if (input.getClass().isArray()) {
	    maybeIterable = Arrays.asList((Object[]) input);
	}
	if (maybeIterable instanceof Iterable) {
	    final Collection<File> ret = Lists.newArrayList();
	    for (Object obj : (Iterable<?>) maybeIterable) {
		final File existing = existingFile(context, obj);
		if (existing != null) {
		    ret.add(existing);
		}
	    }
	    return ImmutableList.copyOf(ret);
	}
	return Collections.emptyList();
    }
}
