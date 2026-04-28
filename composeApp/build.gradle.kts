import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.korino.eyedrops.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi)
            packageName = "EyeDrops"
            packageVersion = "1.0.0"
            description = "Eye Drop Reminder Timer"
            vendor = "Korino"
            copyright = "Copyright 2025 Korino"

            macOS {
                bundleID = "com.korino.eyedrops"
            }
            windows {
                // uuidgen 으로 생성한 고정 UUID — 버전 업그레이드 추적에 사용되므로 변경 금지
                upgradeUuid = "A1B2C3D4-E5F6-7890-ABCD-EF1234567890"
                menuGroup = "EyeDrops"
                shortcut = true
            }
        }
    }
}
