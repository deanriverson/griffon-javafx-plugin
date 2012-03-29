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
    List toolkits = ['javafx']
    List platforms = []
    String source = 'https://github.com/deanriverson/griffon-javafx-plugin'
    String documentation = ''
    List authors = [
        [
            name: 'Dean Iverson',
            email: 'deanriverson@gmail.com'
        ]
    ]
    String title = 'Griffon JavaFX Plugin'
    String description = '''
This plugin allows you to write JavaFX applications using the Griffon framework.
'''
}
