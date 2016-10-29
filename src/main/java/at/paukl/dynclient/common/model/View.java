package at.paukl.dynclient.common.model;

import java.util.List;

/**
 * @author Paul Klingelhuber
 */
public class View {
    private List<ViewElement> elements;

    public View() {
    }

    public View(List<ViewElement> elements) {
        this.elements = elements;
    }

    public List<ViewElement> getElements() {
        return elements;
    }

    public void setElements(List<ViewElement> elements) {
        this.elements = elements;
    }
}
