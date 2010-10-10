

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import eu.ebdit.eau.Porter;
import eu.ebdit.eau.Report;
import eu.ebdit.eau.Reporter;
import eu.ebdit.eau.Result;
import eu.ebdit.eau.Score;
import eu.ebdit.eau.spi.Collector;

public class AssignmentsTester {

    private static final long serialVersionUID = 3692305904083502235L;

    private final Class<?> assignment;
    private final Class<?> tested;
    private final String score;
    
    
    public AssignmentsTester(Class<?> assignment, Class<?> tested, String score){
        this.assignment = assignment;
        this.tested = tested;
        this.score = score;
    }

    public void showReport() {
        try {
            Report report = getReport();
            System.out.println(report.toString());
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }

    private Report getReport() {
        Porter porter = Porter.leasePorter("bluej");
        porter.giveBurden("testedClass", tested.getName());
        Reporter reporter = Reporter
            .withResultCollectors(getResultCollector())
            .withScoreCollectors(getScoreCollector())
            .build();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        Report report = reporter.report(Arrays.asList(assignment, score),tested.getName());
        porter.takeBurden("testedClass");
        return report;
    }

    protected Collector<Score> getScoreCollector() {
        return new XmlScoreParser();
    }

    protected Collector<Result> getResultCollector() {
        return new JUnitResultCollector();
    }

}
