package com.suhoi.audit.util;

public class ClickHouseQuery {
    public static final String CREATE_TABLE_IF_NOT_EXIST_QUERY = """
            CREATE TABLE IF NOT EXISTS default.audits 
            (
                `uuid` UUID,
                `username` String,
                `action` String,
                `timestamp` DateTime('Europe/Moscow')
            ) ENGINE = TinyLog
            """;

    public static final String SAVE_AUDIT_QUERY = """
                INSERT INTO default.audits
                VALUES (?, ?, ?, ?)
                """;

    public static final String GET_ALL_AUDIT_QUERY = """
                SELECT uuid, username, action, timestamp
                FROM default.audits;
                """;
}
