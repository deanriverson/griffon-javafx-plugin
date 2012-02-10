@artifact.package@import groovyx.javafx.beans.FXBindable

class @artifact.name@ {
    @FXBindable String message

    void mvcGroupInit(Map args) {
        message = '@artifact.name.plain@ Group'
    }
}
