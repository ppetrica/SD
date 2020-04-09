package com.sd.laborator.interfaces

import com.sd.laborator.model.Book

fun toXML(book: Book) =
    "<book>\n" +
    "   <title>${book.name}</title>\n" +
    "   <author>${book.author}</author>\n" +
    "   <publisher>${book.publisher}</publisher>\n" +
    "   <content>${book.content}</content>\n" +
    "</book>"


abstract class XMLPrinter {
    open fun printXML(books: Set<Book>): String {
        val header = "<books>\n"
        val content = books.joinToString(transform=::toXML, separator="\n")
        val footer = "\n</books>"

        return header + content + footer
    }
}