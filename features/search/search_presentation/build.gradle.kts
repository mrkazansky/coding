apply {
    from("$rootDir/buildSrc/compose-module.gradle")
}

plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
}

android {
    namespace = "com.kz.search_presentation"
}

ksp {
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(UILibrary.composeDestination)
    "ksp"(UILibrary.kspDestination)

    implementation(DataLibrary.pagingCompose)

    implementation(UILibrary.coil)

    implementation(project(":features:search:search_domain"))

    testImplementation(DataLibrary.pagingTesting)
}
