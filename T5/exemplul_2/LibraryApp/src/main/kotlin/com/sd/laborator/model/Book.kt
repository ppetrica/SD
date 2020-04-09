package com.sd.laborator.model

class Book(private var data: Content) {

    var name: String?
        get() {
            return data.name
        }
        set(value) {
            data.name = value
        }

    var author: String?
        get() {
            return data.author
        }
        set(value) {
            data.author = value
        }

    var publisher: String?
        get() {
            return data.publisher
        }
        set(value) {
            data.publisher = value
        }

    // this is very bad too, why the f*** would class Content even exist,
    // I'm not fixing this, whoever wrote this should be ashamed of himself
    var content: String?
        get() {
            return data.text
        }
        set(value) {
            data.text = value
        }

    // This is a very bad design, but ok
    fun hasAuthor(author: String): Boolean {
        return data.author!!.toLowerCase().indexOf(author) != -1
    }

    fun hasTitle(title: String): Boolean {
        return data.name!!.toLowerCase().indexOf(title) != -1
    }

    fun publishedBy(publisher: String): Boolean {
        return data.publisher!!.toLowerCase().indexOf(publisher) != -1
    }

}