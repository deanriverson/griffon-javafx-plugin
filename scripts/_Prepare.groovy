/*
 * Copyright 2008-2013 the original author or authors.
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
 * Base script for installer scripts
 *
 * @author Danno Ferrin
 * @author Andres Almiray
 *
 * @since 0.7
 */

installerPluginBase = getPluginDirForName('javafx').file as String

target(name: 'prepareDirectories', description: '', prehook: null, posthook: null) {
    ant.mkdir(dir: installerWorkDir)
    ant.mkdir(dir: binaryDir)
    ant.mkdir(dir: "${binaryDir}/icons")
}

target(name: 'test_is_linux', description: '', prehook: null, posthook: null) {
    ant.condition(property: "os.isLinux", value: true) {
        and {
            os( family: "unix" )
            and { os( name: "Linux" ) }
        }
    }
    ant.echo(message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}")    
    ant.fail(message: "You are not running on Linux", unless: "os.isLinux")
}

target(name: 'test_is_osx', description: '', prehook: null, posthook: null) {
    ant.condition(property: "os.isOSX", value: true) {
        and {
            os( family: "mac" )
            and { os( family: "unix" ) }
        }
    }
    ant.echo(message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}")
    ant.fail(message: "You are not running on MacOSX", unless: "os.isOSX")
}
