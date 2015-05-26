package lt.ktu.formbackend.model;

import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class FormsContainer {

    private int count;
    private ArrayList<Form> forms;

    
    //<editor-fold desc="Setters and getters">
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public ArrayList<Form> getForms() {
        return forms;
    }

    public void setForms(ArrayList<Form> forms) {
        this.forms = forms;
    }
    //</editor-fold>  
    
}
