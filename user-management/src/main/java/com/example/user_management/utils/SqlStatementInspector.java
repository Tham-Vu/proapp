package com.example.user_management.utils;


import org.hibernate.resource.jdbc.spi.StatementInspector;

public class SqlStatementInspector implements StatementInspector {
    private static final ThreadLocal<String> lastSql = new ThreadLocal<>();

    @Override
    public String inspect(String s) {
        lastSql.set(s);
        return s;
    }
    public static String getLastSql(){
        String sql = lastSql.get();
        lastSql.remove();
        return sql != null ? sql : Consts.SQL_NOT_CAPTURED;
    }
}
