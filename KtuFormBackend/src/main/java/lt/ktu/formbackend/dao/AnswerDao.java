/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.ktu.formbackend.dao;

import java.util.ArrayList;
import lt.ktu.formbackend.model.Answer;
import lt.ktu.formbackend.model.AnswerStats;
import lt.ktu.formbackend.model.FormAnswer;
import lt.ktu.formbackend.model.FormStats;

/**
 *
 * @author Lukas
 */
public interface AnswerDao {
    public long createFormAnswer(String author, long formId, ArrayList<Answer> answers);
    public long createAnswer(Answer answer, long formAnswerId);
    public ArrayList<Answer> getAnswersOfForm(long formId);
    public int getVotesOfForm(long formId);
    public ArrayList<AnswerStats> getFormQuestionStats(long formId);
    public ArrayList<FormStats> getUserFormStats(String username);
    public ArrayList<Long> getUsersFilledForm(long formId);
    public FormAnswer getUsersAnswerToForm(long userId, long formId);
}
