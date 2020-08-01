package a;

class Main {
    public static void main(String[] args) {
        new Main().init().<caret>
    }

    public Original init() {
        return new Original();
    }
}