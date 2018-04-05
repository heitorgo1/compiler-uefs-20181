package br.uefs.compiler.util.regex;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpecialCharacter {

    public static Set<Character> SPECIAL_CHARACTERS = new HashSet<>(Arrays.asList(
            'w', 'd', 's', 'y'
    ));

    public static boolean check(Character input) {
        return SPECIAL_CHARACTERS.contains(input);
    }

    public static String getExpression(Character input) throws Exception {

        StringBuilder sb = new StringBuilder();

        sb.append("(");

        switch (input) {
            case 'w':
                for (char c = 'a'; c <= 'z'; c++) {
                    sb.append(c);
                    sb.append("|");
                }
                for (char c = 'A'; c < 'Z'; c++) {
                    sb.append(c);
                    sb.append('|');
                }
                sb.append('Z');
                break;
            case 'd':
                for (char c = '0'; c < '9'; c++) {
                    sb.append(c);
                    sb.append("|");
                }
                sb.append('9');
                break;
            case 's':
                sb.append((char) 9);
                sb.append("|");
                sb.append((char) 10);
                sb.append("|");
                sb.append((char) 13);
                sb.append("|");
                sb.append((char) 32);
                break;
            case 'y':
                for (int c = 32; c < 126; c++) {
                    if (c == 34) continue;
                    sb.append((char) c);
                    sb.append("|");
                }
                sb.append((char) 126);
                break;
            default:
                throw new Exception(String.format("'%s' is not a special character.", input));
        }

        sb.append(")");

        return sb.toString();
    }
}