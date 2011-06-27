@artifact.package@

import groovy.beans.Bindable
import griffon.util.GriffonNameUtils

class @artifact.name@ {
    @Bindable String status

    void mvcGroupInit(Map args) {
        status = "Welcome to ${GriffonNameUtils.capitalize(app.config.application.title)}, a JavaFX application"
    }
}
