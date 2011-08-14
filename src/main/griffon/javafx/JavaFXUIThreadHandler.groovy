package griffon.javafx

import griffon.util.UIThreadHandler
import javafx.application.Platform
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.Executors

/**
 * Created by IntelliJ IDEA.
 * User: Dean Iverson
 * Date: 6/8/11
 * Time: 5:29 PM
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
