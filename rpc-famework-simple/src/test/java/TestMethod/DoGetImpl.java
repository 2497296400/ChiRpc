package TestMethod;

public class DoGetImpl implements Doget {
    @Override
    public int get(int cur) {
        return cur * 10;
    }
}
