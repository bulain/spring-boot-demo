package com.bulain.mybatis;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.migration.Change;
import org.apache.ibatis.migration.MigrationException;
import org.apache.ibatis.migration.MigrationLoader;
import org.apache.ibatis.migration.MigrationReader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class ClasspathMigrationLoader implements MigrationLoader {

    private final Map<BigDecimal, Resource> mapResource = new HashMap<>();
    private final Resource[] resources;
    private final String charset;
    private final Properties variables;

    public ClasspathMigrationLoader(Resource[] resources, String charset, Properties variables) {
        this.resources = resources;
        this.charset = charset;
        this.variables = variables;
    }

    @Override
    public List<Change> getMigrations() {

        List<Change> migrations = new ArrayList<Change>();
        Arrays.sort(resources, (a, b) -> {
            return a.getFilename().compareTo(b.getFilename());
        });
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename.endsWith(".sql") && !isSpecialFile(filename)) {
                Change change = parseChangeFromFilename(resource);
                migrations.add(change);
                mapResource.put(change.getId(), resource);
            }
        }

        return migrations;
    }

    protected boolean isSpecialFile(String filename) {
        return "bootstrap.sql".equals(filename) || "onabort.sql".equals(filename);
    }

    protected Change parseChangeFromFilename(Resource resource) {
        try {
            String filename = resource.getFilename();
            Change change = new Change();
            int lastIndexOfDot = filename.lastIndexOf('.');
            String[] parts = filename.substring(0, lastIndexOfDot).split("_");
            change.setId(new BigDecimal(parts[0]));
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                if (i > 1) {
                    builder.append(" ");
                }
                builder.append(parts[i]);
            }
            change.setDescription(builder.toString());
            change.setFilename(filename);

            return change;
        } catch (Exception e) {
            throw new MigrationException("Error parsing change from file.  Cause: " + e, e);
        }
    }

    @Override
    public Reader getScriptReader(Change change, boolean undo) {
        try {
            BigDecimal id = change.getId();
            Resource resource = mapResource.get(id);
            return new MigrationReader(resource.getInputStream(), charset, false, variables);
        } catch (IOException e) {
            throw new MigrationException("Error reading " + change.getFilename(), e);
        }
    }

    @Override
    public Reader getBootstrapReader() {
        String fileName = "bootstrap.sql";
        return getSoleScriptReader(fileName);
    }

    @Override
    public Reader getOnAbortReader() {
        String fileName = "onabort.sql";
        return getSoleScriptReader(fileName);
    }

    protected Reader getSoleScriptReader(String fileName) {
        try {
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (StringUtils.pathEquals(filename, fileName)) {
                    return new MigrationReader(resource.getInputStream(), charset, false, variables);
                }
            }
            return null;
        } catch (IOException e) {
            throw new MigrationException("Error reading " + fileName, e);
        }
    }

}
