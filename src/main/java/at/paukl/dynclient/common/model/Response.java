package at.paukl.dynclient.common.model;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Paul Klingelhuber
 */
public class Response {
    private View view;
    private String script;

    public Response(View view, String script) {
        this.view = view;
        this.script = script;
    }

    public Response() {
    }

    public static Response parseFromYaml(String yaml) {
        return new Yaml().loadAs(yaml, Response.class);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
