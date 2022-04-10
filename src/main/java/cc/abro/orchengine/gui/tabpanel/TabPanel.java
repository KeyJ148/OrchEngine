package cc.abro.orchengine.gui.tabpanel;

import cc.abro.orchengine.gui.MouseReleaseListeners;
import cc.abro.orchengine.gui.tabpanel.modes.SimpleTabPanelButtonMode;
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

    //TODO нужно реализовать режимы отображения кнопок

    private static final float INITIAL_BUTTON_HEIGHT = 60f;

    private final List<TiedButtonPanel> content;

    private final Panel vision;

    private TabPanelButtonMode mode = new SimpleTabPanelButtonMode();

    private Style buttonsStyle;

    public TabPanel() {
        super();
        vision = new Panel();
        content = new ArrayList<>();
        initialize();
    }

    public TabPanel(float x, float y, float width, float height) {
        super(x, y, width, height);
        content = new ArrayList<>();
        vision = new Panel(0, INITIAL_BUTTON_HEIGHT, width, height - INITIAL_BUTTON_HEIGHT);
        initialize();
    }

    public TabPanel(float x, float y, float width, float height, List<TiedButtonPanel> inputContent) {
        super(x, y, width, height);
        content = new ArrayList<>(inputContent);
        vision = new Panel(0, INITIAL_BUTTON_HEIGHT, width, height - INITIAL_BUTTON_HEIGHT);
        for (TiedButtonPanel pair: inputContent) {
            addNewTiedButtonPanel(pair);
        }
        initialize();
    }

    public void setMode(TabPanelButtonMode mode) {
        this.mode = mode;
    }

    public void setSize(float width, float height) {
        super.setSize(width, height);
        vision.setSize(width, height - INITIAL_BUTTON_HEIGHT);
    }

    public void addNewTiedButtonPanel(Button button, Panel panel) {
        addNewTiedButtonPanel(new TiedButtonPanel(button, panel));
    }

    public void addNewTiedButtonPanel(TiedButtonPanel pair) {
        content.add(pair);
        pair.button.setStyle(buttonsStyle);
        pair.button.getListenerMap().addListener(MouseClickEvent.class, getMouseReleaseListener(event -> {
            if(!vision.getChildComponents().contains(pair.panel)) {
                vision.clearChildComponents();
                vision.add(pair.panel);
            }
        }));
        pair.button.getSize().y = INITIAL_BUTTON_HEIGHT;
        add(pair.button);
        mode.updateButtons(content, getSize());
    }

    public void setButtonsStyle(Style style) {
        buttonsStyle = style;
        for(TiedButtonPanel pair : content) {
            pair.button.setStyle(buttonsStyle);
        }
        mode.updateButtons(content, getSize());
    }

    /*private void setActivePanel(int index) {
        vision.clearChildComponents();
        vision.add(content.get(index).panel);
    }*/


    private void initialize() {

        vision.getStyle().getBackground().setColor(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));
        vision.getStyle().setBorder(new SimpleLineBorder(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0));
        buttonsStyle = new Style();
        buttonsStyle.setBackground(new Background());
        buttonsStyle.getBackground().setColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
        buttonsStyle.setTextColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));

        add(vision);
    }

    public static record TiedButtonPanel (Button button, Panel panel) {

    }
}
