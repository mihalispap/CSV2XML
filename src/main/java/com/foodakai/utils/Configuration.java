package com.foodakai.utils;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Configuration {

    private String data_source;

    private boolean has_header;

    private Csvs csvs;

    private Output output;

    private String url_pattern;

    private String type;

    private Map<String, Integer> mappings = new HashMap<String, Integer>();

    private Map<String, Method> post_process = new HashMap<String, Method>();

    public Map<String, Method> getPost_process() {
        return post_process;
    }

    public void setPost_process(Map<String, Method> post_process) {
        this.post_process = post_process;
    }

    public boolean isHas_header() {
        return has_header;
    }

    public void setHas_header(boolean has_header) {
        this.has_header = has_header;
    }

    public Map<String, Integer> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, Integer> mappings) {
        this.mappings = mappings;
    }

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public String getUrl_pattern() {
        return url_pattern;
    }

    public void setUrl_pattern(String url_pattern) {
        this.url_pattern = url_pattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Csvs getCsvs() {
        return csvs;
    }

    public void setCsvs(Csvs csvs) {
        this.csvs = csvs;
    }
}
