#include <iostream>
#include <string>
#include <vector>
#include <map>

#include "LR0Analyser.h"
#include "Analyser.h"
#include "Glossary.h"

using namespace std;

vector<string> escape(vector<pair<string, string>> lexResult);

string format(string s) {

    if (s.length() > 0) {
        if (s[0] == '-' || s[0] == '+')s = s.substr(1);
        for (int i = 0; i < s.length(); i++) {
            if (s[i] == '(' && i + 1 < s.length()) {
                s[i + 1] = ' ';
            }
        }
    }
    //cout<<s<<endl;
    return s;
}

int main() {


    LR0Analyser la;//表达式文法的生产式
    la.addProduction("E", "E", "+", "E");
    la.addProduction("E", "E", "*", "E");
    la.addProduction("E", "(", "E", ")");
    la.addProduction("E", "i");


    //LRO action表
    la.addAction(0, "(", 'S', 2);
    la.addAction(0, "i", 'S', 3);
    la.addAction(1, "+", 'S', 4);
    la.addAction(1, "*", 'S', 5);
    la.addAction(1, LR0Analyser::END_OF_INPUT, 'A', 0);
    la.addAction(2, "(", 'S', 2);
    la.addAction(2, "i", 'S', 3);
    la.addAction(3, "+", 'r', 4);
    la.addAction(3, "*", 'r', 4);
    la.addAction(3, ")", 'r', 4);
    la.addAction(3, LR0Analyser::END_OF_INPUT, 'r', 4);
    la.addAction(4, "(", 'S', 2);
    la.addAction(4, "i", 'S', 3);
    la.addAction(5, "(", 'S', 2);
    la.addAction(5, "i", 'S', 3);
    la.addAction(6, "+", 'S', 4);
    la.addAction(6, "*", 'S', 5);
    la.addAction(6, ")", 'S', 9);
    la.addAction(7, "+", 'r', 1);
    la.addAction(7, "*", 'S', 5);
    la.addAction(7, ")", 'r', 1);
    la.addAction(7, LR0Analyser::END_OF_INPUT, 'r', 1);
    la.addAction(8, "+", 'r', 2);
    la.addAction(8, "*", 'r', 2);
    la.addAction(8, ")", 'r', 2);
    la.addAction(8, LR0Analyser::END_OF_INPUT, 'r', 2);
    la.addAction(9, "+", 'r', 3);
    la.addAction(9, "*", 'r', 3);
    la.addAction(9, ")", 'r', 3);
    la.addAction(9, LR0Analyser::END_OF_INPUT, 'r', 3);

    //LRO GOTO 表
    la.addGoto(0, "E", 1);
    la.addGoto(2, "E", 6);
    la.addGoto(4, "E", 7);
    la.addGoto(5, "E", 8);

    Analyser analyser;
    string input_string;

    while (cout << "input expression: ", getline(cin, input_string)) {
        input_string = format(input_string);


        //用实验二词法分析工具生成　二元组结果
        vector<pair<string, string>> lex_result = analyser.analyseLexical(input_string);
        for (int i = 0; i < lex_result.size(); i++) {
            cout << "(" << lex_result[i].second << "," << lex_result[i].first << ")" << endl;
        }

        int error_index = -1;
        for (size_t i = 0; i < lex_result.size(); ++i) {
            if (lex_result[i].second == "unknown")
                error_index = i;
        }
        if (error_index != -1) {
            cout << "Error on: " << lex_result[error_index].first << endl;
            continue;
        }
        vector<string> escape_result = escape(lex_result);
        for (string str : escape_result)
            cout << str << " ";
        la.input(escape_result);
        cout << endl;

        //调用语法分析器分析语法是否有错
        if ((error_index = la.analyse()) != -1) {
            if (error_index == lex_result.size())
                cout << "Error on: the end of    input" << endl;
            else
                cout << "Error on: " << lex_result[error_index].first << endl;
            continue;
        }

        cout << "Right!" << endl;
    }
}


//执行一些替换策略，即把　减法转为加法，除法转乘法，变量数字统一叫做　ｉ
vector<string> escape(vector<pair<string, string>> lexResult) {
    map<string, string> escape_tactics;
    Glossary glossary;
    escape_tactics.insert(pair<string, string>(glossary.GetValue("+"), string("+")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("-"), string("+")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("*"), string("*")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("/"), string("*")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("name"), string("i")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("100"), string("i")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue("("), string("(")));
    escape_tactics.insert(pair<string, string>(glossary.GetValue(")"), string(")")));

    vector<string> result;
    for (pair<string, string> p : lexResult) {
        map<string, string>::iterator it = escape_tactics.find(p.second);
        result.push_back(it->second);
    }

    return result;
}
