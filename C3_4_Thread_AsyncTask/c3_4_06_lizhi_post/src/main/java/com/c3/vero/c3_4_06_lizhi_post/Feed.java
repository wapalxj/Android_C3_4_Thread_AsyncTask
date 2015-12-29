package com.c3.vero.c3_4_06_lizhi_post;

/**
 * Created by vero on 2015/12/3.
 */
public class Feed {
    private int id;
    private int oid;
    private FeedData data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public FeedData getData() {
        return data;
    }

    public void setDatas(FeedData data) {
        this.data= data;
    }
}
