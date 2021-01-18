package microsoft.fluentui.generator.model.proxies

import microsoft.fluentui.generator.DocumentType
import microsoft.fluentui.generator.global_documentType
import microsoft.fluentui.generator.model.AttributeType
import microsoft.fluentui.generator.model.resources.*

class Button(val type: Type = Type.Default()) : AppearanceProxy() {

    sealed class Type(val apName: String) {
        class Default : Type("ButtonTokens")
        class Primary : Type("PrimaryButtonTokens")
        class Secondary : Type("SecondaryButtonTokens")
        class Ghost : Type("GhostButtonTokens")
    }

    override val stylesheetName: String
        get() = type.apName

    override val viewName: String
        get() = super.viewName + "View"

    override fun setupGeneratorParameters(parameters: LinkedHashMap<String, Any>) {
        val apNameLowerCase = viewName.toLowerCase()

        // Generate all color selectors
        parameters.forEach {
            if (isColorSelector(it.value)) {
                generateColorSelector(apNameLowerCase + "_" + it.key, it.value as ArrayList<*>)
            }
        }

        // Generate Shapes and Styles only for the parent, children stylesheet will simply override the resources values
        if (global_documentType == DocumentType.ROOT) {
            listOf("small", "medium", "large").forEach { apStyle ->
                // Generate background shapes
                mutableMapOf<ShapeKeys, String>().let { attributes ->
                    parameters["backgroundColor"]?.let {
                        attributes[ShapeKeys.BACKGROUND_COLOR] = "${apNameLowerCase}_backgroundColor"
                    }
                    parameters["borderColor"]?.let {
                        attributes[ShapeKeys.STROKE_COLOR] = "${apNameLowerCase}_borderColor"
                    }
                    parameters["borderSize"]?.let {
                        attributes[ShapeKeys.STROKE_WIDTH] = "${apNameLowerCase}_borderSize_${apStyle}"
                    }
                    parameters["borderRadius"]?.let {
                        attributes[ShapeKeys.STROKE_RADIUS] = "${apNameLowerCase}_borderRadius_${apStyle}"
                    }
                    attributes[ShapeKeys.SHAPE] = "@integer/${apNameLowerCase}_backgroundShape"

                    // FIXME: Ripple for transparent buttons?
                    attributes[ShapeKeys.RIPPLE_COLOR] = "@color/${apNameLowerCase}_backgroundColor_pressed"

                    generateShape("${apNameLowerCase}_${apStyle}background", attributes)
                }
            }
        }

        // Force a large corner radius when "rounded" is true.
        if (parameters["rounded"] == true) {
            parameters["cornerRadius"] = "999dp"
        }
    }

    override fun setupCustomAttributes(): List<CustomAttribute> {
        val attrsList = mutableListOf<CustomAttribute>()
        if (type is Type.Default) {
            attrsList.addAll(listOf(
                CustomAttribute("buttonSize", AttributeType.ENUM, propertyReference = "size"),
                CustomAttribute("buttonStyle", AttributeType.ENUM, propertyReference = "style"),
                CustomAttribute("buttonIcon", AttributeType.REFERENCE, propertyReference = "icon")
            ))
        }

        return attrsList
    }

    override fun setupCustomResources(parameters: LinkedHashMap<String, Any>): List<Resource> {
        // On android background shapes can be represented as an integer.
        // Rectangle = 0 and Oval = 1.
        return listOf(
            Resource(
                "backgroundShape", when (parameters["hasEqualDimensions"] == true && parameters["rounded"] == true) {
                    true -> "1"
                    else -> "0"
                }, ResourceType.INTEGER
            )
        )
    }
}