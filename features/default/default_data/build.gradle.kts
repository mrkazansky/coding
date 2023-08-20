apply {
    from("$rootDir/buildSrc/base-module.gradle")
}

plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
}

android {
    namespace = "com.kz.default_data"
}

dependencies {
    implementation(DataLibrary.paging3)

    implementation(DataLibrary.room)
    implementation(DataLibrary.roomCouroutine)
    implementation(DataLibrary.roomPaging)
    "annotationProcessor"(DataLibrary.annotationRoom)
    "ksp"(DataLibrary.kspRoom)

    implementation(DataLibrary.retrofit)
    implementation(DataLibrary.retrofitConverter)
    implementation(DataLibrary.retrofitInterceptor)

    implementation(project(":features:default:default_domain"))
    implementation(project(":core:core_data"))

    debugImplementation(project(":features:default:default_domain"))
    debugImplementation(project(":core:core_data"))
}
