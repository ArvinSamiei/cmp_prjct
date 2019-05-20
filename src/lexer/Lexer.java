package lexer;

import java.io.*;
import java.util.Formatter;
import java.util.Scanner;

public class Lexer {
    boolean fileEnded = false;
    boolean firstTimeToWriteToOutput = true;
    RandomAccessFile reader;
    Formatter outputFormatter;
    Formatter errorFormatter;
    private int lineNumber = 1;
    private int errorLineNumber = 0;
    int shouldWriteNewline = 1;
    private State state;
    char symbols[] = {';', ':', ',', '[', ']', '(', ')', '{', '}', '+', '-', '*', '<'};

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

        return;
    }

    public Token get_next_token() throws IOException {
        Token token;
        while (true) {
            token = scanInputFile();
            if (fileEnded) {
                writeToOutput(Token.EOF);
                outputFormatter.flush();
                return Token.EOF;
            }
            else if (token != null) {
                break;
            }
        }
        return token;
    }


    private Token scanInputFile() throws IOException {
        long pointer;
        while (true) {
            pointer = reader.getFilePointer();
            char c = readByteCastToChar();
            if (c == ' ' || c == '\t' || c == 11 || c == '\f') {
                continue;
            } else if (c == '\r') {
                gotoNewline();
                continue;
            } else {
                reader.seek(pointer);
                break;
            }
        }
        Token token;
        token = getNum();
        if (token == null){
            reader.seek(pointer);
            token = getKeyword();
            if (token == null){
                reader.seek(pointer);
                token = getID();
                if (token == null) {
                    reader.seek(pointer);
                    token = getSymbol();
                    if (token == null) {
                        reader.seek(pointer);
                        token = getComment();
                        if (token != null) {
                            return null;
                        } else {
                            reader.seek(pointer);
                            String errorString = "";
                            while (!fileEnded) {
                                char readChar = readByteCastToChar();
                                errorString += readChar;
                                if (!isCharLegal(readChar)) {
                                    writeToError(errorString);
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (fileEnded)
            return null;
        writeToOutput(token);
        outputFormatter.flush();
        errorFormatter.flush();
        return token;
    }

    private Token getNum() throws IOException {
        Token token = Token.NUM;
        char c;
        String tokenString = "";
        state = State.NUM1;
        while (true) {
            switch (state) {
                case NUM1:
                    c = readByteCastToChar();
                    if (c >= '0' && c <= '9') {
                        state = State.NUM2;
                        tokenString += c;
                    } else {
                        return null;
                    }
                    break;
                case NUM2:
                    while (true) {
                        long pointer = reader.getFilePointer();
                        c = readByteCastToChar();
                        if (!fileEnded && c >= '0' && c <= '9') {
                            tokenString += c;
                        } else if (isCharLegal(c)) {
                            token.tokenString = tokenString;
                            reader.seek(pointer);
                            return token;
                        } else {
                            return null;
                        }
                    }
            }
        }
    }


    private Token getKeyword() throws IOException {
        Token token = Token.KEYWORD;
        boolean bool;
        char c;
        state = State.KEYWORD1;
        while (true) {
            long pointer = reader.getFilePointer();
            c = readByteCastToChar();

            switch (state) {
                case KEYWORD1:
                    if (c == 'i')
                        state = State.KEYWORD2;
                    else if (c == 'e')
                        state = State.KEYWORD6;
                    else if (c == 'v')
                        state = State.KEYWORD10;
                    else if (c == 'c')
                        state = State.KEYWORD14;
                    else if (c == 'w')
                        state = State.KEYWORD25;
                    else if (c == 's')
                        state = State.KEYWORD30;
                    else if (c == 'b')
                        state = State.KEYWORD36;
                    else if (c == 'd')
                        state = State.KEYWORD41;
                    else if (c == 'r')
                        state = State.KEYWORD48;
                    else
                        return null;
                    break;
                case KEYWORD2:
                    if (c == 'f')
                        state = State.KEYWORD3;
                    else if (c == 'n')
                        state = State.KEYWORD4;
                    else
                        return null;
                    break;
                case KEYWORD3:
                    return returnToken(token, c, "if", pointer);
                case KEYWORD4:

                    if (c == 't')
                        state = State.KEYWORD5;
                    else
                        return null;
                    break;
                case KEYWORD5:
                    return returnToken(token, c, "int", pointer);
                case KEYWORD6:

                    if (c == 'l')
                        state = State.KEYWORD7;
                    else
                        return null;
                    break;
                case KEYWORD7:

                    if (c == 's')
                        state = State.KEYWORD8;
                    else
                        return null;
                    break;
                case KEYWORD8:

                    if (c == 'e')
                        state = State.KEYWORD9;
                    else
                        return null;
                    break;
                case KEYWORD9:
                    return returnToken(token, c, "else", pointer);
                case KEYWORD10:
                    bool = handleNonfinalStates(reader, state, 'o', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD11:
                    bool = handleNonfinalStates(reader, state, 'i', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD12:
                    bool = handleNonfinalStates(reader, state, 'd', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD13:
                    return returnToken(token, c, "void", pointer);
                case KEYWORD14:

                    if (c == 'a') {
                        state = State.KEYWORD15;
                    } else if (c == 'o') {
                        state = State.KEYWORD18;
                    } else {
                        return null;
                    }
                    break;
                case KEYWORD15:
                    bool = handleNonfinalStates(reader, state, 's', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD16:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD17:
                    return returnToken(token, c, "case", pointer);
                case KEYWORD18:
                    bool = handleNonfinalStates(reader, state, 'n', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD19:
                    bool = handleNonfinalStates(reader, state, 't', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD20:
                    bool = handleNonfinalStates(reader, state, 'i', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD21:
                    bool = handleNonfinalStates(reader, state, 'n', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD22:
                    bool = handleNonfinalStates(reader, state, 'u', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD23:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD24:
                    return returnToken(token, c, "continue", pointer);
                case KEYWORD25:
                    bool = handleNonfinalStates(reader, state, 'h', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD26:
                    bool = handleNonfinalStates(reader, state, 'i', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD27:
                    bool = handleNonfinalStates(reader, state, 'l', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD28:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD29:
                    return returnToken(token, c, "while", pointer);
                case KEYWORD30:
                    bool = handleNonfinalStates(reader, state, 'w', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD31:
                    bool = handleNonfinalStates(reader, state, 'i', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD32:
                    bool = handleNonfinalStates(reader, state, 't', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD33:
                    bool = handleNonfinalStates(reader, state, 'c', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD34:
                    bool = handleNonfinalStates(reader, state, 'h', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD35:
                    return returnToken(token, c, "switch", pointer);
                case KEYWORD36:
                    bool = handleNonfinalStates(reader, state, 'r', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD37:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD38:
                    bool = handleNonfinalStates(reader, state, 'a', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD39:
                    bool = handleNonfinalStates(reader, state, 'k', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD40:
                    return returnToken(token, c, "break", pointer);
                case KEYWORD41:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD42:
                    bool = handleNonfinalStates(reader, state, 'f', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD43:
                    bool = handleNonfinalStates(reader, state, 'a', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD44:
                    bool = handleNonfinalStates(reader, state, 'u', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD45:
                    bool = handleNonfinalStates(reader, state, 'l', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD46:
                    bool = handleNonfinalStates(reader, state, 't', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD47:
                    return returnToken(token, c, "default", pointer);
                case KEYWORD48:
                    bool = handleNonfinalStates(reader, state, 'e', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD49:
                    bool = handleNonfinalStates(reader, state, 't', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD50:
                    bool = handleNonfinalStates(reader, state, 'u', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD51:
                    bool = handleNonfinalStates(reader, state, 'r', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD52:
                    bool = handleNonfinalStates(reader, state, 'n', c);
                    state = findNextState(state);
                    if (!bool) {
                        return null;
                    }
                    break;
                case KEYWORD53:
                    return returnToken(token, c, "return", pointer);

            }
        }

    }

    private Token getID() throws IOException {
        Token token = Token.ID;
        char c;
        String tokenString = "";
        state = State.ID1;
        while (true) {
            switch (state) {
                case ID1:
                    c = readByteCastToChar();
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                        state = State.ID2;
                        tokenString += c;
                    } else {
                        return null;
                    }
                    break;
                case ID2:
                    while (true) {
                        long pointer = reader.getFilePointer();
                        c = readByteCastToChar();
                        if (!fileEnded && ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                            tokenString += c;
                        } else if (isCharLegal(c)) {
                            token.tokenString = tokenString;
                            reader.seek(pointer);
                            return token;
                        } else {
                            return null;
                        }
                    }
            }
        }
    }

    private Token getSymbol() throws IOException {
        Token token = Token.SYMBOL;
        char c;
        String tokenString = "";
        state = State.SYMBOL1;
        while (true) {
            switch (state) {
                case SYMBOL1:
                    c = readByteCastToChar();
                    if (checkSymbolsExceptEqual(c)) {
                        state = State.SYMBOL2;
                        tokenString += c;
                    } else if (c == '=') {
                        state = State.SYMBOL3;
                        tokenString += c;
                    } else {
                        return null;
                    }
                    break;
                case SYMBOL2:
                    token.tokenString = tokenString;
                    return token;
                case SYMBOL3:
                    long pointer = reader.getFilePointer();
                    c = readByteCastToChar();
                    if (c == '=') {
                        token.tokenString = "==";
                        return token;
                    } else {
                        token.tokenString = "=";
                        reader.seek(pointer);
                        return token;
                    }

            }
        }
    }

    private Token getComment() throws IOException {
        Token token = Token.COMMENT;
        char c;
        state = State.COMMENT1;
        while (true && !fileEnded) {
            switch (state) {
                case COMMENT1:
                    c = readByteCastToChar();
                    if (c == '/') {
                        state = State.COMMENT2;
                    } else {
                        return null;
                    }
                    break;
                case COMMENT2:
                    c = readByteCastToChar();
                    if (c == '*') {
                        state = State.COMMENT3;
                    } else if (c == '/') {
                        state = State.COMMENT6;
                    } else {
                        return null;
                    }
                    break;
                case COMMENT3:
                    c = readByteCastToChar();
                    if (c == '*') {
                        state = State.COMMENT4;
                    }
                    break;
                case COMMENT4:
                    c = readByteCastToChar();
                    if (c == '/') {
                        state = State.COMMENT5;
                    } else {
                        state = State.COMMENT3;
                    }
                    break;
                case COMMENT5:
                    return token;
                case COMMENT6:
                    c = readByteCastToChar();
                    if (c == '\r') {
                        gotoNewline();
                        state = State.COMMENT7;
                    }
                    break;
                case COMMENT7:
                    return token;
            }
        }
        return null;
    }

    private void gotoNewline() throws IOException {
        lineNumber++;
        if (shouldWriteNewline == 0) {
            shouldWriteNewline = 1;
        }
        reader.seek(reader.getFilePointer() + 1);
    }

    private Token returnToken(Token token, char c, String tokenString, long pointer) throws IOException {
        if (isCharLegal(c) && !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            token.tokenString = tokenString;
            reader.seek(pointer);
            return token;
        } else {
            reader.seek(pointer);
            return null;
        }
    }

    private char readByteCastToChar() {
        char c = 0;
        try {
            c = (char) reader.readByte();
        } catch (IOException e) {
            if (e instanceof EOFException) {
                fileEnded = true;
            } else {
                e.printStackTrace();
            }
        }
        return c;
    }

    private boolean handleNonfinalStates(RandomAccessFile reader, State state, char charToCheck, char c) throws IOException {
        if (c == charToCheck) {
            return true;
        }
        return false;
    }

    private State findNextState(State state) {
        String currentState = state.name();
        int nextStateNo = Integer.parseInt(currentState.substring(7));
        String tokenName = currentState.substring(0, 7);
        nextStateNo += 1;
        state = State.valueOf(tokenName + nextStateNo);
        return state;
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

    private boolean checkSymbolsExceptEqual(char c) {
        for (char symbol : symbols) {
            if (c == symbol) {
                return true;
            }
        }
        return false;
    }

    private boolean isCharLegal(char c) {
        for (char symbol : symbols) {
            if (c == symbol) {
                return true;
            }
        }
        if (c >= 'a' && c <= 'z')
            return true;
        if (c >= 'A' && c <= 'Z')
            return true;
        if (c >= '0' && c <= '9')
            return true;
        if (c == 0 || c == 32 || c == 10 || c == 13 || c == 9 || c == 11 || c == 12)
            return true;
        return false;
    }

}
