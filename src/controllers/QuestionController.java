package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import models.Question;
import models.QuestionStatistics;
import utils.QuestionUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {

    private List<Question> questions;

    private int currentQuestionNumber = 0;

    private Question currentQuestion;

    private QuestionStatistics questionStatistics;

    @FXML
    private TextArea questionTitle;

    @FXML
    private Button nextBtn;

    public QuestionController() {
    }


    private void loadParameters() {
        questions = QuestionUtils.getQuestions();
        questionStatistics = new QuestionStatistics();
        loadQuestion();
        currentQuestionNumber++;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadParameters();
    }

    @FXML
    private void loadNextQuestion(ActionEvent event) {
        loadQuestion();
        addAnswerStatistics();
        currentQuestionNumber++;
    }

    private void loadQuestion() {
        if (currentQuestionNumber < questions.size()) {
            currentQuestion = questions.get(currentQuestionNumber);
            questionTitle.setText(currentQuestion.getQuestion());
        }
        else{
            //TODO Изменить на нормальное поведение при последнем вопросе
            System.exit(0);
        }
    }

    private String getResponse(){
        return "TODO";
    }

    private void addAnswerStatistics(){
        boolean isCorrect = QuestionUtils.checkResponse(currentQuestion, getResponse());
        if(isCorrect){
            questionStatistics.setNumberOfCorrect(questionStatistics.getNumberOfCorrect() + 1);
            questionStatistics.addTotalScore(currentQuestion);
        }
        questionStatistics.getQuestionAnswers().add(isCorrect);
    }
}
