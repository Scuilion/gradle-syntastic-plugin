# gradle-syntastic-plugin

A gradle plugin for setting up the .syntastic_javac_config plugin

##### Using the Plugin
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath group: 'com.scuilion.gradle', name: 'gradle-syntastic-plugin', version: '0.+'
    }
}

apply plugin: 'syntastic-plugin'
```

* createSyntastic - for working in vim with the [syntastic](https://github.com/scrooloose/syntastic) syntastic checking plugin.

Creates a .syntastic_javac_config file in the root of the project.

