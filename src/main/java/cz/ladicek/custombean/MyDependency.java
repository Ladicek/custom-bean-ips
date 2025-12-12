package cz.ladicek.custombean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class MyDependency {
    public static boolean constructed;
    public static boolean destroyed;

    @PostConstruct
    public void construct() {
        constructed = true;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    public String hello() {
        return "hello";
    }
}
