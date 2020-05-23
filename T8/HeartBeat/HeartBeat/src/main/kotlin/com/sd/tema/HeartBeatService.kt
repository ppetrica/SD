package com.sd.tema

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Thread.sleep
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException


const val MESSAGE_MANAGER_PORT = 1500
val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
const val TEACHER_PORT = 1600


class HeartBeatService {
    private fun testStudent() {
        // Simulam ca am fi manager (boost in salariu)
        val messageManagerSocket = ServerSocket(MESSAGE_MANAGER_PORT)

        messageManagerSocket.soTimeout = 3 * 1000;

        var studentConn: Socket? = null
        try {
            studentConn = messageManagerSocket.accept()

            val out = PrintWriter(studentConn.getOutputStream(), true);
            out.write("intrebare ${messageManagerSocket.localPort} Esti treaz?")
        } catch (e: SocketTimeoutException) {
            println("Studentul este beat")
        }

        studentConn?.let{
            try {
                val input = BufferedReader(InputStreamReader(studentConn.getInputStream()));
                input.readLine()
            } catch (e: SocketTimeoutException) {
                println("Nu se stie, studentul s-ar putea preface")
            }
        }
    }

    private fun testMessageManager() {
        val managerSock = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT)
        managerSock.soTimeout = 3 * 1000;

        try {
            val out = PrintWriter(managerSock.getOutputStream(), true);
            out.write("intrebare ${managerSock.localPort} Esti treaz?\n")

            val input = BufferedReader(InputStreamReader(managerSock.getInputStream()));
            input.readLine()
            println("Manager este treaz")
        } catch (e: SocketTimeoutException) {
            println("Manager este beat")
        }
    }

    private fun testTeacher() {
        val teacherSock = Socket("localhost", TEACHER_PORT)
        teacherSock.soTimeout = 3 * 1000;

        try {
            val out = PrintWriter(teacherSock.getOutputStream(), true);
            out.write("Esti treaz?")

            val input = BufferedReader(InputStreamReader(teacherSock.getInputStream()));
            input.readLine()
            println("Teacher este treaz")
        } catch (e: SocketTimeoutException) {
            println("Teacher este beat")
        }
    }

    fun run() = runBlocking {
        while (true) {
            println("Testez proful")
            launch {
                testTeacher()
            }

            println("Testez studentul")
            launch {
                testStudent()
            }

            println("Testez managerul")
            launch {
                testMessageManager()
            }

            sleep(10_000)
        }
    }
}


fun main(args: Array<String>) {
    val service = HeartBeatService()
    service.run()
}

