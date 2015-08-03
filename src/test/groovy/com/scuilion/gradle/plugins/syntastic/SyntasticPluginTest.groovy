package com.scuilion.gradle.plugins.syntastic

import static org.junit.Assume.assumeFalse
import static org.junit.Assume.assumeTrue

import org.apache.commons.lang3.SystemUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class SyntasticPluginTest {

    private Project project

    @Before
    void setupProject() {
        project = ProjectBuilder.builder().build()
    }

    private Syntastic extractTask() {
        def task = project.tasks.syntastic
        assert task instanceof Syntastic
        assert task.output != null
        assert task.classpath != null
        task
    }

    @Test
    void nothingToDo() {
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = extractTask()
        assert task.classpath.files.empty
    }

    @Test
    void javaProject() {
        project.pluginManager.apply 'java'
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = extractTask()
        assert !task.classpath.files.empty
    }

    @Test
    void groovyProject() {
        project.pluginManager.apply 'groovy'
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = extractTask()
        assert !task.classpath.files.empty
    }

    @Test
    void windowsSinglePath() {
        assumeTrue SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('a')
        }
        assert !task.value.contains('/')
        assert !task.value.contains(';')
    }

    @Test
    void nixSinglePath() {
        assumeFalse SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('a')
        }
        assert !task.value.contains('\\')
        assert !task.value.contains(';')
    }

    @Test
    void windowsSeveralPaths() {
        assumeTrue SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('a', 'b', 'c')
        }
        assert !task.value.contains('/')
        assert  task.value.chars.findAll { "$it" == ";" }.size == 2
    }

    @Test
    void nixSeveralPaths() {
        assumeFalse SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('a', 'b', 'c')
        }
        assert !task.value.contains('\\')
        assert !task.value.contains(';')
        assert  task.value.chars.findAll { "$it" == ":" }.size == 2
    }

    @Test
    void windowsDeduplicatePaths() {
        assumeTrue SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('A', 'C', 'D', 'C')
        }
        assert !task.value.contains('/')
        assert  task.value.chars.findAll { "$it" == ";" }.size == 2
    }

    @Test
    void nixDeduplicatePaths() {
        assumeFalse SystemUtils.IS_OS_WINDOWS
        project.pluginManager.apply 'com.scuilion.syntastic'

        def task = project.tasks.syntastic
        task.resolver {
            project.files('A', 'C', 'D', 'C')
        }
        assert !task.value.contains('\\')
        assert !task.value.contains(';')
        assert  task.value.chars.findAll { "$it" == ":" }.size == 2
    }
}
