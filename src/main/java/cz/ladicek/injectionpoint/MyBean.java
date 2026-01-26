package cz.ladicek.injectionpoint;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyBean {
    @Inject
    @MyQualifier
    MyDependency injection;

    @Inject
    @Any
    Instance<Object> lookup;

    @Inject
    BeanContainer container;

    @Inject
    @MyQualifier
    transient MyDependency transientInjection;

    @Inject
    @Any
    transient Instance<Object> transientLookup;


    public void test() {
        Log.info("----------------");
        Log.info("direct injection");
        Log.info("----------------");
        injection.printIP();

        Log.info("--------------------------------");
        Log.info("lookup through injected Instance");
        Log.info("--------------------------------");
        lookup.select(MyDependency.class, MyQualifier.Literal.INSTANCE).get().printIP();

        Log.info("---------------------------------------------");
        Log.info("lookup through BeanContainer.createInstance()");
        Log.info("---------------------------------------------");
        container.createInstance().select(MyDependency.class, MyQualifier.Literal.INSTANCE).get().printIP();

        Log.info("--------------------------");
        Log.info("TRANSIENT direct injection");
        Log.info("--------------------------");
        transientInjection.printIP();

        Log.info("------------------------------------------");
        Log.info("TRANSIENT lookup through injected Instance");
        Log.info("------------------------------------------");
        transientLookup.select(MyDependency.class, MyQualifier.Literal.INSTANCE).get().printIP();
    }
}
