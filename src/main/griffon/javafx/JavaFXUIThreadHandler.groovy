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

package griffon.javafx

import griffon.util.UIThreadHandler
import javafx.application.Platform
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.Executors

/**
 * @author Dean Iverson
 */
class JavaFXUIThreadHandler implements UIThreadHandler {
    private ExecutorService threadPool = Executors.newFixedThreadPool(10)

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
            runnable.run();
        } else {
            // Define a Runnable that executes the task on the JavaFX thread
            // then wait for it to complete
            def runOnJavaFXThread = { executeAsync(runnable) } as Runnable
            def future = threadPool.submit(runOnJavaFXThread)
            future.get()
        }
    }

    /**
     * Executes a code block outside of the UI thread.
     */
    void executeOutside(Runnable runnable) {
        if(!isUIThread()) {
            runnable.run();
        } else {
            threadPool.submit(runnable)
        }
    }
}
