import eu.ebdit.eau.Result;
import eu.ebdit.eau.Score;
import eu.ebdit.eau.junit.JUnitResultCollector;
import eu.ebdit.eau.spi.Collector;
import eu.ebdit.eau.util.XmlScoreParser;

public class AssignmentsTesterClassic extends AssignmentsTester {

    public AssignmentsTesterClassic(Class<?> assignment, Class<?> tested,
            String score) {
        super(assignment, tested, score);
    }
    
    @Override
    protected Collector<Result> getResultCollector() {
        return new JUnitResultCollector();
    }
    
    @Override
    protected Collector<Score> getScoreCollector() {
        return new XmlScoreParser();
    }

}
