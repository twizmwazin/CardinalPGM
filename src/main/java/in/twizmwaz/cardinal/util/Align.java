package in.twizmwaz.cardinal.util;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Contains static values and operations designed to assist with aligning text
 *
 * Original author:
 * @author AmoebaMan
 * https://github.com/AmoebaMan/Utils/blob/master/src/main/java/net/amoebaman/amoebautils/chat/Align.java
 *
 * Modified for use in CardinalPGM
 */
public class Align {

    /**
     * The default Minecraft chat box width, in font-pixels
     */
    private static final int SCREEN_WIDTH = 310;

    /**
     * Most characters in Minecraft's default font are this many font-pixels wide
     */
    private static final int DEFAULT_CHAR_WIDTH = 6;

    /**
     * A map of the width of all irregular characters in Minecraft's default font
     */
    private static final Map<Character, Integer> IRREG_CHAR_WIDTH = new HashMap<>();

    static {
        IRREG_CHAR_WIDTH.put(' ', 4);
        IRREG_CHAR_WIDTH.put('f', 5);
        IRREG_CHAR_WIDTH.put('i', 2);
        IRREG_CHAR_WIDTH.put('ï',4);
        IRREG_CHAR_WIDTH.put('ì',4);
        IRREG_CHAR_WIDTH.put('í',4);
        IRREG_CHAR_WIDTH.put('I', 4);
        IRREG_CHAR_WIDTH.put('k', 5);
        IRREG_CHAR_WIDTH.put('l', 3);
        IRREG_CHAR_WIDTH.put('t', 4);
        IRREG_CHAR_WIDTH.put('!', 2);
        IRREG_CHAR_WIDTH.put('¡', 2);
        IRREG_CHAR_WIDTH.put('(', 5);
        IRREG_CHAR_WIDTH.put(')', 5);
        IRREG_CHAR_WIDTH.put('~', 7);
        IRREG_CHAR_WIDTH.put(',', 2);
        IRREG_CHAR_WIDTH.put('.', 2);
        IRREG_CHAR_WIDTH.put('<', 5);
        IRREG_CHAR_WIDTH.put('>', 5);
        IRREG_CHAR_WIDTH.put(':', 2);
        IRREG_CHAR_WIDTH.put(';', 2);
        IRREG_CHAR_WIDTH.put('"', 5);
        IRREG_CHAR_WIDTH.put('[', 4);
        IRREG_CHAR_WIDTH.put(']', 4);
        IRREG_CHAR_WIDTH.put('{', 5);
        IRREG_CHAR_WIDTH.put('}', 5);
        IRREG_CHAR_WIDTH.put('|', 2);
        IRREG_CHAR_WIDTH.put('`', 3);
        IRREG_CHAR_WIDTH.put('\'', 3);
        IRREG_CHAR_WIDTH.put('*', 5);
        IRREG_CHAR_WIDTH.put('@', 7);
        IRREG_CHAR_WIDTH.put('®', 7);
        IRREG_CHAR_WIDTH.put('\u2591', 8);
        IRREG_CHAR_WIDTH.put('\u2592', 9);
        IRREG_CHAR_WIDTH.put('\u2593', 9);
        IRREG_CHAR_WIDTH.put('\u2588', 9);
        IRREG_CHAR_WIDTH.put(ChatColor.COLOR_CHAR, 0);
    }

    /**
     * Gets the width of a character in Minecraft's default font, in font-pixels.
     *
     * @param value a character
     * @param bold  whether this character is in bold style (+1 px)
     * @return the width of the character
     */
    private static int getCharWidth(char value, boolean bold) {
        if (IRREG_CHAR_WIDTH.containsKey(value))
            return IRREG_CHAR_WIDTH.get(value) + (bold ? 1 : 0);
        return DEFAULT_CHAR_WIDTH + (bold ? 1 : 0);
    }

