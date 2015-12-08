package org.zeropage;

import org.zeropage.basic.MockLinkSource;
import org.zeropage.basic.SimplePathFinder;
import org.zeropage.log.LogListener;
import org.zeropage.log.Logger;
import org.zeropage.log.OutputStreamLogListener;
import org.zeropage.wiki.api.WikipediaApi;
import org.zeropage.wiki.WikipediaLinkSource;
import org.zeropage.wiki.WikipediaPathFinder;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static final String CACHE_ROOT = "cache/";

    public static void main(String[] args) throws Exception {
        NamedPathFinder[] pathFinders = {
                new NamedPathFinder(
                        "WikipediaPathFinder (EN)",
                        new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.EN),
                                                new File(CACHE_ROOT, WikipediaApi.Language.EN.toString()))
                ),

                new NamedPathFinder(
                        "WikipediaPathFinder (KO)",
                        new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.KO),
                                                new File(CACHE_ROOT, WikipediaApi.Language.KO.toString()))
                ),


                new NamedPathFinder(
                        "WikipediaPathFinder (JP)",
                        new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.JP),
                                new File(CACHE_ROOT, WikipediaApi.Language.JP.toString()))
                ),

                new NamedPathFinder(
                        "SimplePathFinder (MockLinkSource) [Inputs: 0 ~ 4]",
                        new SimplePathFinder(new MockLinkSource())
                ),

                new NamedPathFinder(
                        "SimplePathFinder (WikipediaLinkSource, EN) [WARNING: SLOW!]",
                        new SimplePathFinder(new WikipediaLinkSource(new WikipediaApi(WikipediaApi.Language.EN, true)))
                )
        };

        OutputStreamLogListener listener = new OutputStreamLogListener(System.out);
        listener.setLevel(LogListener.Level.INFO);
        Logger.getInstance().addListener(listener);

        getUserInput(pathFinders, listener);
    }

    private static void getUserInput(NamedPathFinder[] pathFinders, OutputStreamLogListener listener) {
        Scanner input = new Scanner(System.in);

        while (true) {
            for (int i = 0; i < pathFinders.length; i++) {
                System.out.println(String.format("%d: %s", i, pathFinders[i].name));
            }

            System.out.println(String.format("%d: change log level", pathFinders.length));
            System.out.println("else: exit");
            System.out.print(">");

            int selected;

            try {
                selected = input.nextInt();
            } catch (InputMismatchException e) {
                break;
            }

            if (0 <= selected && selected < pathFinders.length) {
                search(pathFinders[selected].pathFinder);
            } else if (selected == pathFinders.length) {
                changeLogLevel(listener);
            } else {
                break;
            }
        }
    }

    private static void search(PathFinder pFinder) {
        Iterator<String> pIterator;
        String from, to;
        Scanner input = new Scanner(System.in);

        System.out.print("From: ");//4
        from = input.nextLine();
        System.out.print("To: ");//3
        to = input.nextLine();

        try {
            pIterator = pFinder.getPath(from, to).getPathIterator();
            while (pIterator.hasNext()) {
                System.out.print(pIterator.next());
                if (pIterator.hasNext()) {
                    System.out.print("->");
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Something is wrong. Check if your input is appropriate.");
        }
    }

    private static void changeLogLevel(OutputStreamLogListener listener) {
        Scanner input = new Scanner(System.in);
        LogListener.Level[] levels = {
                LogListener.Level.DEBUG,
                LogListener.Level.INFO,
                LogListener.Level.WARN,
                LogListener.Level.FATAL,
                LogListener.Level.ERROR
        };

        for (int i = 0; i < levels.length; i++) {
            LogListener.Level level = levels[i];
            System.out.println(String.format("%d: %s", i, level.toString()));
        }
        System.out.println("else: cancel");

        int selected;

        try {
            selected = input.nextInt();
        } catch (InputMismatchException e) {
            return;
        }

        if (0 <= selected && selected < levels.length) {
            listener.setLevel(levels[selected]);
        }
    }

    private static class NamedPathFinder {
        public String name;
        public PathFinder pathFinder;

        public NamedPathFinder(String name, PathFinder pathFinder) {
            this.name = name;
            this.pathFinder = pathFinder;
        }
    }
}
