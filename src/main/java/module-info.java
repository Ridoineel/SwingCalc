module com.calculator.calculator_v1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calculator.calculator_v1 to javafx.fxml;
    exports com.calculator.calculator_v1;
}