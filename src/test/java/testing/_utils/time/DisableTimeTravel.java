package testing._utils.time;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(DisableTimeTravel.Extension.class)
public @interface DisableTimeTravel {

    final class Extension implements InvocationInterceptor {

        @Override
        public void interceptTestMethod(
                Invocation<Void> invocation,
                ReflectiveInvocationContext<Method> ctx,
                org.junit.jupiter.api.extension.ExtensionContext ext
        ) throws Throwable {

            // Simplemente ejecutamos el test sin activar TimeTravel
            invocation.proceed();
        }
    }
}
