package com.scuilion.gradle.plugins.syntastic

import spock.lang.*

import org.junit.rules.TemporaryFolder
import org.junit.Rule
import org.gradle.testkit.runner.GradleRunner

class SubProjectSpec extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    def listOfFiles

    def buildFile
    def settingsFile
    def subProject
    def subProjectBuildFile
    def result
    def syntasticFile

    def setup() {

        buildFile = testProjectDir.newFile("build.gradle")
        settingsFile = testProjectDir.newFile("settings.gradle")
        subProject = testProjectDir.newFolder("subProject")
        subProjectBuildFile = testProjectDir.newFile("subProject/build.gradle")

        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        def pluginClasspath = pluginClasspathResource.readLines()
            .collect { it.replace('\\', '\\\\') } // escape backslashes in Windows paths
            .collect { "'$it'" }
            .join(", ")

        settingsFile << """
            include 'subProject'
            rootProject.name = 'syntastic-test'
        """

        buildFile << """
            buildscript {
                dependencies {
                    classpath files($pluginClasspath)
                }
            }
            repositories {
                mavenCentral()
            }
            apply plugin: 'java'
            apply plugin: com.scuilion.gradle.plugins.syntastic.SyntasticPlugin

            dependencies {
                compile 'com.google.guava:guava:18.0'
            }
        """

        subProjectBuildFile << """
            buildscript {
                dependencies {
                    classpath files($pluginClasspath)
                }
            }
            repositories {
                mavenCentral()
            }
            apply plugin: 'java'
            apply plugin: com.scuilion.gradle.plugins.syntastic.SyntasticPlugin

            dependencies {
                compile 'com.google.guava:guava:18.0'
            }
        """
        result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments('syntastic')
            .build()
        syntasticFile = new File(testProjectDir.root, ".syntastic_javac_config")
        listOfFiles = matcher[0][1].tokenize(File.pathSeparator)
    }

    def "the syntastic file gets created"() {
        expect:
            syntasticFile.exists()
            syntasticFile.isFile()
    }

    def "found something in the syntastic files"() {
        expect:
            matcher.find()
    }

    def "found the root level classpath in syntastic file"() {
        when:
            def classesFolder = testProjectDir.root.toString() + '/build/classes/main'
            def resourcesFolder = testProjectDir.root.toString() + '/build/resources/main'
        then:
            hasFile(classesFolder)
            hasFile(resourcesFolder)
    }

    def "found the subproject level classpath in syntastic file"() {
        when:
            def classesFolder = testProjectDir.root.toString() + '/subProject/build/classes/main'
            def resourcesFolder = testProjectDir.root.toString() + '/subProject/build/resources/main'
        then:
            hasFile(classesFolder)
            hasFile(resourcesFolder)
    }

    def hasFile(file) {
        listOfFiles.findResult { it.startsWith(file) ? it : null }
    }

    def getMatcher() {
        return syntasticFile.text =~ /"([^"]*)"/
    }
}
