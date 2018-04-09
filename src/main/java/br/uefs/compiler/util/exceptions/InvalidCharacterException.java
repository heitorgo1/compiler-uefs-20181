package br.uefs.compiler.util.exceptions;

public class InvalidCharacterException extends Exception {

    private Character c;

    public InvalidCharacterException(Character c){
        super();
        this.c = c;
    }

    public InvalidCharacterException(String message, Character c){
        super(message);
        this.c = c;
    }

    public Character getInvalidCharacter() {
        return c;
    }
}
