package com.scuilion.gradle.plugins.syntastic

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

class Syntastic extends DefaultTask {

    private Closure resolver

    @InputFiles
    @SkipWhenEmpty
    FileCollection getClasspath() {
        resolver.call()
    }

    void setResolver(Closure closure) {
        resolver = closure
    }

    @OutputFile
    File output = project.file('.syntastic_javac_config')

    String getValue() {
        classpath.files.join File.pathSeparator
    }

    @TaskAction
    void generate() {
        output.withWriter {
            it.write "let g:syntastic_java_javac_classpath = \"${value}\"\n"
        }
    }

}
