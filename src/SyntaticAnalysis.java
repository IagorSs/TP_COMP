public class SyntaticAnalysis {
    private Lexeme currentLexeme;

    private final LexicalAnalysis lexicalAnalysis;

    public SyntaticAnalysis(LexicalAnalysis lexicalAnalysis) {
        this.lexicalAnalysis = lexicalAnalysis;
    }

    private void advance() {
        this.currentLexeme = lexicalAnalysis.getNextLexeme();
    }

    private void eat(TokenType t) {
        if (currentLexeme.type == t) advance();
        // TODO throw specific Exception
        else throw new RuntimeException("Unexpected Lexeme. Expecting '" + t.toString() + "' and get '" + currentLexeme.type.toString() + "'");
    }

    public void analyseProduction() {
        this.currentLexeme = lexicalAnalysis.getNextLexeme();

        this.program();
    }

    /*
        program ::= program identifier begin
                    [decl-list]
                    stmt-list end "."

     */
    private void program() {
        eat(TokenType.TKN_PROGRAM);
        eat(TokenType.TKN_ID);
        eat(TokenType.TKN_BEGIN);
    }
}
