package utils;

import models.Question;

import java.io.File;
import java.util.List;

public class QuestionUtils {

    public static boolean checkResponse(Question question, String response) {
        String correctAnswer = question.getCorrectAnswer();
        return correctAnswer.toLowerCase().equals(response.toLowerCase());
    }

    public static List<Question> getQuestions() {
        QuestionReader questionReader= new QuestionReader();
        questionReader.readXml(new File("questions.xml"));
        return questionReader.getQuestions();
    }
}
