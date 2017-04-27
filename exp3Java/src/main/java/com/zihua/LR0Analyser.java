package com.zihua;

import javafx.util.Pair;
import org.junit.*;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.*;

/**
 * LR0语法分析器
 * Created by zihua on 17-4-24.
 */
public class LR0Analyser {
    private static final String END_OF_INPUT = "end_of_input";
    private static final String ALL_OF_INPUT = "all_of_input";
    private static Glossary g = new Glossary();


    /**
     * 执行一些策略，把减法转为加，除法转为加
     *
     * @param lexResult
     * @return　返回这行策略后的vector<String>
     */
    public static Vector<String> escape(Map<String, String> lexResult) {
        Map<String, String> escapeTactics = new HashMap<String, String>();
        escapeTactics.put(g.getValue("+"), "+");
        escapeTactics.put(g.getValue("-"), "+");
        escapeTactics.put(g.getValue("*"), "*");
        escapeTactics.put(g.getValue("/"), "*");
        escapeTactics.put(g.getValue("name"), "i");
        escapeTactics.put(g.getValue("100"), "i");
        escapeTactics.put(g.getValue("("), "(");
        escapeTactics.put(g.getValue(")"), ")");
        Vector<String> res = new Vector<String>();
        Set<Map.Entry<String, String>> entry = lexResult.entrySet();
        for (Map.Entry<String, String> a : entry) {
            if (escapeTactics.get(g.getValue(a.getKey())) != null) {
                res.add(escapeTactics.get(a.getValue()));
            }
        }
        return res;
    }

    @org.junit.Test
    public void test() {


    }

}

