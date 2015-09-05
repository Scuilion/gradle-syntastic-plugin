# gradle-syntastic-plugin ![alt text](https://travis-ci.org/Scuilion/gradle-syntastic-plugin.svg?branch=master)

A gradle plugin for creating the .syntastic_javac_config file used by syntastic in VIM.

##### Using the Plugin
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.scuilion.syntastic:gradle-syntastic-plugin:0.3.3"
    }
}

plugins {
    id "org.gradle.java"
    id "org.gradle.jacoco"
}

apply plugin: "com.scuilion.syntastic"
```
Note: Plugin cannot be applied in the static 'plugins' block.
*This restriction might be removed at a later time.*

[Plugin Portal](https://plugins.gradle.org/plugin/com.scuilion.syntastic)

Task
* syntastic - for working in vim with the [syntastic](https://github.com/scrooloose/syntastic) syntastic checking plugin.

##### .vimrc Settings

```
let g:syntastic_java_checkers=['javac']
let g:syntastic_java_javac_config_file_enabled = 1
```

`syntastic_java_javac_config_file_enabled` set syntastic to read the classpath from the .syntastic_javac_config file which is expected in the current working directory.
