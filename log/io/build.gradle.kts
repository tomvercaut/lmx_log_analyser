plugins {
    id("lmx.java-library-conventions")
}

dependencies {
    implementation(project(":log:model"))
    implementation(project(":log:parser"))
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
}
