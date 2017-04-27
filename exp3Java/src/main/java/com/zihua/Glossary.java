package com.zihua;


import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.*;

import org.junit.Test;


/**
 * 词法表
 * Created by zihua on 17-4-23.
 */
public class Glossary {
    private Map<String, String> basicWords = new HashMap<String, String>();
    private Map<String, String> operators = new HashMap<String, String>();
    private Map<String, String> delimiters = new HashMap<String, String>();

    public Glossary() {
        basicWords.put("begin", "beginsym");
        basicWords.put("call", "callsym");
        basicWords.put("const", "constsym");
        basicWords.put("do", "dosym");
        basicWords.put("end", "endsym");
        basicWords.put("if", "ifsym");
        basicWords.put("odd", "oddsym");
        basicWords.put("procedure", "proceduresym");
        basicWords.put("read", "readsym");
        basicWords.put("then", "thensym");
        basicWords.put("var", "varsym");
        basicWords.put("while", "whilesym");
        basicWords.put("write", "writesym");


        operators.put("+", "plus");
        operators.put("-", "minus");
        operators.put("*", "times");
        operators.put("/", "slash");
        operators.put("=", "eql");
        operators.put("#", "neq");
        operators.put("<", "lss");
        operators.put("<=", "leq");
        operators.put(">", "gtr");
        operators.put(">=", "geq");
        operators.put(":=", "becomes");

        delimiters.put("(", "lparen");
        delimiters.put(")", "rparen");
        delimiters.put(",", "comma");
        delimiters.put(";", "semicolon");
        delimiters.put(".", "period");
    }

    public String getValue(String key) {
        String res = null;
        if (isNumber(key)) return "number";
        if ((res = basicWords.get(key)) != null) return res;
        if ((res = operators.get(key)) != null) return res;
        if ((res = delimiters.get(key)) != null) return res;
        if (canBeIdentifier(key)) return "ident";
        return "unknown";
    }

    public boolean isOperator(String str) {
        return operators.get(str) != null ? true : false;
    }

    public boolean isNumber(String key) {
        try {
            Double k = Double.valueOf(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canBeIdentifier(String key) {
        if (key.length() == 0) return false;
        if (isDigit(key.charAt(0))) return false;
        key = key.replaceAll("[a-zA-Z0-9]+", "");
        if (key.length() > 0) return false;
        return true;
    }

    @Test
    public void test() {
        System.out.println(isNumber("+1234"));
        System.out.println(canBeIdentifier("0abc"));
        System.out.println(getValue("/"));
        System.out.println(isOperator("="));

    }
}
