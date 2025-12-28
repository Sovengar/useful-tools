package testing;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import testing.config.initializers.UseInfraInitializer1;
import testing.postModel.PostRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@UseInfraInitializer1
@Tag("integration")
class FlywayIT {

    @Autowired
    private PostRepository postRepository;

    @Test
    void flywayShouldNotMigratePopulateScripts() {
        assertThat(postRepository.count()).isZero();
    }
}
