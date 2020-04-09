package com.sd.laborator.interfaces

import com.sd.laborator.model.Book

interface LibraryPrinter {
    fun printHTML(books: List<Book>): String
    fun printJSON(books: List<Book>): String
    fun printRaw(books: List<Book>): String
}