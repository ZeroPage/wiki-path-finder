package org.zeropage;

import org.zeropage.path.Path;

public interface PathFinder {
    Path getPath(String from, String to) throws Exception;
}
