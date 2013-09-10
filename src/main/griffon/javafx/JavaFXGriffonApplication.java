/*
 * Copyright 2009-2013 the original author or authors.
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

import griffon.application.StandaloneGriffonApplication;
import griffon.core.UIThreadManager;
import griffon.util.UIThreadHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;

import static griffon.util.GriffonExceptionHandler.registerExceptionHandler;
import static griffon.util.GriffonExceptionHandler.sanitize;

/**
 * @author Dean Iverson
 * @author Andres Almiray
 */
public class JavaFXGriffonApplication extends AbstractJavaFXGriffonApplication implements StandaloneGriffonApplication {
    private boolean primaryStageDispensed = false;
    private final WindowManager windowManager;
    private WindowDisplayHandler windowDisplayHandler;
    private final WindowDisplayHandler defaultWindowDisplayHandler = new ConfigurableWindowDisplayHandler();
    private Stage primaryStage;

    public JavaFXGriffonApplication() {
        this(EMPTY_ARGS);
    }

    public JavaFXGriffonApplication(String[] args) {
        super(args);
        windowManager = new WindowManager(this);
        UIThreadManager.getInstance().setUIThreadHandler(getUIThreadHandler());
        addShutdownHandler(windowManager);
    }

    protected UIThreadHandler getUIThreadHandler() {
        return new JavaFXUIThreadHandler();
    }

    public Object createApplicationContainer() {
        if (primaryStageDispensed) {
            return new Stage();
        } else {
            primaryStageDispensed = true;
            return primaryStage;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        primaryStage = stage;

        execOutsideUI(new Runnable() {
            public void run() {
                try {
                    bootstrap();
                    realize();
                    show();
                } catch (RuntimeException e) {
                    sanitize(e).printStackTrace();
                }
            }
        });
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public WindowDisplayHandler getWindowDisplayHandler() {
        return windowDisplayHandler;
    }

    public void setWindowDisplayHandler(WindowDisplayHandler windowDisplayHandler) {
        this.windowDisplayHandler = windowDisplayHandler;
    }

    public final WindowDisplayHandler resolveWindowDisplayHandler() {
        return windowDisplayHandler != null ? windowDisplayHandler : defaultWindowDisplayHandler;
    }

    public void bootstrap() {
        initialize();
    }

    public void realize() {
        startup();
    }

    public void show() {
        Window startingWindow = windowManager.getStartingWindow();
        windowManager.show(startingWindow);
        ready();
    }

    public boolean shutdown() {
        if (super.shutdown()) {
            exit();
        }
        return false;
    }

    public void exit() {
        System.exit(0);
    }

    public static void run(Class<? extends Application> applicationClass, String[] args) {
        registerExceptionHandler();
        Application.launch(applicationClass, args);
    }

    public static void main(String[] args) {
        run(JavaFXGriffonApplication.class, args);
    }
}
