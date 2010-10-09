/**
 * 
 */


import eu.ebdit.eau.Score;
import eu.ebdit.eau.beans.ScoreBean;
import eu.ebdit.eau.spi.Collector;



/**
 * @author Vladimir Orany
 *
 */
public class XmlScoreParser implements Collector<Score>{
	
	
	Iterable<Score> collectFrom(toBeParsed){
		List<Score> ret = []
		Files.asFileIterable(toBeParsed).each {
			def points = new XmlParser().parse(it);
			points.'suite'.'check'.each{
				ret << new ScoreBean(
				suiteName: it.parent().'@name', 
				checkName: it.'@name', 
				points: it.'@points'.toDouble(),
				bonus: it.'@bonus'? true : false, 
				description: it.'@message', 
				details: it.text() ? it.text() : null
				)
			}
			
		}
		
		return ret
	}
	
}
