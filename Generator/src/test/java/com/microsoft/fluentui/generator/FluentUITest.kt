package com.microsoft.fluentui.generator

import com.microsoft.fluentui.generator.model.FluentUIDocument
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.yaml.snakeyaml.Yaml
import java.io.File


val testProjectPath = "sandbox"
class FluentUITest {

    @Before
    fun setup() {
        global_documentType = DocumentType.REGULAR
        global_projectRoot = testProjectPath
    }

    @After
    fun tearDown() {
        File(testProjectPath).deleteRecursively()
    }

    @Test
    fun `Flavors folder structure`() {
        testFlavors(listOf("main"))
        testFlavors(listOf("main", "skype", "teams"))
    }


    private fun testFlavors(flavorList: List<String>) {
        val document = "Color:\n black: \"#000000\""

        flavorList.forEach {
            configFlavor(it)
            val doc = Yaml().loadAs(document, FluentUIDocument::class.java)
            doc.generate("it.path")
            val outputfile = File(global_flavorPath +"/res/values/colors_res.autogenerated.xml")
            Assert.assertTrue(outputfile.exists())
        }
    }

}