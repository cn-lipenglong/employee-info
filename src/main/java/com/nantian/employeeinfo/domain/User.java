package com.nantian.employeeinfo.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class User {
	private String id;

	private String username;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date beginTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
	/**
	 * 工作状态 1：占用； 0：未占用
	 */
	private Integer workStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(Integer workStatus) {
		this.workStatus = workStatus;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", beginTime=" + beginTime +
				", endTime=" + endTime +
				", workStatus=" + workStatus +
				'}';
	}

	public User () {}

	public User(String id, String username, Date beginTime, Date endTime, Integer workStatus) {
		this.id = id;
		this.username = username;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.workStatus = workStatus;
	}
}