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
import static griffon.util.GriffonApplicationUtils.isWindows

import net.sf.image4j.codec.ico.ICOEncoder
import net.sf.image4j.codec.bmp.BMPEncoder
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.Toolkit


installerPluginBase = getPluginDirForName('javafx').file as String
includePluginScript('javafx','_Prepare')

target(name: 'preparePackageJfxNative', description: '', prehook: null, posthook: null) {
    event('PreparePackageStart', ['jfxnative'])

    installerWorkDir = "${projectWorkDir}/installer/jfxnative"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'
    prettyAppName = griffon.util.GriffonNameUtils.getNaturalName(griffonAppName)

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

        ant.mkdir(dir: "$installerResourcesDir/package/macosx/")
        [prettyAppName, "$prettyAppName-volume"].each {
            File macIcon = new File("$installerResourcesDir/package/macosx/${it}.icns")
            if (!macIcon.exists()) {
                ant.copy toFile: macIcon, file: "$installerWorkDir/icon.icns"
            }
        }
    }

    if (isWindows()) {
        def iconDef = buildConfig.deploy.application.icon.default
        Image icon
        if (iconDef) {
            icon = Toolkit.defaultToolkit.createImage("$basedir/griffon-app/resources/$iconDef.name")
        } else {
            icon = Toolkit.defaultToolkit.createImage('griffon-icon-64x64.png')
        }

        BufferedImage bi = new BufferedImage(icon.width, icon.height, BufferedImage.TYPE_INT_ARGB)
        bi.graphics.drawImage(icon, 0, 0, null)
        ICOEncoder.write(bi, new File("$installerWorkDir/icon.ico"))

        ant.mkdir(dir: "$installerResourcesDir/package/windows/")
        [prettyAppName].each {
            File ico = new File("$installerResourcesDir/package/windows/${it}.ico")
            if (!ico.exists()) {
                ant.copy toFile: ico, file: "$installerWorkDir/icon.ico"
            }
        }

        // Innosetup requires an icon no larger than 55x58, so scale down to 48x48 if needed
        if (bi.width > 55 || bi.hight > 58) {
            bi = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB)
            def g = bi.graphics
            def t = new AffineTransform()
            t.scale(48f/icon.width, 48f/icon.height)
            g.transform = t
            g.drawImage(icon, 0, 0, null)
        }
        BMPEncoder.write(bi, new File("$installerWorkDir/icon.bmp"))

        ["$prettyAppName-setup-icon"].each {
            File bmp = new File("$installerResourcesDir/package/windows/${it}.bmp")
            if (!bmp.exists()) {
                ant.copy toFile: bmp, file: "$installerWorkDir/icon.bmp"
            }
        }
    }

    prepareDirectories()
    event('PreparePackageEnd', ['jfxnative'])
}
setDefaultTarget(preparePackageJfxNative)
