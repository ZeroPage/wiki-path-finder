package org.zeropage;

import java.util.Set;

public interface LinkSource {
    Set<String> getLinks(String from) throws Exception;
}
