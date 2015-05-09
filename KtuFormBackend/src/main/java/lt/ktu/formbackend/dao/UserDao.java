package lt.ktu.formbackend.dao;

import java.util.List;

import lt.ktu.formbackend.model.User;

/**
 *
 * @author Lukas
 */
public interface UserDao {
    List<User> getAllUsers() throws DaoException;
    User getUserId(Long userId) throws DaoException;
    User getUserUsername(String username) throws DaoException;
    Boolean createUser(User user) throws DaoException;
    Boolean updateUser(User user, String username) throws DaoException;
    Boolean deleteUser(String username)throws DaoException;
}
