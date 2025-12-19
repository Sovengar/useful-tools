package testing.config.runners;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import testing.config.BasicTestTags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@BasicTestTags
@AutoConfigureMockMvc
//No integration tag, as it is using mocks
public @interface MockedWebRunner {
    //Add @WebMvcTest in your target class to import your controllers.
    //Add @Import in your target class to import your controller dependencies.
}
