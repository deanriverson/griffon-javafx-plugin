/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.javafx;

import griffon.core.GriffonController;
import griffon.core.UIThreadManager;
import griffon.core.controller.GriffonControllerActionManager;
import groovyx.javafx.appsupport.Action;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.codehaus.griffon.runtime.core.controller.AbstractGriffonControllerAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static griffon.util.GriffonNameUtils.isBlank;
import static org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.castToBoolean;

/**
 * @author Andres Almiray
 */
public class JavaFXGriffonControllerAction extends AbstractGriffonControllerAction {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ICON = "icon";

    private final Action toolkitAction;

    public JavaFXGriffonControllerAction(final GriffonControllerActionManager actionManager, GriffonController controller, final String actionName) {
        super(actionManager, controller, actionName);
        final JavaFXGriffonControllerAction self = this;
        toolkitAction = new Action();
        toolkitAction.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                actionManager.invokeAction(self.getController(), actionName, new Object[]{actionEvent});
            }
        });
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                UIThreadManager.getInstance().executeAsync(new Runnable() {
                    public void run() {
                        if (KEY_NAME.equals(evt.getPropertyName())) {
                            toolkitAction.setName(String.valueOf(evt.getNewValue()));
                        } else if (KEY_SHORT_DESCRIPTION.equals(evt.getPropertyName()) ||
                            KEY_LONG_DESCRIPTION.equals(evt.getPropertyName()) ||
                            KEY_DESCRIPTION.equals(evt.getPropertyName())) {
                            toolkitAction.setDescription(String.valueOf(evt.getNewValue()));
                        } else if (KEY_ENABLED.equals(evt.getPropertyName())) {
                            toolkitAction.setEnabled(castToBoolean(evt.getNewValue()));
                        } else if (KEY_SELECTED.equals(evt.getPropertyName())) {
                            toolkitAction.setSelected(castToBoolean(evt.getNewValue()));
                        } else if (KEY_ACCELERATOR.equals(evt.getPropertyName())) {
                            String accelerator = (String) evt.getNewValue();
                            if (!isBlank(accelerator))
                                toolkitAction.setAccelerator(accelerator);
                        } else if (KEY_SMALL_ICON.equals(evt.getPropertyName()) ||
                            KEY_LARGE_ICON.equals(evt.getPropertyName()) ||
                            KEY_ICON.equals(evt.getPropertyName())) {
                            final String icon = (String) evt.getNewValue();
                            if (!isBlank(icon)) toolkitAction.setIcon(icon);
                        }
                    }
                });
            }
        });
    }

    public Object getToolkitAction() {
        return toolkitAction;
    }

    protected void doExecute(Object... args) {
        ActionEvent event = null;
        if (args != null && args.length == 1 && args[0] instanceof ActionEvent) {
            event = (ActionEvent) args[0];
        }
        toolkitAction.onActionProperty().get().handle(event);
    }

    @Override
    protected void doInitialize() {
        toolkitAction.setName(getName());
        String description = getShortDescription();
        if (isBlank(description)) description = getLongDescription();
        toolkitAction.setDescription(description);
        toolkitAction.setEnabled(isEnabled());
        toolkitAction.setSelected(isSelected());
        String accelerator = getAccelerator();
        if (!isBlank(accelerator)) toolkitAction.setAccelerator(accelerator);
        String icon = getSmallIcon();
        if (isBlank(icon)) icon = getLargeIcon();
        if (!isBlank(icon)) toolkitAction.setIcon(icon);
    }
}
