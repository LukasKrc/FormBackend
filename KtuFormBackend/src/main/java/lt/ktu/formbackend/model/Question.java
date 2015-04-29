package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class Question {
    private String name;
    @JsonProperty("desc")
    private String description;
    private String type;
    @JsonProperty("allow-empty")
    private Boolean allowEmpty;
    @JsonProperty("allow-custom")
    private String allowCustom;
    @JsonProperty("allow-ws")
    private Boolean allowWhitespace;
    @JsonProperty("allow-ns")
    private Boolean allowNewLines;
    private ArrayList<String> choices;
    @JsonProperty("allowed-providers")
    private ArrayList<String> allowedProviders;
    @JsonProperty("min-val")
    private Integer minValue;
    @JsonProperty("max-val")
    private Integer maxValue;
    @JsonProperty("min-choices")
    private Integer minChoices;
    @JsonProperty("max-choices")
    private Integer maxChoices;

    //<editor-fold desc="Getters, setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAllowEmpty() {
        return allowEmpty;
    }

    public void setAllowEmpty(Boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public String getAllowCustom() {
        return allowCustom;
    }

    public void setAllowCustom(String allowCustom) {
        this.allowCustom = allowCustom;
    }

    public Boolean getAllowWhitespace() {
        return allowWhitespace;
    }

    public void setAllowWhitespace(Boolean allowWhitespace) {
        this.allowWhitespace = allowWhitespace;
    }

    public Boolean getAllowNewLines() {
        return allowNewLines;
    }

    public void setAllowNewLines(Boolean allowNewLines) {
        this.allowNewLines = allowNewLines;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<String> getAllowedProviders() {
        return allowedProviders;
    }

    public void setAllowedProviders(ArrayList<String> allowedProviders) {
        this.allowedProviders = allowedProviders;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinChoices() {
        return minChoices;
    }

    public void setMinChoices(Integer minChoices) {
        this.minChoices = minChoices;
    }

    public Integer getMaxChoices() {
        return maxChoices;
    }

    public void setMaxChoices(Integer maxChoices) {
        this.maxChoices = maxChoices;
    }
    //</editor-fold>
    
}
