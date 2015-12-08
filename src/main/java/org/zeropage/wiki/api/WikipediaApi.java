package org.zeropage.wiki.api;

import org.wikipedia.Wiki;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Get wikipedia data with Wikipedia API through HTTP connection.
 */
public class WikipediaApi {
    private Wiki wiki;
    private boolean suppressRedirected;

    /**
     *
     * @param language language of wikipedia.
     */
    public WikipediaApi(Language language) {
        this(language, false);
    }

    /**
     *
     * @param language language of wikipedia.
     * @param suppressRedirected if true, redirected pages don't throw RedirectedException
     */
    public WikipediaApi(Language language, boolean suppressRedirected) {
        String url = String.format("%s.wikipedia.org/w/api.php/", language.toString());

        wiki = new Wiki(url);
        wiki.setLogLevel(Level.OFF);

        this.suppressRedirected = suppressRedirected;
    }

    /**
     * Get links of the page.
     * @param title title of the page
     * @return all pages linked from the given page
     * @throws IOException network is not available
     */
    public String[] getLinks(String title) throws IOException {
        validate(title);

        return wiki.getLinksOnPage(title);
    }

    /**
     * Get backlinks of the page.
     * @param title title of the page
     * @return all pages linking to the given page
     * @throws IOException network is not available
     */
    public String[] getBacklinks(String title) throws IOException {
        validate(title);

        return wiki.whatLinksHere(title, 0);
    }

    /**
     * Check whether the page exists.
     * @param title title of the page
     * @return if true, page is exist
     * @throws IOException network is not available
     */
    public boolean isExist(String title) throws IOException {
        return wiki.exists(new String[]{title})[0];
    }

    /**
     * Resolve redirection of the page.
     * @param title title of the page
     * @return if given page is redirecting, the result of redirection is given. If not, given title is returned.
     * @throws IOException network is not available
     */
    public String resolve(String title) throws IOException {
        return wiki.resolveRedirect(title);
    }

    /**
     * Normalize title of the page.
     * @param title title of the page
     * @return normalized title of the given page title.
     * @throws IOException network is not available
     */
    public String normalize(String title) throws IOException {
        return wiki.normalize(title);
    }

    /**
     * Check the given page whether it is valid to use.
     * @param title title of the page
     * @throws IOException network is not available
     */
    public void validate(String title) throws IOException {
        String normalized = normalize(title);

        if (!normalized.equals(title)) {
            throw new NotNormalizedException(title, normalized);
        }

        if (!suppressRedirected) {
            String redirected = resolve(title);

            if (redirected != null) {
                throw new RedirectedException(title, redirected);
            }
        }

        if (!isExist(title)) {
            throw new PageNotFoundException(title);
        }
    }

    /**
     * Wikipedia Languages used in constructor of WikipediaApi class.
     */
    public enum Language {
        KO("ko"), EN("en"), JP("jp");

        private String languageCode;

        Language(String languageCode) {
            this.languageCode = languageCode;
        }

        @Override
        public String toString() {
            return languageCode;
        }
    }
}
