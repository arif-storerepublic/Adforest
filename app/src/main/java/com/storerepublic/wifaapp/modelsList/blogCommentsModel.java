package com.storerepublic.wifaapp.modelsList;

import java.util.ArrayList;

public class blogCommentsModel {

    private String comntId;
    private String rating;
    private String blogId;
    private String image;
    private String name;
    private String date;
    private String message;
    private Boolean hasReplyList;
    private boolean canReply;
    private String comntParentId;
    private int pageNumber;
    private ArrayList<blogCommentsModel> listitemsiner = new ArrayList<>();


    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Boolean getHasReplyList() {
        return hasReplyList;
    }

    public void setHasReplyList(Boolean hasReplyList) {
        this.hasReplyList = hasReplyList;
    }


    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    private String reply;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getComntId() {
        return comntId;
    }

    public void setComntId(String comntId) {
        this.comntId = comntId;
    }

    public String getComntParentId() {
        return comntParentId;
    }

    public void setComntParentId(String comntParentId) {
        this.comntParentId = comntParentId;
    }

    public ArrayList<blogCommentsModel> getListitemsiner() {
        return listitemsiner;
    }

    public void setListitemsiner(ArrayList<blogCommentsModel> listitemsiner) {
        this.listitemsiner = listitemsiner;
    }

    public boolean isCanReply() {
        return canReply;
    }

    public void setCanReply(boolean canReply) {
        this.canReply = canReply;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
