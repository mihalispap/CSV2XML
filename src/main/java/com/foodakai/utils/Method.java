package com.foodakai.utils;


import java.util.List;

public final class Method {

    private String action;

    private List<String> css_rule;

    private List<String> parts;

    private int keep;

    private String directory;

    public Method method;

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getCss_rule() {
        return css_rule;
    }

    public void setCss_rule(List<String> css_rule) {
        this.css_rule = css_rule;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }
}



