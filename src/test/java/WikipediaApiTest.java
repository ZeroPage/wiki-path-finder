import org.junit.Before;
import org.junit.Test;
import wiki_api.NotNormalizedException;
import wiki_api.PageNotFoundException;
import wiki_api.RedirectedException;
import wiki_api.WikipediaApi;

import static org.junit.Assert.*;

public class WikipediaApiTest {
    private WikipediaApi api;
    private final static String SAMPLE_TITLE = "JUnit";
    private final static String SAMPLE_NOT_NORMALIZED_TITLE = "junit";
    private final static String SAMPLE_REDIRECT_TITLE = "Junit";
    private final static String NON_EXISTING_TITLE = "!@$%^&^%$@!";

    @Before
    public void before() {
        api = new WikipediaApi(WikipediaApi.Language.EN);
    }

    @Test
    public void testGetLinks() throws Exception {
        String[] links = api.getLinks(SAMPLE_TITLE);
        assertEquals(links[0], "Actionscript"); // valid on 2015-11-23

    }

    @Test
    public void testGetBacklinks() throws Exception {
        String[] links = api.getBacklinks(SAMPLE_TITLE);
        assertEquals(links[0], "List of programmers"); // valid on 2015-11-23
    }

    @Test
    public void testValidate() throws Exception {
        try {
            api.getLinks(SAMPLE_NOT_NORMALIZED_TITLE);
            fail();
        } catch (NotNormalizedException ignored) {

        }

        try {
            api.getLinks(SAMPLE_REDIRECT_TITLE);
            fail();
        } catch (RedirectedException ignored) {

        }

        try {
            api.getLinks(NON_EXISTING_TITLE);
            fail();
        } catch (PageNotFoundException ignored) {

        }
    }
}