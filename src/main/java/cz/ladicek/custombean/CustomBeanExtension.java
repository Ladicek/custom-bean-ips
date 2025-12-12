package cz.ladicek.custombean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomBeanExtension implements Extension {
    private static final Logger LOG = Logger.getLogger(CustomBeanExtension.class.getName());

    private static void log(String message) {
        Exception e = new Exception();
        e.setStackTrace(Arrays.copyOfRange(e.getStackTrace(), 1, Math.min(e.getStackTrace().length, 10)));
        LOG.log(Level.INFO, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + message, e);
    }

    public void register(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
        log("registering CustomBean");
        abd.addBean(new CustomBean(beanManager));
    }

    public static class CustomBeanInjectionPoint implements InjectionPoint {
        private final InjectionPoint delegate;

        public CustomBeanInjectionPoint(InjectionPoint delegate) {
            this.delegate = delegate;
        }

        @Override
        public Type getType() {
            log("InjectionPoint.getType()");
            return delegate.getType();
        }

        @Override
        public Set<Annotation> getQualifiers() {
            log("InjectionPoint.getQualifiers()");
            return delegate.getQualifiers();
        }

        @Override
        public Bean<?> getBean() {
            log("InjectionPoint.getBean()");
            return delegate.getBean();
        }

        @Override
        public Member getMember() {
            log("InjectionPoint.getMember()");
            return delegate.getMember();
        }

        @Override
        public Annotated getAnnotated() {
            log("InjectionPoint.getAnnotated()");
            return delegate.getAnnotated();
        }

        @Override
        public boolean isDelegate() {
            log("InjectionPoint.isDelegate()");
            return delegate.isDelegate();
        }

        @Override
        public boolean isTransient() {
            log("InjectionPoint.isTransient()");
            return delegate.isTransient();
        }
    }

    public static class CustomBean implements Bean<MyBean> {
        private final BeanManager beanManager;

        public CustomBean(BeanManager beanManager) {
            this.beanManager = beanManager;
        }

        @Override
        public Class<?> getBeanClass() {
            log("Bean.getBeanClass()");
            return MyBean.class;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            log("Bean.getInjectionPoints()");
            AnnotatedParameter<MyBean> param = beanManager.createAnnotatedType(MyBean.class)
                    .getConstructors()
                    .stream()
                    .filter(it -> it.getParameters().size() == 1)
                    .findFirst()
                    .orElseThrow()
                    .getParameters()
                    .get(0);
            InjectionPoint ip = new CustomBeanInjectionPoint(beanManager.createInjectionPoint(param));
            return Set.of(ip);
        }

        @Override
        public MyBean create(CreationalContext<MyBean> creationalContext) {
            Bean<?> bean = beanManager.resolve(beanManager.getBeans(MyDependency.class));
            MyDependency instance = (MyDependency) beanManager.getReference(bean, MyDependency.class, creationalContext);
            return new MyBean(instance);
        }

        @Override
        public void destroy(MyBean instance, CreationalContext<MyBean> creationalContext) {
            creationalContext.release();
        }

        @Override
        public Set<Type> getTypes() {
            return Set.of(Object.class, MyBean.class);
        }

        @Override
        public Set<Annotation> getQualifiers() {
            return Set.of(Default.Literal.INSTANCE);
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return ApplicationScoped.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes() {
            return Set.of();
        }

        @Override
        public boolean isAlternative() {
            return false;
        }
    }
}
