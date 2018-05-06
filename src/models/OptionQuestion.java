package models;

import java.util.ArrayList;
import java.util.List;

public class OptionQuestion extends Question {

    private List<String> answerOptions;

    public OptionQuestion(String question, Integer questionNumber, String correctAnswer, Double coefficient, List<String> answerOptions) {
        super(question, questionNumber, correctAnswer, coefficient);
        this.answerOptions = answerOptions;
    }

    public OptionQuestion(String question, Integer questionNumber, String correctAnswer, Double coefficient) {
        super(question, questionNumber, correctAnswer, coefficient);
        this.answerOptions = new ArrayList<>();
    }

    public OptionQuestion() {
        this.answerOptions = new ArrayList<>();
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<String> answerOptions) {
        this.answerOptions = answerOptions;
    }


}
