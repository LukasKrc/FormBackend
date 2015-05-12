package lt.ktu.formbackend.dao.impl.db;


/**
 *
 * @author Lukas
 */
public class DaoFactory {
    
    private static final FormDaoDbImpl formDao;
    private static final AnswerDaoDbImpl answerDao;
    private static final QuestionDaoDbImpl questionDao;
    private static final UserDaoDbImpl userDao;
    
    static {
        formDao = new FormDaoDbImpl();
        answerDao = new AnswerDaoDbImpl();
        questionDao = new QuestionDaoDbImpl();
        userDao = new UserDaoDbImpl();
        formDao.initialize();
        answerDao.initialize();
    }
    
    public static FormDaoDbImpl getFormDao() {
        return formDao;
    }
    
    public static AnswerDaoDbImpl getAnswerDao() {
        return answerDao;
    }
    
    public static QuestionDaoDbImpl getQuestionDao() {
        return questionDao;
    }
    
    public static UserDaoDbImpl getUserDao() {
        return userDao;
    }
}
