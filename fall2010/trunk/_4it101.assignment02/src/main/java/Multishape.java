import java.util.List;

import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.duckapter.checker.Visibility;


public interface Multishape {
    
    @Private(Visibility.AT_LEAST) @Field @Alias("getSeznam") List<Object> getParts();

}
