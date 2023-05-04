package com.example.learning_android_contact_book_kulakov.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.learning_android_contact_book_kulakov.Contact
import com.example.learning_android_contact_book_kulakov.databinding.ActivityMainBinding
import com.example.learning_android_contact_book_kulakov.ui.adapters.ContactAdapter
import com.example.learning_android_contact_book_kulakov.ui.contact_details.ContactDetailsActivity
import com.example.learning_android_contact_book_kulakov.ui.register.NamesActivity

class MainActivity : AppCompatActivity(), ContactAdapter.Listener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val contactAdapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContacts.adapter = contactAdapter
        binding.btnRegister.setOnClickListener(this)

        observe()
    }

    private fun observe() {
        viewModel.contacts.observe(this) {
            contactAdapter.submitList(it)
        }
    }

    override fun onItemClick(contact: Contact) {
        ContactDetailsActivity.startActivity(this, contact.created)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(this, NamesActivity::class.java)
        startActivity(intent)
    }

}