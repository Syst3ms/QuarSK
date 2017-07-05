package fr.syst3ms.quarsk.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 03/02/2017.
 */
public final class StringUtils {

	public static char[] alphabetLetters() {
		return "abdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	}

	@NotNull
	public static List<String> sizedSplitString(@NotNull CharSequence s, int groupSize, boolean equalGroupSizes) {
		if (s.length() % groupSize == 0) {
			return Splitter.fixedLength(groupSize).splitToList(s);
		} else {
			return equalGroupSizes ? new ArrayList<String>() : Splitter.fixedLength(groupSize).splitToList(s);
		}
	}

	public static String join(@NotNull CharSequence... strings) {
		return Joiner.on("").join(strings);
	}

	public static String englishJoin(@NotNull String[] parts) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (i + 1 == parts.length) {
				sb.append(" and ").append(part);
			} else {
				sb.append(", ").append(part);
			}
		}
		return sb.toString();
	}
}
