package com.colin.servlet;

import java.util.Date;

/**
 * 2024年06月26日11:38
 */
public class Cookie {

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
        this.maxAge = -1L;
        this.path = "/";
    }

    private String key;
    private String value;
    private long maxAge;
    private String path;
    private String expires;
    private Boolean httpOnly;

    private void setExpires(){
        long currentTime = System.currentTimeMillis();
        long l = currentTime + this.maxAge * 1000L;
        Date date = new Date(l);
        String s = date.toString();
        String[] s1 = s.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(s1[0]).append(", ").append(s1[2]).append("-").append(s1[1]).append("-").append(s1[s1.length - 1])
                .append(" ").append(s1[3]).append(" ").append("GMT");
        this.expires = sb.toString();
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getExpires() {
        return expires;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        this.setExpires();
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", maxAge=" + maxAge +
                ", path='" + path + '\'' +
                '}';
    }
}
