package com.sd.laborator
import io.micronaut.core.annotation.Introspected

@Introspected
class EratosteneRequest {
	private var numbers: List<Int>? = null

	fun getNumbers(): List<Int>? {
		return numbers
	}
}
