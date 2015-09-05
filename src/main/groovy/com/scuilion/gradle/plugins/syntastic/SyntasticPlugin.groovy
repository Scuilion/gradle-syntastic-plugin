package com.scuilion.gradle.plugins.syntastic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

class SyntasticPlugin implements Plugin<Project> {

    static final TASK_NAME = 'syntastic'

    void apply(Project project) {
        def task = project.tasks.create(TASK_NAME, Syntastic)

        task.with {
            group = 'vim'
            description = 'Generate Syntastic Java configuration'

            resolver { // lazy evaluation
                def cp = project.files()

                project.plugins.withType(JavaBasePlugin) {
                    project?.sourceSets.all {
                        cp += it.output + it.compileClasspath
                    }
                }

                cp
            }
        }
        project.rootProject.tasks.matching { it.name == 'build' }.all { dependsOn TASK_NAME }
    }

}
