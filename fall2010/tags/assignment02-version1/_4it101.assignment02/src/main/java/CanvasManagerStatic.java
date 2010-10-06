

import static org.duckapter.checker.Visibility.AT_LEAST;

import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.duckapter.annotation.Static;

public interface CanvasManagerStatic {
    @Private(AT_LEAST) @Static @Field @Alias("SP") void setCM(SprávcePlátna singleton);
}
