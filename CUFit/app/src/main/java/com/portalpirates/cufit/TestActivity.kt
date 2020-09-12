package com.portalpirates.cufit

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.portalpirates.cufit.ui.view.FitButton


class TestActivity : FitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)

        val btn = findViewById<FitButton>(R.id.btn31)
        btn.setOnClickListener {
            val tv = btn.findViewById<TextView>(R.id.btn_text_view)
            Toast.makeText(this, "${tv.isPressed}", Toast.LENGTH_SHORT).show()
        }
    }
}