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
 * Gant script that creates a Native JavaFX Installer
 *
 * @author Danno Ferrin
 * @author Andres Almiray
 *
 * @since 0.7
 */

import groovy.xml.NamespaceBuilder

installerPluginBase = getPluginDirForName('javafx').file as String
includePluginScript("javafx", "_Create")

target(name: 'jfxSanityCheck', description: '', prehook: null, posthook: null) {
    depends(classpath)


    String javaHome = ant.project.properties['environment.JAVA_HOME']
    antjavafxFile = new File(javaHome, 'lib/ant-javafx.jar')
    if (!antjavafxFile.exists()) {
        event 'StatusError', ['The file ant-javafx.jar could not be found. Is JAVA_HOME set in the environment variables to a JDK?']
        exit 1
    }

    installerWorkDir = "${projectWorkDir}/installer/jfxnative"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'

    fxant = NamespaceBuilder.newInstance(ant, 'antlib:com.sun.javafx.tools.ant')
    taskdef(uri: 'antlib:com.sun.javafx.tools.ant',
            resource: 'com/sun/javafx/tools/ant/antlib.xml',
            classpath: "$installerResourcesDir:$antjavafxFile")

}

target(name: 'createPackageJfxNative', description: '', prehook: null, posthook: null) {
    depends(jfxSanityCheck, copyAllAppArtifacts)

    event("CreatePackageStart", ['jfxnative'])

    ant.replace(dir: installerResourcesDir, includes: '*.xml,*.html,*.txt,*properties') {
        replacefilter(token: '@app.name@', value: griffonAppName)
        replacefilter(token: '@app.version@', value: griffonAppVersion)
        replacefilter(token: '@app.author@', value: 'Griffon')
        replacefilter(token: '@app.author.email@', value: 'user@griffon.codehaus.org')
        replacefilter(token: '@app.url@', value: 'http://griffon.codehaus.org')
    }

    ant.mkdir(dir: distDir + "/jfxnative")


    def prettyAppName = griffon.util.GriffonNameUtils.getNaturalName(griffonAppName)

    fxant.application(
            id: griffonAppName,
            name: prettyAppName,
            mainClass: 'griffon.javafx.JavaFXApplication',
            //fallbackClass: //FIXME
    )

    def tempJarDir = "${projectWorkDir}/jars/${griffonAppName}"
    mkdir dir: tempJarDir
    unjar src: "$installerWorkDir/binary/lib/${griffonAppName}.jar", dest: tempJarDir

    fxant.jar(destFile: "$distDir/jfxnative/${griffonAppName}Jar.jar") {

        fxant.application(refid: griffonAppName)

        fxant.resources {
            fileset dir: "$installerWorkDir/binary/lib", includes: '*.jar', excludes: "${griffonAppName}.jar"
        }


        fxant.manifest {
            manifestMap.each { k, v ->
                fxant.attribute(name: k, value: v)
            }
            //attribute name:'Implementation-Title', value: griffonAppName
            //attribute name:'Implementation-Version', value: griffonAppVersion
        }

        fxant.fileset dir: tempJarDir
    }

    boolean configSaysJarSigning = buildConfig.griffon.jars.sign
    boolean doJarSigning = configSaysJarSigning
    if (doJarSigning) {
        def signJarParams = [:]
        for (key in ['alias', 'storepass', 'keystore', 'storetype', 'keypass', 'verbose']) {
            if (buildConfig.signingkey.params."$key") {
                signJarParams[key] = buildConfig.signingkey.params[key]
            }
        }
        fxant.signjar(signJarParams) {
            fileset file: "$distDir/jfxnative/${griffonAppName}Jar.jar"
        }
        fxant.signjar(signJarParams) {
            fileset dir: "$installerWorkDir/binary/lib", excludes: "${griffonAppName}.jar"
        }
    }

    fxant.deploy(width: argsMap['applet-width'] ?: 480,
            height: argsMap['applet-height'] ?: 640,
            outdir: "$distDir/jfxnative/",
            nativeBundles: 'all',
            //verbose: "true",
            outfile: "$prettyAppName") {

        fxant.application(refid: griffonAppName)

        fxant.resources {
            fileset file: "$distDir/jfxnative/${griffonAppName}Jar.jar"
            fileset dir: "$installerWorkDir/binary/lib", excludes: "${griffonAppName}.jar"
        }

        fxant.info(title: prettyAppName) {
            icon(href: buildConfig.deploy.application.icon.default.name ?: 'griffon-icon-64x64.png',
                    kind: 'default',
                    width: buildConfig.deploy.application.icon.default.width ?: '64',
                    height: buildConfig.deploy.application.icon.default.heignt ?: '64')
            splash(href: buildConfig.deploy.application.icon.splash.name ?: 'griffon.png')

        }

        //fxant.permissions elevate: 'true'
    }

    event("CreatePackageEnd", ['jfxnative'])
}
setDefaultTarget(createPackageJfxNative)
