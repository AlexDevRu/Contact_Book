package com.example.learning_android_contact_book_kulakov

data class Contact(
    val created: Long,
    val name: String,
    val surname: String,
    val patronymic: String,
    val phone: String,
    val email: String,
    val link: String,
    val imageUri: String
) {
    fun getFullName() = "$name $surname $patronymic"
}
