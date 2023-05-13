public enum TokenType {
    // SPECIALS
    TKN_UNEXPECTED_EOF,
    TKN_INVALID_TOKEN,
    TKN_END_OF_FILE,

    // SYMBOLS
    TKN_COMMA,         // ,
    TKN_SEMICOLON,     // ;
    TKN_DOT,           // .

    TKN_OPEN_PAR,      // (
    TKN_CLOSE_PAR,     // )

    TKN_SINGLE_QUOTE,  // '

    // OPERATORS
    TKN_ASSIGN,        // =

    TKN_EQUAL,         // ==
    TKN_GREATER,       // >
    TKN_GREATER_EQ,    // >=
    TKN_LOWER,         // <
    TKN_LOWER_EQ,      // <=
    TKN_NOT_EQUAL,     // !=

    TKN_ADD,           // +
    TKN_SUB,           // -
    TKN_MUL,           // *
    TKN_DIV,           // /

    TKN_INVERT,        // !
    TKN_OR,            // ||
    TKN_AND,           // &&

    // KEYWORDS
    TKN_PROGRAM,       // program
    TKN_BEGIN,         // begin
    TKN_IS,            // is
    TKN_END,           // end

    TKN_IF,            // if
    TKN_ELSE,          // else
    TKN_THEN,          // then

    TKN_WHILE,         // while
    TKN_DO,            // do
    TKN_REPEAT,        // repeat
    TKN_UNTIL,         // until

    TKN_READ,          // read
    TKN_WRITE,         // write

    // TYPES
    TKN_ID,            // identifier
    TKN_INT,           // int
    TKN_FLOAT,         // float
    TKN_CHAR,          // char

    // LITERALS
    TKN_INT_VAL,
    TKN_FLOAT_VAL,
    TKN_STRING,
    TKN_LITERAL_CHAR
}
