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
 * @author Danno Ferrin
 * @author Josh Reed
 * @author Dean Iverson
 * @author Andres Almiray
 */

/**
 * Add the main class to the application's configuration.  This class will have its
 * "main" method invoked to begin execution of the program.
 */
eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.javafx.JavaFXApplication'
}


eventCleanPackage = { type ->
    ant.delete(dir: "${projectWorkDir}/installer/${type}", failonerror: false)
}

eventMakePackage = { type ->
    switch(type.toUpperCase()) {
        case 'JFX-NATIVE':
        case 'JFXNATIVE':
        case 'NATIVE':
            buildPackage('JfxNative')
            break
    }
}

buildPackage = { type ->
    includePluginScript('javafx', 'Prepare'+ type)
    includePluginScript('javafx', 'Create'+ type)

    includeTargets.binding.with {
        getVariable("preparePackage${type}")()
        getVariable("createPackage${type}")()
    }
}
