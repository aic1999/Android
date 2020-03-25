package com.lmm.comwell.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Post extends BmobObject {
	private String content;
	private Integer praise=0;
	private User author;
	private String title;

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	private String img_url;
	public BmobFile getIcon() {
		return icon;
	}

	public void setIcon(BmobFile icon) {
		this.icon = icon;
	}

	private String time;
	private BmobFile icon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private BmobRelation comment;
	
	public BmobRelation getComment() {
		return comment;
	}
	public void setComment(BmobRelation comment) {
		this.comment = comment;
	}
	public String getContent() {
		return content;
	}
	public Integer getPraise() {
		return praise;
	}
	public void setPraise(Integer praise) {
		this.praise = praise;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
}
