plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    group = "com.dongjinpeng.movielens"
    applicationName = "movielens"
    version = "1.0.0"
    mainClassName = "com.dongjinpeng.movielens.FilterMovies"
}

dependencies {
    compile ("org.apache.flink:flink-core:1.6.1")
    compile ("org.apache.flink:flink-java:1.6.1")
    compile ("org.apache.flink:flink-streaming-java_2.11:1.6.1")
    compile ("org.slf4j:slf4j-log4j12:1.7.7")
    compile ("log4j:log4j:1.2.17")

    testCompile("junit:junit:4.12")
}

repositories {
    listOf("http://maven.aliyun.com/mvn/repository/").forEach {
        maven { url = uri(it) }
    }
    mavenCentral()
    jcenter()
}

tasks {
    task("averageRating", JavaExec::class) {
        main = "com.dongjinpeng.movielens.AverageRating"
        classpath = sourceSets["main"].runtimeClasspath
    }
}