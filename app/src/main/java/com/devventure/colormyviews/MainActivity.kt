package com.devventure.colormyviews

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.devventure.colormyviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var color = R.color.grey
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("colors", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.boxOne.setBackgroundResource(getColorBox("boxOne"))
        binding.boxTwo.setBackgroundResource(getColorBox("boxTwo"))
        binding.boxThree.setBackgroundResource(getColorBox("boxThree"))
        binding.boxFour.setBackgroundResource(getColorBox("boxFour"))
        binding.boxFive.setBackgroundResource(getColorBox("boxFive"))

    }

    override fun onStop() {
        super.onStop()
        editor.apply()
    }

    fun setColor(view: View) {
        when(view.id){
            R.id.btn_red    ->  color = R.color.red
            R.id.btn_yellow ->  color = R.color.yellow
            R.id.btn_green  ->  color = R.color.green
        }
    }

    fun colorBox(view: View) {
        when(view.id){
            R.id.boxOne -> {
                binding.boxOne.setBackgroundResource(color)
                editor.putInt("boxOne",   color)
            }

            R.id.boxTwo -> {
                binding.boxTwo.setBackgroundResource(color)
                editor.putInt("boxTwo",   color)
            }

            R.id.boxThree -> {
                binding.boxThree.setBackgroundResource(color)
                editor.putInt("boxThree", color)
            }

            R.id.boxFour -> {
                binding.boxFour.setBackgroundResource(color)
                editor.putInt("boxFour",  color)
            }

            R.id.boxFive -> {
                binding.boxFive.setBackgroundResource(color)
                editor.putInt("boxFive",  color)
            }
        }
    }

    private fun getColorBox(box : String) : Int{
        return sharedPreferences.getInt(box, R.color.grey)
    }

}