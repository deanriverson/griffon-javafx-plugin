package scripts
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
 * Base script for installer scripts
 *
 * @author Danno Ferrin
 * @author Andres Almiray
 *
 * @since 0.7
 */

import static griffon.util.GriffonApplicationUtils.isMacOSX
import griffon.util.RunMode

includeTargets << griffonScript("Package")
includePluginScript("javafx","_Prepare")

target(name: 'copyAllAppArtifacts', description: '', prehook: null, posthook: null) {
   depends(createConfig)

   distDir = buildConfig.griffon.dist.dir ?: "${basedir}/dist"
   targetDistDir = binaryDir
   System.setProperty(RunMode.KEY, RunMode.CUSTOM.name)

   create_binary_package()
   copyAppResources()
}

target(name: 'copyAppResources', description: '', prehook: null, posthook: null) {
    ant.copy( todir: "${binaryDir}/icons" ) {
        fileset( dir: "${basedir}/griffon-app/resources/", includes: "griffon-icon*" )
    }
}
