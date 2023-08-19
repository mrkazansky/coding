apply {
    from("$rootDir/buildSrc/base-module.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.kz.search_domain"
}

dependencies {
    implementation(DataLibrary.paging3)
}
