package com.example.learning_android_contact_book_kulakov

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.learning_android_contact_book_kulakov.databinding.ActivityAdditionalDataBinding

class AdditionalDataActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAdditionalDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME) ?: SharedPrefs.name
        val surname = intent.getStringExtra(SURNAME) ?: SharedPrefs.surname
        val patronymic = intent.getStringExtra(PATRONYMIC) ?: SharedPrefs.patronymic
        binding.tvContactData.text = getString(R.string.contact_data_1, name, surname, patronymic)

        binding.etPhone.setText(SharedPrefs.phone)
        binding.etEmail.setText(SharedPrefs.email)
        binding.etAddress.setText(SharedPrefs.address)

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        val phone = binding.etPhone.text?.toString().orEmpty()
        val email = binding.etEmail.text?.toString().orEmpty()
        val address = binding.etAddress.text?.toString().orEmpty()
        SharedPrefs.phone = phone
        SharedPrefs.email = email
        SharedPrefs.address = address
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnBack -> finish()
            binding.btnNext -> goNext()
        }
    }

    private fun goNext() {
        val name = SharedPrefs.name.orEmpty()
        val surname = SharedPrefs.surname.orEmpty()
        val patronymic = SharedPrefs.patronymic.orEmpty()
        val phone = binding.etPhone.text?.toString().orEmpty()
        val email = binding.etEmail.text?.toString().orEmpty()
        val address = binding.etAddress.text?.toString().orEmpty()
        ContactPhotoActivity.startActivity(this, name, surname, patronymic, phone, email, address)
    }

    companion object {
        const val NAME = "NAME"
        const val SURNAME = "SURNAME"
        const val PATRONYMIC = "PATRONYMIC"

        fun startActivity(context: Context, name: String, surname: String, patronymic: String) {
            val intent = Intent(context, AdditionalDataActivity::class.java)
                .putExtra(NAME, name)
                .putExtra(SURNAME, surname)
                .putExtra(PATRONYMIC, patronymic)
            context.startActivity(intent)
        }
    }

}