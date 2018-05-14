package models;

public abstract class Question {

    protected String question;

    protected Integer questionNumber;

    protected String correctAnswer;

    protected Double coefficient = 1d;

    public Question(String question, Integer questionNumber, String correctAnswer, Double coefficient) {
        this.question = question;
        this.questionNumber = questionNumber;
        this.correctAnswer = correctAnswer;
        this.coefficient = coefficient;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

}
