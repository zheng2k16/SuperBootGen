package com.ssx.utils;

import com.ssx.entity.AbstractModuleEntity;
import com.ssx.entity.DefaultSubModuleEntity;
import com.ssx.entity.DefaultTopProjectEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

public class PomXmlPage {
    private static final transient Logger log = LoggerFactory.getLogger(PomXmlPage.class);
    /**--------------------------------------common-------------------------------------**/
    public static Document creatDocument() throws ParserConfigurationException {
        log.info("-----------------------new pom--------------------------");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document xml = documentBuilderFactory.newDocumentBuilder().newDocument();
        xml.setXmlStandalone(true);
        return xml;
    }
    public static Element creatProject(Document xml) {
        Element project = xml.createElement("project");
        project.setAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
        project.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        project.setAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");
        return project;
    }

    public static void appendModelVersion(Document xml, Element project) {
        Element modelVersion = xml.createElement("modelVersion");
        modelVersion.setTextContent("4.0.0");
        project.appendChild(modelVersion);
    }
    public static void appendArtifactId(Document document, Element element, AbstractModuleEntity moduleEntity) {
        Element artifactId = document.createElement("artifactId");
        artifactId.setTextContent(moduleEntity.getArtifactId());
        element.appendChild(artifactId);
    }
    public static void appendGroupId(Document document, Element element, AbstractModuleEntity moduleEntity) {
        Element groupId = document.createElement("groupId");
        groupId.setTextContent(moduleEntity.getGroupId());
        element.appendChild(groupId);
    }
    public static void appendVersion(Document document, Element element, AbstractModuleEntity moduleEntity) {
        Element version = document.createElement("version");
        version.setTextContent(moduleEntity.getVersion());
        element.appendChild(version);

    }
    public static void appendDescription(Document document, Element element, AbstractModuleEntity moduleEntity) {
        Element description = document.createElement("description");
        description.setTextContent(moduleEntity.getDescription());
        element.appendChild(description);
    }
    public static void appendMavenBuildPlugin(Document document, Element element) {
        Element build = document.createElement("build");
        Element plugins = document.createElement("plugins");
        Element plugin = document.createElement("plugin");
        Element groupId = document.createElement("groupId");
        groupId.setTextContent("org.springframework.boot");
        Element artifactId = document.createElement("artifactId");
        artifactId.setTextContent("spring-boot-maven-plugin");
        plugin.appendChild(groupId);
        plugin.appendChild(artifactId);
        plugins.appendChild(plugin);
        build.appendChild(plugins);
        element.appendChild(build);
        log.info("    - [pom.document] maven plugin added");
    }
    public static void appendBootDependencyList(Document xml, Element dependencies, Collection<String> collection, boolean ifParentAgg, String ifPreTrueBootVersion){
        for (String dep : collection) {
            Element dependency = xml.createElement("dependency");

            Element groupId = xml.createElement("groupId");groupId.setTextContent("org.springframework.boot");
            dependency.appendChild(groupId);
            Element artifactId = xml.createElement("artifactId");artifactId.setTextContent(dep);
            dependency.appendChild(artifactId);

            if (ifParentAgg) {
                Element version = xml.createElement("version");version.setTextContent(ifPreTrueBootVersion);
                dependency.appendChild(version);
            }
            dependencies.appendChild(dependency);

            log.info("    - [pom.xml] "+ dep +" added");
        }
    }
    public static void appendMybatisPlusGen(Document xml, Element dependencies, String version) {
        Element d0 = xml.createElement("dependency");
        Element g0 = xml.createElement("groupId");g0.setTextContent("com.baomidou");
        Element a0 = xml.createElement("artifactId");a0.setTextContent("mybatis-plus-boot-starter");
        Element v0 = xml.createElement("version");
        Element d1 = xml.createElement("dependency");
        Element g1 = xml.createElement("groupId");g1.setTextContent("com.baomidou");
        Element a1 = xml.createElement("artifactId");a1.setTextContent("mybatis-plus-generator");
        Element v1 = xml.createElement("version");
        Element d2 = xml.createElement("dependency");
        Element g2 = xml.createElement("groupId");g2.setTextContent("org.apache.velocity");
        Element a2 = xml.createElement("artifactId");a2.setTextContent("velocity-engine-core");
        Element v2 = xml.createElement("version");
        d0.appendChild(g0);d0.appendChild(a0);
        d1.appendChild(g1);d1.appendChild(a1);
        d2.appendChild(g2);d2.appendChild(a2);
        if ("MANAGE_BY_SELF".equals(version)){
            v0.setTextContent("3.4.2");
            v1.setTextContent("3.4.1");
            v2.setTextContent("2.3");
        } else if ("MANAGE_BY_PARENT".equals(version)){
            v0.setTextContent("${mybatis.plus.version}");
            v1.setTextContent("${mybatis.plus.generator.version}");
            v2.setTextContent("${generator.template.version}");
        }
        if (!"NONE".equals(version)){
            d0.appendChild(v0);
            d1.appendChild(v1);
            d2.appendChild(v2);
        }
        dependencies.appendChild(d0);
        dependencies.appendChild(d1);
        dependencies.appendChild(d2);
        log.info("    - [pom.xml] MP&Gen dep added");
    }