    /**
     * Gets the total width of some text in font-pixels, the sum of its characters.
     *
     * @param str some text
     * @return the width of the text
     */
    public static int getStringWidth(String str) {
        int length = 0;
        boolean bold = false;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) != ChatColor.COLOR_CHAR)
                if (i == 0)
                    length += getCharWidth(str.charAt(i), bold);
                else if (str.charAt(i - 1) != ChatColor.COLOR_CHAR)
                    length += getCharWidth(str.charAt(i), bold);
                else if (str.charAt(i) == 'l')
                    bold = true;
                else if (!Lists.newArrayList('m', 'n', 'o').contains(str.charAt(i)))
                    bold = false;
        return length;
    }

    /**
     * Repeat character 'c' n times.
     */
    public static String repeat(String c, int n) {
        assert n >= 0;
        return new String(new char[n]).replace("\0", c);
    }

    public static String padMessage(String message) {
        return padMessage(message, ChatColor.BLUE);
    }

    public static String padMessage(String message, ChatColor dashColor) {
        return padMessage(message, dashColor, SCREEN_WIDTH);
    }

    public static String padMessage(String message, ChatColor dashColor, int maxLen) {
        message = " " + message + ChatColor.RESET + " ";
        int len = maxLen - getStringWidth(message);
        String dash1 = getDash(dashColor, len / 2);
        String dash2 = len % 2 == 0 ? dash1 : getDash(dashColor, (len / 2) + 1);
        return dash1 + ChatColor.RESET + message + ChatColor.RESET + dash2;
    }

    public static String getDash() {
        return getDash(ChatColor.BLUE, SCREEN_WIDTH);
    }

    public static String getLine(ChatColor color) {
        return getDash(color, SCREEN_WIDTH);
    }

    /**
     * Returns a dash made out of spaces " " and dashes "-", with the desired len in pixels.
     *
     * @param color color the line should have
     * @param len number of pixels
     * @return A string with the desired length as long as the len is >= 4. Else it returns an empty string.
     */
    public static String getDash(ChatColor color, int len) {
        if (len < 4) return "";
        switch (len) {
            case 4:
                return "" + color + ChatColor.STRIKETHROUGH + " ";
            case 5:
                return "" + color + ChatColor.STRIKETHROUGH + ChatColor.BOLD + " ";
            case 6:
                return "" + color + ChatColor.STRIKETHROUGH + "-";
            case 7:
                return "" + color + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "-";
            default:
                if (len % 4 == 0) {
                    return "" + color + ChatColor.STRIKETHROUGH + repeat(" ", len / 4);
                } else {
                    return "" + color + ChatColor.STRIKETHROUGH + 
                            repeat(" ", (len / 4) - 1) + getDash(color, len % 4 + 4);
                }
        }
    }

    /**
     * Breaks a raw string up into a series of lines. Words are wrapped using
     * spaces as decimeters and the newline character is respected.
     *
     * @param rawString The raw string to break.
     * @param lineLength The length of a line of text.
     * @return An array of word-wrapped lines.
     */
    public static List<String> wordWrap(String rawString, int lineLength) {
        // A null string is a single line
        if (rawString == null) {
            return Lists.newArrayList("");
        }

        // A string shorter than the lineWidth is a single line
        if (getStringWidth(rawString) <= lineLength && !rawString.contains("\n")) {
            return Lists.newArrayList(rawString);
        }

        char[] rawChars = (rawString + ' ').toCharArray(); // add a trailing space to trigger pagination
        StringBuilder word = new StringBuilder();
        StringBuilder line = new StringBuilder();
        List<String> lines = new LinkedList<>();

        for (int i = 0; i < rawChars.length; i++) {
            char c = rawChars[i];

            if (c == ' ' || c == '\n') {
                if (getStringWidth(line.toString() + (line.length() > 0 ? " " : "") + word.toString()) > lineLength) {
                    for (String partialWord : wordSplit(word.toString(), lineLength)) {
                        if (line.length() > 0) lines.add(line.toString());
                        line = new StringBuilder(partialWord);
                    }
                } else {
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append(word);
                }
                word = new StringBuilder();

                if (c == '\n') { // Newline forces the line to flush
                    lines.add(line.toString());
                    line = new StringBuilder();
                }
            } else {
                word.append(c);
            }
        }

        if(line.length() > 0) { // Only add the last line if there is anything to add
            lines.add(line.toString());
        }

        // Iterate over the wrapped lines, applying the last color from one line to the beginning of the next
        for (int i = 1; i < lines.size(); i++) {
            lines.set(i, ChatColor.getLastColors(lines.get(i-1)) + lines.get(i));
        }

        return lines;
    }

    public static List<String> wordSplit(String word, int lineLength) {
        if (getStringWidth(word) <= lineLength) return Lists.newArrayList(word);
        List<String> lines = new LinkedList<>();
        StringBuilder line = new StringBuilder();
        char[] rawChars = word.toCharArray();
        for (int i = 0; i < rawChars.length; i++) {
            char c = rawChars[i];
            if (getStringWidth(line.toString() + c) > lineLength) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            line.append(c);
        }
        return lines;
    }

}