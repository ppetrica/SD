package com.sd.laborator.services

import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet


class BookRowMapper: RowMapper<Book> {
    override fun mapRow(p0: ResultSet, p1: Int): Book? {
        return Book(
            p0.getString("author"),
            p0.getString("text"),
            p0.getString("name"),
            p0.getString("publisher")
        )
    }
}


@Service
class LibraryDAOService : LibraryDAO {
    @Autowired
    private lateinit var database: JdbcTemplate;

    override fun getBooks(): List<Book> {
        return database.query("SELECT * FROM books", BookRowMapper())
    }

    override fun addBook(book: Book) {
        database.update("INSERT INTO books(author, text, name, publisher) VALUES(?, ?, ?, ?)",
            book.author, book.text, book.name, book.publisher)
    }

    override fun findAllByAuthor(author: String): List<Book> {
        return database.query("SELECT * FROM books WHERE author=?", arrayOf(author), BookRowMapper())
    }

    override fun findAllByTitle(title: String): List<Book> {
        return database.query("SELECT * FROM books WHERE name=?", arrayOf(title), BookRowMapper())
    }

    override fun findAllByPublisher(publisher: String): List<Book> {
        return database.query("SELECT * FROM books WHERE publisher=?", arrayOf(publisher), BookRowMapper())
    }
}