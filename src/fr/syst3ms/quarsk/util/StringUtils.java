package fr.syst3ms.quarsk.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings("unused")
public final class StringUtils {

    public static char[] alphabetLetters() {
        return "abdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public static List<String> sizedSplitString(CharSequence s, int groupSize, boolean equalGroupSizes) {
        return equalGroupSizes ? s.length() % groupSize == 0 ? Splitter.fixedLength(groupSize).splitToList(s) : new ArrayList<>() : Splitter.fixedLength(groupSize).splitToList(s);
    }

    public static String join(CharSequence... strings) {
        return Joiner.on("").join(strings);
    }

    public static String space(String regex, String s) {
        return sentenceCapitalization(Joiner.on(' ').join(s.split(regex)));
    }

    public static String sentenceCapitalization(String s) {
        return setCharAt(s.toLowerCase(), 0, Character.toUpperCase(s.charAt(0)));
    }

    public static String setCharAt(String s, int index, char c) {
        char[] chars = s.toCharArray();
        chars[index] = c;
        return new String(chars);
    }

    public static boolean containsIgnoreCase(String s, String check) {
        return s.toLowerCase().contains(check.toLowerCase());
    }
}
