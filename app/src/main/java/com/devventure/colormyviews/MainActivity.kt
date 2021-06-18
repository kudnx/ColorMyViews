package com.devventure.colormyviews

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.devventure.colormyviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private var pincelColor = R.color.grey
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("colors", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val boxes = arrayOf(binding.boxOne, binding.boxTwo, binding.boxThree,
                            binding.boxFour, binding.boxFive)

        for (box in boxes)
        {
            findViewById<View>(box.id).setBackgroundResource(getColorBox(box.id.toString()))
        }

    }

    override fun onStop() {
        super.onStop()
        editor.apply()
    }

    fun setColor(view: View) {
        when(view.id){
            R.id.btn_red    ->  pincelColor = R.color.red
            R.id.btn_yellow ->  pincelColor = R.color.yellow
            R.id.btn_green  ->  pincelColor = R.color.green
        }
    }

    fun colorBox(view: View) {
        when(view.id){
            R.id.boxOne -> {
                binding.boxOne.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxOne.id.toString(),   pincelColor)
            }

            R.id.boxTwo -> {
                binding.boxTwo.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxTwo.id.toString(),   pincelColor)
            }

            R.id.boxThree -> {
                binding.boxThree.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxThree.id.toString(), pincelColor)
            }

            R.id.boxFour -> {
                binding.boxFour.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxFour.id.toString(),  pincelColor)
            }

            R.id.boxFive -> {
                binding.boxFive.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxFive.id.toString(),  pincelColor)
            }
        }
    }

    private fun getColorBox(idBox : String) : Int{
        return sharedPreferences.getInt(idBox, R.color.grey)
    }

}