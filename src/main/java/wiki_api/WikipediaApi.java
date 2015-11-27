package wiki_api;

import org.wikipedia.Wiki;

import java.io.IOException;
import java.util.logging.Level;

public class WikipediaApi {
    private Wiki wiki;
    private boolean suppressRedirected;

    public WikipediaApi(Language language) {
        this(language, false);
    }

    public WikipediaApi(Language language, boolean suppressRedirected) {
        String url = String.format("%s.wikipedia.org/w/api.php/", language.toString());

        wiki = new Wiki(url);
        wiki.setLogLevel(Level.OFF);

        this.suppressRedirected = suppressRedirected;
    }

    public String[] getLinks(String title) throws IOException {
        validate(title);

        return wiki.getLinksOnPage(title);
    }

    public String[] getBacklinks(String title) throws IOException {
        validate(title);

        return wiki.whatLinksHere(title, 0);
    }

    public boolean isExist(String title) throws IOException {
        return wiki.exists(new String[] {title})[0];
    }

    public String resolve(String title) throws IOException {
        return wiki.resolveRedirect(title);
    }

    public String normalize(String title) throws IOException {
        return wiki.normalize(title);
    }

    public void validate(String title) throws IOException {
        String normalized = normalize(title);

        if (!normalized.equals(title)) {
            throw new NotNormalizedException();
        }

        if (!suppressRedirected) {
            String redirected = resolve(title);

            if (redirected != null) {
                throw new RedirectedException();
            }
        }

        if (!isExist(title)) {
            throw new PageNotFoundException();
        }
    }

    public enum Language {
        KO("ko"), EN("en");

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
