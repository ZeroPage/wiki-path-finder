package org.zeropage.basic;

import org.zeropage.LinkSource;

import java.util.*;

/**
 *Simple graph data source designed for funtionality test
 */
public class MockLinkSource implements LinkSource {
    private Map<String, String[]> mockData;

    public MockLinkSource() {
        mockData = new HashMap<>();

        mockData.put("0", new String[]{"1", "2"});
        mockData.put("1", new String[]{"2"});
        mockData.put("2", new String[]{"3"});
        mockData.put("3", new String[]{"4"});
        mockData.put("4", new String[]{"0"});
    }

    /**
     *
     * @param from Node to be searched from data source
     * @return graph data related with node from
     */
    @Override
    public Set<String> getLinks(String from) {
        if (mockData.containsKey(from)) {
            return new TreeSet<>(Arrays.asList(mockData.get(from)));
        } else {
            return null;
        }
    }
}
