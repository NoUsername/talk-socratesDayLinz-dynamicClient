package at.paukl.dynclient.client;

import at.paukl.dynclient.common.BaseNashornClient;
import at.paukl.dynclient.common.model.Response;
import at.paukl.dynclient.common.model.ViewElement;
import org.slf4j.Logger;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Paul Klingelhuber
 */
public class Client {

    private static final Logger LOG = getLogger(Client.class);

    public static void main(String[] args) {
        new Client().start();
    }

    private final JFrame window;
    private Map<String, JComponent> elements = new HashMap<>();
    private JsClient baseClient = new JsClient();

    public Client() {
        window = new JFrame();

    }

    public void clear() {
        window.getContentPane().removeAll();
        this.elements.clear();
    }

    public void start() {
        baseClient = new JsClient();
        request();

        addReloadButton();

        window.setBounds(10, 10, 400, 400);
        final LayoutManager layout = new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS);
        window.getContentPane().setLayout(layout);
        window.setVisible(true);
    }

    private void addReloadButton() {
        final JButton reload = new JButton("reload");
        addComponent(new JPanel());
        addComponent(reload);
        reload.addActionListener((a) -> {
            clear();
            LOG.info("reloading UI");
            start();
        });
    }

    private void addComponent(JComponent comp) {
        window.getContentPane().add(comp);
    }

    private void request() {
        final RestTemplate restTemplate = new RestTemplate(new OkHttpClientHttpRequestFactory());
        final DefaultUriTemplateHandler uriHandler = new DefaultUriTemplateHandler();
        uriHandler.setBaseUrl("http://127.0.0.1:9091");
        restTemplate.setUriTemplateHandler(uriHandler);
        final String responseText = restTemplate.getForObject("/test", String.class);
        final Response response = Response.parseFromYaml(responseText);
        String script = response.getScript();
        baseClient.loadScript(script);

        LOG.info("building ui");
        buildUI(response);
        LOG.info("building ui done");
    }

    private void buildUI(Response response) {
        for (ViewElement element : response.getView().getElements()) {
            JComponent toAdd;
            JComponent toLookup = null;
            if (element.getType().equalsIgnoreCase("textField")) {
                final JTextField tf = new JTextField();
                tf.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        baseClient.callElementFocusLostHandler(element.getId());
                    }
                });
                final JPanel container = new JPanel();
                final BoxLayout innerLayout = new BoxLayout(container, BoxLayout.X_AXIS);
                container.setLayout(innerLayout);
                container.add(new JLabel(element.getId()));
                container.add(tf);
                toAdd = container;
                toLookup = tf;
            } else if (element.getType().equalsIgnoreCase("button")) {
                final JButton button = new JButton(element.getId());
                button.addActionListener((action) -> baseClient.callButtonPressedHandler(element.getId()));
                toAdd = button;
            } else {
                throw new RuntimeException("not implemented type: " + element.getType());
            }
            elements.put(element.getId(), toLookup == null ? toAdd : toLookup);
            addComponent(toAdd);
        }
    }

    private static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private class JsClient extends BaseNashornClient {

        // ui object
        @Override
        public void setFocus(String element) {
            LOG.info("called setFocus");
            final JComponent comp = elements.get(element);
            if (comp == null) {
                LOG.warn("not found: {}", element);
                return;
            }
            comp.requestFocus();
            LOG.info("focused {}", element);
        }

        @Override
        public void showMessage(String message) {
            infoBox(message, "script msg");
        }

        // model object
        @Override
        public String getValue(String name) {
            LOG.info("called getValue");
            final JComponent comp = elements.get(name);
            if (comp == null) {
                LOG.warn("not found: {}", name);
                return null;
            }
            if (comp instanceof JTextField) {
                return ((JTextField) comp).getText();
            }
            return null;
        }

    }

}
