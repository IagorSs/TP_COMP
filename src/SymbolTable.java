import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, TokenType> symbolTable = new HashMap<>(){
        {
            put(",", TokenType.TKN_COMMA);
            put(";", TokenType.TKN_SEMICOLON);
            put(".", TokenType.TKN_DOT);

            put("(", TokenType.TKN_OPEN_PAR);
            put(")", TokenType.TKN_CLOSE_PAR);

            put("'", TokenType.TKN_SINGLE_QUOTE);

            put("=", TokenType.TKN_ASSIGN);

            put("==", TokenType.TKN_EQUAL);
            put(">", TokenType.TKN_GREATER);
            put(">=", TokenType.TKN_GREATER_EQ);
            put("<", TokenType.TKN_LOWER);
            put("<=", TokenType.TKN_LOWER_EQ);
            put("!=", TokenType.TKN_NOT_EQUAL);

            put("+", TokenType.TKN_ADD);
            put("-", TokenType.TKN_SUB);
            put("*", TokenType.TKN_MUL);
            put("/", TokenType.TKN_DIV);

            put("!", TokenType.TKN_INVERT);
            put("||", TokenType.TKN_OR);
            put("&&", TokenType.TKN_AND);

            put("program", TokenType.TKN_PROGRAM);
            put("begin", TokenType.TKN_BEGIN);
            put("is", TokenType.TKN_IS);
            put("end", TokenType.TKN_END);

            put("if", TokenType.TKN_IF);
            put("else", TokenType.TKN_ELSE);
            put("then", TokenType.TKN_THEN);

            put("while", TokenType.TKN_WHILE);
            put("do", TokenType.TKN_DO);
            put("repeat", TokenType.TKN_REPEAT);
            put("until", TokenType.TKN_UNTIL);

            put("read", TokenType.TKN_READ);
            put("write", TokenType.TKN_WRITE);

            put("int", TokenType.TKN_INT);
            put("float", TokenType.TKN_FLOAT);
            put("char", TokenType.TKN_CHAR);
        }
    };

    public TokenType getAssociatedTokenType(String token) {
        return symbolTable.getOrDefault(token, TokenType.TKN_ID);
    }
}
