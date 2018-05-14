package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.QuestionStatistics;

import java.net.URL;
import java.util.ResourceBundle;

public class StaticticsController implements Initializable {

    @FXML
    private TextField numberCorrect;

    @FXML
    private TextField totalScore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initStatistics(QuestionStatistics questionStatistics) {
        //good solution is to throw exception if QS is null
        if (questionStatistics != null) {
            Integer numberCorrects = questionStatistics.getNumberOfCorrect();
            numberCorrect.setText(numberCorrects.toString());
            Double totalScoreValue = questionStatistics.getTotalScore();
            totalScore.setText(totalScoreValue.toString());
        }
    }
}
