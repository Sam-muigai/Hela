plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

group = "com.samkt"
version = "0.1.0"

android {
    namespace = "com.samkt.hela"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Types from these appear in Hela's public API (Modifier, Color, TextStyle, ImageVector, …)
    // and @HelaPreviews is a @Preview multipreview, so consumers get them transitively.
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui.tooling.preview)
    // Internal only: HelaTheme maps tokens onto MaterialTheme and HelaBottomSheet wraps ModalBottomSheet.
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.samkt"
            artifactId = "hela"
            version = project.version.toString()
            afterEvaluate { from(components["release"]) }
            pom {
                name.set("Hela")
                description.set("A Vercel/Geist-style design system for Jetpack Compose.")
            }
        }
    }
}
