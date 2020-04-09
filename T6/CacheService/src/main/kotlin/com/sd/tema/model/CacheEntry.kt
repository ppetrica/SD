package com.sd.tema.model

import java.sql.Timestamp

class CacheEntry(val id: Int, val query: String, val result: String, val timestamp: Timestamp)