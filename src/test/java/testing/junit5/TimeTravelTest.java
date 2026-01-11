package testing.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import testing._utils.NonConcurrentTest;
import testing._utils.time.DisableTimeTravel;
import testing._utils.time.FrozenAtEpoch;
import testing._utils.time.FrozenAtNoonUTC;
import testing._utils.time.TimeTravel;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TimeTravel(instant = "2030-01-01T00:00:00Z")
class ClassLevelTest {

    @Test
    void testThatHasAnnotationFromClassLevel() {
        assertThat(LocalDateTime.now().getYear()).isEqualTo(2030);
    }

    @Test
    @DisableTimeTravel
    void testThatIsDisabledByAnnotation() {
        assertThat(LocalDateTime.now().getYear()).isNotEqualTo(2030);
    }

    @Test
    @TimeTravel(instant = "2026-01-11T12:00:00Z")
    void methodAnnotationOverridesClassAnnotation() {
        assertThat(LocalDateTime.now().getYear()).isEqualTo(2026);
    }

    @Test
    @FrozenAtEpoch
    void behavesLikeOldSystem() {
        assertThat(LocalDateTime.now().getYear()).isEqualTo(1970);
    }

    @Test
    @FrozenAtNoonUTC
    void middayRulesApply() {
        assertThat(LocalDateTime.now().getHour()).isEqualTo(12);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    @Disabled("Uncomment to see fail if you want")
    void concurrentTestsAreNotAllowed() {
        assertThatThrownBy(() -> LocalDateTime.now().getYear())
                .isInstanceOf(ExtensionConfigurationException.class)
                .hasMessage("@TimeTravel no es compatible con ejecución paralela. Use @Execution(SAME_THREAD).");
    }

    @Test
    @TimeTravel(instant = "2026-00-00T00:00:00Z")
    @Disabled("Uncomment to see fail if you want")
    void invalidInstantIsRejected() {
        assertThatThrownBy(() -> LocalDateTime.now().getYear())
                .isInstanceOf(ExtensionConfigurationException.class)
                .hasMessage("@TimeTravel instant inválido: '2026-00-00T00:00:00Z'. Debe ser ISO-8601 con zona (ej: 2026-01-11T12:00:00Z)");
    }

    @FrozenAtEpoch
    @FrozenAtNoonUTC
    @Test
    @Disabled("Uncomment to see fail if you want")
    void brokenTest() {}
}

@FrozenAtEpoch
@TimeTravel(instant = "2030-01-01T00:00:00Z")
@Disabled("Uncomment to see fail if you want")
class BrokenClass {

    @Test
    void brokenTest() {}
}

class StrictUssage {
    @Test
    @TimeTravel(instant = "1997-01-12T12:00:00Z", strict = true)
    void deterministicTest() {
        assertThat(LocalDateTime.now().getYear()).isEqualTo(1997);
    }
}

