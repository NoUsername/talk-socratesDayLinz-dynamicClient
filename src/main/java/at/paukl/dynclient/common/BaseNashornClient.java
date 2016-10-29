package at.paukl.dynclient.common;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import javax.script.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Paul Klingelhuber
 */
public abstract class BaseNashornClient {

    private static final Logger LOG = getLogger(BaseNashornClient.class);

    private NashornScriptEngine engine;

    public BaseNashornClient() {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        engine = (NashornScriptEngine) engineManager.getEngineByName("nashorn");
    }

    public void loadScript(String script) {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        for (String key : bindings.keySet()) {
            LOG.info("binding {} -> {}", key, bindings.get(key));
        }
        bindings.put("ui", this);
        bindings.put("model", this);
        bindings.put("util", this);
        try {
            engine.compile(script).eval(bindings);
        } catch (ScriptException e) {
            LOG.warn("scripterror: ", e);
        }
    }

    public void invokeScriptFunction(String functionName) {
        Invocable invocable = engine;
        try {
            LOG.info("trying to run: {}", functionName);
            invocable.invokeFunction(functionName);
        } catch (ScriptException e) {
            LOG.warn("script error!");
        } catch (NoSuchMethodException e) {
            LOG.info("method not defined");
        }
    }

    public void callElementFocusLostHandler(String id) {
        final String fnName = "on" + StringUtils.capitalize(id) + "Exit";
        invokeScriptFunction(fnName);
    }

    public void callButtonPressedHandler(String id) {
        final String fnName = "on" + StringUtils.capitalize(id) + "Pressed";
        invokeScriptFunction(fnName);
    }


    // ui object
    public void setFocus(String element) {
        LOG.info("called setFocus");
    }

    public void showMessage(String message) {
        LOG.info("called showMessage");
    }

    // model object
    public String getValue(String name) {
        LOG.info("called getValue");
        return null;
    }

    // util obj
    public void log(String msg) {
        LOG.info("FROM SCRIPT: {}", msg);
    }

}
