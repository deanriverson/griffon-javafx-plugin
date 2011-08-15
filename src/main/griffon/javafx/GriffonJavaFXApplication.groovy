package griffon.javafx

import org.codehaus.griffon.runtime.core.AbstractGriffonApplication
import javafx.stage.Stage

/**
 * @author dean
 */
class GriffonJavaFXApplication extends AbstractGriffonApplication {
    private Stage primaryStage

    /**
     * Provide read-only access to the primary stage.
     * @return The primary Stage of this application.
     */
    Stage getStage() {
        primaryStage
    }
}
