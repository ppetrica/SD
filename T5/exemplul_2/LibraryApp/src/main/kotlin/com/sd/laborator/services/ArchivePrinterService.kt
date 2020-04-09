package com.sd.laborator.services

import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import org.springframework.stereotype.Service


fun toHTML(book: Book) =
    "<p>Name: ${book.name}<br/>" +
    "Author: ${book.author}<br/>" +
    "Publisher: ${book.publisher}</p>" +
    "<pre>${book.content}</pre><br/>"


fun toJSON(book: Book) =
    "{" +
    "    \"title\": \"${book.name}\",\n" +
    "    \"author\":\"${book.author}\",\n" +
    "    \"publisher\":\"${book.publisher}\"\n" +
    "    \"content\":\"${book.content}\"\n" +
    "}"


@Service
class ArchivePrinterService: LibraryPrinter() {
    override fun printHTML(books: Set<Book>): String {
        val header = "<html><head><title>Archive</title></head><body>"
        val content = books.joinToString(transform=::toHTML)
        val footer = "</body></html>"

        return header + content + footer
    }

    override fun printJSON(books: Set<Book>): String {
        val header = "[\n"
        val content = books.joinToString(transform=::toJSON, separator=",\n")
        val footer = "\n]"

        return header + content + footer
    }

    override fun printRaw(books: Set<Book>) =
         books.joinToString(transform={"${it.name}, ${it.author}, ${it.publisher}, ${it.content}"}, separator="\n")
}