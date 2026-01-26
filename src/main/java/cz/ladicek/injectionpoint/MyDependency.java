package cz.ladicek.injectionpoint;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@Dependent
@MyQualifier
public class MyDependency {
    @Inject
    InjectionPoint ip;

    public void printIP() {
        if (ip == null) {
            Log.info("null IP");
            return;
        }
        Log.info("IP.getType(): " + ip.getType());
        Log.info("IP.getQualifiers(): " + ip.getQualifiers());
        Log.info("IP.getBean(): " + ip.getBean());
        Log.info("IP.getMember(): " + ip.getMember());
        Log.info("IP.getAnnotated(): " + ip.getAnnotated());
        Log.info("IP.isTransient(): " + ip.isTransient());
    }
}
