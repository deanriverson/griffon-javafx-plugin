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

package griffon.javafx

import javafx.application.Platform
import griffon.util.GriffonExceptionHandler
import org.codehaus.griffon.runtime.util.AbstractUIThreadHandler
import java.util.concurrent.FutureTask

/**
 * @author Dean Iverson
 */
class JavaFXUIThreadHandler extends AbstractUIThreadHandler {
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = new GriffonExceptionHandler()

    /**
     * True if the current thread is the UI thread.
     */
    boolean isUIThread() {
        return Platform.fxApplicationThread
    }

    /**
     * Executes a code block asynchronously on the UI thread.
     */
    void executeAsync(Runnable runnable) {
        Platform.runLater(runnable)
    }

    /**
     * Executes a code block synchronously on the UI thread.
     */
    void executeSync(Runnable runnable) {
        if(isUIThread()) {
            runnable.run()
        } else {
            // Define a Runnable that executes the task on the JavaFX thread using
            // Platform.runLater, then wait for it to complete.
            FutureTask<Void> task = new FutureTask({
                try {
                    runnable.run()
                } catch (Throwable throwable) {
                    UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(Thread.currentThread(), throwable)
                }
            }, null)

            Platform.runLater(task)
            task.get()
        }
    }
}
