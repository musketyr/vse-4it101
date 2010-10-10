import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Field;
import org.duckapter.annotation.Private;
import org.duckapter.checker.Visibility;

@Private(Visibility.AT_LEAST)
public interface Part {

    @Private(Visibility.AT_LEAST) @Field @Alias("getTvar") ITvar getPart();
    
}
