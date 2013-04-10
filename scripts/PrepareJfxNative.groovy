/*
 * Copyright 2008-2013 the original author or authors.
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
import static griffon.util.GriffonApplicationUtils.isLinux

import net.sf.image4j.codec.ico.ICOEncoder
import net.sf.image4j.codec.bmp.BMPEncoder
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.Toolkit
import javax.imageio.ImageIO


installerPluginBase = getPluginDirForName('javafx').file as String
includePluginScript('javafx','_Prepare')

target(name: 'preparePackageJfxNative', description: '', prehook: null, posthook: null) {
    event('PreparePackageStart', ['jfxnative'])

    // turn off native Griffon code signing.  JFX does it's own thing
    _skipSigning = true

    // prepare the installer icons
    installerWorkDir = "${projectWorkDir}/installer/jfxnative"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'
    mkdir dir:installerResourcesDir

    prettyAppName = griffon.util.GriffonNameUtils.getNaturalName(griffonAppName)

    def iconDef = buildConfig.deploy.application.icon.default
    Image icon
    if (iconDef) {
        icon = Toolkit.defaultToolkit.createImage("$basedir/griffon-app/resources/$iconDef.name")
    } else {
        icon = Toolkit.defaultToolkit.createImage('griffon-icon-32x32.png')
    }

    BufferedImage bi = new BufferedImage(icon.width, icon.height, BufferedImage.TYPE_INT_ARGB)
    bi.graphics.drawImage(icon, 0, 0, null)
    def pngImageLocation = "$installerWorkDir/${griffonAppName}.png"
    ImageIO.write(bi, "png", new File(pngImageLocation));

    if (isLinux()) {
        ant.mkdir(dir: "$installerResourcesDir/package/linux")
        [prettyAppName.replaceAll('\\s', '')].each {
            File png = new File("$installerResourcesDir/package/linux/${it}.png")
            if (!png.exists()) {
                ant.copy toFile: png, file: pngImageLocation
            }
        }
    }

    if (isMacOSX()) {
        iconWorkDir = "$installerWorkDir/icon.iconset"
        ant.mkdir(dir: iconWorkDir)
        if ([16,32,128,256,512].contains(icon.width)) {
            // only accept happy sized icons.  64x64 is not happy.
            ant.copy toFile: "$iconWorkDir/icon_${icon.width}x${icon.height}.png", file: pngImageLocation
        } else {
            // hammer it into 128x128
            bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
            def g = bi.graphics
            def t = new AffineTransform()
            t.scale(128f/icon.width, 128f/icon.height)
            g.transform = t
            g.drawImage(icon, 0, 0, null)
            ImageIO.write(bi, "png", new File("$iconWorkDir/icon_128x128.png"));
        }

        ant.exec(executable: 'iconutil') {
            arg value: '--convert'
            arg value: 'icns'
            arg value: iconWorkDir
        }

        ant.mkdir(dir: "$installerResourcesDir/package/macosx")
        [prettyAppName, "$prettyAppName-volume"].each {
            File macIcon = new File("$installerResourcesDir/package/macosx/${it}.icns")
            if (!macIcon.exists()) {
                ant.copy toFile: macIcon, file: "$installerWorkDir/icon.icns"
            }
        }
    }

    if (isWindows()) {
        ICOEncoder.write(bi, new File("$installerWorkDir/icon.ico"))

        ant.mkdir(dir: "$installerResourcesDir/package/windows")
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
