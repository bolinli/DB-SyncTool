/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package alchemystar.meta;


import lombok.Data;

/**
 * @Author libolin
 */
@Data
public class Column {

    //$sql = 'select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_DEFAULT,COLUMN_COMMENT,EXTRA from information_schema.columns ';
    //$sql.= 'where TABLE_SCHEMA=\''.$db.'\' and TABLE_NAME=\''.$table.'\' order by ORDINAL_POSITION asc';
    private String name;
    private String type;
    private String isNull;
    private String defaultValue;
    private String comment;
    private String extra;
    // private String charset;
    private String collate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Column column = (Column) o;

        if (name != null ? !name.equals(column.name) : column.name != null) {
            return false;
        }
        if (type != null ? !type.equals(column.type) : column.type != null) {
            return false;
        }
        if (isNull != null ? !isNull.equals(column.isNull) : column.isNull != null) {
            return false;
        }
        if (defaultValue != null ? !defaultValue.equals(column.defaultValue) : column.defaultValue != null) {
            return false;
        }
        if (comment != null ? !comment.equals(column.comment) : column.comment != null) {
            return false;
        }
//        if (charset != null ? !charset.equals(column.charset) : column.charset != null) {
//            return false;
//        }
        if (collate != null ? !collate.equals(column.collate) : column.collate != null) {
            return false;
        }
        return extra != null ? extra.equals(column.extra) : column.extra == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (isNull != null ? isNull.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (extra != null ? extra.hashCode() : 0);
//        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + (collate != null ? collate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isNull='" + isNull + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", comment='" + comment + '\'' +
                ", extra='" + extra + '\'' +
               // ", charset='" + charset + '\'' +
                ", collate='" + collate + '\'' +
                '}';
    }
}
