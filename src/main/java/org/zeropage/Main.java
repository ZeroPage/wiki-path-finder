package org.zeropage;

import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PathFinder pFinder;
        /* Implement the detailed interaction, and instantiate pFinder
         * From here, t'is only the simple implementation of the main method. */
        pFinder = new SimplePathFinder(new MockLinkSource());
        run(pFinder);
    }

    private static void run(PathFinder pFinder) {
        Iterator<String> pIterator;
        String from, to;
        Scanner input = new Scanner(System.in);

        System.out.print("From: ");//4
        from = input.next();
        System.out.print("To: ");//3
        to = input.next();

        try {
            pIterator = pFinder.getPath(from, to).getPathIterator();
            while(pIterator.hasNext()) {
                System.out.print(pIterator.next());
                if(pIterator.hasNext()) {
                    System.out.print("->");
                }
            }
        } catch(Exception e) {
            /* What can I do here?
             * Help me, skywave! */
        }
    }
}
