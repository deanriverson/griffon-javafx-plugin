/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.javafx;

import javafx.stage.Stage;
import javafx.application.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the JavaFX application class.  This class is the one that should be launched
 * via JavaFX's Application#launch method in order to initialize the JavaFX platform.
 *
 * @author Dean Iverson
 */
public class FXApplicationStub extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(FXApplicationStub.class);
    private static JavaFXApplication griffonApp;

    /**
     * Give this class a reference to the Griffon application class to use during the
     * start method.  It's unfortunate that this has to be done as a static value, but
     * Griffon and JavaFX both want control of the startup sequence so something has
     * to give.
     *
     * @param griffonApp The JavaFXApplication instance (derived from GriffonApplication)
     */
    public static void setGriffonApp(JavaFXApplication griffonApp) {
        FXApplicationStub.griffonApp = griffonApp;
    }

    @Override
    public void start(Stage stage) {
        griffonApp.setFXApp(this);
        griffonApp.setPrimaryStage(stage);

        synchronized (griffonApp.fxInitComplete) {
            griffonApp.fxInitComplete.notify();
        }
    }

    @Override
    public void stop() {
        griffonApp.shutdown();
    }
}
