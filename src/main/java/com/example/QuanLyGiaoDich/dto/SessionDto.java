package com.example.QuanLyGiaoDich.dto;


public class SessionDto {
    public final long sid;
    public final long serial;
    public final String username;
    public final String program;
	 public SessionDto(int sid, int serial, String username, String program) {
	        this.sid = sid;
	        this.serial = serial;
	        this.username = username;
	        this.program = program;
	    }
	public long getSid() {
		return sid;
	}
	public long getSerial() {
		return serial;
	}
	public String getUsername() {
		return username;
	}
	public String getProgram() {
		return program;
	}
}
