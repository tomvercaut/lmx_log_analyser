import org.gradle.internal.os.OperatingSystem

plugins {
    id("lmx.java-application-conventions")
    id("org.beryx.runtime") version "1.12.7"
}

dependencies {
//    implementation("org.apache.commons:commons-text")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation(project(":log:model"))
    implementation(project(":log:parser"))
    implementation(project(":log:io"))
    implementation(project(":log:analysis"))
}

application {
    // Define the main class for the application.
    mainClass.set("uzg.rt.lmx.apps.log_analyser.App")
    applicationName = "lmx_log_analyser"
}

runtime {
    addOptions("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    jpackage {
        val currentOs = OperatingSystem.current()
        if(currentOs.isWindows) {
            installerOptions.add("--win-per-user-install")
            installerOptions.add("--win-dir-chooser")
            installerOptions.add("--win-menu")
            installerOptions.add("--win-shortcut")
            imageOptions.add("--win-console")
        } else if (currentOs.isLinux) {
            installerOptions.add("--linux-package-name")
            installerOptions.add("lmx_log_analyser")
            installerOptions.add("--linux-shortcut")
        } else if (currentOs.isMacOsX) {
            installerOptions.add("--mac-package-name")
            installerOptions.add("lmx_log_analyser")
            installerType = "dmg"
        }
    }
}