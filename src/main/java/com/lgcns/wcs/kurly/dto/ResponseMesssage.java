package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * ResponseMesssage
 * */
@Data
@Alias("ResponseMesssage")
public class ResponseMesssage {
	
    private String status;
    private String message;
    
    public ResponseMesssage() {
    	
    }
    public ResponseMesssage(String status, String message) {
    	this.status = status;
    	this.message = message;
    }
}