package org.cqfn.patternika.util;

/**
 * Utility class that provides methods for handling strings.
 *
 * @since 2019/10/30
 */
public final class TextUtils {
    private TextUtils() { }

    /**
     * Repeats the specified string the specified number of times.
     *
     * @param str the string.
     * @param count the number of times the string will be repeated.
     * @return the string repeated the specified number of times.
     */
    public static String repeat(final String str, final int count) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Encodes text into an HTML-compatible format replacing characters,
     * which are not accepted in HTML, with corresponding HTML escape symbols.
     *
     * @param str text to be encoded in HTML.
     * @return the encoded text.
     */
    public static String escapeHtmlEntities(final String str) {
        final StringBuilder result = new StringBuilder();
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            final char charVal = str.charAt(i);
            switch (charVal) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '\'':
                    result.append("&apos;");
                    break;
                case '\"':
                    result.append("&quot;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                default:
                    result.append(charVal);
                    break;
            }
        }
        return result.toString();
    }

    /**
     * Decodes a text replacing HTML escape symbols with corresponding characters.
     *
     * @param str the HTML text to be decoded.
     * @return the decoded text.
     */
    public static String decodeHtmlEntities(final String str) {
        return str
                .replaceAll("<.*?>", "")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&apos;", "\'")
                .replace("&quot;", "\"")
                .replace("&amp;", "&");
    }

    /**
     * Checks whether the specified string represents a link.
     *
     * @param string the string to be checked.
     * @return {@code true} or {@code false}.
     */
    public static boolean isLink(final String string) {
        return string.startsWith("http:/")
            || string.startsWith("https:/")
            || string.startsWith("ssh:/");
    }
}
