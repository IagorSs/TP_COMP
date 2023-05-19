import java.util.ArrayList;

public class SyntaticAnalysis {
    private Lexeme currentLexeme;

    private final LexicalAnalysis lexicalAnalysis;

    public SyntaticAnalysis(LexicalAnalysis lexicalAnalysis) {
        this.lexicalAnalysis = lexicalAnalysis;
    }

    private void advance() {
        this.currentLexeme = lexicalAnalysis.getNextLexeme();
    }

    private static void error(TokenType expected, TokenType getted) {
        // TODO throw specific Exception
        throw new RuntimeException("Unexpected Lexeme. Expecting '" + expected.toString() + "' and get '" + getted.toString() + "'");
    }

    private static void error(TokenType getted) {
        // TODO throw specific Exception
        throw new RuntimeException("Unexpected Lexeme. Get '" + getted.toString() + "'");
    }

    private void eat(TokenType t) {
        if (currentLexeme.type == t) advance();
        else error(t, currentLexeme.type);
    }

    public void analyseProduction() {
        this.currentLexeme = lexicalAnalysis.getNextLexeme();

        program();
    }

    /*
        program ::= program identifier
                    [decl-list]
                    begin
                    stmt-list end "."

     */
    private void program() {
        eat(TokenType.TKN_PROGRAM);
        eat(TokenType.TKN_ID);

        if(this.currentLexeme.type == TokenType.TKN_ID) declList();

        eat(TokenType.TKN_BEGIN);

        stmtList();

        eat(TokenType.TKN_END);
        eat(TokenType.TKN_DOT);
    }

    // decl-list ::= decl ";" { decl ";"}
    private void declList() {
        while(this.currentLexeme.type == TokenType.TKN_ID){
            decl();
            eat(TokenType.TKN_SEMICOLON);
        }
    }

    // decl ::= ident-list is type
    private void decl() {
        identList();
        eat(TokenType.TKN_IS);
        type();
    }

    // ident-list ::= identifier {"," identifier}
    private void identList() {
        eat(TokenType.TKN_ID);

        while(this.currentLexeme.type == TokenType.TKN_COMMA) {
            eat(TokenType.TKN_COMMA);
            eat(TokenType.TKN_ID);
        }
    }

    // type ::= int
    //	        | float
    //	        | char
    private void type() {
        switch (this.currentLexeme.type) {
            case TKN_INT -> eat(TokenType.TKN_INT);
            case TKN_FLOAT -> eat(TokenType.TKN_FLOAT);
            case TKN_CHAR -> eat(TokenType.TKN_CHAR);
            default -> SyntaticAnalysis.error(this.currentLexeme.type);
        }
    }

    // stmt-list ::= stmt {";" stmt}
    private void stmtList() {
        stmt();

        while (this.currentLexeme.type == TokenType.TKN_SEMICOLON) {
            eat(TokenType.TKN_SEMICOLON);
            stmt();
        }
    }

    // stmt ::= assign-stmt
    //	        | if-stmt
    //	        | while-stmt
    //	        | repeat-stmt
    //	        | read-stmt
    //	        | write-stmt
    private void stmt() {
        switch (this.currentLexeme.type) {
            case TKN_ID -> assignStmt();
            case TKN_IF -> ifStmt();
            case TKN_WHILE -> whileStmt();
            case TKN_REPEAT -> repeatStmt();
            case TKN_READ -> readStmt();
            case TKN_WRITE -> writeStmt();
            default -> error(this.currentLexeme.type);
        }
    }

    // assign-stmt ::= identifier "=" simple_expr
    private void assignStmt() {
        eat(TokenType.TKN_ID);
        eat(TokenType.TKN_ASSIGN);

        simpleExpr();
    }

    // if-stmt ::= if condition then stmt-list if-stmt’
    private void ifStmt() {
        eat(TokenType.TKN_IF);

        condition();

        eat(TokenType.TKN_THEN);

        stmtList();

        ifStmt_();
    }

    // if-stmt’ ::= end
    //	        | else stmt-list end
    private void ifStmt_() {
        if (this.currentLexeme.type == TokenType.TKN_END) eat(TokenType.TKN_END);
        else {
            eat(TokenType.TKN_ELSE);

            stmtList();

            eat(TokenType.TKN_END);
        }
    }

    // condition ::= expression
    private void condition() {
        expression();
    }

    // repeat-stmt ::= repeat stmt-list stmt-suffix
    private void repeatStmt() {
        eat(TokenType.TKN_REPEAT);

        stmtList();

        stmtSuffix();
    }

    // stmt-suffix ::= until condition
    private void stmtSuffix() {
        eat(TokenType.TKN_UNTIL);

        condition();
    }

    // while-stmt ::= stmt-prefix stmt-list end
    private void whileStmt() {
        stmtPrefix();

        stmtList();

        eat(TokenType.TKN_END);
    }

    // stmt-prefix ::= while condition do
    private void stmtPrefix() {
        eat(TokenType.TKN_WHILE);

        condition();

        eat(TokenType.TKN_DO);
    }

    // read-stmt ::= read "(" identifier ")"
    private void readStmt() {
        eat(TokenType.TKN_READ);
        eat(TokenType.TKN_OPEN_PAR);
        eat(TokenType.TKN_ID);
        eat(TokenType.TKN_CLOSE_PAR);
    }

