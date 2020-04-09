package com.sd.laborator.services

import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import org.springframework.stereotype.Service

@Service
class LibraryPrinterService: LibraryPrinter {
    override fun printHTML(books: List<Book>): String {
        var content: String = "<html><head><title>Libraria mea HTML</title></head><body>"
        books.forEach {
            content += "<p><h3>${it.name}</h3><h4>${it.author}</h4><h5>${it.publisher}</h5>${it.text}</p><br/>"
        }
        content += "</body></html>"
        return content
    }

    override fun printJSON(books: List<Book>): String {
        var content: String = "[\n"
        books.forEach {
            if (it != books.last())
                content += "    {\"Titlu\": \"${it.name}\", \"Autor\":\"${it.author}\", \"Editura\":\"${it.publisher}\", \"Text\":\"${it.text}\"},\n"
            else
                content += "    {\"Titlu\": \"${it.name}\", \"Autor\":\"${it.author}\", \"Editura\":\"${it.publisher}\", \"Text\":\"${it.text}\"}\n"
        }
        content += "]\n"
        return content
    }

    override fun printRaw(books: List<Book>): String {
        var content: String = ""
        books.forEach {
            content += "${it.name}\n${it.author}\n${it.publisher}\n${it.text}\n\n"
        }
        return content
    }
}