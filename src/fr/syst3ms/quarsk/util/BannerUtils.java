package fr.syst3ms.quarsk.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 25/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class BannerUtils {
    private static BannerUtils instance = new BannerUtils();

    public static BannerUtils getInstance() {
        return instance;
    }

    /**
     * Converts a banner color from Miner Needs Cool Shoes
     * @param c the char representing the color
     * @return DyeColor
     */
    public DyeColor colorFromMnc(char c) { //Don't question the characters, it's just like that.
        switch (c) {
            case 'a':
                return DyeColor.BLACK;
            case 'b':
                return DyeColor.RED;
            case 'c':
                return DyeColor.GREEN;
            case 'd':
                return DyeColor.BROWN;
            case 'e':
                return DyeColor.BLUE;
            case 'f':
                return DyeColor.PURPLE;
            case 'g':
                return DyeColor.CYAN;
            case 'h':
                return DyeColor.SILVER;
            case 'i':
                return DyeColor.GRAY;
            case 'j':
                return DyeColor.PINK;
            case 'k':
                return DyeColor.LIME;
            case 'l':
                return DyeColor.YELLOW;
            case 'm':
                return DyeColor.LIGHT_BLUE;
            case 'n':
                return DyeColor.MAGENTA;
            case 'o':
                return DyeColor.ORANGE;
            case 'p':
                return DyeColor.WHITE;
            default:
                return null;
        }
    }

    /**
     * Converts a banner pattern from Miner Needs Cool Shoes
     * @param c the char representing the pattern
     * @return PatternType
     */
    public PatternType patternTypeFromMnc(char c) {
        switch (c) {
            case 'b':
                return PatternType.SQUARE_BOTTOM_LEFT;
            case 'c':
                return PatternType.BORDER;
            case 'd':
                return PatternType.SQUARE_BOTTOM_RIGHT;
            case 'e':
                return PatternType.BRICKS;
            case 'f':
                return PatternType.STRIPE_BOTTOM;
            case 'g':
                return PatternType.TRIANGLE_BOTTOM;
            case 'h':
                return PatternType.TRIANGLES_BOTTOM;
            case 'i':
                return PatternType.CURLY_BORDER;
            case 'j':
                return PatternType.CROSS;
            case 'k':
                return PatternType.CREEPER;
            case 'l':
                return PatternType.STRIPE_CENTER;
            case 'm':
                return PatternType.STRIPE_DOWNLEFT;
            case 'n':
                return PatternType.STRIPE_DOWNRIGHT;
            case 'o':
                return PatternType.FLOWER;
            case 'p':
                return PatternType.GRADIENT_UP;
            case 'q':
                return PatternType.HALF_HORIZONTAL;
            case 'r':
                return PatternType.DIAGONAL_LEFT;
            case 's':
                return PatternType.STRIPE_LEFT;
            case 't':
                return PatternType.CIRCLE_MIDDLE;
            case 'u':
                return PatternType.MOJANG;
            case 'v':
                return PatternType.RHOMBUS_MIDDLE;
            case 'w':
                return PatternType.STRIPE_MIDDLE;
            case 'x':
                return PatternType.DIAGONAL_RIGHT;
            case 'y':
                return PatternType.STRIPE_RIGHT;
            case 'z':
                return PatternType.STRAIGHT_CROSS;
            case 'A':
                return PatternType.SKULL;
            case 'B':
                return PatternType.STRIPE_SMALL;
            case 'C':
                return PatternType.SQUARE_TOP_LEFT;
            case 'D':
                return PatternType.SQUARE_TOP_RIGHT;
            case 'E':
                return PatternType.STRIPE_TOP;
            case 'F':
                return PatternType.TRIANGLE_TOP;
            case 'G':
                return PatternType.TRIANGLES_TOP;
            case 'H':
                return PatternType.HALF_VERTICAL;
            case 'I':
                return PatternType.DIAGONAL_LEFT_MIRROR;
            case 'J':
                return PatternType.DIAGONAL_RIGHT_MIRROR;
            case 'K':
                return PatternType.GRADIENT;
            case 'L':
                return PatternType.HALF_HORIZONTAL_MIRROR;
            case 'M':
                return PatternType.HALF_VERTICAL_MIRROR;
            default:
                return null;
        }
    }
    
    public BannerMeta emptyBannerMeta() {
        return ((BannerMeta) new ItemStack(Material.BANNER).getItemMeta());
    }

    public boolean isMncPattern(String code) {
        return code.matches("[a-p]a([a-p][b-zA-M])+");
    }

    public BannerMeta parseMncPattern(String mnc) {
        if (mnc.matches("[a-p]a([a-p][b-zA-M])+")) {
            List<String> groups = StringUtils.getInstance().sizedSplitString(mnc, 2, true);
            BannerMeta meta = emptyBannerMeta();
            meta.setBaseColor(colorFromMnc(mnc.charAt(0)));

            for (String str : groups.subList(1, groups.size())) {
                if (!str.isEmpty())
                    meta.addPattern(new Pattern(colorFromMnc(str.toCharArray()[0]), patternTypeFromMnc(str.toCharArray()[1])));
            }
            return meta;
        }
        return null;
    }

    public char colorToMnc(DyeColor color) {
        for (char c : StringUtils.getInstance().alphabetLetters()) {
            if (colorFromMnc(c) == color)
                return c;
        }
        return 0; //Will never happen
    }

    public char patternTypeToMnc(PatternType patternType) {
        for (char c : StringUtils.getInstance().alphabetLetters()) {
            if (patternTypeFromMnc(c) == patternType)
                return c;
        }
        return 0; //Will never happen
    }

    public String toMncPattern(BannerMeta meta) {
        List<String> stringList = new ArrayList<>();
        stringList.add(colorToMnc(meta.getBaseColor()) + "a");
        for (Pattern pattern : meta.getPatterns()) {
            stringList.add(new String(
                new char[]{
                        colorToMnc(pattern.getColor()),
                        patternTypeToMnc(pattern.getPattern())
                    }
                )
            );
        }
        return StringUtils.getInstance().join(stringList.toArray(new String[stringList.size()]));
    }
}
