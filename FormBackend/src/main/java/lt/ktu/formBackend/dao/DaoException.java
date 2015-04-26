package lt.ktu.formBackend.dao;

/**
 *
 * @author Lukas
 */
public class DaoException extends RuntimeException{
    public enum Type {
        NO_DATA, ERROR
    }
    
    private final Type type;
    
    public DaoException(Type type, String message) {
        super(message);
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
}
