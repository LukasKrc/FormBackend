package lt.ktu.formbackend.dao;

import java.util.HashMap;
import java.util.Map;
import lt.ktu.formbackend.dao.impl.db.UserDaoDbImpl;

/**
 *
 * @author Lukas
 */
public class DaoFactory {
    
    private static final Map<String, UserDao> userDaoMap;
    
    static {
        userDaoMap = new HashMap<>();
        userDaoMap.put("Sqlite", new UserDaoDbImpl());
    }
    
    public static final UserDao getUserDao(String name) {
        return userDaoMap.get(name);
    }
    
}
