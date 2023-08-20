apply {
    from("$rootDir/buildSrc/base-module.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.kz.default_domain"
}

dependencies {
    implementation(DataLibrary.paging3)
}
