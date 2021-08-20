package com.ssx.entity;

import com.ssx.utils.MybatisPlusDependencyLocation;
import com.ssx.utils.PomLevel;
import com.ssx.utils.PomXmlPage;
import com.ssx.utils.SuperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author ssx
 * @Date 2021/7/22 14:32
 * @Version 1.0
 */
@SuppressWarnings("all")
@Component
@ConfigurationProperties(prefix = "super-boot-gen-config")
public class DefaultTopProjectEntity extends AbstractModuleEntity implements DisposableBean, Serializable {
    public static String dataSourcePrefix = "spring.datasource.";

    private static final transient Logger log = LoggerFactory.getLogger(DefaultTopProjectEntity.class);
    // global settings
    private Boolean forceToBeSingleProject = false;
    private Boolean ifSingleMybatisPlusGenSupport = false;
    private Boolean ifAggForceToIgnoreParentSrcFolderSupport = false;
    private Boolean autoOpenAfterGenWithWinAndIdea64 = false;
    // dir
    private String workSpaceDir = "D:/test";
    // pom
    private String artifactId = "SuperBootGen";
    private String groupId = "com.ssx";
    private String version = "1.0-SNAPSHOT";
    // pom
    private String springBootVersion = "2.5.2";
    private String description = "Powered By Chain-Gen";
    private String javaVersion = "1.8";
    // pom
    private Set<String> bootDependencyList = new HashSet<>();
    private Boolean mavenBuildPluginSupport = false;
    // project
    private Boolean mainFolderSupport = false;
    private Boolean applicationClassSupport = false;
    private Boolean testClassSupport = false;
    // project resources
    private Set<String> settingFileList = new HashSet<>();
    private Set<String> resourcesFolderList = new HashSet<>();
    // sub module
    private Set<DefaultSubModuleEntity> subModulesArtifactIdList = new HashSet<>();


    public DefaultTopProjectEntity() {
        setPomLevel(PomLevel.PARENT);
    }

    @PostConstruct
    private void pp(){
        if (!dataSourcePrefix.endsWith(".")) dataSourcePrefix += ".";
        setDirName(SuperUtils.filePathPostProcess(workSpaceDir + File.separator + artifactId));
    }

