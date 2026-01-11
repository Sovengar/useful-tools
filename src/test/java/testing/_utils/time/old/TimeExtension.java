package testing._utils.time.old;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeExtension implements InvocationInterceptor {
    private LocalDate fixedDate;
    private LocalTime fixedTime;

    public TimeExtension(LocalDate fixedDate) {
        this.fixedDate = fixedDate;
        this.fixedTime = LocalTime.now();
    }

    public TimeExtension(LocalDateTime fixedDateTime) {
        this.fixedDate = fixedDateTime.toLocalDate();
        this.fixedTime = fixedDateTime.toLocalTime();
    }

    public TimeExtension(String fixedDateTime) {
        this.fixedDate = LocalDateTime.parse(fixedDateTime).toLocalDate();
        this.fixedTime = LocalDateTime.parse(fixedDateTime).toLocalTime();
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        try (var staticMock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
             var staticMockTime = Mockito.mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS);
             var staticMockDateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            staticMock.when(LocalDate::now)
                    .thenAnswer(call -> fixedDate);

            staticMockTime.when(LocalTime::now)
                    .thenAnswer(call -> fixedTime);

            staticMockDateTime.when(LocalDateTime::now)
                    .thenAnswer(call -> LocalDateTime.of(fixedDate, fixedTime));

            invocation.proceed(); // call the @Test method
        }
    }

    public void setDate(LocalDate date) {
        fixedDate = date;
    }

    public void setDateTime(LocalDateTime dateTime) {
        fixedDate = dateTime.toLocalDate();
        fixedTime = dateTime.toLocalTime();
    }
}

class TimeFactory { // ~ Clock
    public LocalDate today() {
        return LocalDate.now();
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

class TimeExtensionTest {

    @RegisterExtension
    static TimeExtension time = new TimeExtension("2024-01-01T10:00:00");

    @Test
    void should_fix_local_date() {
        assertThat(LocalDate.now()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void should_fix_local_time() {
        assertThat(LocalTime.now()).isEqualTo(LocalTime.of(10, 0, 0));
    }

    @Test
    void should_fix_local_date_time() {
        assertThat(LocalDateTime.now()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
    }
}
