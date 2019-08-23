/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package alchemystar.compare;

import java.util.ArrayList;
import java.util.List;

import alchemystar.meta.Column;
import alchemystar.meta.Index;
import alchemystar.meta.MetaData;
import alchemystar.meta.Table;
import alchemystar.util.SqlUtil;
import lombok.Data;

/**
 * @Author libolin
 */
@Data
public class CompareUnits {

    private MetaData source;
    private MetaData target;

    private List<String> changeSql;

    public CompareUnits(MetaData source, MetaData target) {
        this.source = source;
        this.target = target;
        changeSql = new ArrayList<String>();
    }

    public void compare() {
        compareSchema();
        compareTables();
        compareKeys();
    }

    private void compareSchema() {
        // if not exist
        // changeSql.add("create database if not exists " + SqlUtil.getDbString(source.getSchema())+";");
        source.init();
        target.init();
    }

    private void compareTables() {
        for (Table table : source.getTables().values()) {
            if (target.getTables().get(table.getTableName()) == null) {
                // 如果对应的target没有这张表,直接把create Table拿出
                changeSql.add(table.getCreateTable() + ";");
                continue;
            }
            // 这样就需要比较两者的字段
            compareSingelTable(table, target.getTables().get(table.getTableName()));
        }

    }

    private void compareSingelTable(Table sourceTable, Table targetTable) {
        compareColumns(sourceTable, targetTable);
    }

    private void compareColumns(Table sourceTable, Table targetTable) {
        // 记录最后一个比较的column
        String after = null;
        for (Column column : sourceTable.getColumns().values()) {
            if (targetTable.getColumns().get(column.getName()) == null) {
                // 如果对应的target没有这个字段,直接alter
                String sql = "alter table " + target.getSchema() + "." + targetTable.getTableName() + " add " + column
                        .getName() + " ";
                sql += column.getType() + " ";
                if ("NO".equals(column.getIsNull())) {
                    sql += "NOT NULL ";
                } else {
                    sql += "NULL ";
                }
                if (column.getCollate() != null) {
                    sql += "COLLATE " + SqlUtil.getDbString(column.getCollate()) + " ";
                }
                if (column.getDefaultValue() != null) {
                    sql += "DEFAULT " + SqlUtil.getDbString(column.getDefaultValue()) + " ";
                } else {
                    sql += "DEFAULT NULL ";
                }
                if (column.getComment() != null) {
                    sql += "COMMENT " + SqlUtil.getDbString(column.getComment()) + " ";
                }
                if (after != null) {
                    sql += "after " + after;
                }
                changeSql.add(sql + ";");
            } else {
                // 检查对应的source 和 target的属性
                String sql =
                        "alter table " + target.getSchema() + "." + targetTable.getTableName() + " change " + column
                                .getName() + " ";
                Column targetColumn = targetTable.getColumns().get(column.getName());
                // 比较两者字段,如果返回null,表明一致
                String sqlExtend = compareSingleColumn(column, targetColumn);
                if (sqlExtend != null) {
                    changeSql.add(sql + sqlExtend + ";");
                }
            }
            after = column.getName();
        }

        // remove the target redundancy columns
        for (Column column : targetTable.getColumns().values()) {
            if (sourceTable.getColumns().get(column.getName()) == null) {
                // redundancy , so drop it
                String sql = "alter table " + target.getSchema() + "." + targetTable.getTableName() + " drop " + column
                        .getName() + " ";
                changeSql.add(sql + ";");
            }
        }
    }

    private String compareSingleColumn(Column sourceColumn, Column targetColumn) {
        if (sourceColumn.equals(targetColumn)) {
            return null;
        }
        String changeSql = "";
        if (!sourceColumn.getName().equals(targetColumn.getName())) {
            // never reach here
            throw new RuntimeException("the bug in this tool");
        }
        changeSql += sourceColumn.getName() + " ";
        changeSql += sourceColumn.getType() + " ";
        if ("NO".equals(sourceColumn.getIsNull())) {
            changeSql += "NOT NULL ";
        } else {
            changeSql += "NULL ";
        }
        if (sourceColumn.getCollate() != null) {
            changeSql += "COLLATE " + SqlUtil.getDbString(sourceColumn.getCollate()) + " ";
        }
        if (sourceColumn.getExtra().toUpperCase().contains("AUTO_INCREMENT")) {
            changeSql += "AUTO_INCREMENT ";
        }
        if (sourceColumn.getDefaultValue() != null) {
            changeSql += "DEFAULT " + SqlUtil.getDbString(sourceColumn.getDefaultValue()) + " ";
        } else {
            changeSql += "DEFAULT NULL ";
        }
        if (sourceColumn.getComment() != null) {
            changeSql += "COMMENT " + SqlUtil.getDbString(sourceColumn.getComment()) + " ";
        }
        return changeSql;
    }

    // compare the index
    private void compareKeys() {
        for (Table table : source.getTables().values()) {
            // 这样就需要比较两者的索引)
            if (target.getTables().get(table.getTableName()) != null) {
                compareSingleKeys(table, target.getTables().get(table.getTableName()));
            }
        }
    }

    private void compareSingleKeys(Table sourceTable, Table targetTable) {
        for (Index index : sourceTable.getIndexes().values()) {
            StringBuilder sql = new StringBuilder("alter table " + target.getSchema() + "." + targetTable.getTableName() + " ");
            if (targetTable.getIndexes().get(index.getIndexName()) == null) {
                if (index.getIndexName().equals("PRIMARY")) {
                    sql.append("add primary key ");
                } else {
                    if (index.getNotUnique().equals("0")) {
                        sql.append("add unique ").append(index.getIndexName()).append(" ");
                    } else {
                        sql.append("add index ").append(index.getIndexName()).append(" ");
                    }
                }
                sql.append("(`");
                for (String key : index.getColumns()) {
                    sql.append(key.trim()).append("`,`");
                }
                // 去掉最后一个,`
                sql = new StringBuilder(sql.substring(0, sql.length() - 2) + ")");
                changeSql.add(sql + ";");
            }
        }
        for (Index index : targetTable.getIndexes().values()) {
            if (sourceTable.getIndexes().get(index.getIndexName()) == null) {
                // 表明此索引多余
                String sql = "alter table " + target.getSchema() + "." + targetTable.getTableName() + " ";
                if ("PRIMARY".equals(index.getIndexName())) {
                    sql += "drop primary key ";
                } else {
                    sql += "drop index " + index.getIndexName();
                }
                changeSql.add(sql + ";");
            }
        }
    }

}
