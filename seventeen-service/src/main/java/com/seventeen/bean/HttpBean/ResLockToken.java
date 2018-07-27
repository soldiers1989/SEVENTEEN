package com.seventeen.bean.HttpBean;

public class ResLockToken {
   private String  access_token;//	String	f45a713ef706d09c892084ee7e350384	Access token
    private int openid;//	Int	1234567890	开放的科技侠用户id
    private int  expires_in	;//Int	7776000（默认7776000秒，即90天后过期）	Access token过期时间
    private String scope;//	String	user,key,room	权限范围
    private String refresh_token;//	String	f45a713ef706d09c892084ee7e350384	Refresh token

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
