

import eu.ebdit.eau.Result;
import eu.ebdit.eau.spi.Collector;

public final class JUnitResultCollector implements Collector<Result> {

    private final Collector<Result> currentImplementation;
    
    public JUnitResultCollector() {
	this.currentImplementation = getCollector();
    }


    private Collector<Result> getCollector() {
	    return new JUnit3ResultCollector();
    }
    
    public Iterable<Result> collectFrom(final Object input) {
        return currentImplementation.collectFrom(input);
    }


}
