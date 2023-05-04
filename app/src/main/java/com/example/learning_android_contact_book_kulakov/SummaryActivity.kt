package com.example.learning_android_contact_book_kulakov

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.learning_android_contact_book_kulakov.databinding.ActivitySummaryBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SummaryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySummaryBinding

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(AdditionalDataActivity.NAME) ?: SharedPrefs.name
        val surname = intent.getStringExtra(AdditionalDataActivity.SURNAME) ?: SharedPrefs.surname
        val patronymic = intent.getStringExtra(AdditionalDataActivity.PATRONYMIC) ?: SharedPrefs.patronymic
        val phone = intent.getStringExtra(ContactPhotoActivity.PHONE) ?: SharedPrefs.phone
        val email = intent.getStringExtra(ContactPhotoActivity.EMAIL) ?: SharedPrefs.email
        val address = intent.getStringExtra(ContactPhotoActivity.ADDRESS) ?: SharedPrefs.address

        binding.tvContactData1.text = getString(R.string.contact_data_1, name, surname, patronymic)
        binding.tvContactData2.text = getString(R.string.contact_data_2, phone, email, address)

        SharedPrefs.uri?.let {
            Glide.with(binding.ivPhoto)
                .load(it)
                .into(binding.ivPhoto)
        }

        binding.btnRegister.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnBack -> finish()
            binding.btnRegister -> register()
        }
    }

    private fun register() {
        lifecycleScope.launch(Dispatchers.IO) {
            val file = File(filesDir, "${System.currentTimeMillis()}.json")
            val contact = Contact(
                name = SharedPrefs.name.orEmpty(),
                surname = SharedPrefs.surname.orEmpty(),
                patronymic = SharedPrefs.patronymic.orEmpty(),
                phone = SharedPrefs.phone.orEmpty(),
                email = SharedPrefs.email.orEmpty(),
                address = SharedPrefs.address.orEmpty(),
                imageUri = SharedPrefs.uri.orEmpty(),
            )
            val json = gson.toJson(contact)
            file.createNewFile()
            file.writeText(json)
            SharedPrefs.clear()
            withContext(Dispatchers.Main) {
                val intent = Intent(this@SummaryActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val URI = "URI"

        fun startActivity(
            context: Context,
            name: String,
            surname: String,
            patronymic: String,
            phone: String,
            email: String,
            address: String,
            uri: String,
        ) {
            val intent = Intent(context, SummaryActivity::class.java)
                .putExtra(AdditionalDataActivity.NAME, name)
                .putExtra(AdditionalDataActivity.SURNAME, surname)
                .putExtra(AdditionalDataActivity.PATRONYMIC, patronymic)
                .putExtra(ContactPhotoActivity.PHONE, phone)
                .putExtra(ContactPhotoActivity.EMAIL, email)
                .putExtra(ContactPhotoActivity.ADDRESS, address)
                .putExtra(URI, uri)
            context.startActivity(intent)
        }
    }

}