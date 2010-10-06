

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.runner.BaseTestRunner;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import eu.ebdit.eau.Result;
import eu.ebdit.eau.spi.Collector;

final class JUnit3ResultCollector extends BaseTestRunner implements
		Collector<Result> {

	private transient Result lastResult;

	private transient Collection<Result> results = Lists.newArrayList();

	private boolean canCollectFrom(final Object input) {
		return !Iterables.isEmpty(Classes.asClassIterable(input));
	}

	public Iterable<Result> collectFrom(final Object input) {
		if (!canCollectFrom(input)) {
			return Collections.emptyList();
		}
		reset();
		for (Class<?> clazz : Classes.asClassIterable(input)) {
			final Test test = getTest((Class<? extends TestCase>)clazz);
			doRun(test);
		}
		return results;
	}

	public junit.framework.TestResult doRun(final Test suite) {
		final junit.framework.TestResult result = createResult();
		suite.run(result);
		return result;
	}

	@Override
	// NOPMD
	public void testEnded(final String testName) {
		handleTestFinished();
	}

	@Override
	// NOPMD
	public void testFailed(final int status, final Test test,
			final Throwable trowable) {
		lastResult = Result.ofNames(lastResult.getSuiteName(),
				lastResult.getCheckName(), false, getFilteredTrace(trowable));
	}

	@Override
	// NOPMD
	public void testStarted(final String testName) {
		lastResult = Result.ofFullName(testName, true, null);
	}

	protected void runFailed(final String message) {
		//lastResult.setMessage(message);
	}

	private junit.framework.TestResult createResult() {
		final junit.framework.TestResult result = new junit.framework.TestResult();
		result.addListener(this);
		return result;
	}

	private void handleTestFinished() {
		results.add(lastResult);
		lastResult = null;
	}

	private void reset() {
		lastResult = null;
		results = Lists.newArrayList();
	}
	
	public Test getTest(Class<? extends TestCase> testClass) {
		Method suiteMethod= null;
		try {
			suiteMethod= testClass.getMethod(SUITE_METHODNAME, new Class[0]);
	 	} catch(Exception e) {
			clearStatus();
			TestSuite testSuite = new TestSuite();
			testSuite.addTestSuite(testClass);
			return testSuite;
		}
		if (! Modifier.isStatic(suiteMethod.getModifiers())) {
			runFailed("Suite() method must be static");
			return null;
		}
		Test test= null;
		try {
			test= (Test)suiteMethod.invoke(null, (Object[])new Class[0]); // static method
			if (test == null)
				return test;
		}
		catch (InvocationTargetException e) {
			runFailed("Failed to invoke suite():" + e.getTargetException().toString());
			return null;
		}
		catch (IllegalAccessException e) {
			runFailed("Failed to invoke suite():" + e.toString());
			return null;
		}

		clearStatus();
		return test;
	}

}
