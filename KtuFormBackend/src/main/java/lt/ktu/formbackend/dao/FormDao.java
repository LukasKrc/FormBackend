/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.ktu.formbackend.dao;

import java.util.List;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public interface FormDao {
    Long createForm(Form form);
    Form getFormId(long id);
    Boolean userHasForm(String formName, String username);
    String getFormAuthor(long id);
    List<Form> getUsersForms(long userId);
    Boolean deleteForm(long id);
}
