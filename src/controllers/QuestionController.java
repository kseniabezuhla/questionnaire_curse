package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.OpenQuestion;
import models.OptionQuestion;
import models.Question;
import models.QuestionStatistics;
import utils.QuestionUtils;

import java.net.URL;
import java.util.ArrayList;
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
    private ToggleGroup currentToggleGroup;

    private List<Node> currentNodesList;

    @FXML
    private GridPane answersGrid;

    @FXML
    private Button nextBtn;

    @FXML
    private void loadNextQuestion(ActionEvent event) {
        if (!handleCurrentAnswer())
            return; // handle not selected answer or empty text field here
        loadQuestion();
        currentQuestionNumber++;
    }

    public QuestionController() {
        currentNodesList = new ArrayList<>();
    }

    private void loadParameters() {
        questions = QuestionUtils.getQuestions();
        questionStatistics = new QuestionStatistics();
        loadQuestion();
        currentQuestionNumber++;
    }

    private void loadRadioButtons(OptionQuestion question) {
        answersGrid.setVgap(question.getAnswerOptions().size());
        answersGrid.setHgap(2);
        removePreviousQuestion();
        currentNodesList.clear();
        ToggleGroup toggleGroup = new ToggleGroup();
        int top = 10, left = 20, increase = 30, idRow = 0;
        for (String option :
                question.getAnswerOptions()) {
            RadioButton rb = new RadioButton(option);
            rb.setToggleGroup(currentToggleGroup);
            rb.setUserData(option);
            answersGrid.add(rb, 0, idRow);
            currentNodesList.add(rb);
            idRow++;
        }
    }

    private void removePreviousQuestion() {
        answersGrid.getChildren().removeAll(currentNodesList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadParameters();
    }

    private boolean handleCurrentAnswer() {
        if (currentQuestion instanceof OptionQuestion)
            return handleOptionQuestion();
        else
            return handleOpenQuestion();
    }

    private boolean handleOpenQuestion() {
        //you can add some other verifications if you want
        //better solution is to throw and handle exceptions, but we need to create them first
        String userAnswer = ((TextArea) currentNodesList.get(0)).getText();
        if (userAnswer.trim().equals(""))
            return false;
        else{
            addAnswerStatistics(userAnswer);
            return true;
        }

    }

    private boolean handleOptionQuestion() {
        if (currentToggleGroup.selectedToggleProperty().getValue() == null)
            return false; // handle situation when nothing is checked: add message or something else
        String userAnswer = (String) currentToggleGroup.getSelectedToggle().getUserData();
        currentToggleGroup.getSelectedToggle().setSelected(false);
        addAnswerStatistics(userAnswer);
        return true;
    }

    private void clearCreateGrid() {
        answersGrid.getChildren().removeAll();
    }

    private void loadQuestion() {
        if (currentQuestionNumber < questions.size()) {
            currentQuestion = questions.get(currentQuestionNumber);
            questionTitle.setText(currentQuestion.getQuestion());
            clearCreateGrid();
            if (currentQuestion instanceof OpenQuestion) {
                loadTextArea();
            } else {
                loadRadioButtons((OptionQuestion) currentQuestion);
            }
        } else {
            Stage stage = (Stage) nextBtn.getScene().getWindow();
            stage.close();
            showStatistics();
        }
    }

    private void loadTextArea() {
        TextArea textArea = new TextArea();
        textArea.setMaxHeight(20);
        textArea.setWrapText(true);
        answersGrid.setVgap(1);
        answersGrid.setHgap(2);
        removePreviousQuestion();
        currentNodesList.clear();
        answersGrid.add(textArea, 0, 0);
        currentNodesList.add(textArea);
    }

    private void addAnswerStatistics(String answer) {
        boolean isCorrect = QuestionUtils.checkResponse(currentQuestion, answer);
        if (isCorrect) {
            questionStatistics.setNumberOfCorrect(questionStatistics.getNumberOfCorrect() + 1);
            questionStatistics.addTotalScore(currentQuestion);
        }
        questionStatistics.getQuestionAnswers().add(isCorrect);
    }

    private void showStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/StatisticsWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,400,200);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Статистика");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            StaticticsController staticticsController = loader.<StaticticsController>getController();
            staticticsController.initStatistics(questionStatistics);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
