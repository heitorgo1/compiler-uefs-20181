package br.uefs.compiler.util.errors;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GalvaoPhraseGenerator implements PhraseGenerator {

    private static Random rnd = new Random();
    private static int GERMANY_GOALS = 0;
    private static List<String> GERMANY_PLAYERS = Arrays.asList(
            "Lahm",
            "Boateng",
            "Hummels",
            "Höwedes",
            "Schweinsteiger",
            "Khedira",
            "Müller",
            "Kroos",
            "Özil",
            "Klose",
            "Schürrle",
            "Draxler"
    );
    private static List<String> COMMON_PHRASES = Arrays.asList(
            "Pode isso Arnaldo?",
            "Virou passeio!",
            "E lá vem mais!",
            "Realmente, um dia muito triste.",
            "Perdeu inteiramente o controle do jogo.",
            "É pedir pra tomar gol.",
            "Toma-se o caminho do maior vexame brasileiro!",
            "E a torcida grita 'olé'!",
            "É teste pra cardiáco, amigo!",
            "Olha o que ele faz, olha o que ele faz!"
    );

    private static String germanyGamePhrase() {
        return String.format("Gol da Alemanha, %s! Alemanha %d x 0 Brasil.",
                GERMANY_PLAYERS.get(rnd.nextInt(GERMANY_PLAYERS.size())),
                ++GERMANY_GOALS);
    }

    private static String getCommonPhrase() {
        return COMMON_PHRASES.get(rnd.nextInt(COMMON_PHRASES.size()));
    }

    @Override
    public String getPhrase() {
        return rnd.nextBoolean() ? germanyGamePhrase() : getCommonPhrase();
    }
}
