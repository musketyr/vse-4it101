package cz.vse._4it101;


import org.duckapter.annotation.All;
import org.duckapter.annotation.Declared;
import org.duckapter.annotation.Matching;


public interface HasTests  {

	@All @Declared @Matching("test.*")  TestMethod[] allTests();
}
