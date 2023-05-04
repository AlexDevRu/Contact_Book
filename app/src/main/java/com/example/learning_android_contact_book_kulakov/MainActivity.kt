package com.example.learning_android_contact_book_kulakov

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.learning_android_contact_book_kulakov.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener(this)

        binding.etName.setText(SharedPrefs.name)
        binding.etSurname.setText(SharedPrefs.surname)
        binding.etPatronymic.setText(SharedPrefs.patronymic)
    }

    override fun onStop() {
        super.onStop()
        val name = binding.etName.text?.toString().orEmpty()
        val surname = binding.etSurname.text?.toString().orEmpty()
        val patronymic = binding.etPatronymic.text?.toString().orEmpty()
        SharedPrefs.name = name
        SharedPrefs.surname = surname
        SharedPrefs.patronymic = patronymic
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnNext -> goNext()
        }
    }

    private fun goNext() {
        val name = binding.etName.text?.toString().orEmpty()
        val surname = binding.etSurname.text?.toString().orEmpty()
        val patronymic = binding.etPatronymic.text?.toString().orEmpty()
        AdditionalDataActivity.startActivity(this, name, surname, patronymic)
    }
}