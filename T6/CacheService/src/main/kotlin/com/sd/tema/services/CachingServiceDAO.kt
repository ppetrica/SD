package com.sd.tema.services

import com.sd.tema.model.CacheEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*


class CacheRowMapper: RowMapper<CacheEntry> {
    override fun mapRow(p0: ResultSet, p1: Int): CacheEntry? {
        return CacheEntry(
            p0.getInt("id"),
            p0.getString("query"),
            p0.getString("result"),
            p0.getTimestamp("timestamp")
        )
    }
}


@Service
class CachingServiceDAO {
    //Spring Boot will automatically wire this object using application.properties:
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun getEntryByQuery(query: String): Optional<CacheEntry> {
        val results = jdbcTemplate.query("SELECT * FROM cache WHERE query=?", arrayOf(query), CacheRowMapper());

        return if (results.isEmpty()) Optional.empty() else Optional.of(results[0]);
    }

    fun insertEntry(query: String, ts: Timestamp, result: String): Boolean {
        return jdbcTemplate.update("INSERT INTO cache(query, timestamp, result) VALUES(?, ?, ?)",
            query, ts, result) == 1
    }

    fun updateEntry(id: Int, ts: Timestamp, result: String): Boolean {
        return jdbcTemplate.update("UPDATE cache SET timestamp=?, result=? WHERE id=?",
            ts, result, id) == 1
    }
}