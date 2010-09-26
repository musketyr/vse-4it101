

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import eu.ebdit.eau.Porter;
import eu.ebdit.eau.Report;
import eu.ebdit.eau.Reporter;
import eu.ebdit.eau.junit.JUnitResultCollector;
import eu.ebdit.eau.util.XmlScoreParser;

public class AssignmentTester {

    private static final long serialVersionUID = 3692305904083502235L;

    private final Class<?> assignment;
    private final Class<?> tested;
    
    
    public AssignmentTester(Class<?> assignment, Class<?> tested){
        this.assignment = assignment;
        this.tested = tested;
    }

    public void showReport() {
        try {
            Report report = getReport();
            JDialog dialog = new JDialog();
            JTextArea text = new JTextArea(report.toString());
            text.setEditable(false);
            Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            text.setFont(font);
            dialog.add(text);
            center(dialog);
            dialog.setVisible(true);
            dialog.setTitle("Výsledek vyhodnocení");
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }

    private void center(JDialog dialog) {
        Dimension dim = dialog.getToolkit().getScreenSize();
        dialog.setSize((int) Math.round(dim.getWidth() / 3 * 2),
                (int) Math.round(dim.getHeight() / 3 * 2));
        Rectangle abounds = dialog.getBounds();
        dialog.setLocation((dim.width - abounds.width) / 2,
                (dim.height - abounds.height) / 2);
    }

    private Report getReport() {
        Porter.leasePorter("bluej").giveBurden("testedClass", tested.getName());
        Reporter reporter = Reporter
            .withResultCollectors(new JUnitResultCollector())
            .withScoreCollectors(new XmlScoreParser())
            .build();
        Report report = reporter.report(Arrays.asList(assignment, "/+libs/ukol01.score.xml"),tested.getName());
        return report;
    }

}