    @Override
    public void destroy() throws Exception {
        try{
            createParentDir();
            if (!forceToBeSingleProject) createSubDir();
            if (autoOpenAfterGenWithWinAndIdea64) SuperUtils.open(getDirName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createParentDir() throws Exception {
        SuperUtils.createProjectFolder(getDirName());
        solveForceToBeSingleProject();
        solveMybatisPlusDependencyLocation();
        solveBootDependencyList();
        createPom(this);
        if (!forceToBeSingleProject && ifAggForceToIgnoreParentSrcFolderSupport) return;
        if (this.mainFolderSupport) SuperUtils.createJavaAndTestDir(this);
        if (this.applicationClassSupport) createApplicationClass(this);
        if (this.testClassSupport) createTestClass(this);
        if (!this.resourcesFolderList.isEmpty()) SuperUtils.createFolders4Resources(this);
        if (!this.settingFileList.isEmpty()) SuperUtils.createSettingFiles(this);
        if (forceToBeSingleProject && ifSingleMybatisPlusGenSupport) SuperUtils.createMybatisGenerator(this);
    }

    private void createSubDir() throws IOException, TransformerException, ParserConfigurationException {
        for (DefaultSubModuleEntity sm : subModulesArtifactIdList) {
            if (sm.getSubModuleIgnoredSupport()) continue;
            sm.setDirName(getDirName() + File.separator  + sm.getArtifactId());
            SuperUtils.createJavaAndTestDir(sm);
            createPom(sm);
            createApplicationClass(sm);
            createTestClass(sm);
            if (!sm.getResourcesFolderList().isEmpty()) SuperUtils.createFolders4Resources(sm);
            if (!sm.getSettingFileList().isEmpty()) SuperUtils.createSettingFiles(sm);
            if (sm.getMybatisPlusDependencyLocation() != MybatisPlusDependencyLocation.OFF) SuperUtils.createMybatisGenerator(sm);
        }
    }

    private void solveForceToBeSingleProject(){
        if (forceToBeSingleProject) return;
        for (DefaultSubModuleEntity sm : subModulesArtifactIdList) {
            if (!sm.getSubModuleIgnoredSupport()) return;
        }
        setForceToBeSingleProject(true);
        log.warn("U want a agg pro but no sub module will be created, so it forced to be a single project");
    }
    private void solveMybatisPlusDependencyLocation() {
        if (forceToBeSingleProject) {
            if (ifSingleMybatisPlusGenSupport)
                setMybatisPlusDependencyLocation(MybatisPlusDependencyLocation.OFF);
            else setMybatisPlusDependencyLocation(MybatisPlusDependencyLocation.MANAGE_BY_SELF);
            return;
        }
        int mpl = MybatisPlusDependencyLocation.OFF;
        for (DefaultSubModuleEntity sm: subModulesArtifactIdList){
            if (sm.getSubModuleIgnoredSupport()) continue;
            mpl |= sm.getMybatisPlusDependencyLocation();
        }

        if (mpl >= MybatisPlusDependencyLocation.MANAGE_BY_PARENT) setMybatisPlusDependencyLocation(MybatisPlusDependencyLocation.MANAGE_BY_PARENT);
    }
    private void solveBootDependencyList() {
        if (bootDependencyList.isEmpty()) bootDependencyList.add("spring-boot-starter");
        if (forceToBeSingleProject && testClassSupport) bootDependencyList.add("spring-boot-starter-test");
    }

    private void createPom(AbstractModuleEntity moduleEntity) throws ParserConfigurationException, TransformerException, IOException {
        Document xml = PomXmlPage.creatDocument();
        Element project = PomXmlPage.creatProject(xml);
        PomXmlPage.appendModelVersion(xml, project);
        if (moduleEntity.getPomLevel() == PomLevel.PARENT){
            PomXmlPage.appendPackagingAndModules2Parent(xml, project, moduleEntity);
            PomXmlPage.appendSpringBootParent2Parent(xml, project, moduleEntity);
            PomXmlPage.appendGroupId2Parent(xml, project, moduleEntity);
            PomXmlPage.appendArtifactId2Parent(xml, project, moduleEntity);
            PomXmlPage.appendVersion2Parent(xml, project, moduleEntity);
            PomXmlPage.appendDescription2Parent(xml, project, moduleEntity);
            PomXmlPage.appendProperties2Parent(xml, project, moduleEntity);
            PomXmlPage.appendDepManageStrategy2Parent(xml, project, moduleEntity);
            PomXmlPage.appendMavenBuildPlugin2Parent(xml, project, moduleEntity);
        }
        else if (moduleEntity.getPomLevel() == PomLevel.CHILDREN) {
            PomXmlPage.appendParentGAV2Children(xml, project, this);
            PomXmlPage.appendGAVWithCompare2Children(xml, project, moduleEntity, this.groupId == moduleEntity.getGroupId());
            PomXmlPage.appendDescription2Children(xml, project, moduleEntity);
            PomXmlPage.appendDepManageStrategy2Children(xml, project, moduleEntity);
            PomXmlPage.appendMavenBuildPlugin2Children(xml, project, moduleEntity);
        }
        PomXmlPage.toPomXml(xml, project, moduleEntity);
    }

    private void createApplicationClass(AbstractModuleEntity moduleEntity) throws IOException {
        String appDir = SuperUtils.filePathPostProcess(moduleEntity.getDirName() + "/src/main/java/" + moduleEntity.getGroupId().replace(".","/") + File.separator);
        String appName = SuperUtils.fromArtifactIdGetName(moduleEntity.getArtifactId());

        String applicationCodeString = SuperUtils.tempFile2String("application");
        applicationCodeString = applicationCodeString.replace("${groupId}", moduleEntity.getGroupId());
        applicationCodeString = applicationCodeString.replace("${ApplicationClassName}", appName);
        File distFile= new File(appDir + File.separator + appName + ".java");
        SuperUtils.code2File(applicationCodeString, distFile);

        log.info("    - [" + appName + ".java] created and writed");
    }

    private void createTestClass(AbstractModuleEntity moduleEntity) throws IOException {
        String testDir = SuperUtils.filePathPostProcess(moduleEntity.getDirName() + "/src/test/java/" + moduleEntity.getGroupId().replace(".","/") + File.separator);
        String testName =SuperUtils.fromArtifactIdGetName(moduleEntity.getArtifactId());

        String testCodeString = SuperUtils.tempFile2String("applicationTest");

        testCodeString = testCodeString.replace("${groupId}", moduleEntity.getGroupId());
        testCodeString = testCodeString.replace("${TextClassName}", testName + "Tests");

        File distFile = new File(testDir + File.separator + testName + "Tests.java");
        SuperUtils.code2File(testCodeString, distFile);

        log.info("    - [" + testName + "Tests.java] created and writed");
    }

    public Boolean getAutoOpenAfterGenWithWinAndIdea64() {
        return autoOpenAfterGenWithWinAndIdea64;
    }

    public void setAutoOpenAfterGenWithWinAndIdea64(Boolean autoOpenAfterGenWithWinAndIdea64) {
        this.autoOpenAfterGenWithWinAndIdea64 = autoOpenAfterGenWithWinAndIdea64;
    }

    public Boolean getForceToBeSingleProject() {
        return forceToBeSingleProject;
    }

    public void setForceToBeSingleProject(Boolean forceToBeSingleProject) {
        this.forceToBeSingleProject = forceToBeSingleProject;
    }

    public Boolean getIfSingleMybatisPlusGenSupport() {
        return ifSingleMybatisPlusGenSupport;
    }

    public void setIfSingleMybatisPlusGenSupport(Boolean ifSingleMybatisPlusGenSupport) {
        this.ifSingleMybatisPlusGenSupport = ifSingleMybatisPlusGenSupport;
    }

    public Boolean getIfAggForceToIgnoreParentSrcFolderSupport() {
        return ifAggForceToIgnoreParentSrcFolderSupport;
    }

    public void setIfAggForceToIgnoreParentSrcFolderSupport(Boolean ifAggForceToIgnoreParentSrcFolderSupport) {
        this.ifAggForceToIgnoreParentSrcFolderSupport = ifAggForceToIgnoreParentSrcFolderSupport;
    }

    public String getWorkSpaceDir() {
        return workSpaceDir;
    }

    public void setWorkSpaceDir(String workSpaceDir) {
        this.workSpaceDir = workSpaceDir;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }


    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Set<String> getBootDependencyList() {
        return bootDependencyList;
    }

    public void setBootDependencyList(Set<String> bootDependencyList) {
        this.bootDependencyList = bootDependencyList;
    }

    public Boolean getMainFolderSupport() {
        return mainFolderSupport;
    }

    public void setMainFolderSupport(Boolean mainFolderSupport) {
        this.mainFolderSupport = mainFolderSupport;
    }

    public Boolean getApplicationClassSupport() {
        return applicationClassSupport;
    }

    public void setApplicationClassSupport(Boolean applicationClassSupport) {
        this.applicationClassSupport = applicationClassSupport;
    }

    public Boolean getTestClassSupport() {
        return testClassSupport;
    }

    public void setTestClassSupport(Boolean testClassSupport) {
        this.testClassSupport = testClassSupport;
    }

    public Set<DefaultSubModuleEntity> getSubModulesArtifactIdList() {
        return subModulesArtifactIdList;
    }

    public void setSubModulesArtifactIdList(Set<DefaultSubModuleEntity> subModulesArtifactIdList) {
        this.subModulesArtifactIdList = subModulesArtifactIdList;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean getMavenBuildPluginSupport() {
        return mavenBuildPluginSupport;
    }

    @Override
    public void setMavenBuildPluginSupport(Boolean mavenBuildPluginSupport) {
        this.mavenBuildPluginSupport = mavenBuildPluginSupport;
    }

    @Override
    public Set<String> getSettingFileList() {
        return settingFileList;
    }

    @Override
    public void setSettingFileList(Set<String> settingFileList) {
        this.settingFileList = settingFileList;
    }

    @Override
    public Set<String> getResourcesFolderList() {
        return resourcesFolderList;
    }

    @Override
    public void setResourcesFolderList(Set<String> resourcesFolderList) {
        this.resourcesFolderList = resourcesFolderList;
    }
}
