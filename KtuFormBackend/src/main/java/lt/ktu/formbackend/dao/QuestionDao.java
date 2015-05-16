/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.ktu.formbackend.dao;

import java.util.ArrayList;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.Question;

/**
 *
 * @author Lukas
 */
public interface QuestionDao {
    public long createQuestion(Question question, long formId);
    public ArrayList<Question> getQuestionsOfForm(long formId);
    public boolean updateQuestionsOfForm(Form form);
}
