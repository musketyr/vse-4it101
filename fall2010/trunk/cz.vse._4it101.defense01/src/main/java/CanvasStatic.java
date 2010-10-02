

import static org.duckapter.checker.Visibility.AT_LEAST;

import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.duckapter.annotation.Static;

public interface CanvasStatic {
    @Private(AT_LEAST) @Static @Field @Alias("jedináček") void setSingleton(Plátno singleton);
}
