package com.scuilion.gradle.plugins.syntastic

import spock.lang.*

import org.apache.commons.lang3.SystemUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class SyntasticPluginTest extends Specification {

    private Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    private Syntastic extractTask() {
        def task = project.tasks.syntastic
        assert task instanceof Syntastic
        assert task.output != null
        assert task.classpath != null
        task
    }

    def "there is nothing to do"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = extractTask()

        then:
            task.classpath.files.empty
    }

    def "it works on a java project"() {
        given:
            project.pluginManager.apply 'java'
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = extractTask()

        then:
            !task.classpath.files.empty
    }

    def "it works on a groovy project"() {
        given:
            project.pluginManager.apply 'groovy'
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = extractTask()

        then:
            !task.classpath.files.empty
    }

    @Requires({ SystemUtils.IS_OS_WINDOWS })
    def "check for the resolver on windows system"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('a')
            }

        then:
            !task.value.contains('/')
            !task.value.contains(';')
    }

    @Requires({ !SystemUtils.IS_OS_WINDOWS })
    def "check for the resolver on linux system"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('a')
            }

        then:
            !task.value.contains('\\')
            !task.value.contains(';')
    }

    @Requires({ SystemUtils.IS_OS_WINDOWS })
    def "check several paths on windows"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('a', 'b', 'c')
            }

        then:
            !task.value.contains('/')
            task.value.chars.findAll { "$it" == ";" }.size == 2
    }

    @Requires({ !SystemUtils.IS_OS_WINDOWS })
    def "check several paths on nix"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('a', 'b', 'c')
            }

        then:
            !task.value.contains('\\')
            !task.value.contains(';')
            task.value.chars.findAll { "$it" == ":" }.size == 2
    }

    @Requires({ SystemUtils.IS_OS_WINDOWS })
    def "check that duplicate paths were removed on windows systems"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('A', 'C', 'D', 'C')
            }

        then:
            !task.value.contains('/')
            task.value.chars.findAll { "$it" == ";" }.size == 2
    }

    @Requires({ !SystemUtils.IS_OS_WINDOWS })
    def "check that duplicate paths were removed on nix systems"() {
        given:
            project.pluginManager.apply 'com.scuilion.syntastic'

        when:
            def task = project.tasks.syntastic
            task.resolver {
                project.files('A', 'C', 'D', 'C')
            }

        then:
            !task.value.contains('\\')
            !task.value.contains(';')
            task.value.chars.findAll { "$it" == ":" }.size == 2
    }
}
