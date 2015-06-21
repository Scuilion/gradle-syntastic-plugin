package com.scuilion.gradle.plugins.syntastic

import org.gradle.api.Project
import org.gradle.api.Plugin

class SyntasticPlugin implements Plugin<Project> {

    private static final String GROUP = 'Syntastic'
    private static final String DESCRIPTION = 'A plugin integration for Syntastic'
    private static final String SYNTASTIC_TASK_NAME = 'createSyntastic'

    void apply(Project project) {
        addTask(project)
    }

    private static void addTask(Project project) {
        project.task(SYNTASTIC_TASK_NAME) {
            def classpathFiles = [] as Set
            def addJars = { proj ->
                addJarDeps(proj, classpathFiles)
                addSrcDirs(proj, classpathFiles)
            }

            project.childProjects.each { proj ->
                addJars(proj.value)
            }
            addJars(project.rootProject)
            def outputFile = project.file(project.rootProject.projectDir.absolutePath + "/.syntastic_javac_config")
            inputs.property "classpath", getClassPathListed(classpathFiles)
            outputs.file outputFile
            doLast {
                outputFile.write ''
                outputFile.write inputs.properties.classpath
            }
        }

        attachTo(project, 'compileJava')
        attachTo(project, 'compileGroovy')
        project.tasks.createSyntastic.group = GROUP
        project.tasks.createSyntastic.description = DESCRIPTION
    }

    static private void attachTo(Project project, String lang) {
        if (project.tasks.findByName(lang)) {
            project.tasks.getByName(lang).dependsOn(SYNTASTIC_TASK_NAME)
        }
    }

    static private String getClassPathListed(def classpathFiles) {
        //def classpathList = getUnifiedPath(classpathFiles)  assume the path separator is correct.
        return 'let g:syntastic_java_javac_classpath = "' + classpathFiles.join(joinCharacter) + '"'
    }

    static private String getJoinCharacter() {
        return File.pathSeparator
    }

    static private void addSrcDirs(project, classpathFiles) {
        if (project.hasProperty('sourceSets')) {
            project.sourceSets.each { srcSet ->
                srcSet.java.srcDirs.each { dir ->
                    classpathFiles.add(new File(dir.absolutePath))
                }
            }
        }
    }

    static private void addJarDeps(Project project, Set<String> classpathFiles) {
        project.configurations.each { conf ->
            conf.each { jar ->
                classpathFiles.add(jar)
            }
        }
    }
}
