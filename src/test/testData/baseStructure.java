class BaseStructure {
    public static void main(String[] args) {
        new Original().<caret>
    }

    public static interface Decorable{}

    public static class Original implements Decorable{
        public void alphaTest() {
            System.out.println("suggest me after dot");
        }
        public void betaTest() {
            System.out.println("suggest me after dot");
        }
    }

    public static class Decorator implements Decorable{
        Decorator(Decorable origin){}
    }
}