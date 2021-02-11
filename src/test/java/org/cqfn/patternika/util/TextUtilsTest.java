package org.cqfn.patternika.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link TextUtils} class.
 *
 * @since 2021/02/11
 */
public class TextUtilsTest {

    /**
     * Test for the {@link TextUtils#repeat} method.
     */
    @Test
    public void testRepeat() {
        Assert.assertEquals("", TextUtils.repeat("1", 0));
        Assert.assertEquals("", TextUtils.repeat("1", -1));
        Assert.assertEquals("1", TextUtils.repeat("1", 1));
        final int repeatCount = 8;
        Assert.assertEquals("11111111", TextUtils.repeat("1", repeatCount));
    }

    /**
     * Test for the {@link TextUtils#encodeHtml} method.
     */
    @Test
    public void testEscapeHtmlEntities() {
        Assert.assertEquals(
                "&lt;one val=&apos;1&apos;&gt;  &amp;  &lt;two val=&quot;2&quot;&gt;",
                TextUtils.encodeHtml("<one val='1'>  &  <two val=\"2\">")
            );
    }

    /**
     * Test for the {@link TextUtils#decodeHtml} method.
     */
    @Test
    public void testDecodeHtmlEntities() {
        Assert.assertEquals(
                "<one val='1'>  &  <two val=\"2\">",
                TextUtils.decodeHtml(
                        "&lt;one val=&apos;1&apos;&gt;  &amp;  &lt;two val=&quot;2&quot;&gt;"
                    )
            );
    }

    /**
     * Test for the {@link TextUtils#isLink} method.
     */
    @Test
    public void testIsLink() {
        Assert.assertTrue(TextUtils.isLink("https://github.com/cqfn/patternika"));
        Assert.assertTrue(TextUtils.isLink("http://github.com/cqfn/patternika"));
        Assert.assertTrue(TextUtils.isLink("ssh://github.com"));
        Assert.assertFalse(TextUtils.isLink("github.com"));
    }

}
