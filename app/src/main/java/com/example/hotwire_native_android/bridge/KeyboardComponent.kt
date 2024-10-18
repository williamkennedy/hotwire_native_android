package com.example.hotwire_native_android.bridge


import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import com.example.hotwire_native_android.R
import com.google.android.material.button.MaterialButton
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination


class KeyboardComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>,
) : BridgeComponent<HotwireDestination>(name, delegate) {

    private val fragment: Fragment
        get() = delegate.destination.fragment

    private val bottomToolbar: LinearLayout?
        get() = fragment.view?.findViewById(R.id.bottom_toolbar)


    private var rootView: ViewGroup? = null
        get() = fragment.view as? ViewGroup
    private var hidden = true
    private var built = false



    override fun onReceive(message: Message) {
        buildKeyboard()
        when (message.event) {
            "focus" -> handleFocusEvent()
            else -> Log.w("KeyboardComponent", "Unknown event for message: $message")
        }
    }

    private fun handleFocusEvent() {
        toggleKeyboard()
    }

    private fun buildKeyboard() {
        if (rootView == null) {
            Log.w("KeyboardComponent", "Rootview NULL")
            return
        }

        // Add buttons to the toolbar
        if (!built) {
            hideToolbarButton()
            addButton("H1", "heading1",R.drawable.format_h1_24px)
            addButton("B", "bold", R.drawable.format_bold_24px)
            addButton("I", "italic", R.drawable.format_italic_24px)
            built = true
        } else {
            built = true
        }
    }

    private fun addButton(text: String, eventName: String, iconRedId: Int?) {

        val btn = rootView?.context?.let {
            MaterialButton(it).apply {

                setOnClickListener { replyTo(eventName) }

                iconRedId?.let {
                    icon = rootView?.context?.let { it1 -> ContextCompat.getDrawable(it1, it) }
                    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
                }

            }
        }
        bottomToolbar?.addView(btn)
    }

    private fun showToolbarButton() {
        // Display the toolbar
        bottomToolbar?.visibility = View.VISIBLE
    }

    private fun hideToolbarButton() {
        // Hide the toolbar when it's not needed
        bottomToolbar?.visibility = View.GONE
    }

    private fun toggleKeyboard() {
        hidden = !hidden

        if (hidden) {
            hideToolbarButton()
        } else {
            showToolbarButton()
        }
    }


}
