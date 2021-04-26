package com.microsoft.fluentui.vNext.widget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.microsoft.fluentui.generator.ButtonSize
import com.microsoft.fluentui.generator.ButtonStyle
import com.microsoft.fluentui.util.FieldUpdateListener

class Button : IButton {
    var fieldUpdateListener: FieldUpdateListener? = null

    override var buttonIconDrawable: Drawable? = null
        set(value) {
            if (field == value)
                return
            field = value
            fieldUpdateListener?.onFieldUpdated()
        }
    override var buttonStyle: ButtonStyle? = ButtonStyle.PRIMARY
        set(value) {
            if (field == value)
                return
            field = value
            fieldUpdateListener?.onFieldUpdated()
        }
    override var buttonSize: ButtonSize? = ButtonSize.SMALL
        set(value) {
            if (field == value)
                return
            field = value
            fieldUpdateListener?.onFieldUpdated()
        }
}