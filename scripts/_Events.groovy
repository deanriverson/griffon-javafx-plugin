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
 * @author Andres Almiray
 */

/**
 * Add the main class to the application's configuration.  This class will have its
 * "main" method invoked to begin execution of the program.
 */
eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.javafx.JavaFXApplication'
}

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl ->}

/**
 * Create a compile dependency for the plugin and also for the JavaFX runtime jar.
 */
eventSetClasspath = { cl ->
    eventClosure1(cl)

    if (compilingPlugin('javafx'))
        return

    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-javafx-plugin', dirs: "${javafxPluginDir}/addon"

    // Don't add the dependencies during uninstall since that will cause errors
    // when the temporary javafx-rt.jar file is not actually found.
    if (scriptName != 'UninstallPlugin') {
        // Temporary copy of the jfxrt.jar file for compiling purposes.  Without this the
        // compiler can't find the JavaFX ObjectProperty classes used by @FXBindable.
        ant.copy(file: "${System.getenv('JAVAFX_HOME')}/rt/lib/jfxrt.jar",
                 tofile: "${javafxPluginDir}/addon/javafxrt-${javafxPluginVersion}.jar")

        griffonSettings.dependencyManager.addPluginDependency('javafx', [
            conf: 'compile',
            group: 'org.codehaus.griffon.plugins',
            name: 'javafxrt',
            version: javafxPluginVersion
        ])

        griffonSettings.dependencyManager.addPluginDependency('javafx', [
            conf: 'compile',
            group: 'org.codehaus.griffon.plugins',
            name: 'griffon-javafx-addon',
            version: javafxPluginVersion
        ])
    }
}

/**
 * Delete the temporary copy of the JavaFX runtime jar so it's not on the path
 * at run time.
 */
eventCopyLibsEnd = { jardir ->
    def javafxJar = new File("staging/javafxrt-${javafxPluginVersion}.jar")
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
