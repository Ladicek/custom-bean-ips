package cz.ladicek.experiment;

import cz.ladicek.injectionpoint.MyBean;
import cz.ladicek.injectionpoint.MyDependency;
import cz.ladicek.injectionpoint.MyQualifier;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InjectionPointTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(MyBean.class, MyDependency.class, MyQualifier.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    MyBean bean;

    @Test
    public void test() {
        bean.test();
    }
}
