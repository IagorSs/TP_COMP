import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(args[0]);
        SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis(lexicalAnalysis);

        syntaticAnalysis.analyseProduction();

//        Lexeme lexeme;
//
//        while ((lexeme = lexicalAnalysis.getNextLexeme()).type != TokenType.TKN_END_OF_FILE) {
//            if (lexeme.type != TokenType.TKN_INVALID_TOKEN && lexeme.type != TokenType.TKN_UNEXPECTED_EOF) System.out.println(lexeme.token + " " + lexeme.type);
//            else System.out.println("ERRO: " + lexeme.token + " " + lexeme.type + " -> linha " + lexicalAnalysis.getLine());
//        }
    }
}