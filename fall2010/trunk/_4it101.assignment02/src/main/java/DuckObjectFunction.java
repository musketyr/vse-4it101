import org.duckapter.Duck;

import com.google.common.base.Function;


public class DuckObjectFunction<O,D> implements Function<O,D> {
    
    private final Class<D> duckInterface;
    
    public DuckObjectFunction(Class<D> duckInterface) {
        this.duckInterface = duckInterface;
    }

    public D apply(O from) {
        return Duck.type(from, duckInterface);
    }

}
