package com.calculator.calculator_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
    private Button clearButton;
    @FXML
    private Button equalButton;
    @FXML
    private TextField textField;
    @FXML
    private Text alertText;
    @FXML
    private Label firstHisLabel;
    @FXML
    private Label secondHisLabel;

    private String text_item;
    private double result;
    private List<String[]> history = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set textField keyPressed action
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

        // cursor hand on clear button and equal button
        clearButton.setCursor(Cursor.HAND);
        equalButton.setCursor(Cursor.HAND);
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
                textField.backward();
                textField.backward();
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
        String python_calc_file = Env.calcApiPath;
        python_calc_file = python_calc_file.replace("file:", "");

        String[] command = {"python3", python_calc_file, "-e", expression};
        Boolean success = false;

        String result_string = systemCommand(command);

        try {
            result = Double.parseDouble(result_string);
            success = true;
        }catch (Exception e) {
            // print error message
            // if text field is not empty
            if (textField.getLength() != 0) {
                alertText.setText("Invalid expression");
                alertText.setVisible(true);
            }
        }

        if (success) {
            // add to history
            history.add(new String[]{textField.getText(), result_string});
            // print result
            textField.setText(result_string);

            // refresh history labels text
            updateHistoryLabels();
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

    public void previous(ActionEvent event) {
        int lastIndex = history.size() - 1;

        if (lastIndex >= 0) {
            String[] last = history.get(lastIndex);

            // write in textField
            textField.setText(last[0]);

            // delete last of history
            history.remove(lastIndex);

            // refresh history labels text
            updateHistoryLabels();
        }
    }

    public void cancel(ActionEvent event) {
        /* delete last character of text field
        *
        * */

        int nb_chars = textField.getLength();

        if (nb_chars != 0)
            textField.setText(textField.getText().substring(0, nb_chars - 1));
    }

    public void updateHistoryLabels() {
        int lastIndex = history.size() - 1;

        String[] last = null;
        String[] behindLast = null;

        if (lastIndex >= 0) {
            last = history.get(lastIndex);
        }

        if (lastIndex > 0) {
            behindLast = history.get(lastIndex - 1);
        }


        // write history data in labels

        if (last != null) {
            firstHisLabel.setText(String.format("%s    = %s", last[0], last[1]));
        }else {
            firstHisLabel.setText("");
        }

        if (behindLast != null) {
            secondHisLabel.setText(String.format("%s    = %s", behindLast[0], behindLast[1]));
        }else {
            secondHisLabel.setText("");
        }
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