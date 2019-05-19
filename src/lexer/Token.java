package lexer;

public enum Token {
    NUM(1), ID(2), KEYWORD(3),SYMBOL(4),COMMENT(5),WHITESPACE(6),EOF(7);

    int number;
    String tokenString;

    Token(int number) {
        this.number = number;
        if (number == 7){
            this.tokenString = "EOF";
        }
    }

    @Override
    public String toString() {
        if (this.number == 1 || this.number == 2 || this.number == 7){
            return this.name();
        }
        else if (this.number == 3 || this.number == 4){
            return this.tokenString;
        }
    return "";
    }
}