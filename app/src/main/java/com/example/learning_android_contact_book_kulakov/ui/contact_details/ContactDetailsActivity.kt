package com.example.learning_android_contact_book_kulakov.ui.contact_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.learning_android_contact_book_kulakov.R
import com.example.learning_android_contact_book_kulakov.databinding.ActivityContactDetailsBinding

class ContactDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityContactDetailsBinding

    private val viewModel by viewModels<ContactDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPhone.setOnClickListener(this)
        binding.tvEmail.setOnClickListener(this)
        binding.tvLink.setOnClickListener(this)

        observe()
    }

    private fun observe() {
        viewModel.contact.observe(this) {
            binding.tvNames.text = getString(R.string.contact_data_1, it.name, it.surname, it.patronymic)
            Glide.with(binding.ivPhoto)
                .load(it.imageUri)
                .error(R.drawable.person_black_24dp)
                .into(binding.ivPhoto)
            binding.tvPhone.text = it.phone
            binding.tvEmail.text = it.email
            binding.tvLink.text = it.link
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvPhone -> openDialer()
            binding.tvEmail -> openMailApp()
            binding.tvLink -> openLink()
        }
    }

    private fun openDialer() {
        val phoneNumber = viewModel.contact.value?.phone ?: return
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    private fun openMailApp() {
        val email = viewModel.contact.value?.email ?: return
        val uri = Uri.parse("mailto:$email")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }

    private fun openLink() {
        val link = viewModel.contact.value?.link ?: return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    companion object {
        const val CREATED = "CREATED"

        fun startActivity(context: Context, created: Long) {
            val intent = Intent(context, ContactDetailsActivity::class.java)
                .putExtra(CREATED, created)
            context.startActivity(intent)
        }
    }
}