    public static void toPomXml(Document xml, Element project, AbstractModuleEntity moduleEntity) throws TransformerException {
        xml.appendChild(project);
        File pom = new File(moduleEntity.getDirName() + File.separator + "pom.xml");

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.VERSION, "1.0");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(xml), new StreamResult(pom));
        log.info(moduleEntity.getPomLevel() + "[" + SuperUtils.fromDirGetName(moduleEntity.getDirName()) + "] pom.xml created");
    }
    /**-------------------------------------parent-------------------------------------**/
    public static void appendPackagingAndModules2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        DefaultTopProjectEntity defaultTopProjectEntity = (DefaultTopProjectEntity) moduleEntity;
        if (!defaultTopProjectEntity.getForceToBeSingleProject()) {
            Element packaging = xml.createElement("packaging");
            packaging.setTextContent("pom");
            Element modules = xml.createElement("modules");
            for (DefaultSubModuleEntity sm : defaultTopProjectEntity.getSubModulesArtifactIdList()) {
                if (sm.getSubModuleIgnoredSupport()) {
                    log.info("    - [" + sm.getArtifactId() + "] module is ignored");
                    log.info("--------------------------------------------------------");
                    continue;
                }
                Element module = xml.createElement("module");
                module.setTextContent(sm.getArtifactId());
                modules.appendChild(module);
            }
            project.appendChild(packaging);
            project.appendChild(modules);
        }
    }
    public static void appendSpringBootParent2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        DefaultTopProjectEntity defaultTopProjectEntity = (DefaultTopProjectEntity) moduleEntity;
        Element parent = xml.createElement("parent");
        Element groupId = xml.createElement("groupId");
        groupId.setTextContent("org.springframework.boot");
        parent.appendChild(groupId);
        Element artifactId = xml.createElement("artifactId");
        artifactId.setTextContent("spring-boot-starter-parent");
        parent.appendChild(artifactId);
        Element version = xml.createElement("version");
        version.setTextContent(defaultTopProjectEntity.getSpringBootVersion());
        parent.appendChild(version);
        // Element relativePath = xml.createElement("relativePath");
        // parent.appendChild(relativePath);
        project.appendChild(parent);
    }
    public static void appendGroupId2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if ("".equals(moduleEntity.getGroupId()) || moduleEntity.getGroupId() == null )
            throw new RuntimeException("please check your setting file, parent <groupId> must have a value");
        appendGroupId(xml, project, moduleEntity);
    }
    public static void appendArtifactId2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if ("".equals(moduleEntity.getArtifactId()) || moduleEntity.getArtifactId() == null)
            throw new RuntimeException("please check your setting file, parent <artifactId> must have a value");
        appendArtifactId(xml, project, moduleEntity);
    }
    public static void appendVersion2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if ("".equals(moduleEntity.getVersion()) || moduleEntity.getVersion() == null)
            throw new RuntimeException("please check your setting file, parent <version> must have a value");
        appendVersion(xml, project, moduleEntity);
    }
    public static void appendDescription2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if (moduleEntity.getDescription() == null || "".equals(moduleEntity.getDescription())) return;
        appendDescription(xml, project, moduleEntity);
    }
    public static void appendProperties2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        DefaultTopProjectEntity defaultTopProjectEntity = (DefaultTopProjectEntity) moduleEntity;
        if ("".equals(defaultTopProjectEntity.getJavaVersion()) || defaultTopProjectEntity.getJavaVersion() == null)
            throw new RuntimeException("please check your setting file, java.version must have a value");
        Element properties = xml.createElement("properties");

        Element javaVersion = xml.createElement("java.version");
        javaVersion.setTextContent(defaultTopProjectEntity.getJavaVersion());
        properties.appendChild(javaVersion);

        if (defaultTopProjectEntity.getMybatisPlusDependencyLocation() == MybatisPlusDependencyLocation.MANAGE_BY_PARENT){
            Element mybatis_plus = xml.createElement("mybatis.plus.version");
            mybatis_plus.setTextContent("3.4.2");
            properties.appendChild(mybatis_plus);
            Element mybatis_plus_generator = xml.createElement("mybatis.plus.generator.version");
            mybatis_plus_generator.setTextContent("3.4.1");
            properties.appendChild(mybatis_plus_generator);
            Element generator_template = xml.createElement("generator.template.version");
            generator_template.setTextContent("2.3");
            properties.appendChild(generator_template);
        }
        // TODO: Other dependencies’ version

        project.appendChild(properties);
    }
    public static void appendDepManageStrategy2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        DefaultTopProjectEntity defaultTopProjectEntity = (DefaultTopProjectEntity) moduleEntity;

        // 单体无dependencyManagement
        if (defaultTopProjectEntity.getForceToBeSingleProject()){
            Element dependencies = xml.createElement("dependencies");
            appendBootDependencyList(xml, dependencies, defaultTopProjectEntity.getBootDependencyList(),false, null);

            if (defaultTopProjectEntity.getIfSingleMybatisPlusGenSupport())
                appendMybatisPlusGen(xml,dependencies, "MANAGE_BY_SELF");
            // TODO: add more dependencies - single


            // singleProject all done although spring-boot-starter is padding
            // but we still checked to make sure its strong
            if (dependencies.getElementsByTagName("dependency").getLength() > 0) project.appendChild(dependencies);
        }
        else { // 聚合
            Element dependencyManagement = xml.createElement("dependencyManagement");
            Element dependencies = xml.createElement("dependencies");
            appendBootDependencyList(xml, dependencies, defaultTopProjectEntity.getBootDependencyList(),true, defaultTopProjectEntity.getSpringBootVersion());

            if (defaultTopProjectEntity.getMybatisPlusDependencyLocation() == MybatisPlusDependencyLocation.MANAGE_BY_PARENT){
                appendMybatisPlusGen(xml, dependencies, "MANAGE_BY_PARENT");
            }
            // TODO: add more dependencies to dependencyManagement - agg

            // aggProject all done
            if (dependencies.getElementsByTagName("dependency").getLength() > 0){
                dependencyManagement.appendChild(dependencies);
                project.appendChild(dependencyManagement);
            }
        }
    }
    public static void appendMavenBuildPlugin2Parent(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if (!moduleEntity.getMavenBuildPluginSupport()) return;
        appendMavenBuildPlugin(xml, project);
    }

    /**-------------------------------------children------------------------------------**/
    public static void appendParentGAV2Children(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        Element parent = xml.createElement("parent");
        appendArtifactId(xml, parent, moduleEntity);
        appendVersion(xml, parent, moduleEntity);
        appendGroupId(xml, parent, moduleEntity);
        project.appendChild(parent);
    }
    public static void appendGAVWithCompare2Children(Document xml, Element project, AbstractModuleEntity moduleEntity, boolean gEqual) {
        if (moduleEntity.getArtifactId() == null || "".equals(moduleEntity.getArtifactId()))
            throw new RuntimeException("please check your setting file, children <artifactId> must have a value");
        appendArtifactId(xml, project, moduleEntity);
        if (!gEqual) appendGroupId(xml, project, moduleEntity);
        appendVersion(xml, project, moduleEntity);

    }
    public static void appendDescription2Children(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if (moduleEntity.getDescription() == null || "".equals(moduleEntity.getDescription())) return;
        appendDescription(xml, project, moduleEntity);
    }
    public static void appendDepManageStrategy2Children(Document xml, Element project, AbstractModuleEntity moduleEntity){
        Element dependencies = xml.createElement("dependencies");
        appendBootDependencyList(xml, dependencies, Arrays.asList("spring-boot-starter", "spring-boot-starter-test"),false,null);
        if (moduleEntity.getMybatisPlusDependencyLocation() == MybatisPlusDependencyLocation.MANAGE_BY_SELF)
            appendMybatisPlusGen(xml,dependencies, "MANAGE_BY_SELF");
        else if (moduleEntity.getMybatisPlusDependencyLocation() == MybatisPlusDependencyLocation.MANAGE_BY_PARENT)
            appendMybatisPlusGen(xml,dependencies, "MANAGE_BY_PARENT");
        // TODO: add more dependencies without version - agg


        if (dependencies.getElementsByTagName("dependency").getLength() > 0) project.appendChild(dependencies);
    }
    public static void appendMavenBuildPlugin2Children(Document xml, Element project, AbstractModuleEntity moduleEntity) {
        if (!moduleEntity.getMavenBuildPluginSupport()) return;
        appendMavenBuildPlugin(xml, project);
    }
}
