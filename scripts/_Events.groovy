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
 * @author Andres Almiray
 */

/**
 * Add the main class to the application's configuration.  This class will have its
 * "main" method invoked to begin execution of the program.
 */
eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.javafx.JavaFXApplication'
}

/**
 * Create a compile dependency for the plugin and also for the JavaFX runtime jar.
 */
eventClasspathStart = {
    // Temporary copy of the jfxrt.jar file for compiling purposes.  Without this the
    // compiler can't find the JavaFX ObjectProperty classes used by @FXBindable.
    File javafxRuntimeJar = new File("${javafxPluginDir}/dist/javafxrt-2.0.jar")
    ant.copy(file: "${System.getenv('JAVAFX_HOME')}/rt/lib/jfxrt.jar",
             tofile: javafxRuntimeJar) 
    griffonSettings.updateDependenciesFor 'compile', [javafxRuntimeJar]
    griffonSettings.updateDependenciesFor 'runtime', [javafxRuntimeJar]
    griffonSettings.updateDependenciesFor 'test', [javafxRuntimeJar]
}

/**
 * Delete the temporary copy of the JavaFX runtime jar so it's not on the path
 * at run time.
 */
eventCopyLibsEnd = { jardir ->
    def javafxJar = new File("staging/javafxrt-2.0.jar")
    ant.delete(file: javafxJar, failonerror: false, quiet: true)
}

/**
 * Add the actual JavaFX runtime jar from its real location so that it can find
 * it's hard-coded native library dependencies.
 */
eventRunAppTweak = { message ->
    def originalSetupRuntimeJars = setupRuntimeJars
    setupRuntimeJars = {
        def runtimeJars = []

        if (originalSetupRuntimeJars)
            runtimeJars = originalSetupRuntimeJars()

        def javafxrt = new File("${System.getenv('JAVAFX_HOME')}/rt/lib/jfxrt.jar")
        if (javafxrt)
            runtimeJars << javafxrt

        return runtimeJars
    }
}
