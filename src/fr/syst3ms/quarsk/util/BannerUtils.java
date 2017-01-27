package fr.syst3ms.quarsk.util;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;

/**
 * Created by ARTHUR on 25/01/2017.
 */
@SuppressWarnings("unused")
public class BannerUtils {
    private final BannerUtils instance = new BannerUtils();

    private BannerUtils() {}

    public BannerUtils getInstance() {
        return instance;
    }

    /**
     * Converts a banner color from Miner Needs Cool Shoes
     * @param c the char representing the color
     * @return DyeColor
     */
    public DyeColor colorFromMnc(char c) { //Don't question the characters, it's just like that.
        switch (c) {
            case 'p':
                return DyeColor.WHITE;
            case 'h':
                return DyeColor.SILVER;
            case 'i':
                return DyeColor.GRAY;
            case 'a':
                return DyeColor.BLACK;
            case 'c':
                return DyeColor.YELLOW;
            case 'o':
                return DyeColor.ORANGE;
            case '2':
                return DyeColor.RED;
            case '4':
                return DyeColor.BROWN;
            case 'b':
                return DyeColor.LIME;
            case '3':
                return DyeColor.GREEN;
            case 'm':
                return DyeColor.LIGHT_BLUE;
            case 'g':
                return DyeColor.CYAN;
            case 'e':
                return DyeColor.BLUE;
            case 'j':
                return DyeColor.PINK;
            case 'n':
                return DyeColor.MAGENTA;
            case 'f':
                return DyeColor.PURPLE;
            default:
                return null;
        }
    }

    /**
     * Converts a banner pattern from Miner Needs Cool Shoes
     * @param c the char representing the pattern
     * @return PatternType
     */
    public PatternType patternTypeFromInternet(char c) {
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
}
