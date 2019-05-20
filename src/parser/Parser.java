package parser;

import lexer.Lexer;
import lexer.Token;

import java.io.IOException;
import java.util.*;

public class Parser {
    private Map<String, String[]> firsts;
    private Map<String, String[]> follows;
    private Map<String, Stack<Integer>> stacks;
    private Map<String, String> simplests;
    static Token nextToken;
    static Lexer lexer;
    private Formatter outputFormatter;
    private Formatter errorFormatter;

    public Parser() {
        this.stacks = new HashMap<>();
        initStack();
        try {
            lexer = new Lexer("input.txt");
            outputFormatter = new Formatter("parser.txt");
            outputFormatter.format("program\n");
            errorFormatter = lexer.getErrorFormatter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.firsts = new HashMap<>();
        this.follows = new HashMap<>();
        this.simplests = new HashMap<>();
        initFirsts();
        initFollows();
        initSimplests();

    }

    private void initSimplests() {
        simplests.put("declaration_list", "int ID;");
        simplests.put("declaration", "int ID;");
        simplests.put("s1", "ID;");
        simplests.put("s10", ";");
        simplests.put("type_specifier", "int");
        simplests.put("params", "int ID");
        simplests.put("s11", "ID");
        simplests.put("s2", ", int ID");
        simplests.put("param", "int ID");
        simplests.put("s3", "[]");
        simplests.put("compound_stmt", "{ int ID;}");
        simplests.put("statement_list", ";");
        simplests.put("statement", ";");
        simplests.put("expression_stmt", ";");
        simplests.put("selection_stmt", "if (NUM) ; else ;");
        simplests.put("iteration_stmt", "while (NUM) ;");
        simplests.put("switch_stmt", "switch ( ID ) {}");
        simplests.put("return_stmt", "return;");
        simplests.put("case_stmts", "case NUM: ;");
        simplests.put("case_stmt", "case NUM: ;");
        simplests.put("default_stmt", "default NUM: ;");
        simplests.put("expression", "ID");
        simplests.put("s12", "[ID]");
        simplests.put("s13", "= ID");
        simplests.put("s5", "[ID]");
        simplests.put("var", "ID");
        simplests.put("signed_factor", "(ID)");
        simplests.put("s8", "* ID");
        simplests.put("additive_expression", "(ID)");
        simplests.put("relop", "==");
        simplests.put("s6", "< (ID)");
        simplests.put("addop", "+");
        simplests.put("factor", "(ID)");
        simplests.put("s15", "()");
        simplests.put("term", "(ID)");
        simplests.put("s7", "+(ID)");
        simplests.put("call", "ID()");
        simplests.put("args", "ID");
        simplests.put("arg_list", "ID");
        simplests.put("s9", ", ID");
        simplests.put("s16", ";");
    }

    private void initFollows() {
        follows.put("program", new String[]{"EOF"});
        follows.put("declaration_list", new String[]{"continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "EOF"});
        follows.put("declaration", new String[]{"int", "void", "continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while", "EOF"});
        follows.put("s1", new String[]{"int", "void", "continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while", "EOF"});
        follows.put("s10", new String[]{"int", "void", "continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while", "EOF"});
        follows.put("type_specifier", new String[]{"ID"});
        follows.put("params", new String[]{")"});
        follows.put("s11", new String[]{")"});
        follows.put("s2", new String[]{")"});
        follows.put("param", new String[]{",", ")"});
        follows.put("s3", new String[]{",", ")"});
        follows.put("compound_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "int", "void", "EOF", "}", "case", "default"});
        follows.put("statement_list", new String[]{"}", "case", "ID", "(", "NUM", "+", "-", "default"});
        follows.put("statement", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("expression_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("selection_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("iteration_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("switch_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("return_stmt", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "+", "-", "if", "return", "{", "switch", "while", "}", "case", "default"});
        follows.put("case_stmts", new String[]{"ID", "(", "NUM", "+", "-", "default"});
        follows.put("case_stmt", new String[]{"case", "ID", "(", "NUM", "+", "-", "default"});
        follows.put("default_stmt", new String[]{"}"});
        follows.put("expression", new String[]{";", ")", "]", ","});
        follows.put("s12", new String[]{";", ")", "]", ","});
        follows.put("s13", new String[]{";", ")", "]", ","});
        follows.put("s5", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("var", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("signed_factor", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("s8", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("additive_expression", new String[]{"<", "=="});
        follows.put("relop", new String[]{"+", "-", "(", "NUM", "ID"});
        follows.put("s6", new String[]{",", ")", "]", ";"});
        follows.put("addop", new String[]{"-", "+", "ID", "(", "NUM"});
        follows.put("factor", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("s15", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("term", new String[]{"+", "-", "ID", "(", "NUM", "<", "==", ",", ")", "]", ";"});
        follows.put("s7", new String[]{"ID", "(", "NUM", "-", "+", "<", "==", ",", ")", "]", ";"});
        follows.put("call", new String[]{"*", "+", "-", "<", "==", ")", "ID", "(", "NUM", ",", "]", ";"});
        follows.put("args", new String[]{")"});
        follows.put("arg_list", new String[]{")"});
        follows.put("s9", new String[]{")"});
        follows.put("s16", new String[]{"else", "continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while", "}", "case", "default"});
    }

    private void initFirsts() {
        firsts.put("program", new String[]{"EOF", "int", "void"});
        firsts.put("declaration_list", new String[]{"epsilon", "int", "void"});
        firsts.put("declaration", new String[]{"int", "void"});
        firsts.put("s1", new String[]{"ID"});
        firsts.put("s10", new String[]{";", "[", "("});
        firsts.put("type_specifier", new String[]{"int", "void"});
        firsts.put("params", new String[]{"int", "void"});
        firsts.put("s11", new String[]{"ID", "epsilon"});
        firsts.put("s2", new String[]{",", "epsilon"});
        firsts.put("param", new String[]{"int", "void"});
        firsts.put("s3", new String[]{"epsilon", "["});
        firsts.put("compound_stmt", new String[]{"{"});
        firsts.put("statement_list", new String[]{"epsilon", "continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while"});
        firsts.put("statement", new String[]{"continue", "break", ";", "ID", "(", "NUM", "-", "+", "if", "return", "{", "switch", "while"});
        firsts.put("expression_stmt", new String[]{"continue", "break", ";", "ID", "(", "NUM", "-", "+"});
        firsts.put("selection_stmt", new String[]{"if"});
        firsts.put("iteration_stmt", new String[]{"while"});
        firsts.put("return_stmt", new String[]{"return"});
        firsts.put("switch_stmt", new String[]{"switch"});
        firsts.put("case_stmts", new String[]{"case", "epsilon"});
        firsts.put("case_stmt", new String[]{"case"});
        firsts.put("default_stmt", new String[]{"epsilon", "default"});
        firsts.put("expression", new String[]{"+", "-", "ID", "(", "NUM"});
        firsts.put("s12", new String[]{"=", "[", "(", "*", "epsilon", "+", "-", "<", "=="});
        firsts.put("s13", new String[]{"=", "*", "epsilon", "+", "-", "<", "=="});
        firsts.put("var", new String[]{"ID"});
        firsts.put("s5", new String[]{"[", "epsilon"});
        firsts.put("simple_expression", new String[]{"-", "+", "ID", "(", "NUM"});
        firsts.put("s6", new String[]{"epsilon", "<", "=="});
        firsts.put("relop", new String[]{"<", "=="});
        firsts.put("additive_expression", new String[]{"-", "+", "ID", "(", "NUM"});
        firsts.put("s7", new String[]{"-", "+", "epsilon"});
        firsts.put("addop", new String[]{"+", "-"});
        firsts.put("term", new String[]{"-", "+", "ID", "(", "NUM"});
        firsts.put("s8", new String[]{"epsilon", "*"});
        firsts.put("signed_factor", new String[]{"-", "+", "ID", "(", "NUM"});
        firsts.put("factor", new String[]{"ID", "(", "NUM"});
        firsts.put("call", new String[]{"ID"});
        firsts.put("args", new String[]{"epsilon", "ID", "(", "NUM", "-", "+"});
        firsts.put("arg_list", new String[]{"ID", "(", "NUM", "-", "+"});
        firsts.put("s9", new String[]{",", "epsilon"});
        firsts.put("s15", new String[]{"(", "[", "epsilon"});
        firsts.put("s16", new String[]{";", "ID", "(", "NUM", "-", "+"});
    }

    private void initStack() {
        stacks.put("program", new Stack<>());
        stacks.put("declaration_list", new Stack<>());
        stacks.put("declaration", new Stack<>());
        stacks.put("s1", new Stack<>());
        stacks.put("s10", new Stack<>());
        stacks.put("type_specifier", new Stack<>());
        stacks.put("params", new Stack<>());
        stacks.put("s11", new Stack<>());
        stacks.put("s2", new Stack<>());
        stacks.put("param", new Stack<>());
        stacks.put("s3", new Stack<>());
        stacks.put("compound_stmt", new Stack<>());
        stacks.put("statement_list", new Stack<>());
        stacks.put("statement", new Stack<>());
        stacks.put("expression_stmt", new Stack<>());
        stacks.put("selection_stmt", new Stack<>());
        stacks.put("iteration_stmt", new Stack<>());
        stacks.put("return_stmt", new Stack<>());
        stacks.put("switch_stmt", new Stack<>());
        stacks.put("case_stmts", new Stack<>());
        stacks.put("case_stmt", new Stack<>());
        stacks.put("default_stmt", new Stack<>());
        stacks.put("expression", new Stack<>());
        stacks.put("s12", new Stack<>());
        stacks.put("s13", new Stack<>());
        stacks.put("var", new Stack<>());
        stacks.put("s5", new Stack<>());
        stacks.put("simple_expression", new Stack<>());
        stacks.put("s6", new Stack<>());
        stacks.put("relop", new Stack<>());
        stacks.put("additive_expression", new Stack<>());
        stacks.put("s7", new Stack<>());
        stacks.put("addop", new Stack<>());
        stacks.put("term", new Stack<>());
        stacks.put("s8", new Stack<>());
        stacks.put("signed_factor", new Stack<>());
        stacks.put("factor", new Stack<>());
        stacks.put("call", new Stack<>());
        stacks.put("args", new Stack<>());
        stacks.put("arg_list", new Stack<>());
        stacks.put("s9", new Stack<>());
        stacks.put("s15", new Stack<>());
        stacks.put("s16", new Stack<>());
    }


    public void parse(Parser parser) {
        try {
            nextToken = lexer.get_next_token();
            moveOnDiagrams(parser, TransitionStates.program_0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveOnDiagrams(Parser parser, TransitionStates transitionState) {
        switch (transitionState) {
            case program_0:
                stacks.get("program").push(0);
                stacks.get("program").push(0);
                if (isValidNonTerminal("declaration_list")) {
                    pushToOtherStack("program", "declaration_list");
                    moveOnDiagrams(parser, TransitionStates.declaration_list_0);
                    moveOnDiagrams(parser, TransitionStates.program_1);
                }

                break;
            case program_1:
                if (isValidTerminal("EOF", transitionState)) {
                    writeToOutput("EOF", stacks.get("program").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.program_2);
                } else {
                    errorFormatter.format("Parser : %d : Syntax Error! Malformed Input\n", lexer.getLineNumber());
                    errorFormatter.close();
                    outputFormatter.close();
                    lexer.getOutputFormatter().close();
                    System.exit(0);
                }
                return;
            case program_2:
                return;
            case declaration_list_0:
                if (isValidNonTerminal("declaration")) {
                    pushToItsStack("declaration_list", 2);
                    pushToOtherStack("declaration_list", "declaration");
                    moveOnDiagrams(parser, TransitionStates.declaration_0);
                    moveOnDiagrams(parser, TransitionStates.declaration_list_1);
                } else if (isValidEpsilon("declaration_list")) {
                    moveOnDiagrams(parser, TransitionStates.declaration_list_2);
                }
                break;
            case declaration_list_1:
                if (isValidNonTerminal("declaration_list")) {
                    pushToOtherStack("declaration_list", "declaration_list");
                    moveOnDiagrams(parser, TransitionStates.declaration_list_0);
                    moveOnDiagrams(parser, TransitionStates.declaration_list_2);
                } else {
                    int res = handleNonterminalErrors("declaration_list");
                    if (res == 2) {
                        popFromStack("declaration_list");
                        writeToError("declaration_list", 2);
                        moveOnDiagrams(parser, TransitionStates.declaration_list_2);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.declaration_list_1);
                    }
                }
                break;
            case declaration_list_2:
                return;
            case declaration_0:
                if (isValidNonTerminal("type_specifier")) {
                    pushToItsStack("declaration", 2);
                    pushToOtherStack("declaration", "type_specifier");
                    moveOnDiagrams(parser, TransitionStates.type_specifier_0);
                    moveOnDiagrams(parser, TransitionStates.declaration_1);
                }
                break;
            case declaration_1:
                handleNonzeroNonterminalStates(parser, "s1", "declaration", TransitionStates.s1_0, TransitionStates.declaration_2, TransitionStates.declaration_1);
                break;
            case declaration_2:
                return;
            case s1_0:
                if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("s1", 2);
                    writeToOutput("ID", stacks.get("s1").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s1_1);
                }
                break;
            case s1_1:
                handleNonzeroNonterminalStates(parser, "s10", "s1", TransitionStates.s10_0, TransitionStates.s1_2, TransitionStates.s1_1);
                break;
            case s1_2:
                return;
            case s10_0:
                if (isValidTerminal("(", transitionState)) {
                    pushToItsStack("s10", 4);
                    writeToOutput("(", stacks.get("s10").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s10_1);
                } else if (isValidTerminal("[", transitionState)) {
                    pushToItsStack("s10", 4);
                    writeToOutput("[", stacks.get("s10").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s10_5);
                } else if (isValidTerminal(";", transitionState)) {
                    pushToItsStack("s10", 1);
                    writeToOutput(";", stacks.get("s10").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s10_4);
                }
                break;
            case s10_1:
                handleNonzeroNonterminalStates(parser, "params", "s10", TransitionStates.params_0, TransitionStates.s10_2, TransitionStates.s10_1);
                break;
            case s10_2:
                handleNonzeroTerminalStates(parser, transitionState, ")", "s10", TransitionStates.s10_3);
                break;
            case s10_3:
                handleNonzeroNonterminalStates(parser, "compound_stmt", "s10", TransitionStates.compound_stmt_0, TransitionStates.s10_4, TransitionStates.s10_3);
                break;
            case s10_4:
                return;
            case s10_5:
                handleNonzeroTerminalStates(parser, transitionState, "NUM", "s10", TransitionStates.s10_6);
                break;
            case s10_6:
                handleNonzeroTerminalStates(parser, transitionState, "]", "s10", TransitionStates.s10_7);
                break;
            case s10_7:
                handleNonzeroTerminalStates(parser, transitionState, ";", "s10", TransitionStates.s10_4);
                break;
            case type_specifier_0:
                if (isValidTerminal("int", transitionState)) {
                    pushToItsStack("type_specifier", 1);
                    writeToOutput("int", stacks.get("type_specifier").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.type_specifier_1);
                } else if (isValidTerminal("void", transitionState)) {
                    pushToItsStack("type_specifier", 1);
                    writeToOutput("void", stacks.get("type_specifier").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.type_specifier_1);
                }
                break;
            case type_specifier_1:
                return;
            case params_0:
                if (isValidTerminal("int", transitionState)) {
                    pushToItsStack("params", 4);
                    writeToOutput("int", stacks.get("params").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.params_1);
                } else if (isValidTerminal("void", transitionState)) {
                    pushToItsStack("params", 2);
                    writeToOutput("void", stacks.get("params").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.params_5);
                }
                break;
            case params_1:
                handleNonzeroTerminalStates(parser, transitionState, "ID", "params", TransitionStates.params_2);
                break;
            case params_2:
                handleNonzeroNonterminalStates(parser, "s3", "params", TransitionStates.s3_0, TransitionStates.params_3, TransitionStates.params_2);
                break;
            case params_3:
                handleNonzeroNonterminalStates(parser, "s2", "params", TransitionStates.s2_0, TransitionStates.params_4, TransitionStates.params_3);
                break;
            case params_4:
                return;
            case params_5:
                handleNonzeroNonterminalStates(parser, "s11", "params", TransitionStates.s11_0, TransitionStates.params_4, TransitionStates.params_5);
                break;
            case s11_0:
                if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("s11", 3);
                    writeToOutput("ID", stacks.get("s11").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s11_1);
                } else if (isValidEpsilon("s11"))
                    moveOnDiagrams(parser, TransitionStates.s11_3);
                break;
            case s11_1:
                handleNonzeroNonterminalStates(parser, "s3", "s11", TransitionStates.s3_0, TransitionStates.s11_2, TransitionStates.s11_1);
                break;
            case s11_2:
                handleNonzeroNonterminalStates(parser, "s2", "s11", TransitionStates.s2_0, TransitionStates.s11_3, TransitionStates.s11_2);
                break;
            case s11_3:
                return;
            case s2_0:
                if (isValidTerminal(",", transitionState)) {
                    pushToItsStack("s2", 3);
                    writeToOutput(",", stacks.get("s2").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s2_1);
                } else if (isValidEpsilon("s2"))
                    moveOnDiagrams(parser, TransitionStates.s2_3);
                break;
            case s2_1:
                handleNonzeroNonterminalStates(parser, "param", "s2", TransitionStates.param_0, TransitionStates.s2_2, TransitionStates.s2_1);
                break;
            case s2_2:
                if (isValidNonTerminal("s2")) {
                    pushToOtherStack("s2", "s2");
                    moveOnDiagrams(parser, TransitionStates.s2_0);
                    moveOnDiagrams(parser, TransitionStates.s2_3);
                } else {
                    int res = handleNonterminalErrors("s2");
                    if (res == 2) {
                        popFromStack("s2");
                        writeToError("s2", 2);
                        moveOnDiagrams(parser, TransitionStates.s2_3);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.s2_2);
                    }
                }
                break;
            case s2_3:
                return;
            case param_0:
                if (isValidNonTerminal("type_specifier")) {
                    pushToItsStack("param", 3);
                    pushToOtherStack("param", "type_specifier");
                    moveOnDiagrams(parser, TransitionStates.type_specifier_0);
                    moveOnDiagrams(parser, TransitionStates.param_1);
                }
                break;
            case param_1:
                handleNonzeroTerminalStates(parser, transitionState, "ID", "param", TransitionStates.param_2);
                break;
            case param_2:
                handleNonzeroNonterminalStates(parser, "s3", "param", TransitionStates.s3_0, TransitionStates.param_3, TransitionStates.param_2);
                break;
            case param_3:
                return;
            case s3_0:
                if (isValidTerminal("[", transitionState)) {
                    pushToItsStack("s3", 2);
                    writeToOutput("[", stacks.get("s3").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s3_1);
                } else if (isValidEpsilon("s3"))
                    moveOnDiagrams(parser, TransitionStates.s3_2);
                break;
            case s3_1:
                if (!isValidTerminal("]", transitionState)) {
                    writeToError("]", 0);
                }
                writeToOutput("]", stacks.get("s3").pop());
                moveOnDiagrams(parser, TransitionStates.s3_2);
                break;
            case s3_2:
                return;
            case compound_stmt_0:
                if (isValidTerminal("{", transitionState)) {
                    pushToItsStack("compound_stmt", 4);
                    writeToOutput("{", stacks.get("compound_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.compound_stmt_1);
                }
                break;
            case compound_stmt_1:
                handleNonzeroNonterminalStates(parser, "declaration_list", "compound_stmt", TransitionStates.declaration_list_0, TransitionStates.compound_stmt_2, TransitionStates.compound_stmt_1);
                break;
            case compound_stmt_2:
                handleNonzeroNonterminalStates(parser, "statement_list", "compound_stmt", TransitionStates.statement_list_0, TransitionStates.compound_stmt_3, TransitionStates.compound_stmt_2);
                break;
            case compound_stmt_3:
                handleNonzeroTerminalStates(parser, transitionState, "}", "compound_stmt", TransitionStates.compound_stmt_4);
            case compound_stmt_4:
                return;
            case statement_list_0:
                if (isValidNonTerminal("statement")) {
                    pushToItsStack("statement_list", 2);
                    pushToOtherStack("statement_list", "statement");
                    moveOnDiagrams(parser, TransitionStates.statement_0);
                    moveOnDiagrams(parser, TransitionStates.statement_list_1);
                } else if (isValidEpsilon("statement_list"))
                    moveOnDiagrams(parser, TransitionStates.statement_list_2);
                break;
            case statement_list_1:
                if (isValidNonTerminal("statement_list")) {
                    pushToOtherStack("statement_list", "statement_list");
                    moveOnDiagrams(parser, TransitionStates.statement_list_0);
                    moveOnDiagrams(parser, TransitionStates.statement_list_2);
                } else {
                    int res = handleNonterminalErrors("statement_list");
                    if (res == 2) {
                        popFromStack("statement_list");
                        writeToError("statement_list", 2);
                        moveOnDiagrams(parser, TransitionStates.statement_list_2);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.statement_list_1);
                    }
                }

                break;
            case statement_list_2:
                return;
            case statement_0:
                if (isValidNonTerminal("expression_stmt")) {
                    pushToOtherStack("statement", "expression_stmt");
                    moveOnDiagrams(parser, TransitionStates.expression_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                } else if (isValidNonTerminal("selection_stmt")) {
                    pushToOtherStack("statement", "selection_stmt");
                    moveOnDiagrams(parser, TransitionStates.selection_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                } else if (isValidNonTerminal("iteration_stmt")) {
                    pushToOtherStack("statement", "iteration_stmt");
                    moveOnDiagrams(parser, TransitionStates.iteration_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                } else if (isValidNonTerminal("return_stmt")) {
                    pushToOtherStack("statement", "return_stmt");
                    moveOnDiagrams(parser, TransitionStates.return_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                } else if (isValidNonTerminal("switch_stmt")) {
                    pushToOtherStack("statement", "switch_stmt");
                    moveOnDiagrams(parser, TransitionStates.switch_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                } else if (isValidNonTerminal("compound_stmt")) {
                    pushToOtherStack("statement", "compound_stmt");
                    moveOnDiagrams(parser, TransitionStates.compound_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.statement_1);
                }
                break;
            case statement_1:
                return;
            case expression_stmt_0:
                if (isValidNonTerminal("expression")) {
                    pushToItsStack("expression_stmt", 2);
                    pushToOtherStack("expression_stmt", "expression");
                    moveOnDiagrams(parser, TransitionStates.expression_0);
                    moveOnDiagrams(parser, TransitionStates.expression_stmt_1);
                } else if (isValidTerminal("continue", transitionState)) {
                    pushToItsStack("expression_stmt", 2);
                    writeToOutput("continue", stacks.get("expression_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_stmt_1);
                } else if (isValidTerminal("break", transitionState)) {
                    pushToItsStack("expression_stmt", 2);
                    writeToOutput("break", stacks.get("expression_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_stmt_1);
                } else if (isValidTerminal(";", transitionState)) {
                    writeToOutput(";", stacks.get("expression_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_stmt_2);
                }
                break;
            case expression_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, ";", "expression_stmt", TransitionStates.expression_stmt_2);
                break;
            case expression_stmt_2:
                return;
            case selection_stmt_0:
                if (isValidTerminal("if", transitionState)) {
                    pushToItsStack("selection_stmt", 7);
                    writeToOutput("if", stacks.get("selection_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.selection_stmt_1);
                }
                break;
            case selection_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, "(", "selection_stmt", TransitionStates.selection_stmt_2);
                break;
            case selection_stmt_2:
                handleNonzeroNonterminalStates(parser, "expression", "selection_stmt", TransitionStates.expression_0, TransitionStates.selection_stmt_3, TransitionStates.selection_stmt_2);
                break;
            case selection_stmt_3:
                handleNonzeroTerminalStates(parser, transitionState, ")", "selection_stmt", TransitionStates.selection_stmt_4);
                break;
            case selection_stmt_4:
                handleNonzeroNonterminalStates(parser, "statement", "selection_stmt", TransitionStates.statement_0, TransitionStates.selection_stmt_5, TransitionStates.selection_stmt_4);
                break;
            case selection_stmt_5:
                handleNonzeroTerminalStates(parser, transitionState, "else", "selection_stmt", TransitionStates.selection_stmt_6);
                break;
            case selection_stmt_6:
                handleNonzeroNonterminalStates(parser, "statement", "selection_stmt", TransitionStates.statement_0, TransitionStates.selection_stmt_7, TransitionStates.selection_stmt_6);
                break;
            case selection_stmt_7:
                return;
            case iteration_stmt_0:
                if (isValidTerminal("while", transitionState)) {
                    pushToItsStack("iteration_stmt", 5);
                    writeToOutput("while", stacks.get("iteration_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.iteration_stmt_1);
                }
                break;
            case iteration_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, "(", "iteration_stmt", TransitionStates.iteration_stmt_2);
                break;
            case iteration_stmt_2:
                handleNonzeroNonterminalStates(parser, "expression", "iteration_stmt", TransitionStates.expression_0, TransitionStates.iteration_stmt_3, TransitionStates.iteration_stmt_2);
                break;
            case iteration_stmt_3:
                handleNonzeroTerminalStates(parser, transitionState, ")", "iteration_stmt", TransitionStates.iteration_stmt_4);
                break;
            case iteration_stmt_4:
                handleNonzeroNonterminalStates(parser, "statement", "iteration_stmt", TransitionStates.statement_0, TransitionStates.iteration_stmt_5, TransitionStates.iteration_stmt_4);
                break;
            case iteration_stmt_5:
                return;
            case return_stmt_0:
                if (isValidTerminal("return", transitionState)) {
                    pushToItsStack("return_stmt", 2);
                    writeToOutput("return", stacks.get("return_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.return_stmt_1);
                }
                break;
            case return_stmt_1:
                handleNonzeroNonterminalStates(parser, "s16", "return_stmt", TransitionStates.s16_0, TransitionStates.return_stmt_2, TransitionStates.return_stmt_1);
                break;
            case return_stmt_2:
                return;
            case switch_stmt_0:
                if (isValidTerminal("switch", transitionState)) {
                    pushToItsStack("switch_stmt", 8);
                    writeToOutput("switch", stacks.get("switch_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.switch_stmt_1);
                }
                break;
            case switch_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, "(", "switch_stmt", TransitionStates.switch_stmt_2);
                break;
            case switch_stmt_2:
                handleNonzeroNonterminalStates(parser, "expression", "switch_stmt", TransitionStates.expression_0, TransitionStates.switch_stmt_3, TransitionStates.switch_stmt_2);
                break;
            case switch_stmt_3:
                handleNonzeroTerminalStates(parser, transitionState, ")", "switch_stmt", TransitionStates.switch_stmt_4);
                break;
            case switch_stmt_4:
                handleNonzeroTerminalStates(parser, transitionState, "{", "switch_stmt", TransitionStates.switch_stmt_5);
                break;
            case switch_stmt_5:
                handleNonzeroNonterminalStates(parser, "case_stmts", "switch_stmt", TransitionStates.case_stmts_0, TransitionStates.switch_stmt_6, TransitionStates.switch_stmt_5);

                break;
            case switch_stmt_6:
                handleNonzeroNonterminalStates(parser, "default_stmt", "switch_stmt", TransitionStates.default_stmt_0, TransitionStates.switch_stmt_7, TransitionStates.switch_stmt_6);
                break;
            case switch_stmt_7:
                handleNonzeroTerminalStates(parser, transitionState, "}", "switch_stmt", TransitionStates.switch_stmt_8);
                break;
            case switch_stmt_8:
                return;
            case case_stmts_0:
                if (isValidNonTerminal("case_stmt")) {
                    pushToItsStack("case_stmts", 2);
                    pushToOtherStack("case_stmts", "case_stmt");
                    moveOnDiagrams(parser, TransitionStates.case_stmt_0);
                    moveOnDiagrams(parser, TransitionStates.case_stmts_1);
                } else if (isValidEpsilon("case_stmts"))
                    moveOnDiagrams(parser, TransitionStates.case_stmts_2);
                break;
            case case_stmts_1:
                if (isValidNonTerminal("case_stmts")) {
                    pushToOtherStack("case_stmts", "case_stmts");
                    moveOnDiagrams(parser, TransitionStates.case_stmts_0);
                } else {
                    int res = handleNonterminalErrors("case_stmts");
                    if (res == 2) {
                        popFromStack("case_stmts");
                        writeToError("case_stmts", 2);
                        moveOnDiagrams(parser, TransitionStates.case_stmts_0);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.case_stmts_1);
                    }
                }

                break;
            case case_stmts_2:
                return;
            case case_stmt_0:
                if (isValidTerminal("case", transitionState)) {
                    pushToItsStack("case_stmt", 4);
                    writeToOutput("case", stacks.get("case_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.case_stmt_1);
                }
                break;
            case case_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, "NUM", "case_stmt", TransitionStates.case_stmt_2);
                break;
            case case_stmt_2:
                handleNonzeroTerminalStates(parser, transitionState, ":", "case_stmt", TransitionStates.case_stmt_3);
                break;
            case case_stmt_3:
                handleNonzeroNonterminalStates(parser, "statement_list", "case_stmt", TransitionStates.statement_list_0, TransitionStates.case_stmt_4, TransitionStates.case_stmt_3);

                break;
            case case_stmt_4:
                return;
            case default_stmt_0:
                if (isValidTerminal("default", transitionState)) {
                    pushToItsStack("default_stmt", 3);
                    writeToOutput("default", stacks.get("default_stmt").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.default_stmt_1);
                } else if (isValidEpsilon("default_stmt"))
                    moveOnDiagrams(parser, TransitionStates.default_stmt_3);
                break;
            case default_stmt_1:
                handleNonzeroTerminalStates(parser, transitionState, ":", "default_stmt", TransitionStates.default_stmt_2);
                break;
            case default_stmt_2:
                handleNonzeroNonterminalStates(parser, "statement_list", "default_stmt", TransitionStates.statement_list_0, TransitionStates.default_stmt_3, TransitionStates.default_stmt_2);

                break;
            case default_stmt_3:
                return;
            case expression_0:
                if (isValidTerminal("(", transitionState)) {
                    pushToItsStack("expression", 6);
                    writeToOutput("(", stacks.get("expression").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_1);
                } else if (isValidTerminal("NUM", transitionState)) {
                    pushToItsStack("expression", 4);
                    writeToOutput("NUM", stacks.get("expression").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_3);
                } else if (isValidTerminal("+", transitionState)) {
                    pushToItsStack("expression", 5);
                    writeToOutput("+", stacks.get("expression").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_7);
                } else if (isValidTerminal("-", transitionState)) {
                    pushToItsStack("expression", 5);
                    writeToOutput("-", stacks.get("expression").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_8);
                } else if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("expression", 2);
                    writeToOutput("ID", stacks.get("expression").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.expression_9);
                }
                break;
            case expression_1:
                if (isValidNonTerminal("expression")) {
                    pushToOtherStack("expression", "expression");
                    moveOnDiagrams(parser, TransitionStates.expression_0);
                    moveOnDiagrams(parser, TransitionStates.expression_2);
                } else {
                    int res = handleNonterminalErrors("expression");
                    if (res == 2) {
                        popFromStack("expression");
                        writeToError("expression", 2);
                        moveOnDiagrams(parser, TransitionStates.expression_2);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.expression_1);
                    }
                }
                break;
            case expression_2:
                handleNonzeroTerminalStates(parser, transitionState, ")", "expression", TransitionStates.expression_3);
                break;
            case expression_3:
                handleNonzeroNonterminalStates(parser, "s8", "expression", TransitionStates.s8_0, TransitionStates.expression_4, TransitionStates.expression_3);
                break;
            case expression_4:
                handleNonzeroNonterminalStates(parser, "s7", "expression", TransitionStates.s7_0, TransitionStates.expression_5, TransitionStates.expression_4);
                break;
            case expression_5:
                handleNonzeroNonterminalStates(parser, "s6", "expression", TransitionStates.s6_0, TransitionStates.expression_6, TransitionStates.expression_5);
                break;
            case expression_6:
                return;
            case expression_7:
                handleNonzeroNonterminalStates(parser, "factor", "expression", TransitionStates.factor_0, TransitionStates.expression_3, TransitionStates.expression_7);
                break;
            case expression_8:
                handleNonzeroNonterminalStates(parser, "factor", "expression", TransitionStates.factor_0, TransitionStates.expression_3, TransitionStates.expression_8);
                break;
            case expression_9:
                handleNonzeroNonterminalStates(parser, "s12", "expression", TransitionStates.s12_0, TransitionStates.expression_6, TransitionStates.expression_9);
                break;
            case s12_0:
                if (isValidTerminal("(", transitionState)) {
                    pushToItsStack("s12", 6);
                    writeToOutput("(", stacks.get("s12").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s12_1);
                } else if (isValidTerminal("=", transitionState)) {
                    pushToItsStack("s12", 2);
                    writeToOutput("=", stacks.get("s12").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s12_7);
                } else if (isValidTerminal("[", transitionState)) {
                    pushToItsStack("s12", 4);
                    writeToOutput("[", stacks.get("s12").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s12_8);
                } else if (isValidNonTerminal("s8")) {
                    pushToItsStack("s12", 3);
                    pushToOtherStack("s12", "s8");
                    moveOnDiagrams(parser, TransitionStates.s8_0);
                    moveOnDiagrams(parser, TransitionStates.s12_11);
                }
                break;
            case s12_1:
                handleNonzeroNonterminalStates(parser, "args", "s12", TransitionStates.args_0, TransitionStates.s12_2, TransitionStates.s12_1);
                break;
            case s12_2:
                handleNonzeroTerminalStates(parser, transitionState, ")", "s12", TransitionStates.s12_3);
                break;
            case s12_3:
                handleNonzeroNonterminalStates(parser, "s8", "s12", TransitionStates.s8_0, TransitionStates.s12_4, TransitionStates.s12_3);
                break;
            case s12_4:
                handleNonzeroNonterminalStates(parser, "s7", "s12", TransitionStates.s7_0, TransitionStates.s12_5, TransitionStates.s12_4);
                break;
            case s12_5:
                handleNonzeroNonterminalStates(parser, "s6", "s12", TransitionStates.s6_0, TransitionStates.s12_6, TransitionStates.s12_5);
                break;
            case s12_6:
                return;
            case s12_7:
                handleNonzeroNonterminalStates(parser, "expression", "s12", TransitionStates.expression_0, TransitionStates.s12_6, TransitionStates.s12_7);
                break;
            case s12_8:
                handleNonzeroNonterminalStates(parser, "expression", "s12", TransitionStates.expression_0, TransitionStates.s12_9, TransitionStates.s12_8);
                break;
            case s12_9:
                handleNonzeroTerminalStates(parser, transitionState, "]", "s12", TransitionStates.s12_10);
                break;
            case s12_10:
                handleNonzeroNonterminalStates(parser, "s13", "s12", TransitionStates.s13_0, TransitionStates.s12_6, TransitionStates.s12_10);
                break;
            case s12_11:
                handleNonzeroNonterminalStates(parser, "s7", "s12", TransitionStates.s7_0, TransitionStates.s12_12, TransitionStates.s12_11);
                break;
            case s12_12:
                handleNonzeroNonterminalStates(parser, "s6", "s12", TransitionStates.s6_0, TransitionStates.s12_6, TransitionStates.s12_12);
                break;
            case s13_0:
                if (isValidNonTerminal("s8")) {
                    pushToItsStack("s13", 3);
                    pushToOtherStack("s13", "s8");
                    moveOnDiagrams(parser, TransitionStates.s8_0);
                    moveOnDiagrams(parser, TransitionStates.s13_1);
                } else if (isValidTerminal("=", transitionState)) {
                    pushToItsStack("s13", 2);
                    writeToOutput("=", stacks.get("s13").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s13_4);
                }
                break;
            case s13_1:
                handleNonzeroNonterminalStates(parser, "s7", "s13", TransitionStates.s7_0, TransitionStates.s13_2, TransitionStates.s13_1);
                break;
            case s13_2:
                handleNonzeroNonterminalStates(parser, "s6", "s13", TransitionStates.s6_0, TransitionStates.s13_3, TransitionStates.s13_2);
                break;
            case s13_3:
                return;
            case s13_4:
                handleNonzeroNonterminalStates(parser, "expression", "s13", TransitionStates.expression_0, TransitionStates.s13_3, TransitionStates.s13_4);
            case var_0:
                if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("var", 2);
                    writeToOutput("ID", stacks.get("var").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.var_1);
                }
                break;
            case var_1:
                handleNonzeroNonterminalStates(parser, "s5", "var", TransitionStates.s5_0, TransitionStates.var_2, TransitionStates.var_1);
                break;
            case var_2:
                return;
            case s5_0:
                if (isValidTerminal("[", transitionState)) {
                    pushToItsStack("s5", 3);
                    writeToOutput("[", stacks.get("s5").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s5_1);
                } else if (isValidEpsilon("s5"))
                    moveOnDiagrams(parser, TransitionStates.s5_3);
                break;
            case s5_1:
                handleNonzeroNonterminalStates(parser, "expression", "s5", TransitionStates.expression_0, TransitionStates.s5_2, TransitionStates.s5_1);
                break;
            case s5_2:
                handleNonzeroTerminalStates(parser, transitionState, "]", "s5", TransitionStates.s5_3);
                break;
            case s5_3:
                return;
            case s6_0:
                if (isValidNonTerminal("relop")) {
                    pushToItsStack("s6", 2);
                    pushToOtherStack("s6", "relop");
                    moveOnDiagrams(parser, TransitionStates.relop_0);
                    moveOnDiagrams(parser, TransitionStates.s6_1);
                } else if (isValidEpsilon("s6"))
                    moveOnDiagrams(parser, TransitionStates.s6_2);
                break;
            case s6_1:
                handleNonzeroNonterminalStates(parser, "additive_expression", "s6", TransitionStates.additive_expression_0, TransitionStates.s6_2, TransitionStates.s6_1);
                break;
            case s6_2:
                return;
            case relop_0:
                if (isValidTerminal("==", transitionState)) {
                    writeToOutput("==", stacks.get("relop").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.relop_2);
                } else if (isValidTerminal("<", transitionState)) {
                    writeToOutput("==", stacks.get("relop").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.relop_2);
                }
                break;
            case relop_2:
                return;
            case additive_expression_0:
                if (isValidNonTerminal("term")) {
                    pushToItsStack("additive_expression", 2);
                    pushToOtherStack("additive_expression", "term");
                    moveOnDiagrams(parser, TransitionStates.term_0);
                    moveOnDiagrams(parser, TransitionStates.additive_expression_1);
                }
                break;
            case additive_expression_1:
                handleNonzeroNonterminalStates(parser, "s7", "additive_expression", TransitionStates.s7_0, TransitionStates.additive_expression_2, TransitionStates.additive_expression_1);
                break;
            case additive_expression_2:
                return;
            case s7_0:
                if (isValidNonTerminal("addop")) {
                    pushToItsStack("s7", 3);
                    pushToOtherStack("s7", "addop");
                    moveOnDiagrams(parser, TransitionStates.addop_0);
                    moveOnDiagrams(parser, TransitionStates.s7_1);
                } else if (isValidEpsilon("s7"))
                    moveOnDiagrams(parser, TransitionStates.s7_3);
                break;
            case s7_1:
                handleNonzeroNonterminalStates(parser, "term", "s7", TransitionStates.term_0, TransitionStates.s7_2, TransitionStates.s7_1);
                break;
            case s7_2:
                if (isValidNonTerminal("s7")) {
                    pushToOtherStack("s7", "s7");
                    moveOnDiagrams(parser, TransitionStates.s7_0);
                    moveOnDiagrams(parser, TransitionStates.s7_3);
                } else {
                    int res = handleNonterminalErrors("s7");
                    if (res == 2) {
                        popFromStack("s7");
                        writeToError("s7", 2);
                        moveOnDiagrams(parser, TransitionStates.s7_3);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.s7_2);
                    }
                }
                break;
            case s7_3:
                return;
            case addop_0:
                if (isValidTerminal("+", transitionState)) {
                    writeToOutput("+", stacks.get("addop").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.addop_1);
                } else if (isValidTerminal("-", transitionState)) {
                    writeToOutput("+", stacks.get("addop").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.addop_1);
                }
                break;
            case addop_1:
                return;
            case term_0:
                if (isValidNonTerminal("signed_factor")) {
                    pushToItsStack("term", 2);
                    pushToOtherStack("term", "signed_factor");
                    moveOnDiagrams(parser, TransitionStates.signed_factor_0);
                    moveOnDiagrams(parser, TransitionStates.term_1);
                }
                break;
            case term_1:
                handleNonzeroNonterminalStates(parser, "s8", "term", TransitionStates.s8_0, TransitionStates.term_2, TransitionStates.term_1);
                break;
            case term_2:
                return;
            case s8_0:
                if (isValidTerminal("*", transitionState)) {
                    pushToItsStack("s8", 3);
                    writeToOutput("*", stacks.get("s8").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s8_1);
                } else if (isValidEpsilon("s8"))
                    moveOnDiagrams(parser, TransitionStates.s8_3);
                break;
            case s8_1:
                handleNonzeroNonterminalStates(parser, "signed_factor", "s8", TransitionStates.signed_factor_0, TransitionStates.s8_2, TransitionStates.s8_1);
                break;
            case s8_2:
                if (isValidNonTerminal("s8")) {
                    pushToOtherStack("s8", "s8");
                    moveOnDiagrams(parser, TransitionStates.s8_0);
                    moveOnDiagrams(parser, TransitionStates.s8_3);
                } else {
                    int res = handleNonterminalErrors("s8");
                    if (res == 2) {
                        popFromStack("s8");
                        writeToError("s8", 2);
                        moveOnDiagrams(parser, TransitionStates.s8_3);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.s8_2);
                    }
                }
                break;
            case s8_3:
                return;
            case signed_factor_0:
                if (isValidTerminal("+", transitionState)) {
                    pushToItsStack("signed_factor", 2);
                    writeToOutput("+", stacks.get("signed_factor").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.signed_factor_1);
                } else if (isValidTerminal("-", transitionState)) {
                    pushToItsStack("signed_factor", 2);
                    writeToOutput("-", stacks.get("signed_factor").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.signed_factor_3);
                } else if (isValidNonTerminal("factor")) {
                    pushToOtherStack("signed_factor", "factor");
                    moveOnDiagrams(parser, TransitionStates.factor_0);
                    moveOnDiagrams(parser, TransitionStates.signed_factor_2);
                }
                break;
            case signed_factor_1:
                handleNonzeroNonterminalStates(parser, "factor", "signed_factor", TransitionStates.factor_0, TransitionStates.signed_factor_2, TransitionStates.signed_factor_1);
                break;
            case signed_factor_2:
                return;
            case signed_factor_3:
                handleNonzeroNonterminalStates(parser, "factor", "signed_factor", TransitionStates.factor_0, TransitionStates.signed_factor_2, TransitionStates.signed_factor_3);
                break;
            case factor_0:
                if (isValidTerminal("(", transitionState)) {
                    pushToItsStack("factor", 3);
                    writeToOutput("(", stacks.get("factor").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.factor_1);
                } else if (isValidTerminal("NUM", transitionState)) {
                    writeToOutput("NUM", stacks.get("factor").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.factor_3);
                } else if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("factor", 2);
                    writeToOutput("ID", stacks.get("factor").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.factor_4);
                }
                break;
            case factor_1:
                handleNonzeroNonterminalStates(parser, "expression", "factor", TransitionStates.expression_0, TransitionStates.factor_2, TransitionStates.factor_1);
                break;
            case factor_2:
                handleNonzeroTerminalStates(parser, transitionState, ")", "factor", TransitionStates.factor_3);
                break;
            case factor_3:
                return;
            case factor_4:
                handleNonzeroNonterminalStates(parser, "s15", "factor", TransitionStates.s15_0, TransitionStates.factor_3, TransitionStates.factor_4);
                break;
            case call_0:
                if (isValidTerminal("ID", transitionState)) {
                    pushToItsStack("call", 4);
                    writeToOutput("ID", stacks.get("call").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.call_1);
                }
                break;
            case call_1:
                handleNonzeroTerminalStates(parser, transitionState, "(", "call", TransitionStates.call_2);
                break;
            case call_2:
                handleNonzeroNonterminalStates(parser, "args", "call", TransitionStates.args_0, TransitionStates.call_3, TransitionStates.call_2);
                break;
            case call_3:
                handleNonzeroTerminalStates(parser, transitionState, ")", "call", TransitionStates.call_4);
                break;
            case call_4:
                return;
            case args_0:
                if (isValidNonTerminal("arg_list")) {
                    pushToOtherStack("args", "arg_list");
                    moveOnDiagrams(parser, TransitionStates.arg_list_0);
                    moveOnDiagrams(parser, TransitionStates.args_1);
                } else if (isValidEpsilon("args"))
                    moveOnDiagrams(parser, TransitionStates.args_1);
                break;
            case args_1:
                return;
            case arg_list_0:
                if (isValidNonTerminal("expression")) {
                    pushToItsStack("arg_list", 2);
                    pushToOtherStack("arg_list", "expression");
                    moveOnDiagrams(parser, TransitionStates.expression_0);
                    moveOnDiagrams(parser, TransitionStates.arg_list_1);
                }
                break;
            case arg_list_1:
                handleNonzeroNonterminalStates(parser, "s9", "arg_list", TransitionStates.s9_0, TransitionStates.arg_list_2, TransitionStates.arg_list_1);
                break;
            case arg_list_2:
                return;
            case s9_0:
                if (isValidTerminal(",", transitionState)) {
                    pushToItsStack("s9", 3);
                    writeToOutput(",", stacks.get("s9").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s9_1);
                } else if (isValidEpsilon("s9"))
                    moveOnDiagrams(parser, TransitionStates.s9_3);
                break;
            case s9_1:
                handleNonzeroNonterminalStates(parser, "expression", "s9", TransitionStates.expression_0, TransitionStates.s9_2, TransitionStates.s9_1);
                break;
            case s9_2:
                if (isValidNonTerminal("s9")) {
                    pushToOtherStack("s9", "s9");
                    moveOnDiagrams(parser, TransitionStates.s9_0);
                    moveOnDiagrams(parser, TransitionStates.s9_3);
                } else {
                    int res = handleNonterminalErrors("s9");
                    if (res == 2) {
                        popFromStack("s9");
                        writeToError("s9", 2);
                        moveOnDiagrams(parser, TransitionStates.s9_3);
                    } else {
                        moveOnDiagrams(parser, TransitionStates.s9_2);
                    }
                }

                break;
            case s9_3:
                return;
            case s15_0:
                if (isValidTerminal("(", transitionState)) {
                    pushToItsStack("s15", 3);
                    writeToOutput("(", stacks.get("s15").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s15_1);
                } else if (isValidNonTerminal("s5")) {
                    pushToOtherStack("s15", "s5");
                    moveOnDiagrams(parser, TransitionStates.s5_0);
                    moveOnDiagrams(parser, TransitionStates.s15_3);
                }
                break;
            case s15_1:
                handleNonzeroNonterminalStates(parser, "args", "s15", TransitionStates.args_0, TransitionStates.s15_2, TransitionStates.s15_1);
                break;
            case s15_2:
                handleNonzeroTerminalStates(parser, transitionState, ")", "s15", TransitionStates.s15_3);
            case s15_3:
                return;
            case s16_0:
                if (isValidNonTerminal("expression")) {
                    pushToItsStack("s16", 2);
                    pushToOtherStack("s16", "expression");
                    moveOnDiagrams(parser, TransitionStates.expression_0);
                    moveOnDiagrams(parser, TransitionStates.s16_1);
                } else if (isValidTerminal(";", transitionState)) {
                    writeToOutput(";", stacks.get("s16").pop() + 1);
                    moveOnDiagrams(parser, TransitionStates.s16_2);
                }
                break;
            case s16_1:
                handleNonzeroTerminalStates(parser, transitionState, ";", "s16", TransitionStates.s16_2);
                break;
            case s16_2:
                return;


        }
    }

    private void handleNonzeroNonterminalStates(Parser parser, String nonterminal, String currentStateName, TransitionStates nextZeroStateIfValid, TransitionStates nextOfCurDiagramStateIFValid, TransitionStates currentState) {
        if (isValidNonTerminal(nonterminal)) {
            pushToOtherStack(currentStateName, nonterminal);
            moveOnDiagrams(parser, nextZeroStateIfValid);
            moveOnDiagrams(parser, nextOfCurDiagramStateIFValid);
        } else {
            int res = handleNonterminalErrors(nonterminal);
            if (res == 2) {
                popFromStack(currentStateName);
                writeToError(nonterminal, 2);
                moveOnDiagrams(parser, nextOfCurDiagramStateIFValid);
            } else {
                moveOnDiagrams(parser, currentState);
            }
        }
    }

    private void handleNonzeroTerminalStates(Parser parser, TransitionStates transitionState, String terminal, String currentStateName, TransitionStates nextTransitionState) {
        if (!isValidTerminal(terminal, transitionState)) {
            writeToError(terminal, 0);
        }
        writeToOutput(terminal, stacks.get(currentStateName).pop() + 1);
        moveOnDiagrams(parser, nextTransitionState);
    }

    boolean searchList(String[] firstOrFollow, String searchString) {
        return Arrays.asList(firstOrFollow).contains(searchString);
    }

    boolean isValidNonTerminal(String nonTerminal) {
        return searchList(firsts.get(nonTerminal), nextToken.toString()) || (searchList(firsts.get(nonTerminal), "epsilon") && searchList(follows.get(nonTerminal), nextToken.toString()));
    }

    boolean isValidTerminal(String terminal, TransitionStates transitionState) {
        boolean res = nextToken.toString().equals(terminal);
        if (res) {
            if (terminal.equals("EOF")) {
                return res;
            }
            try {
                nextToken = lexer.get_next_token();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    boolean isValidEpsilon(String transitionDiagramName) {
        boolean res = searchList(follows.get(transitionDiagramName), nextToken.toString());
        if (res) {
            int a = popFromStack(transitionDiagramName);
            writeToOutput("ϵ", a + 1);
        }
        return res;
    }


    private void pushToOtherStack(String s1, String s2) {
        int a = popFromStack(s1);

        stacks.get(s2).push(a + 1);

    }

    private void pushToItsStack(String s1, int number) {
        int a = popFromStack(s1);
        writeToOutput(s1, a);
        for (int i = 0; i < number; i++) {
            stacks.get(s1).push(a);
        }
    }

    private void writeToOutput(String s1, int a) {
        for (int i = 0; i < a; i++) {
            outputFormatter.format("\t");
        }
        outputFormatter.format("%s\n", s1);
    }

    private void writeToError(String s1, int type) {
        switch (type) {
            case 0:
                errorFormatter.format("Parser : %d : Syntax Error! Missing %s\n", lexer.getLineNumber(), s1);
                break;
            case 1:
                errorFormatter.format("Parser : %d : Syntax Error! Unexpected %s\n", lexer.getLineNumber(), s1);
                break;
            case 2:
                errorFormatter.format("Parser : %d : Syntax Error! Missing %s\n", lexer.getLineNumber(), simplests.get(s1));
        }


    }

    private int popFromStack(String s1) {
        return stacks.get(s1).pop();
    }


    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.parse(parser);
        parser.outputFormatter.flush();
        parser.outputFormatter.close();
        parser.errorFormatter.close();
    }


    private int isValidNonterminalForErrorHandling(String nonTerminal) {
        if (searchList(firsts.get(nonTerminal), nextToken.toString())) {
            return 0;
        } else if (searchList(firsts.get(nonTerminal), "epsilon") && searchList(follows.get(nonTerminal), nextToken.toString())) {
            return 1;
        } else if (searchList(follows.get(nonTerminal), nextToken.toString())) {
            return 2;
        } else {
            return 3;
        }
    }

    private int handleNonterminalErrors(String nonterminal) {
        int res;
        while (true) {
            res = isValidNonterminalForErrorHandling(nonterminal);
            if (res != 3)
                break;
            writeToError(nextToken.toString(), 1);
            try {
                nextToken = lexer.get_next_token();
                if (nextToken.toString().equals("EOF")) {
                    errorFormatter.format("Parser : %d : Syntax Error! Unexpected EndOfFile\n", lexer.getLineNumber());
                    errorFormatter.close();
                    outputFormatter.close();
                    lexer.getOutputFormatter().close();
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (res == 2) {
            return 2;
        } else {
            return 0;
        }
    }

}