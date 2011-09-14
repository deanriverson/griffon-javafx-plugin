/*
 * Copyright 2009-2011 the original author or authors.
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
    def version = "0.2"
    def griffonVersion = '0.9.3 > *'
    def dependsOn = ['groovyfx':'0.2']
    def pluginIncludes = []
    def license = 'Apache Public License 2.0'
    def toolkits = ['javafx']
    def platforms = []

    // TODO Fill in these fields
    def author = 'Dean Iverson'
    def authorEmail = 'deanriverson@gmail.com'
    def title = 'Griffon JavaFX Plugin'
    def description = '''
This plugin allows you to write JavaFX applications using the Griffon framework.
'''
    def documentation = 'http://griffon.codehaus.org/Javafx+Plugin'
}
