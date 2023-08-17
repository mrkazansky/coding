plugins {
    id("com.android.library")
}

apply {
    from("$rootDir/buildSrc/compose-module.gradle")
}

android {
    namespace = "com.kz.core_ui"
}
