package com.info.web.util;

public class DataSourceContextHolder {
	   private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();
	  
	    public static void setDbType(String dbType) {  
	        CONTEXT_HOLDER.set(dbType);
	    }  
	  
	    public static String getDbType() {  
	        return ((String) CONTEXT_HOLDER.get());
	    }  
	  
	    public static void clearDbType() {  
	        CONTEXT_HOLDER.remove();
	    }  
	}  