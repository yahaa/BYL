package com.zihua;

import org.junit.Test;

import java.util.*;

/**
 * 词法析器
 * Created by zihua on 17-4-24.
 */
public class Analyser {
    private Glossary g = new Glossary();

    public Vector<String> analyseLexical(String inputString) {
        Vector<String> words = recognizeWord(inputString);
        Vector<String> res = new Vector<String>();
        for (String word : words) {
            res.add(g.getValue(word));
        }
        return res;
    }

    public Vector<String> recognizeWord(String inputString) {
        inputString += "\n";
        Vector<String> words = new Vector<String>();
        int start = -1;
        int n = inputString.length();
        for (int end = 0; end < n; ++end) {
            if (isSeparator(inputString.charAt(end))) {
                String sub = inputString.substring(start + 1, end);
                if (sub.length() > 0) {
                    sub = sub.toLowerCase();
                    words.add(sub);
                }
                if (issUsefulChar(inputString.charAt(end))) {
                    String temp = "";
                    if (!words.isEmpty() && g.isOperator((temp = words.lastElement() + inputString.charAt(end)))) {
                        int tn = words.size();
                        words.remove(tn - 1);
                        words.add(temp);
                    } else {
                        words.add(inputString.substring(end, end + 1));
                    }
                }
                start = end;
            }

        }
        return words;
    }

    public boolean isSeparator(Character ch) {
        String str = "+-*/=#<>:(),;. \n\t";
        return str.contains(ch.toString());
    }

    public boolean issUsefulChar(Character ch) {
        String str = "+-*/=#<>:(),;.";
        return str.contains(ch.toString());
    }


    public void analySyntax(Vector<String> lex) {
        //本来词法分析的结果应该是　二元组，但是用暴力来做只需要知道输入的表达式对应的值就好了


        boolean rOk = true;   //用于记录语法是否合法
        int countParen = 0;   //用于记录括号数，左括号　countParen++,右括号countParen--
        int others = 0;
        for (int i = 0; i < lex.size(); i++) {
            String s = lex.elementAt(i);
            if (s.equals("ident") || s.equals("number")) {
                if (!rOk) {
                    System.out.println("语法错误：缺少运算符。");
                    return;
                } else {
                    rOk = false;
                }
            } else if (s.equals("lparen")) {
                if (!rOk) {
                    System.out.println("语法错误：缺少运算符。");
                    return;
                } else {
                    countParen++;
                }
            } else if (s.equals("rparen")) {
                if (rOk) {
                    System.out.println("语法错误：缺少操作数。");
                    return;
                } else {
                    rOk = false;
                    countParen--;
                    if (countParen < 0) {
                        System.out.println("语法错误：括号不匹配。");
                        return;
                    }
                }
            } else if (s.equals("plus") || s.equals("minus") || s.equals("times") || s.equals("slash")) {
                if (rOk) {
                    System.out.println("语法错误：缺少操作数");
                    return;
                } else {
                    rOk = true;
                }
            } else {
                others++;
            }
        }
        if (others > 0) {
            System.out.println("不是表达式");
            return;
        }
        if (countParen > 0) System.out.println("语法错误：括号不匹配。");
        else System.out.println("语法正确！");

    }

    @Test
    public void test() {
        //analyseLexical 返回的是词法分析的结果，
        Vector<String> lex = analyseLexical("a-b+c");
        //System.out.println(lex);
        //拿到词法分析的结果后用于语法分析（这里的语法分析只是分析分析表达式　如 a+b ,(a+b)*c）
        //在PL0语言中，表达式无非就是　(ident,number,lparen,rparen,plus,minus,times,slash)　的组合，所以用暴力也可以分析
        analySyntax(lex);
    }


    public static void main(String[] args) {
        Analyser analyser = new Analyser();
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String s = input.nextLine();
            Vector<String> lex = analyser.analyseLexical(s);
            analyser.analySyntax(lex);
        }
    }

}