    // write-stmt ::= write "(" writable ")"
    private void writeStmt() {
        eat(TokenType.TKN_WRITE);
        eat(TokenType.TKN_OPEN_PAR);
        writable();
        eat(TokenType.TKN_CLOSE_PAR);
    }

    // writable ::= simple-expr
    //	            | literal
    private void writable() {
        if (this.currentLexeme.type == TokenType.TKN_OPEN_KEY) literal();
        else {
            simpleExpr();
        }
    }

    // expression ::= simple-expr expression’
    private void expression() {
        simpleExpr();

        expression_();
    }

    // expression’ ::= relop simple-expr
    //	                | λ
    private void expression_() {
        ArrayList<TokenType> relopFirst = new ArrayList<>(){
            {
                add(TokenType.TKN_EQUAL);
                add(TokenType.TKN_GREATER);
                add(TokenType.TKN_GREATER_EQ);
                add(TokenType.TKN_LOWER);
                add(TokenType.TKN_LOWER_EQ);
                add(TokenType.TKN_NOT_EQUAL);
            }
        };

        if (relopFirst.contains(this.currentLexeme.type)) {
            relop();

            simpleExpr();
        }
    }

    // simple-expr ::= term simple-expr’
    private void simpleExpr() {
        term();

        simpleExpr_();
    }

    // simple-expr’ ::= addop term simple-expr’
    //	                | λ
    private void simpleExpr_() {
        ArrayList<TokenType> addopFirst = new ArrayList<>(){
            {
                add(TokenType.TKN_ADD);
                add(TokenType.TKN_SUB);
                add(TokenType.TKN_OR);
            }
        };

        if (addopFirst.contains(this.currentLexeme.type)) {
            addop();

            term();

            simpleExpr_();
        }
    }

    // term ::= factor-a term’
    private void term() {
        factorA();

        term_();
    }

    // term’ ::= mulop factor-a term’
    //          | λ
    private void term_() {
        ArrayList<TokenType> mulopFirst = new ArrayList<>(){
            {
                add(TokenType.TKN_MUL);
                add(TokenType.TKN_DIV);
                add(TokenType.TKN_AND);
            }
        };

        if (mulopFirst.contains(this.currentLexeme.type)) {
            mulop();

            factorA();

            term_();
        }
    }

    // factor-a ::= factor
    //	            | "!" factor
    //              | "-" factor
    private void factorA() {
        if (this.currentLexeme.type == TokenType.TKN_INVERT) eat(TokenType.TKN_INVERT);
        else if (this.currentLexeme.type == TokenType.TKN_SUB) eat(TokenType.TKN_SUB);

        factor();
    }

    // factor ::= identifier
    //	            | constant
    //              | "(" expression ")"
    private void factor() {
        switch (this.currentLexeme.type) {
            case TKN_ID:
                eat(TokenType.TKN_ID);
                break;

            case TKN_INT_VAL, TKN_FLOAT_VAL, TKN_CHAR:
                constant();
                break;

            case TKN_OPEN_PAR:
                eat(TokenType.TKN_OPEN_PAR);
                expression();
                eat(TokenType.TKN_CLOSE_PAR);
                break;

            default:
                error(this.currentLexeme.type);
                break;
        }
    }

    // relop ::= "=="
    //     	   | ">"
    //  	   | ">="
    //  	   | "<"
    //  	   | "<="
    //  	   | "!="
    private void relop() {
        switch (this.currentLexeme.type) {
            case TKN_EQUAL -> eat(TokenType.TKN_EQUAL);
            case TKN_GREATER -> eat(TokenType.TKN_GREATER);
            case TKN_GREATER_EQ -> eat(TokenType.TKN_GREATER_EQ);
            case TKN_LOWER -> eat(TokenType.TKN_LOWER);
            case TKN_LOWER_EQ -> eat(TokenType.TKN_LOWER_EQ);
            case TKN_NOT_EQUAL -> eat(TokenType.TKN_NOT_EQUAL);
            default -> error(this.currentLexeme.type);
        }
    }

    // addop ::= "+"
    //  	   | "-"
    //  	   | "||"
    private void addop() {
        switch (this.currentLexeme.type) {
            case TKN_ADD -> eat(TokenType.TKN_ADD);
            case TKN_SUB -> eat(TokenType.TKN_SUB);
            case TKN_OR -> eat(TokenType.TKN_OR);
            default -> error(this.currentLexeme.type);
        }
    }

    // mulop ::= "*"
    //  	   | "/"
    //  	   | "&&"
    private void mulop() {
        switch (this.currentLexeme.type) {
            case TKN_MUL -> eat(TokenType.TKN_MUL);
            case TKN_DIV -> eat(TokenType.TKN_DIV);
            case TKN_AND -> eat(TokenType.TKN_AND);
            default -> error(this.currentLexeme.type);
        }
    }

    // constant ::= integer_const
    //  	       | float_const
    //      	   | char_const
    private void constant() {
        switch (this.currentLexeme.type) {
            case TKN_INT_VAL -> eat(TokenType.TKN_INT_VAL);
            case TKN_FLOAT_VAL -> eat(TokenType.TKN_FLOAT_VAL);
            case TKN_CHAR_VAL -> eat(TokenType.TKN_CHAR_VAL);
            default -> error(this.currentLexeme.type);
        }
    }

    // literal ::= “{“ string “}”
    private void literal() {
        eat(TokenType.TKN_OPEN_KEY);
        eat(TokenType.TKN_STRING);
        eat(TokenType.TKN_CLOSE_KEY);
    }
}
