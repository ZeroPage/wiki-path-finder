package org.zeropage.wiki_api;


import org.junit.Test;

public class WikipediaApiExceptionTest {

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    public void testExceptions() {
        new NotNormalizedException();
        new NotNormalizedException(null);
        new NotNormalizedException(null, null);

        new PageNotFoundException();
        new PageNotFoundException(null);

        new RedirectedException();
        new RedirectedException(null);
        new RedirectedException(null, null);
    }
}
