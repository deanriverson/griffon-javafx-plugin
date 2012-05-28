@artifact.package@application(title: '@griffon.app.class.name@', sizeToScene: true, centerOnScreen: true) {
    scene(fill: black, width: 400, height: 300) {
        text(text: '@griffon.app.class.name', font: "80pt sanserif") {
            fill linearGradient(endX: 0, stops: [[0, RED], [1, ORANGE]])
        }
    }
}
