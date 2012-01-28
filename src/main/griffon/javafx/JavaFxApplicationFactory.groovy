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

import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.Group
import groovyx.javafx.factory.SceneWrapper
import javafx.scene.paint.Color
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Dean Iverson
 */
class JavaFxApplicationFactory extends AbstractFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JavaFxApplicationFactory)
    
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        return builder.app.primaryStage;
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def stage = node as Stage
        stage.width = 800
        stage.height = 600
        
        attributes.each { key, value ->
            if (key == "title")
                stage.title = value
        }

        if (!stage.title && builder.app.config?.application?.title)
            stage.title = builder.app.config.application.title

        return true
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        final stage = node as Stage
        Scene scene = builder.sceneWrapper?.build() ?: new Scene(new Group(), 800, 600)
        stage.scene = scene
        stage.visible = true
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof SceneWrapper) {
            builder.sceneWrapper = child
        }
    }
}
