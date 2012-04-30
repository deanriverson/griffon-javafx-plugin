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
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.javafx;

import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.application.Application;

import griffon.core.UIThreadManager;
import griffon.util.GriffonExceptionHandler;
import griffon.application.StandaloneGriffonApplication;
import org.codehaus.griffon.runtime.core.AbstractGriffonApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main entry point for a Griffon JavaFX application.  It uses the
 * FXApplicationStub class to initialize the JavaFX platform and then takes care of
 * initializing the Griffon framework.
 *
 * @author Dean Iverson
 */
class JavaFXApplication extends AbstractGriffonApplication implements StandaloneGriffonApplication {
    private static final Logger LOG = LoggerFactory.getLogger(JavaFXApplication.class);

    private Application fxApp;
    private Stage primaryStage;

    final Object fxInitComplete = new Object();

    public JavaFXApplication() {
        this(AbstractGriffonApplication.EMPTY_ARGS);
    }

    public JavaFXApplication(String[] args) {
        super(args);
    }

    public HostServices getHostServices() {
        if (fxApp == null)
            return null;
        else
            return fxApp.getHostServices();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setFXApp(Application fxApp) {
        this.fxApp = fxApp;
    }

    public void bootstrap() {
        FXApplicationStub.setGriffonApp(this);

        // Application.launch doesn't return until the app is closed, so we need to
        // launch it in a different thread and wait for the FXApplicationStub to
        // notify us that JavaFX initialization is complete.
        synchronized (fxInitComplete) {
            new Thread(new Runnable() {
                public void run() {
                    Application.launch(FXApplicationStub.class, getStartupArgs());
                }
            }).start();

            try {
                fxInitComplete.wait();
                doGriffonInit();
            } catch (InterruptedException e) {
                if (LOG.isErrorEnabled())
                    LOG.error("Interrupted while waiting for JavaFX initialization: " + e.getMessage());
                GriffonExceptionHandler.sanitize(e).printStackTrace();
            }
        }
    }

    public void realize() {
        startup();
    }

    public void show() {
        ready();
    }

    public Object createApplicationContainer() {
        return primaryStage;
    }

    private void doGriffonInit() {
        // windowManager = new WindowManager(this);
        UIThreadManager.getInstance().setUIThreadHandler(new JavaFXUIThreadHandler());
        // addShutdownHandler(windowManager);

        initialize();
    }

    public static void main(String[] args) {
        GriffonExceptionHandler.registerExceptionHandler();
        StandaloneGriffonApplication app = new JavaFXApplication(args);
        try {
            app.bootstrap();
            app.realize();
            app.show();
        } catch (RuntimeException e) {
            GriffonExceptionHandler.sanitize(e).printStackTrace();
        }
    }
}