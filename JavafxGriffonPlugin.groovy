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

/**
 * @author Dean Iverson
 */
class JavafxGriffonPlugin {
    String version = '0.10.2'
    String griffonVersion = '1.4.0 > *'
    Map dependsOn = [groovyfx: '0.9.1']
    List pluginIncludes = []
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, javafx, pivot, gtk
    List toolkits = ['javafx']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL to the plugin's documentation
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/deanriverson/griffon-javafx-plugin'

    List authors = [
        [
            id: 'deanriverson',
            name: 'Dean Iverson',
            email: 'deanriverson@gmail.com'
        ],
        [
            id: 'aalmiray',
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Griffon JavaFX Plugin'
    String description = '''
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
'''
}
