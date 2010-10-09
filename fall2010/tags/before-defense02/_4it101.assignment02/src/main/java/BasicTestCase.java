

import static org.duckapter.checker.Visibility.AT_LEAST;

import org.duckapter.annotation.Protected;

public interface BasicTestCase {

	@Protected(AT_LEAST)
	public abstract void setUp() throws Exception;

	@Protected(AT_LEAST)
	public abstract void tearDown() throws Exception;

}