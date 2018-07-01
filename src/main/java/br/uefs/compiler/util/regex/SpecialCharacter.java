package br.uefs.compiler.util.regex;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Characters with special meaning for Regular Expressions
 * <p>
 * Supported special characters:
 * * l - Letter - [a-z] | [A-Z]
 * * d - Digit - [0-9]
 * * s - Whitespace - ASCII 9 | ASCII 10 | ASCII 13 | ASCII 32
 * * y - Symbols - ASCII from 32 to 126 (except 34)
 * * N - Special Symbols - "|#|$|%|'|:|?|@|\|^|_|`
 * * b - Symbols Without Slash - ASCII from 32 to 126 (except 34 and 47)
 * * i - Same as \y but with additional invalid symbols
 */
public class SpecialCharacter {

    public static Set<Character> SPECIAL_CHARACTERS = new HashSet<>(Arrays.asList(
            'l', 'd', 's', 'y', 'N', 'b', 'i'
    ));

    public static boolean check(Character input) {
        return SPECIAL_CHARACTERS.contains(input);
    }

    private static List<String> allLetters() {
        List<String> list = new ArrayList<>();
        IntStream range1 = IntStream.rangeClosed('a', 'z');
        IntStream range2 = IntStream.rangeClosed('A', 'Z');
        for (int i : IntStream.concat(range1, range2).toArray()) {
            list.add(Character.toString((char) i));
        }
        return list;
    }

    private static List<String> allDigits() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed('0', '9').toArray()) {
            list.add(Character.toString((char) i));
        }
        return list;
    }

    private static List<String> allSymbols() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed(32, 126).toArray()) {
            if (i == 34) continue;
            if (Operator.check((char) i) || '\\' == (char) i) {
                list.add("\\" + Character.toString((char) i));
            } else {
                list.add(Character.toString((char) i));

            }
        }
        return list;
    }

    private static List<String> validAndInvalidSymbols() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed(32, 255).toArray()) {
            if (i == 34 || i == 127) continue;
            if (Operator.check((char) i) || '\\' == (char) i) {
                list.add("\\" + Character.toString((char) i));
            } else {
                list.add(Character.toString((char) i));

            }
        }
        return list;
    }

    private static List<String> allNotDigitsNorLetterNorSpaceNorOperators() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed(34, 96).toArray()) {
            if (Character.isLetterOrDigit(i)) continue;
            if ((i >= 40 && i <= 47) || i == 91 || i == 93 || (i >= 59 && i <= 62) || i == 38) continue;
            if (Operator.check((char) i) || '\\' == (char) i) {
                list.add("\\" + Character.toString((char) i));
            } else {
                list.add(Character.toString((char) i));

            }
        }
        return list;
    }

    private static List<String> allBlockCommentSymbols() {
        List<String> list = new ArrayList<>();
        for (int i : IntStream.rangeClosed(32, 126).toArray()) {
            if (i == 34 || i == 47) continue;
            if (Operator.check((char) i) || '\\' == (char) i) {
                list.add("\\" + Character.toString((char) i));
            } else {
                list.add(Character.toString((char) i));

            }
        }
        return list;
    }

    /**
     * Given a special character, return the regular expression that
     * it represents.
     *
     * e.g.: for input = d, return = "(0|1|2|3|4|5|6|7|8|9)"
     *
     * @param input Special Character
     * @return String regular expression
     */
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
                        Character.toString((char) 9),
                        Character.toString((char) 10),
                        Character.toString((char) 13),
                        Character.toString((char) 32)
                        )
                );
                break;
            case 'y':
                sb.append(String.join("|", allSymbols()));
                break;
            case 'i':
                sb.append(String.join("|", validAndInvalidSymbols()));
                break;
            case 'N':
                sb.append(String.join("|", allNotDigitsNorLetterNorSpaceNorOperators()));
                break;
            case 'b':
                sb.append(String.join("|", allBlockCommentSymbols()));
                break;
            default:
                throw new Exception(String.format("'%s' is not a special character.", input));
        }

        sb.append(")");

        return sb.toString();
    }
}