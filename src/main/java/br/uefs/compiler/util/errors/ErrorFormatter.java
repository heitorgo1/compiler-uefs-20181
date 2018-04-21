package br.uefs.compiler.util.errors;

public class ErrorFormatter {

    private final PhraseGenerator pg;
    private final String format;

    public ErrorFormatter(String format, PhraseGenerator pg) {
        this.pg = pg;
        this.format = format;
    }

    public String interpolate(CompilerError error) {
        String message = format;
        message = message.replace("{{line}}", String.valueOf(error.getLine()));
        message = message.replace("{{phrase}}", pg.getPhrase());
        message = message.replace("{{errorMessage}}", error.getMessage());
        return message;
    }

    public String format(CompilerError error) {
        String message = interpolate(error);
        return message;
    }
}
