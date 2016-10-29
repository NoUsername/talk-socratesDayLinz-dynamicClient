import at.paukl.dynclient.common.BaseNashornClient;
import at.paukl.dynclient.common.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paul Klingelhuber
 */
public class TestClient extends BaseNashornClient {

    String focusedElement = null;
    String shownMessage = null;
    String loggedMessage = null;

    private Map<String, String> model = new HashMap<>();

    public static TestClient fromResponse(Response response) {
        final TestClient testClient = new TestClient();
        testClient.loadScript(response.getScript());
        return testClient;
    }

    public void resetJsActions() {
        focusedElement = null;
        shownMessage = null;
        loggedMessage = null;
    }

    public void setModelValue(String key, String value) {
        model.put(key, value);
    }

    public void simulateInputAndLeave(String key, String value) {
        setModelValue(key, value);
        callElementFocusLostHandler(key);
    }

    @Override
    public void setFocus(String element) {
        focusedElement = element;
    }

    @Override
    public void showMessage(String message) {
        shownMessage = message;
    }

    @Override
    public String getValue(String name) {
        return model.get(name);
    }

    @Override
    public void log(String msg) {
        loggedMessage = msg;
    }
}
