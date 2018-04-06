package br.uefs.compiler.util.regex;

import java.util.*;
import java.util.stream.IntStream;

public class SpecialCharacter {

    public static Set<Character> SPECIAL_CHARACTERS = new HashSet<>(Arrays.asList(
            'l', 'd', 's', 'y'
    ));

    public static boolean check(Character input) {
        return SPECIAL_CHARACTERS.contains(input);
    }

    private static List<String> allLetters() {
        List<String> list = new ArrayList<>();
        IntStream range1 = IntStream.rangeClosed('a','z');
        IntStream range2 = IntStream.rangeClosed('A','Z');
        for (int i : IntStream.concat(range1, range2).toArray()) {
            list.add(Character.toString((char)i));
        }
        return list;
    }

    private static List<String> allDigits() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed('0', '9').toArray()) {
            list.add(Character.toString((char)i));
        }
        return list;
    }

    private static List<String> allSymbols() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed(32, 126).toArray()) {
            if (i == 34) continue;
            list.add(Character.toString((char)i));
        }
        return list;
    }

    public static String getExpression(Character input) throws Exception {

        StringBuilder sb = new StringBuilder();

        sb.append("(");

        switch (input) {
            case 'l':
                sb.append(String.join("|", allLetters()));
                break;
            case 'd':
                sb.append(String.join("|", allDigits()));
                break;
            case 's':
                sb.append(String.join("|",
                        Character.toString((char)9),
                        Character.toString((char)10),
                        Character.toString((char)13),
                        Character.toString((char)32)
                        )
                );
                break;
            case 'y':
                sb.append(String.join("|", allSymbols()));
                break;
            default:
                throw new Exception(String.format("'%s' is not a special character.", input));
        }

        sb.append(")");

        return sb.toString();
    }
}