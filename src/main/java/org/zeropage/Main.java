package org.zeropage;

public class Main {
    public static void main(String[] args) {
        // hello world for java8
        Runnable r = () -> System.out.println("Hello, World!");
        r.run();
    }
}
