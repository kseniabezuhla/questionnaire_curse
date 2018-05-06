package utils;


import enums.QuestionType;
import models.OpenQuestion;
import models.OptionQuestion;
import models.Question;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;


import java.util.ArrayList;
import java.util.List;

public class QuestionReader {

    private List<Question> questions;
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;


    public QuestionReader() {
        questions = new ArrayList<>();
    }

    public void readXml(File file) {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("question");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Question question;
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    question = getQuestionInstanceByType(eElement.getAttribute("type"));
                    if (question == null)
                        throw new Exception("You must specify the type of question!");

                    if (question instanceof OptionQuestion)
                        addOptionQuestion((OptionQuestion) question, eElement);
                    if (question instanceof OpenQuestion)
                        addSimpleQuestion((OpenQuestion) question, eElement);
                    question.setQuestionNumber(temp + 1);
                }
            }
            System.out.println("Hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Question getQuestionInstanceByType(String type) {
        if (QuestionType.SIMPLE.toString().toLowerCase().equals(type.toLowerCase()))
            return new OpenQuestion();
        if (QuestionType.OPTIONAL.toString().toLowerCase().equals(type.toLowerCase()))
            return new OptionQuestion();
        return null;
    }

    private void addSimpleQuestion(OpenQuestion openQuestion, Element eElement) {
        openQuestion.setCoefficient(Double.parseDouble(eElement.getElementsByTagName("coeff").item(0).getTextContent()));
        openQuestion.setCorrectAnswer(eElement.getElementsByTagName("answer").item(0).getTextContent());
        openQuestion.setQuestion(eElement.getElementsByTagName("name").item(0).getTextContent());
        questions.add(openQuestion);
    }

    private void addOptionQuestion(OptionQuestion optionQuestion, Element eElement) {
        optionQuestion.setCoefficient(Double.parseDouble(eElement.getElementsByTagName("coeff").item(0).getTextContent()));
        optionQuestion.setCorrectAnswer(eElement.getElementsByTagName("answer").item(0).getTextContent());
        optionQuestion.setQuestion(eElement.getElementsByTagName("name").item(0).getTextContent());
        Node options = eElement.getElementsByTagName("options").item(0);
        NodeList nList = eElement.getElementsByTagName("option"); // возможно на options вызывать надо ((Element)options).getElementsByTagName("option");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            String content = nNode.getTextContent();
            optionQuestion.getAnswerOptions().add(content);
        }
        questions.add(optionQuestion);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
