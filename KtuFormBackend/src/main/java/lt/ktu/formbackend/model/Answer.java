package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class Answer {
    
    private String type;
    @JsonIgnore
    private long id;
    @JsonProperty("valInt")
    private int valueInteger;
    @JsonProperty("valStr")
    private String valueText;
    @JsonProperty("id")
    private int oneChoice;
    @JsonProperty("customInt")
    private int customInteger;
    @JsonProperty("customIntArr")
    private ArrayList<Integer> customIntegerArray;
    @JsonProperty("customStrArr")
    private ArrayList<String> customStringArray;
    @JsonProperty("customStr")
    private String customText;
    @JsonProperty("selected")
    private ArrayList<Boolean> multiChoice;
    private int questionNumber;
    private int formAnswerId;
    
    //<editor-fold desc="Getters and setters">

    public ArrayList<Integer> getCustomIntegerArray() {
        return customIntegerArray;
    }

    public void setCustomIntegerArray(ArrayList<Integer> customIntegerArray) {
        this.customIntegerArray = customIntegerArray;
    }

    public ArrayList<String> getCustomStringArray() {
        return customStringArray;
    }

    public void setCustomStringArray(ArrayList<String> customStringArray) {
        this.customStringArray = customStringArray;
    }
    
    public int getFormAnswerId() {    
        return formAnswerId;
    }
    
    public void setFormAnswerId(int formAnswerId) {    
        this.formAnswerId = formAnswerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(int valueInteger) {
        this.valueInteger = valueInteger;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public int getOneChoice() {
        return oneChoice;
    }

    public void setOneChoice(int oneChoice) {
        this.oneChoice = oneChoice;
    }

    public int getCustomInteger() {
        return customInteger;
    }

    public void setCustomInteger(int customInteger) {
        this.customInteger = customInteger;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public ArrayList<Boolean> getMultiChoice() {
        return multiChoice;
    }

    public void setMultiChoice(ArrayList<Boolean> multiChoice) {
        this.multiChoice = multiChoice;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
    //</editor-fold>
}
