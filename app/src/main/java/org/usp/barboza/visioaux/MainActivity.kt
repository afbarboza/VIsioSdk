package org.usp.barboza.visioaux

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : AppCompatActivity() {

    private lateinit var addButton: ImageView
    private lateinit var subtractButton: ImageView
    private lateinit var counter: TextView
    private lateinit var materialSwitch: SwitchMaterial
    private lateinit var tvTitle: TextView

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       initWidgets()
    }

    private fun initWidgets() {
        counter = findViewById(R.id.countTV)
        tvTitle = findViewById(R.id.tvTitle)

        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            count += 1
            counter.text = count.toString()
        }

        subtractButton = findViewById(R.id.subtract_button)
        subtractButton.setOnClickListener {
            count -= 1
            counter.text = count.toString()
        }

        materialSwitch = findViewById(R.id.material_switch)
        materialSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                tvTitle.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                tvTitle.setTextColor(Color.parseColor("#000000"))
            }
        }
    }
}