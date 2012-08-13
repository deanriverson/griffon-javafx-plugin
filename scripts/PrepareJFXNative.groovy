package scripts
/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Gant script that prepares a native JavaFX installer
 *
 * @author Danno Ferrin
 * @author Andres Almiray
 *
 * @since 0.7
 */

import static griffon.util.GriffonApplicationUtils.isMacOSX

installerPluginBase = getPluginDirForName('javafx').file as String
includePluginScript('javafx','_Prepare')

target(name: 'preparePackageJfxNative', description: '', prehook: null, posthook: null) {
    event('PreparePackageStart', ['jfxnative'])

    installerWorkDir = "${projectWorkDir}/installer/jfxnative"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'
    prettyAppName = griffon.util.GriffonNameUtils.getNaturalName(griffonAppName)

    ant.copy(todir: installerResourcesDir) {
        fileset(dir: "${installerPluginBase}/src/templates/jfxnative")
    }

    if (isMacOSX()) {
        iconWorkDir = "$installerWorkDir/icon.iconset"
        ant.mkdir(dir: iconWorkDir)
        def iconDef = buildConfig.deploy.application.icon.default
        if (iconDef) {
            ant.copy toFile: "$iconWorkDir/icon_${iconDef.width}x${iconDef.height}.png", file: "$basedir/griffon-app/resources/$iconDef.name"
        } else {
            ant.copy toFile: "$iconWorkDir/icon_64x64.png", file: 'griffon-icon-64x64.png'
        }
        ant.exec(executable: 'iconutil') {
            arg value: '--convert'
            arg value: 'icns'
            arg value: iconWorkDir
        }

        [prettyAppName, "$prettyAppName-volume"].each {
            File macIcon = new File("$installerResourcesDir/package/macosx/${it}.icns")
            if (!macIcon.exists()) {
                ant.copy toFile: macIcon, file: "$installerWorkDir/icon.icns"
            }
        }
    }

    prepareDirectories()
    event('PreparePackageEnd', ['jfxnative'])
}
setDefaultTarget(preparePackageJfxNative)
