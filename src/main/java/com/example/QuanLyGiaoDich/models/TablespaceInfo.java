package com.example.QuanLyGiaoDich.models;

public class TablespaceInfo {
    private String tablespace_name;
    private String file_name;
    private int size;

    public TablespaceInfo() {}
    public TablespaceInfo(String tablespace_name, int size, String file_name) {
        this.tablespace_name = tablespace_name;
        this.size = size;
        this.file_name = file_name;
    }

    public String getTablespaceName() {
        return tablespace_name;
    }

    public void setTablespaceName(String tablespace_name) {
        this.tablespace_name = tablespace_name;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    @Override
    public String toString() {
        return "TablespaceInfo{" +
                "Size=" +  size +
                ", fileName='" + file_name + '\'' +
                ", tablespaceName='" + tablespace_name + '\'' +
                '}';
    }
}
