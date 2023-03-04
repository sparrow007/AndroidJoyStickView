package com.jackandphantom.joystickdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jackandphantom.joystickdemo.databinding.ActivityMainBinding
import com.jackandphantom.joystickview.OnMoveListener

class MainActivity : AppCompatActivity(), OnMoveListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        setContentView(viewRoot)

        binding.joystick.setOnMoveListener(this)
        binding.joystick.setOnClickListener {
            Log.d("MainActivity", "setOnClickListener")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMove(angle: Double, strength: Float) {
        binding.text.text  = "angle=${angle.toInt()} strength=${strength.toInt()}"
        Log.d("MainActivity", "${angle.toInt()} ${strength.toInt()}")
    }

    @SuppressLint("SetTextI18n")
    override fun onDoubleClick() {
        binding.text.text  = "onDoubleClick"
        Log.d("MainActivity", "onDoubleClick")
    }
}