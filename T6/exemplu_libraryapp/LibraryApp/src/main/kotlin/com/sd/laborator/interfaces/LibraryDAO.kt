package com.sd.laborator.interfaces

import com.sd.laborator.model.Book

interface LibraryDAO {
    fun getBooks(): List<Book>
    fun addBook(book: Book)
    fun findAllByAuthor(author: String): List<Book>
    fun findAllByTitle(title: String): List<Book>
    fun findAllByPublisher(publisher: String): List<Book>
}