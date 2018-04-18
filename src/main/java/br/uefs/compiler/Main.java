package br.uefs.compiler;

import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenRecognizerAutomataFactory;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.cache.CacheHandler;
import br.uefs.compiler.util.regex.Regex;
import br.uefs.compiler.util.regex.SpecialCharacter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String INPUT_FOLDER = "./entrada";
    private static final String OUTPUT_FOLDER = "./saida";

    public static void main(String[] args) throws Exception {
        new File(INPUT_FOLDER).mkdirs();
        new File(OUTPUT_FOLDER).mkdirs();
        Lexer lexer;
        DFA dfa;
        String cacheValidationStr = String.join(" ",
                TokenClass.CLASSES.stream().map(c -> c.toString()).collect(Collectors.toList()));

        if (CacheHandler.isCacheValid(cacheValidationStr)) {
            System.out.println("Automata in cache still valid! Reading it...");
            dfa = DFA.class.cast(CacheHandler.readCache());
        }
        else {
            System.out.println("Invalid automata in cache. Generating new automata...");
            dfa = TokenRecognizerAutomataFactory.getDFAFromClassList(TokenClass.CLASSES);
            CacheHandler.updateCache(cacheValidationStr, dfa);
        }
        System.out.println("Done.");

        lexer = new Lexer(dfa);

        try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
            paths.filter(Files::isRegularFile).forEach((Path path) -> {
                try {
                    lexer.withReader(new FileReader(path.toFile()));
                } catch (IOException e) {
                    System.err.format("File not found or unavailable: %s\n",path.toString());
                    return;
                }
                try {
                    List<Token> tokens = lexer.readAllTokens();
                    List<Token> errors = lexer.getErrors();

                    String outputFileName = String.format("saida_%s", path.getFileName());
                    File outputFile = Paths.get(OUTPUT_FOLDER).resolve(outputFileName).toFile();

                    try (FileWriter fw = new FileWriter(outputFile)) {
                        fw.write(String.join("\n", tokens
                                .stream()
                                .map(t -> t.toString())
                                .collect(Collectors.toList())));
                        fw.write("\n\n");
                        fw.write(String.join("\n", errors
                                .stream()
                                .map(t -> t.toString())
                                .collect(Collectors.toList())));
                    }
                } catch (Exception e) {
                    System.err.format("Error trying to read tokens from: %s\n",path.toString());
                    return;
                }
            });
        }

    }
}
