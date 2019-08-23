/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package alchemystar.meta;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author libolin
 */
@Data
public class Table {
    private String tableName;
    private String createTable;
    private Map<String, Column> columns;
    private Map<String, Index> indexes;

    public Table() {
        columns = new LinkedHashMap<String, Column>();
        indexes = new LinkedHashMap<String, Index>();
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                ", createTable='" + createTable + '\'' +
                ", columns=" + columns +
                ", indexes=" + indexes +
                '}';
    }
}
