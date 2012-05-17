/*
 * Copyright 2008-2012 the original author or authors.
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

package griffon.javafx

import groovyx.javafx.factory.StageFactory
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.Window
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @author Dean Iverson
 */
class JavaFxApplicationFactory extends StageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JavaFxApplicationFactory)

    JavaFxApplicationFactory() {
        super(Stage)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        Window window = builder.app.createApplicationContainer()
        String windowName = (attributes.remove('name') ?: attributes.id) ?: computeWindowName()
        builder.app.windowManager.attach(windowName, window)
        window
    }

    private static int COUNT = 0

    private static String computeWindowName() {
        'window' + (COUNT++)
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def stage = node as Stage
        // stage.width = 800
        // stage.height = 600

        attributes.each { key, value ->
            if (key == "title")
                stage.title = value
        }

        if (!stage.title && builder.app.config?.application?.title) {
            stage.title = builder.app.config.application.title
        }

        def style = attributes.remove("style")
        if (style == null) {
            style = StageStyle.DECORATED;
        }
        if (style instanceof String) {
            style = StageStyle.valueOf(style.toUpperCase())
        }
        stage.style = style

        builder.context.put("sizeToScene", attributes.remove("sizeToScene"))
        builder.context.put("centerOnScreen", attributes.remove("centerOnScreen"))

        return true
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (node instanceof Stage) {
            if (builder.context.sizeToScene || node.getWidth() == -1) {
                node.sizeToScene()
            }
            if (builder.context.centerOnScreen) {
                node.centerOnScreen();
            }
            /* handled by WindowManager
            if (builder.context.show) {
                node.show();
            }
            */
        }
    }

    /*
    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        final stage = node as Stage
        Scene scene = builder.currentScene ?: new Scene(new Group(), 800, 600)
        stage.scene = scene
        // stage.show()
        // stage.visible = true
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Scene) {
            builder.currentScene = child
        }
    }
    */
}
