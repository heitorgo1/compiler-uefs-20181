package br.uefs.compiler.lexer.token;

import br.uefs.compiler.util.errors.CompilerError;

import java.util.HashMap;
import java.util.Map;

/**
 * Associate error tokens to messages
 */
public class TokenError implements CompilerError {

    private static Map<String, String> MESSAGE_MAP = new HashMap<String, String>() {{
        put("CADEIAABERTA", "Cadeia aberta '%s'.");
        put("VALORINESPERADO", "Valor inesperado '%s'.");
        put("CARACTEREINVALIDO", "Caractere inválido '%s'.");
        put("COMENTARIOBLOCOABERTO", "Comentário de bloco aberto.");
    }};

    private Token token;
    private String message;

    public TokenError(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    public long getLine() {
        return token.getLine();
    }

    public String getMessage() {
        return message;
    }

    public static TokenError of(Token token) throws Exception {
        String tokenName = token.getTokenClass().getName();
        if (!tokenName.contains("ERROR"))
            throw new Exception(String.format("Not an error token: %s", token));
        if (!MESSAGE_MAP.containsKey(tokenName.substring(6)))
            throw new Exception(String.format("Invalid error token: %s", token));
        String message = MESSAGE_MAP.get(tokenName.substring(6));
        message = String.format(message, token.getLexeme());
        return new TokenError(token, message);
    }

}
