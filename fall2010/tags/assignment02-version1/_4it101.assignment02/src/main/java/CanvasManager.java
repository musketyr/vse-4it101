import org.duckapter.annotation.Alias;
import org.duckapter.annotation.Private;
import org.duckapter.checker.Visibility;


public interface CanvasManager {
    
    @Private(Visibility.AT_LEAST) @Alias("setViditelné") void setVisible(boolean visible);

}
