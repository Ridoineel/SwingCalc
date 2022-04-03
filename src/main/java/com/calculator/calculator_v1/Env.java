package com.calculator.calculator_v1;

public class Env {
    protected static String calcApiPath = null;
    private static boolean production = true;


    static {
        if (production) {
            //calcApiPath = "api/calc.py";
            calcApiPath = "/opt/swingcalc/api/calc.py";
        }else {
            calcApiPath = Env.class.getResource("api/calc.py").getFile();
        }
    }
}
