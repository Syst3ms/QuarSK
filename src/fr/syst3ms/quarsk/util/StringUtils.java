package fr.syst3ms.quarsk.util;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings("unused")
public class StringUtils {
    private static final StringUtils instance = new StringUtils();

    public static StringUtils getInstance() {
        return instance;
    }

    public char[] alphabetLetters() {
        return "abdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public List<String> sizedSplitString(CharSequence s, int groupSize, boolean equalGroupSizes) {
        return equalGroupSizes ? s.length() % groupSize == 0 ? Splitter.fixedLength(groupSize).splitToList(s) : new ArrayList<>() : Splitter.fixedLength(groupSize).splitToList(s);
    }

    public String join(CharSequence... strings) {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(strings).forEach(builder::append);
        return builder.toString();
    }
}
