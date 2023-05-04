package com.example.learning_android_contact_book_kulakov.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.learning_android_contact_book_kulakov.Contact
import com.example.learning_android_contact_book_kulakov.databinding.ListItemContactBinding

class ContactAdapter(
    private val listener: Listener
): ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.created == newItem.created
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {
        fun onItemClick(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ListItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(
        private val binding: ListItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var contact: Contact? = null

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(contact: Contact) {
            this.contact = contact
            binding.tvFullName.text = contact.getFullName()
        }

        override fun onClick(p0: View?) {
            contact?.let { listener.onItemClick(it) }
        }

    }
}