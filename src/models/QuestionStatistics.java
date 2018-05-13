package models;

import java.util.ArrayList;
import java.util.List;

public class QuestionStatistics {

    private int numberOfCorrect = 0;

    private double totalScore = 0d;

    private List<Boolean> questionAnswers;

    public QuestionStatistics() {
        questionAnswers = new ArrayList<>();
    }

    public int getNumberOfCorrect() {
        return numberOfCorrect;
    }

    public void setNumberOfCorrect(int numberOfCorrect) {
        this.numberOfCorrect = numberOfCorrect;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public List<Boolean> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<Boolean> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public void addTotalScore(Question question) {
        totalScore += question.coefficient;
    }
}
