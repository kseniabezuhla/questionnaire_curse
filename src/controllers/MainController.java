package controllers;
import java.io.IOException;
import java.util.List;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Question;
import utils.QuestionUtils;

public class MainController {

    private Main main;
    @FXML
    private VBox rootBox;
    @FXML
    private Button startButton;

    @FXML
    private Button nextBtn;

    @FXML
    private void aboutProgram(ActionEvent event) {
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../views/About.fxml"));
            Scene scene = new Scene(root,490,290);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("О программе");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goTest(ActionEvent event) throws IOException {

        Stage stage = (Stage) startButton.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/QuestionWindow.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Тест");
        stage.setScene(new Scene(root1));
        stage.show();

    }


    @FXML
    private void handleExit(ActionEvent actionEvent)
    {
        System.exit(0);
    }


}
