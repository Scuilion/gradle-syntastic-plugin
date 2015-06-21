# gradle-syntastic-plugin

A gradle plugin for creating the .syntastic_javac_config file used by syntastic in VIM.

##### Using the Plugin
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath group: 'com.scuilion', name: 'syntastic', version: '0.+'
    }
}

apply plugin: 'syntastic'
```

Task
* createSyntastic - for working in vim with the [syntastic](https://github.com/scrooloose/syntastic) syntastic checking plugin.

This plugin attaches the createSyntastic plugin to 'compileJava' and 'compileGroovy' tasks.
