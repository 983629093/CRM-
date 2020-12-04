package com.demo.crm.vo;

import java.util.HashMap;
import java.util.List;

public class VoListData<T> {

    public int page;//页数
    public List<T> list;//对象数据

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
