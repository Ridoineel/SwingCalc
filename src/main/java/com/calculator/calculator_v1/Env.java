package com.calculator.calculator_v1;

public class Env {
    protected static String calcApiPath = null;
    private static boolean production;
    private static boolean build_linux;


    static {
        production = true;
        build_linux = false;

        if (production) {
            if (build_linux)
                calcApiPath = "/opt/swingcalc/api/calc.py";
            else
                calcApiPath = "api/calc.py";

        }else {
            calcApiPath = Env.class.getResource("api/calc.py").getFile();
        }
    }
}
