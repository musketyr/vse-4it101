

import org.duckapter.annotation.Constructor;

public interface HasStringContructor {
	@Constructor Object newInstance(String name);
}