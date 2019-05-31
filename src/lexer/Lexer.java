package lexer;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Formatter;

public class Lexer {
    boolean fileEnded = false;
    boolean firstTimeToWriteToOutput = true;
    RandomAccessFile reader;
    Formatter outputFormatter;
    Formatter errorFormatter;
    private int lineNumber = 1;
    int shouldWriteNewline = 1;
    boolean readFromInput = true;
    private int state = 1;
    private char next_char;
    Character symbols[] = {';', ':', ',', '[', ']', '(', ')', '{', '}', '+', '-', '*', '<'};
    Character whiteSpaces[] = {32, 10, 13, 9, 11, 12};
    String keyWords[] = {"if", "else", "void", "int", "while", "break", "continue", "switch", "default", "case", "return"};

    public Formatter getOutputFormatter() {
        return outputFormatter;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Lexer(String fileName) throws IOException {
        reader = new RandomAccessFile(fileName, "rw");
        outputFormatter = new Formatter("scanner.txt");
        errorFormatter = new Formatter("error.txt");
        next_char = readByteCastToChar();
        try {
            reader.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        String inputFile = "input.txt";
        Lexer lexer = null;
        try {
            lexer = new Lexer(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (!lexer.fileEnded) {
            Token token = lexer.get_next_token();
        }
        lexer.errorFormatter.close();
        lexer.outputFormatter.close();
    }

    public Token get_next_token() throws IOException {
        Token token;
        token = scanInputFile();
        return token;
    }


    private Token scanInputFile() {

        String tokenString = "";
        while (!fileEnded) {
            if (readFromInput) {
                next_char = readByteCastToChar();
            } else {
                readFromInput = true;
            }

            switch (state) {
                case 1:
                    tokenString = "";
                    if ((next_char >= 'A' && next_char <= 'Z') || (next_char >= 'a' && next_char <= 'z')) {
                        state = 2;
                        tokenString += next_char;
                    } else if (next_char >= '0' && next_char <= '9') {
                        state = 3;
                        tokenString += next_char;
                    } else if (searchList(next_char, 0)) {
                        return getToken("" + next_char, Token.SYMBOL, 1);
                    } else if (next_char == '/') {
                        tokenString += '/';
                        state = 4;
                    } else if (next_char == '=') {
                        state = 8;
                        tokenString += '=';
                    } else if (searchList(next_char, 1)) {
                        continue;
                    } else {
                        if (!fileEnded)
                            writeToError(next_char + "");
                    }
                    break;
                case 2:

                    if ((next_char >= 'A' && next_char <= 'Z') || (next_char >= 'a' && next_char <= 'z') || (next_char >= '0' && next_char <= '9')) {
                        tokenString += next_char;
                        continue;
                    } else if (searchList(next_char, 0) || next_char == '=') {
                        readFromInput = false;
                        return getToken(tokenString, Token.ID, 1);
                    } else if (searchList(next_char, 1)) {
                        return getToken(tokenString, Token.ID, 1);
                    } else if (next_char == '/') {
                        tokenString += '/';
                        state = 4;
                    } else {
                        tokenString += next_char;
                        writeToError(tokenString);
                        state = 1;
                    }
                    break;
                case 3:
                    if (next_char >= '0' && next_char <= '9') {
                        tokenString += next_char;
                        continue;
                    } else if (searchList(next_char, 0) || next_char == '=' || (next_char >= 'A' && next_char <= 'Z') || (next_char >= 'a' && next_char <= 'z')) {
                        readFromInput = false;
                        return getToken(tokenString, Token.NUM, 1);
                    } else if (searchList(next_char, 1)) {
                        return getToken(tokenString, Token.NUM, 1);
                    } else if (next_char == '/') {
                        tokenString += '/';
                        state = 4;
                    } else {
                        tokenString += next_char;
                        writeToError(tokenString);
                        state = 1;
                    }
                    break;
                case 4:
                    if (next_char == '/' || next_char == '*') {
                        state = (next_char == '/') ? 5 : 6;
                        if (tokenString.length() > 1) {
                            tokenString = tokenString.substring(0, tokenString.length() - 1);
                            char firstChar = tokenString.charAt(0);
                            if (firstChar >= '0' && firstChar <= '9') {
                                return getToken(tokenString, Token.NUM, state);
                            } else {
                                return getToken(tokenString, Token.ID, state);
                            }
                        }
                    } else {
                        writeToError(tokenString);
                        readFromInput = false;
                        state = 1;
                    }
                    break;
                case 5:
                    if (next_char == '\n' || next_char == '\r') {
                        state = 1;
                    }
                    break;
                case 6:
                    if (next_char == '*') {
                        state = 7;
                    }
                    break;
                case 7:
                    if (next_char == '/') {
                        state = 1;
                    } else {
                        state = 6;
                    }
                    break;
                case 8:
                    if (next_char == '=') {
                        return getToken("==", Token.SYMBOL, 1);
                    } else if (searchList(next_char, 0) || next_char == '/' || (next_char >= 'A' && next_char <= 'Z') || (next_char >= 'a' && next_char <= 'z') || (next_char >= '0' && next_char <= '9')) {
                        readFromInput = false;
                        return getToken("=", Token.SYMBOL, 1);
                    }


            }
        }
        return Token.EOF;
    }

    private Token getToken(String tokenString, Token tokenFound, int next_state) {
        if (tokenFound.name().equals("ID")) {
            if (searchList(tokenString, 2)) {
                tokenFound = Token.KEYWORD;
            }
        }
        Token token;
        token = tokenFound;
        token.tokenString = tokenString;
        writeToOutput(token);
        state = next_state;
        return token;
    }

    boolean searchList(Comparable seacrhValue, int type) {
        if (type == 0) {
            return searcher(seacrhValue, symbols);
        } else if (type == 1) {
            return searcher(seacrhValue, whiteSpaces);
        } else {
            return searcher(seacrhValue, keyWords);
        }
    }

    private boolean searcher(Comparable searchValue, Comparable[] arrayToSearch) {
        for (int i = 0; i < arrayToSearch.length; i++) {
            if (searchValue.compareTo(arrayToSearch[i]) == 0) {
                return true;
            }
        }
        return false;
    }


    private void gotoNewline(int type) throws IOException {
        lineNumber++;
        if (shouldWriteNewline == 0) {
            shouldWriteNewline = 1;
        }
        if (type == 0)
            reader.seek(reader.getFilePointer() + 1);
    }


    private char readByteCastToChar() {
        char c = 0;
        try {
            c = (char) reader.readByte();
            if (c == '\r') {
                gotoNewline(0);
            }
            if (c == '\n') {
                gotoNewline(1);
            }
        } catch (IOException e) {
            if (e instanceof EOFException) {
                fileEnded = true;
            } else {
                e.printStackTrace();
            }
        }
        return c;
    }


    private void writeToOutput(Token token) {
        if (shouldWriteNewline == 1 && firstTimeToWriteToOutput) {
            outputFormatter.format("%d. ", lineNumber);
            firstTimeToWriteToOutput = false;
        } else if (shouldWriteNewline == 1 && !firstTimeToWriteToOutput) {
            outputFormatter.format("\n%d. ", lineNumber);
        }
        shouldWriteNewline = 0;
        outputFormatter.format("(%s, %s) ", token.name(), token.tokenString);
    }

    public Formatter getErrorFormatter() {
        return errorFormatter;
    }

    private void writeToError(String string) {
        errorFormatter.format("Scanner : %d (%s, invalid input)\n", lineNumber, string);

    }


}
