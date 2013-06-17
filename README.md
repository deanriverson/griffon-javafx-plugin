
Griffon JavaFX Plugin
---------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/javafx](http://artifacts.griffon-framework.org/plugin/javafx)


Allows writing Griffon JavaFX applications using GroovyFX for the views.

This plugin assumes the existence of [JavaFX][1] on the developer's computer.
The environment variable JAVAFX_HOME should point to the root directory of the
JavaFX SDK installation.

Usage
-----
Once installed, you can use any of the nodes provided by GroovyFX in your views.
GroovyFX documentation can be found [here][2] and JavaFX documentation can be
found [here][3]. In addition, you will be able to use the @FXBindable annotation
on your models.

### Property Editors

This plugin contributes the following property editors

| *Type*                      | *Format*                                           |
| --------------------------- | -------------------------------------------------- |
| javafx.scene.paint.Color    | #RGB ; #RGBA ; #RRGGBB; #RRGGBBAA ; Color constant |
| javafx.geometry.Dimension2D | width, height                                      |
| javafx.scene.image.Image    | path/to/image_file                                 |
| javafx.geometry.Insets      | top, left, bottom, right                           |
| javafx.geometry.Point2D     | x, y                                               |
| javafx.geometry.Rectangle2D | x, y , width, height                               |

Notes:

 * Color supports all color constants defined by `javafx.scene.paint.Color`.


### Default Imports

This plugin enables additional imports to be used in Groovy based artifacts.
Here's the full list per type

 * Controller
   * javafx.collections
   * javafx.application
   * javafx.util
 * Model
   * javafx.collections
   * javafx.beans
   * javafx.beans.binding
   * javafx.beans.property
   * javafx.util.
   * groovyx.javafx.beans.
 * View
   * javafx.collections
   * javafx.concurrent
   * javafx.event
   * javafx.geometry
   * javafx.scene
   * javafx.scene.chart
   * javafx.scene.control
   * javafx.scene.control.cell
   * javafx.scene.effect
   * javafx.scene.image
   * javafx.scene.input
   * javafx.scene.layout
   * javafx.scene.media
   * javafx.scene.paint
   * javafx.scene.shape
   * javafx.scene.text
   * javafx.scene.transform
   * javafx.scene.web
   * javafx.stage
   * javafx.util

### Example

Your main application's view might look like this (in fact it will look very
similar if you create your application with the [JavaFX archetype][4]):

    application(title: 'Cool App', sizeToScene: true, centerOnScreen: true) {
        scene(fill: black, width: 400, height: 300) {
            hbox(padding: 80) {
                text(text: "Java", font: "80pt sanserif") {
                    fill linearGradient(endX: 0, stops: [[0, orange], [1, chocolate]])
                }
                text(text: "FX", font: "80pt sanserif") {
                    fill linearGradient(endX: 0, stops: [[0, cyan], [1, dodgerblue]])
                    effect dropShadow(color: dodgerblue, radius: 25, spread: 0.25)
                }
            }
        }
    }

[1]: http://www.oracle.com/technetwork/java/javafx/downloads/index.html
[2]: http://groovy.codehaus.org/GroovyFX
[3]: http://docs.oracle.com/javafx/2.0/api/index.html
[4]: http://artifacts.griffon-framework.org/archetype/javafx/description

Griffon JavaFX Plugin
=====================

This plugin allows you to create JavaFX 2.0 applications with the Griffon
framework.

*This plugin is highly experimental*
