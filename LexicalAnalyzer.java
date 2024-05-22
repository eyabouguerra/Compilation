import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    public static void main(String[] args) {
        String inputProgram = "@PROG @DECL %1:ENTIER,%2:REEL DECL@ CORPS @CORPS %1:=10;%2:=3.14;ECRIRE(\"Hello, World!\") CORPS@ PROG@";
        ArrayList<Token> tokens = analyze(inputProgram);
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public static ArrayList<Token> analyze(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        // Define regular expressions for each token type
        Pattern pattern = Pattern.compile("@\\w+|%,?\\d+\\.?\\d*|%\\d+|[A-Z]+|:=|\".*?\"|\\+\\+|\\[|\\]|\\(|\\)|;|\\<\\>|\\<\\=|\\>|\\=\\=|\\<|\\>|\\=|\\,|\\:");
        Matcher matcher = pattern.matcher(input);

        // Tokenize input
        while (matcher.find()) {
            String lexeme = matcher.group();
            LexicalUnit lexicalUnit = determineLexicalUnit(lexeme);
            tokens.add(new Token(lexeme, lexicalUnit));
        }

        return tokens;
    }

    private static LexicalUnit determineLexicalUnit(String lexeme) {
        switch (lexeme) {
            case "@PROG":
            case "@DECL":
            case "@CORPS":
            case "CORPS@":
            case "DECL@":
            case "FOR":
            case "ECRIRE":
                return LexicalUnit.KEYWORD;
            case "ENTIER":
            case "REEL":
            case "CHAINE":
            case "TABLEAU":
                return LexicalUnit.TYPE;
            case ":=":
            case "++":
                return LexicalUnit.OPERATOR;
            case "%":
                return LexicalUnit.MODIFIER;
            case "(":
            case ")":
            case "[":
            case "]":
            case ";":
            case ",":
            case ":":
                return LexicalUnit.SYMBOL;
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "==":
            case "<>":
            case "=":
                return LexicalUnit.RELATIONAL_OPERATOR;
            case "\"":
                return LexicalUnit.STRING_DELIMITER;
            default:
                if (lexeme.matches("%\\d+")) {
                    return LexicalUnit.VARIABLE;
                } else if (lexeme.matches("\\d+\\.?\\d*")) {
                    return LexicalUnit.FLOAT;
                } else if (lexeme.matches("\\d+")) {
                    return LexicalUnit.INTEGER;
                } else if (lexeme.matches("\".*?\"")) {
                    return LexicalUnit.STRING_LITERAL;
                } else {
                    return LexicalUnit.IDENTIFIER;
                }
        }
    }
}

enum LexicalUnit {
    KEYWORD,
    TYPE,
    OPERATOR,
    MODIFIER,
    SYMBOL,
    RELATIONAL_OPERATOR,
    VARIABLE,
    INTEGER,
    FLOAT,
    STRING_LITERAL,
    IDENTIFIER,
    STRING_DELIMITER
}

class Token {
    String lexeme;
    LexicalUnit lexicalUnit;

    Token(String lexeme, LexicalUnit lexicalUnit) {
        this.lexeme = lexeme;
        this.lexicalUnit = lexicalUnit;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexeme='" + lexeme + '\'' +
                ", lexicalUnit=" + lexicalUnit +
                '}';
    }
}
