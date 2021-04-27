package com.kh.spring.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(boolean.class)
@MappedJdbcTypes(JdbcType.CHAR)
public class BooleanTypeHandler implements TypeHandler<Boolean> {

	@Override
	public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parseString(parameter));
	}

	@Override
	public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
		String s = rs.getString(columnName);
		return parseBoolean(s);
	}

	@Override
	public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
		return parseBoolean(s);
	}

	@Override
	public Boolean getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String s = cs.getString(columnIndex);
		return parseBoolean(s);
	}
	
	// String --> bool
	private boolean parseBoolean(String s) {
		if(s == null) {
			return false;
		}
		s = s.trim().toUpperCase(); // 공백제거 및 대문자로 변경
		
		if(s.length() == 0) {
			return false;
		}
		
		return "Y".equals(s);
	}
	
	// bool --> String
	private String parseString(Boolean bool) {
		return (bool != null && bool == true) ? "Y" : "N";
	}
	

}
