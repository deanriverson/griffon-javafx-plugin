@artifact.package@

class @artifact.name@ {
    def model
    def view

    // void mvcGroupInit(Map args) {
    //    // this method is called after model and view are injected
    // }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }

    def anAction = { evt = null ->
        // this is how you define an action closure that is called from a view
    }
}
