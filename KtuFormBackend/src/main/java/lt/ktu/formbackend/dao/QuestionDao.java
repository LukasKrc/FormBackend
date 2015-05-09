/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.ktu.formbackend.dao;

import lt.ktu.formbackend.model.Question;

/**
 *
 * @author Lukas
 */
public interface QuestionDao {
    public Boolean createQuestion(Question question);
}
