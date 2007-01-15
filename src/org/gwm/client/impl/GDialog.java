package org.gwm.client.impl;

import org.gwm.client.GInternalFrame;
import org.gwm.client.GWmConstants;
import org.gwm.client.event.GDialogChoiceListener;
import org.gwtwidgets.client.ui.PNGImage;

import asquare.gwt.tk.client.ui.GlassPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GDialog extends GwtInternalFrame {

    public static final int ERROR_MESSAGE = 1;

    public static final int INFORMATION_MESSAGE = 2;

    public static final int WARNING_MESSAGE = 3;

    public static final int QUESTION_MESSAGE = 4;

    public static final int PLAIN_MESSAGE = 5;

    public static final int YES_NO_OPTION = 5;

    public static final int YES_NO_CANCEL_OPTION = 6;

    public static final int OK_CANCEL_OPTION = 7;

    private static final String YES_OPTION_LABEL = "Yes";

    private static final String NO_OPTION_LABEL = "No";

    private static final String CANCEL_OPTION_LABEL = "Cancel";

    private static String theme = GWmConstants.DEFAULT_THEME;

    private static GInternalFrame currentDialog;

    private static GlassPanel overlayLayer;

    private static Object selectedValue;

    private UIObject parentContainer;

    private Image icon;

    private Object initialValue;

    private Object inputValue;

    private Object message;

    private int messageType;

    private Option[] options;

    private int optionType;

    private Object[] selectionValues;

    private Object value;

    public static void setDialogTheme(String theme) {
        GDialog.theme = theme;
    }

    public static final Option OK_OPTION = new Option("Ok");

    public static final Option YES_OPTION = new Option("Yes");

    public static final Option NO_OPTION = new Option("No");

    public static final Option CANCEL_OPTION = new Option("Cancel");

    public GDialog(UIObject parent, String title) {
        super(title);
        this.parentContainer = parent;
    }

    public static Object getSelectedValue() {
        return selectedValue;
    }

    public static void showConfirmDialog(UIObject parent, Object message,
            GDialogChoiceListener choiceListener) {
        showMessage(parent, message, null, QUESTION_MESSAGE,
                getOptions(YES_NO_CANCEL_OPTION), null, choiceListener);
    }

    public static void showConfirmDialog(UIObject parent, Object message,
            String title, int optionType, GDialogChoiceListener choiceListener) {
        showMessage(parent, message, title, QUESTION_MESSAGE,
                getOptions(optionType), null, choiceListener);
    }

    public static void showConfirmDialog(UIObject parent, Object message,
            String title, int optionType, int messageType,
            GDialogChoiceListener choiceListener) {
        showMessage(parent, message, title, messageType,
                getOptions(optionType), null, choiceListener);
    }

    public static void showConfirmDialog(UIObject parent, Object message,
            String title, int optionType, int messageType, String imagePath,
            GDialogChoiceListener choiceListener) {
        showMessage(parent, message, title, messageType,
                getOptions(optionType), imagePath, choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, null, QUESTION_MESSAGE,
                getOptions(OK_CANCEL_OPTION), null, null, null, choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, QUESTION_MESSAGE,
                getOptions(OK_CANCEL_OPTION), null, initialValue, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, int messageType,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION), null, initialValue, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, int messageType,
            String imagePath, GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION), imagePath, initialValue, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, Object[] selectionValues,
            int messageType, String imagePath,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION), imagePath, initialValue,
                selectionValues, choiceListener);
    }

    private static Option[] getOptions(int optionType) {
        if (optionType == YES_NO_OPTION) {
            return new Option[] { OK_OPTION, NO_OPTION };
        } else if (optionType == YES_NO_CANCEL_OPTION) {
            return new Option[] { OK_OPTION, NO_OPTION, CANCEL_OPTION };

        } else if (optionType == OK_CANCEL_OPTION) {
            return new Option[] { OK_OPTION, CANCEL_OPTION };
        }
        throw new IllegalStateException("Invalid value");

    }

    public static void showMessage(UIObject parent, Object message) {
        showMessage(parent, message, null, INFORMATION_MESSAGE, null, null,
                null);
    }

    public static void showMessage(UIObject parent, Object message,
            String title, int messageType) {
        showMessage(parent, message, title, messageType, null, null, null);
    }

    public static void showMessage(UIObject parent, Object message,
            String title, int messageType, Option[] options, String imagePath,
            GDialogChoiceListener choiceListener) {
        setDefaultDialogProperties(title, messageType);
        Image icon = getIcon(messageType, imagePath);
        currentDialog.setContent(new DialogPane(message, options, icon,
                choiceListener));
        computeSizeAndDisplay(parent);

    }

    private static void computeSizeAndDisplay(UIObject parent) {
        if (overlayLayer == null) {
            overlayLayer = new GlassPanel();
        }
        overlayLayer.show();
        currentDialog.setVisible(true);
        adjustDialogSizeToContent(parent, currentDialog);
    }

    private static void adjustDialogSizeToContent(UIObject parent,
            GInternalFrame internalFrame) {
        Widget content = internalFrame.getContent();
        int height = content.getOffsetHeight();
        currentDialog.setHeight(height);
        int width = content.getOffsetWidth();
        currentDialog.setWidth(width);
        int left = 0;
        int top = 0;
        if (parent != null) {
            left = parent.getAbsoluteLeft() + (parent.getOffsetWidth() - width)
                    / 2;
            top = parent.getAbsoluteTop() + (parent.getOffsetHeight() - height)
                    / 2;
        } else {
            left = (Window.getClientWidth() - width) / 2;
            top = (Window.getClientHeight() - height) / 2;
        }
        left = left > 0 ? left : 0;
        top = top > 0 ? top : 0;
        currentDialog.setLocation(left, top);

    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, int messageType, Option[] options, String imagePath,
            Object initialValue, Object[] selectionValues,
            GDialogChoiceListener choiceListener) {
        setDefaultDialogProperties(title, messageType);
        Image icon = getIcon(messageType, imagePath);
        currentDialog.setContent(new InputDialogPane(message, options, icon,
                initialValue, selectionValues, choiceListener));
        computeSizeAndDisplay(parent);
    }

    private static void setDefaultDialogProperties(String title, int messageType) {
        if (currentDialog != null) {
            throw new IllegalStateException("A Dialog is already opened!");
        }
        currentDialog = new GwtInternalFrame("");

        currentDialog.setClosable(false);
        currentDialog.setMinimizable(false);
        currentDialog.setMaximizable(false);

        currentDialog.setTheme(theme);
        if (title != null) {
            currentDialog.setCaption(title);
        }

    }

    private static Image getIcon(int messageType, String imagePath) {
        Image icon = new PNGImage(getImagePath(messageType, imagePath), 32, 32);
        return icon;

    }

    private static String getImagePath(int messageType, String image) {
        if (image != null) {
            return image;
        }
        switch (messageType) {
        case INFORMATION_MESSAGE:
            return "gwm/images/information.png";
        case QUESTION_MESSAGE:
            return "gwm/images/question.png";
        case PLAIN_MESSAGE:
            return "gwm/images/text.png";
        case WARNING_MESSAGE:
            return "gwm/images/warning.png";
        case ERROR_MESSAGE:
            return "gwm/images/error.png";
        default:
        }
        return "gwm/images/unknown.png";
    }

    static class InputDialogPane extends DialogPane {
        private TextBox textBoxInput;

        private ListBox selectionValuesInput;

        Object initialValue;

        Object selectionValues;

        public InputDialogPane(Object message, Option[] options, Image icon,
                Object initialValue, Object[] selectionValues,
                GDialogChoiceListener choiceListener) {
            super(message, options, icon, choiceListener);
            if (selectionValues != null) {
                selectionValuesInput = new ListBox();
                for (int i = 0; i < selectionValues.length; i++) {
                    selectionValuesInput.addItem(selectionValues[i].toString());
                    if (selectionValues[i].equals(initialValue)) {
                        selectionValuesInput.setSelectedIndex(i);
                    }
                }
                centerContentPanel.add(selectionValuesInput);
            } else {
                textBoxInput = new TextBox();
                if (initialValue != null) {
                    textBoxInput.setText(initialValue.toString());
                    this.initialValue = initialValue;
                }
                centerContentPanel.add(textBoxInput);
            }
        }

        protected void buildUI(Object message, Option[] options, Image icon) {
            super.buildUI(message, options, icon);
        }

        public String getInputValue() {
            if (selectionValuesInput != null) {
                return selectionValuesInput.getItemText(selectionValuesInput
                        .getSelectedIndex());
            }
            return textBoxInput.getText();
        }
    }

    static class DialogPane extends Composite {

        DockPanel layout;

        GDialogChoiceListener choiceListener;

        protected VerticalPanel centerContentPanel;

        public DialogPane(Object message, Option[] options, Image icon,
                GDialogChoiceListener choiceListener) {
            this.choiceListener = choiceListener;
            buildUI(message, options, icon);
        }

        protected void buildUI(Object message, Option[] options, Image icon) {
            layout = new DockPanel();
            layout.setSpacing(10);
            centerContentPanel = new VerticalPanel();
            if (message != null) {
                centerContentPanel.add(new Label(message.toString()));
            }
            if (icon != null) {
                layout.add(icon, DockPanel.WEST);
            }
            if (options == null) {
                options = new Option[] { OK_OPTION };
            }
            if (options != null) {

                FlowPanel optionsPanel = new FlowPanel();
                DOM.setStyleAttribute(optionsPanel.getElement(), "textAlign",
                        "center");
                for (int i = 0; i < options.length; i++) {
                    final Option option = options[i];
                    Button optionBtn = new Button(option.getLabel());
                    optionBtn.addClickListener(new ClickListener() {
                        public void onClick(Widget sender) {
                            if (choiceListener != null) {
                                String inputValue = null;
                                if (option.equals(OK_OPTION)
                                        && DialogPane.this instanceof InputDialogPane) {
                                    inputValue = ((InputDialogPane) DialogPane.this)
                                            .getInputValue();

                                }
                                choiceListener.onChoice(option, inputValue);
                            }
                            overlayLayer.hide();
                            // TODO with Johan for release process
                            currentDialog.close();
                            currentDialog = null;
                        }
                    });
                    optionsPanel.add(optionBtn);
                    DOM
                            .setStyleAttribute(optionBtn.getElement(),
                                    "margin", "1");

                }
                layout.add(optionsPanel, DockPanel.SOUTH);
            }
            layout.add(centerContentPanel, DockPanel.CENTER);
            initWidget(layout);
        }

    }

    public static class Option {
        String label;

        public Option(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
        
//        public boolean equals(Object value){
//            if(value == null || this != value){
//                return false;
//            }
//            if(!((Option)value).getLabel().equals(label)){
//                return false;
//            }
//            return true;
//        }
        public String toString(){
            return label;
        }
    }

    public static class OkOption extends Option {
        public OkOption() {
            super("OK");
        }
    }

    public static class YesOption extends Option {
        public YesOption() {
            super("Yes");
        }
    }

    public static class NoOption extends Option {
        public NoOption() {
            super("No");
        }
    }

    public static class CancelOption extends Option {
        public CancelOption() {
            super("Cancel");
        }
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Object getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Object initialValue) {
        this.initialValue = initialValue;
    }

    public Object getInputValue() {
        return inputValue;
    }

    public void setInputValue(Object inputValue) {
        this.inputValue = inputValue;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        this.options = options;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public UIObject getParentContainer() {
        return parentContainer;
    }

    public void setParentContainer(UIObject parent) {
        this.parentContainer = parent;
    }

    public Object[] getSelectionValues() {
        return selectionValues;
    }

    public void setSelectionValues(Object[] selectionValues) {
        this.selectionValues = selectionValues;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static void setSelectedValue(Object selectedValue) {
        GDialog.selectedValue = selectedValue;
    }

}