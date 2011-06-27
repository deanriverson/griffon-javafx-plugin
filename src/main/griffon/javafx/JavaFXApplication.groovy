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

import griffon.core.GriffonController
import griffon.core.GriffonModel
import griffon.core.GriffonView
import griffon.core.MVCClosure
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper
import javafx.application.Application
import javafx.stage.Stage
import org.codehaus.griffon.runtime.core.BaseGriffonApplication
import javafx.event.EventType
import javafx.stage.WindowEvent
import javafx.event.EventHandler
import javafx.application.Platform

/**
 * @author dean
 */
class JavaFXApplication extends Application {
    @Delegate
    private final BaseGriffonApplication base
    private Stage primaryStage

    JavaFXApplication() {
        base = new BaseGriffonApplication(this)
    }

    /**
     * Provide read-only access to the primary stage.
     * @return The primary Stage of this application.
     */
    Stage getStage() { primaryStage }

    @Override
    void init() {
        UIThreadHelper.instance.setUIThreadHandler(new JavaFXUIThreadHandler())
        GriffonExceptionHandler.registerExceptionHandler()
        initialize()
    }

    @Override
    void start(Stage stage) {
        primaryStage = stage

        // Ugly hack to get the system to shut down correctly.  Something is preventing
        // the app thread from exiting.  Another stage??  Must find it!
        //
        // Note: Calling Platform.exit() doesn't even work - I have to shoot Java in the
        // head with System.exit().
        primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
            void handle(WindowEvent t) {
                System.exit(0)
            }
        });

        startup()
        ready()
    }

    @Override
    void stop() {
        println "IN STOP!!!!!!!!!!!!!!!"
        shutdown()
    }

    public static void main(String[] args) {
        Application.launch(griffon.javafx.JavaFXApplication, args)
    }

    def <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, MVCClosure<M, V, C> mvcClosure) {
        base.withMVCGroup(mvcType, mvcClosure)
    }

    def <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, MVCClosure<M, V, C> mvcClosure) {
        base.withMVCGroup(mvcType, mvcName, mvcClosure)
    }

    def <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, Map<String, Object> args, MVCClosure<M, V, C> mvcClosure) {
        base.withMVCGroup(mvcType, mvcName, args, mvcClosure)
    }

    def <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, Map<String, Object> args, MVCClosure<M, V, C> mvcClosure) {
        base.withMVCGroup(mvcType, args, mvcClosure)
    }
}

