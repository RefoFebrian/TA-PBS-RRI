package com.refo.lelego.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

//    init {
//        addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s != null && !s.endsWith("@gmail.com")) {
//                    error = "Email harus mengandung @gmail.com"
//                } else {
//                    error = null
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
}