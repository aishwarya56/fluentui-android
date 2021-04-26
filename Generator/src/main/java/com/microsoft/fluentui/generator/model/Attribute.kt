package com.microsoft.fluentui.generator.model

import com.microsoft.fluentui.generator.ATTRIBUTE_PREFIX
import com.microsoft.fluentui.generator.AUTOGENERATED_XML_HEADER
import com.microsoft.fluentui.generator.printError
import com.microsoft.fluentui.generator.writeToResourceFile

enum class AttributeType(val androidType: String) {
    NONE(""),

    BOOLEAN("boolean"),
    COLOR("color"),
    DIMENSION("dimension"),
    ENUM("enum"),
    FLAG("flag"),
    FLOAT("float"),
    INTEGER("integer"),
    REFERENCE("reference"),
    STRING("string")
}

data class Attribute(val name: String, val type: AttributeType, val values: Map<String, Int>? = null) {}

fun generateAttributes(flavorPath:String, name: String, attributes: List<Attribute>) {
    if (attributes.isEmpty()) {
        println("Empty attributes list for \"$name\"")
    } else {
        StringBuilder().apply {
            append(AUTOGENERATED_XML_HEADER)
            append("<resources>\n")
            append("\t<declare-styleable name=\"$name\">\n")

            attributes.forEach {
                if (it.name.startsWith(ATTRIBUTE_PREFIX)) {
                    printError("Attribute ${it.name} already contains the attribute prefix")
                } else {
                    val fluentUIAttributeName = "$ATTRIBUTE_PREFIX${it.name}"
                    when (it.type) {
                        AttributeType.ENUM, AttributeType.FLAG -> {
                            val type = it.type.androidType
                            it.values?.let { enumVal ->
                                append("\t\t<attr name=\"$fluentUIAttributeName\" ${if (it.type == AttributeType.ENUM) "format=\"enum\"" else ""}>\n")
                                enumVal.forEach { append("\t\t\t<$type name=\"${
                                it.key}\" value=\"${it.value}\"/>\n") }
                                append("\t\t</attr>\n")
                            } ?: printError("No enum values specified for enum ${it.name}")
                        }
                        AttributeType.NONE -> append("\t\t<attr name=\"$fluentUIAttributeName\"/>\n")
                        else -> append("\t\t<attr name=\"$fluentUIAttributeName\" format=\"${it.type.androidType}\"/>\n")
                    }
                }
            }

            append("\t</declare-styleable>\n")
            append("</resources>")

            // ideally we would like to group all the styles into a single resource file, right now we're generating
            // one file per style
            toString().writeToResourceFile(
                flavorPath.plus("res/values/${name.toLowerCase()}_attrs.autogenerated.xml"),
                "${attributes.count()} attributes"
            )
        }
    }
}
