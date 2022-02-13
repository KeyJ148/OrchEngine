package cc.abro.orchengine.gui;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.border.SimpleLineBorder;

import java.util.ArrayList;
import java.util.List;

public class TabPanel extends Panel implements MouseReleaseListeners {

    //TODO больше возможностей по изменению конкретных кнопок (размер, стили и т.д.)

    private static final float INITIAL_BUTTON_HEIGHT = 60f;

    private final List<TiedButtonPanel> content = new ArrayList<>();

    private final Panel vision;

    private Style buttonsStyle;

    public TabPanel() {
        super();
        vision = new Panel();
        initialize();
    }

    public TabPanel(float x, float y, float width, float height) {
        super(x, y, width, height);
        vision = new Panel(0, INITIAL_BUTTON_HEIGHT, width, height - INITIAL_BUTTON_HEIGHT);
        //vision.getStyle().getBackground().setColor(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));
        //vision.getStyle().setBorder(new SimpleLineBorder(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0));
        initialize();
    }

    public void setSize(float width, float height) {
        super.setSize(width, height);
        //vision.setSize(width, height - INITIAL_BUTTON_HEIGHT);
    }

    public void addNewPanel(String name, Panel panel) {
        content.add(new TiedButtonPanel(new Button(name), panel));
        content.get(content.size() - 1).button.setStyle(buttonsStyle);
        clearChildComponents();
        updateButtons();
    }

    public void setButtonsStyle(Style style) {
        buttonsStyle = style;
        for(TiedButtonPanel pair : content) {
            pair.button.setStyle(buttonsStyle);
        }
        updateButtons();
    }

    private void updateButtons() {
        for(int i = 0; i < content.size(); i++) {
            TiedButtonPanel pair = content.get(i);
            pair.button.setSize(
                    (getSize().x - ((SimpleLineBorder) buttonsStyle.getBorder()).getThickness()*(content.size() + 1))/content.size(),
                    INITIAL_BUTTON_HEIGHT);
            pair.button.setPosition(
                    pair.button.getSize().x*i + ((SimpleLineBorder) buttonsStyle.getBorder()).getThickness()*(i + 1),
                    ((SimpleLineBorder) buttonsStyle.getBorder()).getThickness());
            pair.button.getListenerMap().addListener(MouseClickEvent.class, getMouseReleaseListener(event -> {
                //if(!vision.getChildComponents().contains(pair.panel)) {
                //vision.clearChildComponents();
                //vision.add(pair.panel);
                //}
            }));
            add(pair.button);
        }
    }

    /*private void setActivePanel(int index) {
        vision.clearChildComponents();
        vision.add(content.get(index).panel);
    }*/


    private void initialize() {
        buttonsStyle = new Style();
        buttonsStyle.setBackground(new Background());
        buttonsStyle.getBackground().setColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
        buttonsStyle.setTextColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));

        add(vision);
    }

    public static record TiedButtonPanel (Button button, Panel panel) {}
}
