/*
 * Copyright 2009-2012 the original author or authors.
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
    String version = '0.4'
    String griffonVersion = '0.9.5 > *'
    Map dependsOn = [groovyfx: '0.3']
    List pluginIncludes = []
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = ['swing', 'javafx']
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
                    name: 'Dean Iverson',
                    email: 'deanriverson@gmail.com'
            ],
            [
                    name: 'Andres Almiray',
                    email: 'aalmiray@yahoo.com'
            ]
    ]
    String title = 'Griffon JavaFX Plugin'
    String description = '''
Allows writing Griffon JavaFX applications using GroovyFX for the views.

This plugin assumes the existence of [JavaFX][1] on the developer's computer.  The environment variable JAVAFX_HOME
should point to the root directory of the JavaFX SDK installation.

Usage
-----

Once installed, you can use any of the nodes provided by GroovyFX in your views.  GroovyFX documentation can be
found [here][2] and JavaFX documentation can be found [here][3].  In addition, you will be able to use the
@FXBindable annotation on your models.

### Example

Your main application's view might look like this (in fact it will look *exactly* like this if you create your
application with the [JavaFX archetype][4]):

    stage(title: '@griffon.app.class.name@', visible: true, centerOnScreen: true) {
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
