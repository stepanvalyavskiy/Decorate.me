package a;

import x.Decorator;

class Main {
    public static void main(String[] args) {
        new Decorator(new Main().init())
    }

    public Original init() {
        return new Original();
    }
}