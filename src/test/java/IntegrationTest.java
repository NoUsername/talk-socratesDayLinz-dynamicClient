import at.paukl.dynclient.common.model.Response;
import at.paukl.dynclient.server.beans.TestController;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Paul Klingelhuber
 */
public class IntegrationTest {

    TestController testController;

    @Before
    public void setup() {
        testController = new TestController();
    }

    @Test
    public void testAgeFieldLeaveNok() {
        final TestClient testClient = testClientFromTestResponse();

        testClient.simulateInputAndLeave("age", "");

        assertThat(testClient.loggedMessage)
            .isEqualTo("age not yet filled");
        assertThat(testClient.focusedElement)
                .isEqualTo("age");
    }

    @Test
    public void testAgeFieldLeaveOk() {
        final TestClient testClient = testClientFromTestResponse();

        testClient.simulateInputAndLeave("age", "abc");

        assertThat(testClient.focusedElement)
                .isEqualTo("test");
    }

    private TestClient testClientFromTestResponse() {
        final String responseText;
        try {
            responseText = IOUtils.toString(
                    ((ClassPathResource) testController.testResource()).getInputStream(),
                    Charset.forName("UTF8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final Response response = Response.parseFromYaml(responseText);
        final TestClient testClient = TestClient.fromResponse(response);
        return testClient;
    }

}
