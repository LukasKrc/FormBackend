/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.ktu.formbackend.dao;

import java.util.ArrayList;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.SearchQuery;

/**
 *
 * @author Lukas
 */
public interface FormDao {
    Long createForm(Form form);
    Form getFormId(long id);
    Long getIdOfForm(Form form);
    Boolean userHasForm(String formName, String username);
    String getFormAuthor(long id);
    ArrayList<Form> getUsersForms(long userId);
    ArrayList<Form> searchForms(SearchQuery query);
    Boolean deleteForm(long id);
    Boolean updateForm(long id, Form form);
}
