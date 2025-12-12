package cz.ladicek.experiment;

import cz.ladicek.custombean.MyDependency;
import cz.ladicek.custombean.MyBean;
import cz.ladicek.custombean.CustomBeanExtension;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.Extension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CustomBeanIPsTest {
    @Deployment
    public static Archive<?> deploy() {
        JavaArchive archive1 = ShrinkWrap.create(JavaArchive.class)
                .addClasses(MyDependency.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        JavaArchive archive2 = ShrinkWrap.create(JavaArchive.class)
                .addClasses(MyBean.class, CustomBeanExtension.class)
                .addAsServiceProvider(Extension.class, CustomBeanExtension.class);

        return ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(archive1, archive2);
    }

    @Test
    public void test() {
        // OWB implements `CDI.destroy()` incorrectly, so have to obtain proper `Instance`
        Instance<Object> lookup = CDI.current().getBeanContainer().createInstance();

        assertFalse(MyDependency.constructed);
        assertFalse(MyDependency.destroyed);

        MyBean foobar = lookup.select(MyBean.class).get();
        assertEquals("foobar: hello", foobar.toString());

        assertTrue(MyDependency.constructed);
        assertFalse(MyDependency.destroyed);

        lookup.destroy(foobar);

        assertTrue(MyDependency.constructed);
        assertTrue(MyDependency.destroyed);
    }
}
