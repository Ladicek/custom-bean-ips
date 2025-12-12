package cz.ladicek.custombean;

public class MyBean {
    private final MyDependency bean;

    // only for client proxy
    public MyBean() {
        this(null);
    }

    public MyBean(MyDependency bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "foobar: " + bean.hello();
    }
}
