package br.uefs.compiler;

import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenRecognizerAutomataFactory;
import br.uefs.compiler.parser.Grammar;
import br.uefs.compiler.parser.GrammarBuilder;
import br.uefs.compiler.parser.ParsingTable;
import br.uefs.compiler.parser.PredictiveParser;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.cache.CacheHandler;
import br.uefs.compiler.util.errors.CompilerError;
import br.uefs.compiler.util.errors.ErrorFormatter;
import br.uefs.compiler.util.errors.GalvaoPhraseGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String INPUT_FOLDER = "./entrada";
    private static final String OUTPUT_FOLDER = "./saida";

    private static Grammar grammar = GrammarBuilder.build();
    private static DFA dfa;
    private static ErrorFormatter errorFormatter = new ErrorFormatter(
            "Erro na linha {{line}}: {{errorMessage}}",
            new GalvaoPhraseGenerator());

    private static void makeSureInputAndOutputFoldersExist() {
        new File(INPUT_FOLDER).mkdirs();
        new File(OUTPUT_FOLDER).mkdirs();
    }

    private static void tryToLoadAutomataFromCache() throws Exception {
        String cacheValidationStr = String.join(" ",
                TokenClass.CLASSES.stream().map(c -> c.toString()).collect(Collectors.toList()));

        if (CacheHandler.isCacheValid(cacheValidationStr)) {
            System.out.println("Automata in cache still valid! Reading it...");
            dfa = DFA.class.cast(CacheHandler.readCache());
        } else {
            System.out.println("Invalid automata in cache. Generating new automata...");
            dfa = TokenRecognizerAutomataFactory.getDFAFromClassList(TokenClass.CLASSES);
            CacheHandler.updateCache(cacheValidationStr, dfa);
        }
        System.out.println("Done.");
    }

    private static void writeTokensAndErrorsToFile(List<Token> tokens, List<CompilerError> errors, File outputFile) {
        try (FileWriter fw = new FileWriter(outputFile)) {
            fw.write(String.join("\n", tokens
                    .stream()
                    .map(t -> t.toString())
                    .collect(Collectors.toList())));
            fw.write("\n\n");
            if (errors.isEmpty()) fw.write("Sucesso!");
            else
                fw.write(String.join("\n", errors
                        .stream()
                        .map(t -> errorFormatter.format(t))
                        .collect(Collectors.toList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runForFilePath(Path path) {
        try {
            Lexer lexer = new Lexer(dfa, new FileReader(path.toFile()));
            PredictiveParser parser = new PredictiveParser(ParsingTable.build(grammar));

            List<Token> tokens = lexer.readAllTokens();
            parser.parse(tokens);

            List<CompilerError> errors = new ArrayList<>();
            errors.addAll(lexer.getErrors());
            errors.addAll(parser.getSyntaticErrors());
            errors.addAll(parser.getSemanticErrors());

            String outputFileName = String.format("saida_%s", path.getFileName());
            File outputFile = Paths.get(OUTPUT_FOLDER).resolve(outputFileName).toFile();

            writeTokensAndErrorsToFile(tokens, errors, outputFile);
        } catch (IOException e) {
            System.err.format("File not found or unavailable: %s\n", path.toString());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.format("Error trying to read tokens from: %s\n", path.toString());
            return;
        }
    }

    private static void readAndParseForInputFilenames(String[] filenames) {
        for (String filename : filenames) {
            runForFilePath(Paths.get(INPUT_FOLDER, filename));
        }
    }

    private static void readAndParseForEachInputFile() {
        try (Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER))) {
            paths.filter(Files::isRegularFile).forEach((Path path) -> {
                runForFilePath(path);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        makeSureInputAndOutputFoldersExist();
        tryToLoadAutomataFromCache();

        if (args.length > 0)
            readAndParseForInputFilenames(args);
        else
            readAndParseForEachInputFile();
    }
}
