package com.scuilion.gradle.plugins.syntastic

import spock.lang.*

import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import org.gradle.tooling.GradleConnectionException
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.tooling.GradleConnector
import org.apache.commons.lang3.SystemUtils

class SyntasticSpec extends Specification {

    @Rule
    def TemporaryFolder testProjectDir = new TemporaryFolder();

    def listOfFiles

    def buildFile
    def result
    def syntasticFile

    def setup() {

        buildFile = testProjectDir.newFile("build.gradle");

        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        def pluginClasspath = pluginClasspathResource.readLines()
            .collect { it.replace('\\', '\\\\') } // escape backslashes in Windows paths
            .collect { "'$it'" }
            .join(", ")

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

    @Requires({!SystemUtils.IS_OS_WINDOWS})
    def "the file has the correct includes, *nix version"() {
        expect:
            !listOfFiles.empty
            hasGuavaFile()
            !syntasticFile.text.contains("\\")
            !syntasticFile.text.contains(';')
    }

    @Requires({SystemUtils.IS_OS_WINDOWS})
    def "the file has the correct includes, windows version"() {
        expect:
            !listOfFiles.empty
            hasGuavaFile()
            !syntasticFile.text.contains('/')
            syntasticFile.text.contains(';')
    }

    def "found something in the syntastic files"() {
        expect:
            matcher.find()
    }

    def hasGuavaFile() {
        def exists = false
        listOfFiles.each {
            if (it.contains('guava')) {
                exists =
                    new File(it).exists() && new File(it).isFile()
            }
        }
        return exists
    }

    def getMatcher() {
        return syntasticFile.text =~ /"([^"]*)"/
    }
}
