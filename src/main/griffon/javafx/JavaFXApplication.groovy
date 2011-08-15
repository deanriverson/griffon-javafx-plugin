/*
 * Copyright 2009-2010 the original author or authors.
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
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.javafx

import javafx.application.Application
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.stage.WindowEvent

import griffon.util.GriffonExceptionHandler
import griffon.core.*

/**
 * @author dean
 */
class JavaFXApplication extends Application {
    private GriffonJavaFXApplication app

    JavaFXApplication() {
        app = new GriffonJavaFXApplication()
    }

    @Override
    void init() {
        UIThreadManager.instance.setUIThreadHandler(new JavaFXUIThreadHandler())
        GriffonExceptionHandler.registerExceptionHandler()
        app.initialize()
    }

    @Override
    void start(Stage stage) {
        app.primaryStage = stage

        // Ugly hack to get the system to shut down correctly.  Something is preventing
        // the app thread from exiting.  Another stage??  Must find it!
        //
        // Note: Calling Platform.exit() doesn't even work - I have to shoot Java in the
        // head with System.exit().
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            void handle(WindowEvent t) {
                app.shutdown()
                System.exit(0)
            }
        });

        app.startup()
        app.ready()
    }

    @Override
    void stop() {
        println "IN STOP!!!!!!!!!!!!!!!"
        app.shutdown()
    }

    public static void main(String[] args) {
        Application.launch(JavaFXApplication, args)
    }
}

