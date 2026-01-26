package cz.ladicek.injectionpoint;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface MyQualifier {
    final class Literal extends AnnotationLiteral<MyQualifier> implements MyQualifier {
        public static final Literal INSTANCE = new Literal();

        private Literal() {
        }
    }
}
