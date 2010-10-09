

import org.duckapter.annotation.All;
import org.duckapter.annotation.Any;
import org.duckapter.annotation.Declared;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;

public interface HasShapeFixtures {
	@All @Declared @Any @Private @Field  ShapeFixture[] allFixture();
}