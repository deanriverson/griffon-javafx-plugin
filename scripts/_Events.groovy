import java.awt.PageAttributes.OriginType

/*
* Copyright 2009-2010 the original author or authors.
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

/**
 * Add the JavaFX runtime jar to the compile and runtime classpaths.
 */
def addJavaFXRuntimeJarToClasspath = {
//    def javafxHome = buildConfig.griffon.javafx?.home
//    println "javafxHome is $javafxHome"
//    println "runtime exists? ${new File(javafxHome+'/rt/lib/jfxrt.jar').exists()}"
//    griffonSettings.dependencyManager.flatDirResolver name: 'javafx-runtime', dirs: "$javafxHome/rt/lib"
//    griffonSettings.dependencyManager.addApplicationDependency(conf: 'compile', group: 'com.oracle',
//            name: 'jfxrt', version: '')

    def originalCompileClasspath = compileClasspath

    compileClasspath = {
        if (originalCompileClasspath)
            originalCompileClasspath()

//        def javafxrt = new File(buildConfig.griffon.javafx?.runtime?.path)
        def javafxrt = new File("${buildConfig.griffon.javafx?.home}/rt/lib/jfxrt.jar")
        if (javafxrt) {
            debug "  ${javafxrt.absolutePath}"
            pathelement(location: javafxrt.absolutePath)
        }
    }

//    def javafxrt = new File(buildConfig.griffon.javafx?.runtime?.path)
//    if (javafxrt) {
//        griffonSettings.compileDependencies.add(javafxrt)
//        println "ADDED jfxrt.jar to compile deps: ${griffonSettings.compileDependencies}"
//    }
}

def originalEventSetClasspath = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl ->}

/**
 * Add the main class to the application's configuration.  This class will have its
 * "main" method invoked to begin execution of the program.
 */
eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.javafx.JavaFXApplication'
}


eventSetClasspath = { cl ->
    addJavaFXRuntimeJarToClasspath()
    originalEventSetClasspath(cl)

    if (compilingPlugin('javafx'))
        return

    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-javafx-plugin', dirs: "${javafxPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('javafx', [
            conf: 'compile',
            name: 'griffon-javafx-addon',
            group: 'org.codehaus.griffon.plugins',
            version: javafxPluginVersion
    ])
}

eventRunAppTweak = { message ->
    def originalSetupRuntimeJars = setupRuntimeJars
    setupRuntimeJars = {
        def runtimeJars = []

        if (originalSetupRuntimeJars)
            runtimeJars = originalSetupRuntimeJars()

        def javafxrt = new File("${buildConfig.griffon.javafx?.home}/rt/lib/jfxrt.jar")
        if (javafxrt)
            runtimeJars << javafxrt

        return runtimeJars
    }
}