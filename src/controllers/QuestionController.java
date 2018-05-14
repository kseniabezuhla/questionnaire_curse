package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.OpenQuestion;
import models.OptionQuestion;
import models.Question;
import models.QuestionStatistics;
import utils.QuestionUtils;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {

    //region Fields
    private List<Question> questions;

    private int currentQuestionNumber = 0;

    private List<Node> currentNodesList;

    private Question currentQuestion;

    private QuestionStatistics questionStatistics;

    @FXML
    private TextArea questionTitle;

    @FXML
    private ToggleGroup currentToggleGroup;

    @FXML
    private Label errorMessage;

    @FXML
    private GridPane answersGrid;

    @FXML
    private Button nextBtn;

    //endregion

    @FXML
    private void loadNextQuestion(ActionEvent event) {
        if (!handleCurrentAnswer())
            return; // handle not selected answer or empty text field here
        loadQuestion();
        currentQuestionNumber++;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadParameters();
        nextBtn.setOnDragOver(event -> {
            if (event.getGestureSource() != nextBtn &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        nextBtn.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                loadNextQuestion(new ActionEvent());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
            onDragAdd(rb);
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
        else {
            addAnswerStatistics(userAnswer);
            return true;
        }

    }

    private boolean handleOptionQuestion() {
        if (currentToggleGroup.selectedToggleProperty().getValue() == null)
        {
            errorMessage.setVisible(true);
            return false;
        }


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
            errorMessage.setVisible(false);
            if (currentQuestion instanceof OpenQuestion) {
                loadTextArea();
            } else {
                ((OptionQuestion)currentQuestion).shuffleOptions();
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
        onDragAdd(textArea);
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
            Scene scene = new Scene(root, 400, 200);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Статистика");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            StaticticsController staticticsController = loader.<StaticticsController>getController();
            staticticsController.initStatistics(questionStatistics);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDragAdd(Control source) {
        source.setOnDragDetected(event -> {
            if (source instanceof TextArea) {
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(((TextArea) source).getText());
                db.setContent(content);
            }
            if (source instanceof RadioButton) {
                ((RadioButton) source).setSelected(true);
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString((String) ((RadioButton) source).getUserData());
                db.setContent(content);
            }
            event.consume();
        });
    }
}
