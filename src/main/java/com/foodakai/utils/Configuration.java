package com.foodakai.utils;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Configuration {

    private String data_source;

    private Csvs csvs;

    private Output output;

    private String url_pattern;

    private String type;

    private Map<String, Integer> mappings = new HashMap<String, Integer>();

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

    public Csvs getCsvss() {
        return csvs;
    }

    public void setCsvs(Csvs htmls) {
        this.csvs = csvs;
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
}
