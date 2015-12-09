package org.zeropage;

import java.util.Set;

/**
 * A data source which gives connected links from given node distinguished by String.
 */
public interface LinkSource {
    Set<String> getLinks(String from) throws Exception;
}
