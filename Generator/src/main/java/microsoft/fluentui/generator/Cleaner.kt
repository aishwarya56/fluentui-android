package microsoft.fluentui.generator

import java.io.File

// Deletes all autogenerated files

fun cleaner() {
    File("$global_projectRoot/generatedSrc").deleteRecursively()
    println("> Deleted generatedSrc folder")
}