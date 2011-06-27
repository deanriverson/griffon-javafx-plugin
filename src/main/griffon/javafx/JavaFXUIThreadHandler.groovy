package griffon.javafx

import griffon.util.UIThreadHandler
import javafx.application.Platform
import java.lang.reflect.InvocationTargetException

/**
 * Created by IntelliJ IDEA.
 * User: Dean Iverson
 * Date: 6/8/11
 * Time: 5:29 PM
 */
class JavaFXUIThreadHandler implements UIThreadHandler {
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
            try {
                // Fake it: run the task using runLater but block
                // this method until it finishes.  GPars?
            } catch(InterruptedException ie) {
                // ignore
            } catch(InvocationTargetException ite) {
                // ignore
            }
        }
    }

    /**
     * Executes a code block outside of the UI thread.
     */
    void executeOutside(Runnable runnable) {
        if(!isUIThread()) {
            runnable.run();
        } else {
            // TODO use a ThreadPool - again GPars?
            new Thread(runnable).start();
        }
    }
}
