package lt.ktu.formbackend.model;

import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class AnswerStats {

    private ArrayList<String> choices;
    private ArrayList<Integer> votes;

    public AnswerStats(Question question, ArrayList<Answer> answers) {
        ArrayList<String> choices = new ArrayList();
        ArrayList<Integer> votes = new ArrayList();
        switch (question.getType()) {
            case "integer":
                for (int x = 0; x < answers.size(); x++) {
                    if (answers.get(x).getQuestionNumber() == question.getQuestionNumber()) {
                        if (answers.get(x).getType().equals("integer")) {
                            if (!choices.contains(Integer.toString(answers.get(x).getValueInteger()))) {
                                choices.add(Integer.toString(answers.get(x).getValueInteger()));
                                votes.add(1);
                            } else {
                                int choicePosition = choices.indexOf(Integer.toString(answers.get(x).getValueInteger()));
                                votes.set(choicePosition, votes.get(choicePosition) + 1);
                            }
                        }
                    }
                }
                break;

            case "string":
                for (int x = 0; x < answers.size(); x++) {
                    if (answers.get(x).getQuestionNumber() == question.getQuestionNumber()) {
                        if (answers.get(x).getType().equals("string")) {
                            if (!choices.contains(answers.get(x).getValueText())) {
                                choices.add(answers.get(x).getValueText());
                                votes.add(1);
                            } else {
                                int choicePosition = choices.indexOf(answers.get(x).getValueText());
                                votes.set(choicePosition, votes.get(choicePosition) + 1);
                            }
                        }
                    }
                }
                break;

            case "one-choice":
                choices.addAll(question.getChoices());
                for (int y = 0; y < question.getChoices().size(); votes.add(0), y++);
                for (int x = 0; x < answers.size(); x++) {
                    if (answers.get(x).getQuestionNumber() == question.getQuestionNumber()) {
                        if (answers.get(x).getType().equals("one-choice")) {
                            if (answers.get(x).getCustomText() != null) {
                                if (!choices.contains(answers.get(x).getCustomText())) {
                                    choices.add(answers.get(x).getCustomText());
                                    votes.add(1);
                                } else {
                                    int choicePosition = choices.indexOf(answers.get(x).getCustomText());
                                    votes.set(choicePosition, votes.get(choicePosition) + 1);
                                }
                            } else {
                                votes.set(answers.get(x).getOneChoice(), votes.get(answers.get(x).getOneChoice()) + 1);
                            }
                        }
                    }
                }
                break;

            case "multi-choice":
                choices.addAll(question.getChoices());
                for (int y = 0; y < question.getChoices().size(); votes.add(0), y++);
                for (int x = 0; x < answers.size(); x++) {
                    if (answers.get(x).getQuestionNumber() == question.getQuestionNumber()) {
                        if (answers.get(x).getType().equals("multi-choice")) {
                            if (answers.get(x).getCustomStringArray() != null) {
                                for (int j = 0; j < answers.get(x).getCustomStringArray().size(); j++) {
                                    if (!choices.contains(answers.get(x).getCustomStringArray().get(j))) {
                                        choices.add(answers.get(x).getCustomStringArray().get(j));
                                        votes.add(1);
                                    } else {
                                        int choicePosition = choices.indexOf(answers.get(x).getCustomStringArray().get(j));
                                        votes.set(choicePosition, votes.get(choicePosition) + 1);
                                    }
                                }
                            } else {
                                for (int b = 0; b < answers.get(x).getMultiChoice().size(); b++) {
                                    if (answers.get(x).getMultiChoice().get(b)) {
                                        votes.set(b, votes.get(b) + 1);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
        this.choices = choices;
        this.votes = votes;
    }
    
    //<editor-fold desc="Getters and setters">
    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<Integer> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Integer> votes) {
        this.votes = votes;
    }
    //</editor-fold>
    
}
