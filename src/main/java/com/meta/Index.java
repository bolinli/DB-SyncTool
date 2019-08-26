package com.meta;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Index {

    private List<String> columns;
    private String indexName;
    /**
     * 0表示unique,1表示普通索引
     */
    private String notUnique;

    public Index() {
        columns = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "Index{" +
                "columns=" + columns +
                ", indexName='" + indexName + '\'' +
                ", notUnique='" + notUnique + '\'' +
                '}' + "\n";
    }
}
