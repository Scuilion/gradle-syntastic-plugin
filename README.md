# gradle-syntastic-plugin https://travis-ci.org/Scuilion/gradle-syntastic-plugin.svg?branch=master

A gradle plugin for creating the .syntastic_javac_config file used by syntastic in VIM.

##### Using the Plugin
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.scuilion.syntastic:gradle-syntastic-plugin:0.3.2"
  }
}

apply plugin: "com.scuilion.syntastic"
```

Task
* createSyntastic - for working in vim with the [syntastic](https://github.com/scrooloose/syntastic) syntastic checking plugin.

This plugin attaches the createSyntastic plugin to 'compileJava' and 'compileGroovy' tasks. It must be applied after your configure you project.
