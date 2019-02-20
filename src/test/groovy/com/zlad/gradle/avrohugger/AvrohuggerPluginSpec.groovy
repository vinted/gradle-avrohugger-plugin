package com.zlad.gradle.avrohugger

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


class AvrohuggerPluginSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    def "should be applied with default source directories"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.avrohugger.sourceDirectories.files == project.files('src/main/avro').files
    }

    def "should be applied with default destination directory"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.avrohugger.destinationDirectory.get().getAsFile() == new File("${project.buildDir}/generated-src/avro")
    }

    def "should be applied with default type mapping"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.avrohugger.typeMapping.get() == new CustomTypes()
    }

    def "should be applied with default empty namespace mapping"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.avrohugger.namespaceMapping.get() == [:]
    }

    def "should be applied with default standard source type"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.avrohugger.sourceFormat.get() == SourceFormatValues.Standard
    }

    def "should change default enum type to java enum when applied with specific record source format"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin
        project.avrohugger.sourceFormat = SourceFormatValues.SpecificRecord

        then:
        project.tasks.avroScalaGenerate.customTypes.get().enum() == CustomTypesValues.JavaEnum
    }

    def "should change default array type to scala array when applied with scavro source format"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin
        project.avrohugger.sourceFormat = SourceFormatValues.Scavro

        then:
        project.tasks.avroScalaGenerate.customTypes.get().array() == CustomTypesValues.ScalaArray
    }

    def "should add avro scala generate task to project"() {
        when:
        project.pluginManager.apply AvrohuggerPlugin

        then:
        project.tasks.collect { it.name }.contains('avroScalaGenerate')
    }
    
}
