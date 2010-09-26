package cz.vse._4it101;

import org.duckapter.annotation.All;
import org.duckapter.annotation.Any;
import org.duckapter.annotation.Declared;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;

public interface HasFixtures {
	@All @Declared @Any @Private @Field  SomeFixture[] allFixture();
}