/*
 * Copyright 2008-2011 the original author or authors.
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

/**
 * @author Dean Iverson
 */
class JavaFxApplicationFactory extends AbstractFactory {

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        println "app from builder is ${builder.app}, stage is ${builder.app.primaryStage}"
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
        println "sceneWrapper is $builder.sceneWrapper"
        println "stage is $stage, scene is $scene"
        println "stage.scene is ${stage.scene}"
        stage.visible = true
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof SceneWrapper) {
            println "Application got child SceneWrapper"
            builder.sceneWrapper = child
        }
    }
}
