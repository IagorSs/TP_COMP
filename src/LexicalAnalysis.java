import java.io.*;

public class LexicalAnalysis {
    private final PushbackInputStream pushbackInputStream;
    private int currentLine;

    private int state;

    private final SymbolTable symbolTable = new SymbolTable();

    public LexicalAnalysis(String filename) throws FileNotFoundException {
        File file = new File(filename);

        pushbackInputStream = new PushbackInputStream(new FileInputStream(file));

        currentLine = 1;
        state = 1;
    }

    public int getLine() {
        return currentLine;
    }

    private void unread(int fileChar) throws IOException {
        if (fileChar != -1) pushbackInputStream.unread(fileChar);
    }

    public Lexeme getNextLexeme() {
        try{
            return this.getNextLexemePrivate();
        } catch (Exception err) {
            return null;
        }
    }

    private Lexeme getNextLexemePrivate() throws IOException {
        Lexeme lexeme = new Lexeme();
        lexeme.token = "";
        lexeme.type = TokenType.TKN_END_OF_FILE;

        int fileChar;

        boolean returnLexema = false;

        while((state != 2 && state != 17) && !returnLexema) {
            fileChar = pushbackInputStream.read();

            switch (state) {
                case 1:
                    switch (fileChar) {
                        case '\n':
                            currentLine++;
                        case '\t', '\r', ' ':
                            break;

                        case -1:
                            state = 17;
                            break;

                        case ',', ';', '.', '(', ')', '+', '-', '*', '}':
                            state = 2;
                            lexeme.token += (char) fileChar;
                            break;

                        case '=', '>', '<', '!':
                            state = 3;
                            lexeme.token += (char) fileChar;
                            break;

                        case '|':
                            state = 4;
                            lexeme.token += (char) fileChar;
                            break;

                        case '&':
                            state = 5;
                            lexeme.token += (char) fileChar;
                            break;

                        case '/':
                            state = 6;
                            lexeme.token += (char) fileChar;
                            break;

                        case '\'':
                            lexeme.token += (char) fileChar;
                            state = 11;
                            returnLexema = true;
                            break;

                        case '{':
                            lexeme.token += (char) fileChar;
                            state = 13;
                            returnLexema = true;
                            break;

                        default:
                            if (Character.isDigit(fileChar)) {
                                lexeme.token += (char) fileChar;
                                state = 9;
                            } else if (Character.isAlphabetic(fileChar)) {
                                lexeme.token += (char) fileChar;
                                state = 15;
                            }
                            else {
                                lexeme.type = TokenType.TKN_INVALID_TOKEN;
                                state = 17;
                            }
                            break;
                    }
                    break;

                case 3:
                    if (fileChar == '=') lexeme.token += (char) fileChar;
                    else unread(fileChar);

                    state = 2;
                    break;

                case 4:
                    if (fileChar == '|') {
                        lexeme.token += (char) fileChar;
                        state = 2;
                    }
                    else {
                        lexeme.type = TokenType.TKN_INVALID_TOKEN;
                        state = 17;
                    }

                    break;

                case 5:
                    if (fileChar == '&') {
                        lexeme.token += (char) fileChar;
                        state = 2;
                    }
                    else {
                        lexeme.type = TokenType.TKN_INVALID_TOKEN;
                        state = 17;
                    }

                    break;

                case 6:
                    if (fileChar == '*') {
                        state = 7;
                    } else {
                        state = 2;
                    }

                    break;

                case 7:
                    if (fileChar == '*') {
                        state = 8;
                    } else if (fileChar == -1) {
                        lexeme.type = TokenType.TKN_UNEXPECTED_EOF;
                        state = 17;
                    } else if (fileChar == '\n') {
                        currentLine++;
                    }

                    break;

                case 8:
                    if (fileChar == '/') {
                        lexeme.token = "";
                        state = 1;
                    }
                    else {
                        unread(fileChar);
                        state = 7;
                    }

                    break;

                case 9:
                    if (Character.isDigit(fileChar)) {
                        lexeme.token += (char) fileChar;
                    } else if (fileChar == '.') {
                        lexeme.token += (char) fileChar;
                        state = 16;
                    } else {
                        lexeme.type = TokenType.TKN_INT_VAL;
                        unread(fileChar);
                        state = 17;
                    }

                    break;

                case 16:
                    if (Character.isDigit(fileChar)) {
                        lexeme.token += (char) fileChar;
                        state = 10;
                    }
                    else {
                        unread(fileChar);
                        lexeme.type = TokenType.TKN_INVALID_TOKEN;
                        state = 17;
                    }

                    break;

                case 10:
                    if (Character.isDigit(fileChar)) {
                        lexeme.token += (char) fileChar;
                    }
                    else {
                        unread(fileChar);
                        lexeme.type = TokenType.TKN_FLOAT_VAL;
                        state = 17;
                    }

                    break;

                case 11:
                    lexeme.token += (char) fileChar;
                    lexeme.type = TokenType.TKN_CHAR_VAL;

                    state = 12;
                    returnLexema = true;
                    break;

                case 12:
                    lexeme.token += (char) fileChar;

                    if ((char) fileChar == '\'') {
                        state = 2;
                    }
                    else {
                        lexeme.type = TokenType.TKN_INVALID_TOKEN;
                        state = 17;
                    }

                    break;

                case 13:
                    if ((char) fileChar == '}') {
                        unread(fileChar);
                        lexeme.type = TokenType.TKN_STRING;
                        state = 17;
                    }
                    else if ((char) fileChar == '\n') {
                        state = 17;
                        lexeme.type = TokenType.TKN_INVALID_TOKEN;
                    } else {
                        lexeme.token += (char) fileChar;
                    }

                    break;

                case 15:
                    if (Character.isDigit(fileChar) || Character.isAlphabetic(fileChar) || fileChar == '_') {
                        lexeme.token += (char) fileChar;
                    } else {
                        unread(fileChar);
                        state = 2;
                    }
            }
        }

        if (state == 2 || state == 11 || state == 13) lexeme.type = symbolTable.getAssociatedTokenType(lexeme.token);

        if (state == 2 || state == 17) state = 1;

        return lexeme;
    }
}
