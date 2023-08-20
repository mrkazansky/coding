apply {
    from("$rootDir/buildSrc/compose-module.gradle")
}

plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
}

android {
    namespace = "com.kz.default_presentation"
}

ksp {
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(UILibrary.composeDestination)
    "ksp"(UILibrary.kspDestination)

    implementation(DataLibrary.pagingCompose)

    implementation(UILibrary.coil)

    implementation(project(":features:default:default_domain"))
    debugImplementation(project(":features:default:default_domain"))

    testImplementation(DataLibrary.pagingTesting)
}
