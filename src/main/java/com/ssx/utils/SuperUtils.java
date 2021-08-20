package com.ssx.utils;

import com.ssx.entity.AbstractModuleEntity;
import com.ssx.entity.DefaultTopProjectEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.util.regex.Matcher;

@SuppressWarnings("all")
public class SuperUtils {
    private static final transient Logger log = LoggerFactory.getLogger(SuperUtils.class);

    public static String tempFile2String(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream("./src/main/resources/" + fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int length = -1;
        while ((length = fis.read(buf)) != -1){
            bos.write(buf,0,length);
        }
        String applicationCode = bos.toString();
        bos.close();
        fis.close();
        return applicationCode;
    }
    public static void code2File(String codeString, File distFile) throws IOException {
        File dir = new File(fromAbsPathGetDir(distFile.getAbsolutePath()));
        if (!dir.exists()){
            dir.mkdirs(); log.info(dir.getAbsolutePath() + " created");
        }
        distFile.createNewFile();
        FileWriter fw = new FileWriter(distFile);
        fw.write(codeString);
        fw.close();
    }

    public static String filePathPostProcess(String filePath){
        return filePath.replaceAll("/|\\\\", Matcher.quoteReplacement(File.separator));
    }

    public static void createJavaAndTestDir(AbstractModuleEntity moduleEntity) {
        File jf = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/main/java"));
        jf.mkdirs(); log.info(jf.getAbsolutePath() + " created");
        File jrf = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/main/resources"));
        jrf.mkdirs(); log.info(jrf.getAbsolutePath() + " created");
        File tf = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/test/java"));
        tf.mkdirs(); log.info(tf.getAbsolutePath() + " created");
        //File trf = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/test/resources"));
        //trf.mkdirs(); log.info(tf.getAbsolutePath() + " created");
    }
    public static void createSettingFiles(AbstractModuleEntity moduleEntity) throws IOException {
        File dir = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/main/resources"));
        if (!dir.exists()) dir.mkdirs();
        for (String fn: moduleEntity.getSettingFileList()) {
            File file = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/main/resources/" + fn));
            file.createNewFile();
            log.info("    - [" + fn + "] created");
        }
    }

    public static void createProjectFolder(String newProDir) throws FileSystemException {
        File dir = new File(newProDir);
        if (dir.exists()) {
            log.error(dir.getAbsolutePath() + " already exist");
            throw new FileAlreadyExistsException(dir.getAbsolutePath() + " already exist");
        } else{
            boolean flag = dir.mkdirs();
            if (flag) log.info(dir.getAbsolutePath() + " created");
            else throw new FileSystemException(dir.getAbsolutePath() + " created failed");
        }
    }
    public static void createFolders4Resources(AbstractModuleEntity moduleEntity) {
        File dir = new File(filePathPostProcess(moduleEntity.getDirName()+"/src/main/resources"));
        if (!dir.exists()) {
            dir.mkdirs();
            log.info(dir.getAbsolutePath() + " created");
        }
        for (String ls: moduleEntity.getResourcesFolderList()) {
            File subDir = new File(filePathPostProcess(moduleEntity.getDirName() + "/src/main/resources/" + ls));
            subDir.mkdirs();
            log.info(subDir.getAbsolutePath() + " created");
        }
    }

    public static void createMybatisGenerator(AbstractModuleEntity moduleEntity) throws IOException {
        //.java
        String superBootMpGenCode = tempFile2String("mybatis-plus");
        superBootMpGenCode = superBootMpGenCode.replace("${outputDir}", filePathPostProcess(moduleEntity.getDirName() + "/src/main/java").replace("\\","/"));
        superBootMpGenCode = superBootMpGenCode.replace("${dataSourcePrefix}",DefaultTopProjectEntity.dataSourcePrefix);
        superBootMpGenCode = superBootMpGenCode.replace("${groupId}",moduleEntity.getGroupId());
        File superBootMpGenJava = new File(moduleEntity.getDirName() + "/src/main/java/" + moduleEntity.getGroupId().replace(".", "/") + File.separator + "SuperBootMpGen.java");
        code2File(superBootMpGenCode, superBootMpGenJava);

        //.resources
        String resources = tempFile2String("mybatis-plus-properties");
        resources = resources.replace("${dataSourcePrefix}", DefaultTopProjectEntity.dataSourcePrefix);
        File settings = new File(moduleEntity.getDirName() + "/src/main/resources/sbg-mpg.properties");
        code2File(resources, settings);

        log.info("    - created mybatis generator for [" + moduleEntity.getArtifactId() + "]");
    }

    public static void open(String newProDir) {
        try {
            log.info("--------------------------------------------------------");
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Windows")) {
                    log.info("os.name = " + osName + " trying to open the project ...");
                    Thread.sleep(500);
                    Runtime.getRuntime().exec("idea64 " + newProDir);
                    log.info("--------------------------------------------------------");
                }
                else log.warn("author is a foo");
            } else  log.warn("why os.name is null");
        } catch (Exception e){
            log.warn("oops, seems ur computer can not carry idea64 command");
        }
    }

    public static String fromDirGetName(String newProDir){
        return newProDir.substring(newProDir.lastIndexOf(File.separator) + 1);
    }
    public static String fromAbsPathGetDir(String absPath) throws IOException {
        return absPath.substring(0, absPath.lastIndexOf(File.separator));
    }
    public static String fromArtifactIdGetName(String artifactId){
        String[] split = artifactId.split("-|_");
        String applicationClassName = "";
        for (String s : split) {
            applicationClassName += StringUtils.capitalize(s);
        }
        return applicationClassName + "Application";
    }

}
