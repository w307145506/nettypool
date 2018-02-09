package com.wpm.cloud.pool;

public class Msg {	
	
	
	private String uuId;
	
	private String msg;

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Msg [uuId=" + uuId + ", msg=" + msg + "]";
	}
	
}
