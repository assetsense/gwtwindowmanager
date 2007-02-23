/*
 * Copyright (c) 2006-2007 Luciano Broussal, Johan Vos, Andy Scholz, Marcelo Emanoel  (http://www.gwtwindowmanager.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.gwm.client.impl;

import org.gwm.client.GDialog;
import org.gwm.client.GFrame;
import org.gwm.client.event.GDialogChoiceListener;
import org.gwm.client.util.GWmConstants;
import org.gwm.client.util.GwmUtilities;
import org.gwtwidgets.client.ui.PNGImage;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DefaultGDialog extends DefaultGFrame implements GDialog {

    public DefaultGDialog() {
        super();
    }

    public DefaultGDialog(String caption) {
        super(caption);
        setTheme(GWmConstants.DEFAULT_THEME);
        setResizable(false);
        setMaximizable(false);
        setMinimizable(false);
        setClosable(false);
    }

    public static final String YES_OPTION_LABEL = "Yes";

    public static final String NO_OPTION_LABEL = "No";

    public static final String CANCEL_OPTION_LABEL = "Cancel";

    private static String theme = GWmConstants.DEFAULT_THEME;

    private static DefaultGDialog currentDialog;

    private static GlassPanel overlayLayer;

   

    private Object selectedValue;

    private Option selectedOption;

    private int messageType = INFORMATION_MESSAGE;

    private int optionsType = OK_OPTION_TYPE;

    private GDialogChoiceListener choiceListener;

    private Object message;

    private static UIObject parent;

    public Object getSelectedValue() {
        return selectedValue;
    }

    public Object getSelectedOption() {
        return selectedOption;
    }

    public static void showConfirmDialog(UIObject parent, Object message,
            GDialogChoiceListener choiceListener) {
        showMessage(parent, message, null, QUESTION_MESSAGE,
                getOptions(YES_NO_CANCEL_OPTION_TYPE), null, choiceListener);
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
                getOptions(OK_CANCEL_OPTION_TYPE), null, null, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, QUESTION_MESSAGE,
                getOptions(OK_CANCEL_OPTION_TYPE), null, initialValue, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, int messageType,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION_TYPE), null, initialValue, null,
                choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, int messageType,
            String imagePath, GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION_TYPE), imagePath, initialValue,
                null, choiceListener);
    }

    public static void showInputDialog(UIObject parent, Object message,
            String title, Object initialValue, Object[] selectionValues,
            int messageType, String imagePath,
            GDialogChoiceListener choiceListener) {
        showInputDialog(parent, message, title, messageType,
                getOptions(OK_CANCEL_OPTION_TYPE), imagePath, initialValue,
                selectionValues, choiceListener);
    }

    private static Option[] getOptions(int optionType) {
        if (optionType == OK_OPTION_TYPE) {
            return new Option[] { OK_OPTION };
        } else if (optionType == YES_NO_OPTION_TYPE) {
            return new Option[] { YES_OPTION, NO_OPTION };
        } else if (optionType == YES_NO_CANCEL_OPTION_TYPE) {
            return new Option[] { YES_OPTION, NO_OPTION, CANCEL_OPTION };

        } else if (optionType == OK_CANCEL_OPTION_TYPE) {
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

    public static void showMessage(UIObject parentFrame, Object message,
            String title, int messageType, Option[] options, String imagePath,
            GDialogChoiceListener choiceListener) {
        setDefaultDialogProperties(title, messageType);
        Image icon = getIcon(messageType, imagePath);
        currentDialog.setMessage(new DialogPane(message, options, icon,
                choiceListener, currentDialog));
        parent = parentFrame;
        computeSizeAndDisplay();

    }

    private static void computeSizeAndDisplay() {
        if (overlayLayer == null) {
            overlayLayer = new GlassPanel();
        }
        overlayLayer.show();
        currentDialog.setVisible(true);
        adjustDialogSizeToContent(parent, currentDialog);
    }

    public void show() {
        if (overlayLayer == null) {
            overlayLayer = new GlassPanel();
        }
        buildContent();
        overlayLayer.show();
        GwmUtilities.diplayAtScreenCenter(this);
    }

    private void buildContent() {
        if (message == null) {
            throw new IllegalStateException(
                    "Please use the GDialog.setMessage(Object) or verify your message object is not null");
        }
        DialogPane dialogContent = new DialogPane(message,
                getOptions(optionsType), getIcon(QUESTION_MESSAGE,
                        getImagePath(messageType, null)), choiceListener, this);
        super.setContent(dialogContent);
    }

    private static void adjustDialogSizeToContent(UIObject parent,
            GFrame internalFrame) {
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
        currentDialog.setLocation(top, left);

    }

    public static void showInputDialog(UIObject parentFrame, Object message,
            String title, int messageType, Option[] options, String imagePath,
            Object initialValue, Object[] selectionValues,
            GDialogChoiceListener choiceListener) {
        setDefaultDialogProperties(title, messageType);
        Image icon = getIcon(messageType, imagePath);
        currentDialog.setMessage(new InputDialogPane(message, options, icon,
                initialValue, selectionValues, choiceListener));
        parent = parentFrame;
        computeSizeAndDisplay();
    }

    private static void setDefaultDialogProperties(String title, int messageType) {
        if (currentDialog != null) {
            throw new IllegalStateException("A Dialog is already opened!");
        }
        currentDialog = new DefaultGDialog();

        currentDialog.setClosable(false);
        currentDialog.setMinimizable(false);
        currentDialog.setMaximizable(false);
        currentDialog.setDraggable(false);
        currentDialog.setResizable(true);

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
            super(message, options, icon, choiceListener, currentDialog);
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

        private DefaultGDialog dialog;

        public DialogPane(Object message, Option[] options, Image icon,
                GDialogChoiceListener choiceListener, DefaultGDialog dialog) {
            this.choiceListener = choiceListener;
            this.dialog = dialog;
            buildUI(message, options, icon);
        }

        protected void buildUI(Object message, Option[] options, Image icon) {
            layout = new DockPanel();
            layout.setSpacing(10);
            centerContentPanel = new VerticalPanel();
            if (message != null) {
                if (message instanceof String)
                    centerContentPanel.add(new HTML(message.toString()));
                else if (message instanceof Widget)
                    centerContentPanel.add((Widget) message);
                else
                    throw new IllegalArgumentException(
                            "Error => The content message of the GDialog can be a Text or a Widget");

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
                                dialog.selectedOption = option;
                                dialog.selectedValue = inputValue;

                                choiceListener.onChoice(dialog);
                            }
                            overlayLayer.hide();

                            dialog.close();
                            dialog = null;

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

        public String toString() {
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

    public static void setDialogTheme(String theme) {
        DefaultGDialog.theme = theme;

    }

    private static class GlassPanel extends ComplexPanel {
        public GlassPanel() {
            setElement(DOM.createDiv());
        }

        public void show() {
            DOM.setStyleAttribute(getElement(), "position", "absolute");
            DOM.setStyleAttribute(getElement(), "left", "0px");
            DOM.setStyleAttribute(getElement(), "top", "0px");
            DOM.setStyleAttribute(getElement(), "width", "100%");
            DOM.setStyleAttribute(getElement(), "height", "100%");
            Window.enableScrolling(false);

            setStyleName("overlay_" + theme);

            DOM.setIntStyleAttribute(getElement(), "zIndex", DefaultGFrame
                    .getLayerOfTheTopWindow() + 1);

            RootPanel.get().add(this);
            setVisible(true);
        }

        public void hide() {
            RootPanel.get().remove(this);
        }

    }

    public static DefaultGDialog getGDialog() {
        return currentDialog;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setOptionType(int optionsType) {
        this.optionsType = optionsType;
    }

    public void setGDialogChoiceListener(GDialogChoiceListener choiceListener) {
        this.choiceListener = choiceListener;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public void setContent(String content) {
        throw new IllegalStateException(
                "Warning: user the setMessage(Object message) method instead");
    }

    public void setContent(Widget widget) {
        throw new IllegalStateException(
                "Warning: user the setMessage(Object message) method instead");
    }

}
