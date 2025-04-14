package stepDefinitions;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",   // Path to your feature files
    glue = "stepDefinitions"           // Package of your step definitions (no "classpath:")
)
public class CucumberTest {
}
