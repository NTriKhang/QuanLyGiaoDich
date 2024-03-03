package com.example.QuanLyGiaoDich.Configure;

import java.sql.Connection;
import java.util.HashMap;

public class MapUserConnection {
	public static final HashMap<String, Connection> mapUserConnection = new HashMap<String, Connection>();
	public static Connection getConnection(String username) {
		if(mapUserConnection.containsKey(username))
			return mapUserConnection.get(username);
		return null;
	}
}
