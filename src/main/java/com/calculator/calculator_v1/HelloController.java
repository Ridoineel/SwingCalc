package com.calculator.calculator_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    private TextField textField;
    @FXML
    private Text alertText;
    private String text_item;
    private double result;
    private Map<String, Double> history = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    printResult();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                alertText.setVisible(false);
            }
        });
    }

    public void addToExpression(Button target) {
        text_item = target.getText();

        if ("+-x÷".contains(text_item)) {
            if (textField.getLength() != 0) {
                text_item = " " + text_item;
            }

            text_item += " ";
        }

        switch (text_item) {
            case "(":
                if (textField.getLength() != 0)
                    text_item = " " + text_item;
                break;
            case ")":
                text_item += " ";
                break;
            case "x²":
                text_item = "^2";
                break;
        }

        // add item to text field
        textField.setText(textField.getText() + text_item);
        // set alertText as hidden
        alertText.setVisible(false);
    }

    public void addToExpression(ActionEvent event) {
        Button button = (Button) event.getTarget();

        addToExpression(button);
    }

    public void clear(ActionEvent event) {
        textField.clear();
    }

    public void printResult() throws IOException {
        String expression = parseExpression(textField.getText());

        String python_eval = String.format("print(eval('%s'))", expression);
        String[] command = {"python3", "-c", python_eval};
        Boolean success = false;

        String result_string = systemCommand(command);

        try {
            result = Double.parseDouble(result_string);
            success = true;
        }catch (Exception e) {
            alertText.setText("Invalid expression");
            alertText.setVisible(true);
        }

        if (success) {
            // add to history
            history.put(textField.getText(), result);
            // print result
            textField.setText(result_string);
        }
    }

    public void printResult(ActionEvent event) throws IOException {
        printResult();
    }

    private String parseExpression(String text) {
        String parsedText = "";

        for (int i=0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '%':
                    parsedText += "/100";
                    break;
                case '÷':
                    parsedText += "/";
                    break;
                case 'x':
                    parsedText += "*";
                    break;
                case '^':
                    parsedText += "**";
                    break;
                case ',':
                    parsedText += ".";
                    break;
                default:
                    parsedText += text.charAt(i);
            }
        }

        return parsedText;
    }

    public String systemCommand(String[] command) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(command);

        String output = "";
        String s = null;

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream())
        );

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream())
        );


        while ((s = stdInput.readLine()) != null) {
            output += s + "\n";
        }

        while ((s = stdError.readLine()) != null) {
            output += s + "\n";
        }

        return output;
    }
}