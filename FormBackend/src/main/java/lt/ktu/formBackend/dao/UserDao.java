package lt.ktu.formBackend.dao;

import java.util.List;

import lt.ktu.formBackend.model.User;

/**
 *
 * @author Lukas
 */
public interface UserDao {
    List<User> getAllUsers() throws DaoException;
    User getUserId(Long userId) throws DaoException;
    User getUserUsername(String username) throws DaoException;
}
