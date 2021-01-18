package microsoft.fluentui.generator.model.resources

import microsoft.fluentui.generator.AUTOGENERATED_XML_HEADER
import microsoft.fluentui.generator.global_flavorPath
import microsoft.fluentui.generator.writeToResourceFile

enum class StyleKeys(val androidKey: String) {
    TEXT_COLOR("textColor"), DRAWABLE_TINT("drawableTint"), BACKGROUND("background"), MAX_LINES("maxLines"), TEXT_ALL_CAPS("textAllCaps"),
    ELLIPSIZE("ellipsize"), GRAVITY("gravity"), CLICKABLE("clickable"), MIN_HEIGHT("minHeight"), MAX_HEIGHT("maxHeight"),
    MIN_WIDTH("minWidth"), MAX_WIDTH("maxWidth"),
    PADDING("padding"), PADDING_START("paddingStart"), PADDING_END("paddingEnd"),
    TINT("tint"), ADJUST_VIEW_BOUNDS("adjustViewBounds"), SCALE_TYPE("scaleType"), TEXT_SIZE("textSize"), TEXT_STYLE("textStyle"),
    TEXT_APPEARANCE("textAppearance")
}

data class Style(val name: String, val values: Map<StyleKeys, String>, val includeTint: Boolean = true)


fun generateStyles(name:String, styles: List<Style>) {
    StringBuilder().apply {
        append(AUTOGENERATED_XML_HEADER)
        append("<resources xmlns:android=\"http://schemas.android.com/apk/res/android\">\n")

        styles.forEach { style ->
            append("\n\t<style name=\"${style.name}\">\n")
            style.values.forEach {
                append("\t\t<item name=\"android:${it.key.androidKey}\">${it.value}</item>\n")
            }

            // Force the tint value to be the same as the text color in case this style is applied to
            // an image view
            if (style.includeTint && !style.values.containsKey(StyleKeys.TINT)) {
                append("\t\t<item name=\"android:${StyleKeys.TINT.androidKey}\">${style.values[StyleKeys.TEXT_COLOR]}</item>\n")
            }

            append("\t</style>\n")
        }
        append("</resources>")

        toString().writeToResourceFile(
            global_flavorPath.plus("res/values/${name}_styles.autogenerated.xml"),
            "${styles.count()} styles"
        )
    }
}