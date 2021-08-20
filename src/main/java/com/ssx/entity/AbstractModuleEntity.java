package com.ssx.entity;

import com.ssx.utils.PomLevel;

import java.io.Serializable;
import java.util.Set;

/**
 * @Author ssx
 * @Date 2021/7/22 14:34
 * @Version 1.0
 */
public abstract class AbstractModuleEntity implements Serializable {
    // auto param
    private int mybatisPlusDependencyLocation;
    private PomLevel pomLevel;
    private String dirName;

    private String artifactId;
    private String groupId;
    private String version;
    private String description;

    private Boolean mavenBuildPluginSupport = false;

    private Set<String> settingFileList;
    private Set<String> resourcesFolderList;

    public int getMybatisPlusDependencyLocation() {
        return mybatisPlusDependencyLocation;
    }
    public void setMybatisPlusDependencyLocation(int mybatisPlusDependencyLocation) {
        this.mybatisPlusDependencyLocation = mybatisPlusDependencyLocation;
    }

    public PomLevel getPomLevel() {
        return pomLevel;
    }
    public void setPomLevel(PomLevel pomLevel) {
        this.pomLevel = pomLevel;
    }

    public String getArtifactId() {
        return artifactId;
    }
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMavenBuildPluginSupport() {
        return mavenBuildPluginSupport;
    }
    public void setMavenBuildPluginSupport(Boolean mavenBuildPluginSupport) {
        this.mavenBuildPluginSupport = mavenBuildPluginSupport;
    }

    public Set<String> getSettingFileList() {
        return settingFileList;
    }
    public void setSettingFileList(Set<String> settingFileList) {
        this.settingFileList = settingFileList;
    }

    public Set<String> getResourcesFolderList() {
        return resourcesFolderList;
    }
    public void setResourcesFolderList(Set<String> resourcesFolderList) {
        this.resourcesFolderList = resourcesFolderList;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

}
