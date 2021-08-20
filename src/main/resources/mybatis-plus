package ${groupId};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SuperBootMpGen {
    // Do not touch -------------------------------
    private static final Properties SETTINGS = new Properties();
    private static String autoSolveDataSourceConfigFileType = "";
    // user settings ---------------------------------------------------------

    // globalConfig | Dist = outputDir + packageParent + moduleName
    private static final String author = "super-boot-gen";
    private static final String outputDir = "${outputDir}";
    private static final boolean fileOverride = false;
    private static final boolean open = false;
    private static final boolean enableCache = false;
    private static final boolean kotlin = false;
    private static final boolean swagger2 = false;
    private static final boolean activeRecord = false;
    private static final boolean baseResultMap = false;
    private static final DateType dateType = DateType.ONLY_DATE;
    private static final String entityName = "%s";
    private static final String mapperName = "%sDao";
    private static final String xmlName = "%sDaoMapper";
    private static final String serviceName = "%sService";
    private static final String serviceImplName = "%sServiceImpl";
    private static final String controllerName = "%sController";
    private static final IdType idType = IdType.ASSIGN_ID;

    // dataSourceConfig
    private static final String schemaName = "public";
    private static String dataSourcePrefix = "${dataSourcePrefix}";
    // more DataSourceConfig do it yourself

    // packageConfig
    private static final String packageParent = "${groupId}";
    private static final String moduleName = "";
    private static final String entity = "entity";
    private static final String service = "service";
    private static final String serviceImpl = "service.impl";
    private static final String mapper = "dao";
    private static final String xml = "dao";
    private static final String controller = "controller";

    // strategyConfig
    private static final String[] targetTables = {

    };
    private static final String[] tablePrefix = {
        "",
    }; // leave "" for default
    private static final NamingStrategy naming = NamingStrategy.underline_to_camel;
    private static final NamingStrategy columnNaming = NamingStrategy.underline_to_camel;
    private static final boolean entitySerialVersionUID = true;
    private static final boolean chainModel = false;
    private static final boolean entityLombokModel = false;
    private static final boolean restControllerStyle = false;
    private static final boolean controllerMappingHyphenStyle = true;
    private static final boolean entityTableFieldAnnotationEnable = true;
    // more StrategyConfig do it yourself

    static {
        String settingsFilesPath = SuperBootMpGen.class.getResource("/").getPath();
        File file = new File(settingsFilesPath);
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.getName().toLowerCase().endsWith(".properties")
                    || f.getName().toLowerCase().endsWith(".yml")
                    || f.getName().toLowerCase().endsWith(".yaml")){
                try {
                    InputStream is = new FileInputStream(f.getAbsoluteFile());
                    SETTINGS.load(is);
                    // yaml yml or properties
                    autoSolveDataSourceConfigFileType = f.getName().substring(f.getName().lastIndexOf(".") + 1);

                    String url = getPropertyByType("url", autoSolveDataSourceConfigFileType);
                    String driverName = getPropertyByType("driver-class-name", autoSolveDataSourceConfigFileType);
                    String username = getPropertyByType("username", autoSolveDataSourceConfigFileType);
                    String password = getPropertyByType("password", autoSolveDataSourceConfigFileType);

                    if (null != url && !"".equals(url) && null != driverName && !"".equals(driverName) && null != username && !"".equals(username) && null != password && !"".equals(password)){
                        System.out.println(f.getName() + " will be the settings file");
                        break;
                    }

                    autoSolveDataSourceConfigFileType = "";
                    SETTINGS.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if ("".equals(autoSolveDataSourceConfigFileType)) throw new RuntimeException("no dataSource configuration, please check sbg-mpg.properties");
    }

    public static void main(String[] args) throws IOException {

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                .setOutputDir(outputDir)
                .setFileOverride(fileOverride)
                .setOpen(open)
                .setEnableCache(enableCache)
                .setAuthor(author)
                .setKotlin(kotlin)
                .setSwagger2(swagger2)
                .setActiveRecord(activeRecord)
                .setBaseResultMap(baseResultMap)
                .setDateType(dateType)
                .setEntityName(entityName)
                .setMapperName(mapperName)
                .setXmlName(xmlName)
                .setServiceName(serviceName)
                .setServiceImplName(serviceImplName)
                .setControllerName(controllerName)
                .setIdType(idType);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl(getPropertyByType("url", autoSolveDataSourceConfigFileType))
                .setDriverName(getPropertyByType("driver-class-name", autoSolveDataSourceConfigFileType))
                .setUsername(getPropertyByType("username", autoSolveDataSourceConfigFileType))
                .setPassword(getPropertyByType("password", autoSolveDataSourceConfigFileType))
                .setSchemaName(schemaName);

        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setParent(packageParent)
                .setModuleName(moduleName)
                .setEntity(entity)
                .setService(service)
                .setServiceImpl(serviceImpl)
                .setMapper(mapper)
                .setXml(xml)
                .setController(controller);

        StrategyConfig strategyConfig = new StrategyConfig();
        for (String table : targetTables) strategyConfig.setInclude(table);
        strategyConfig
                .setTablePrefix(tablePrefix)
                .setNaming(naming)
                .setColumnNaming(columnNaming)
                .setEntitySerialVersionUID(entitySerialVersionUID)
                .setChainModel(chainModel)
                .setEntityLombokModel(entityLombokModel)
                .setRestControllerStyle(restControllerStyle)
                .setControllerMappingHyphenStyle(controllerMappingHyphenStyle)
                .setEntityTableFieldAnnotationEnable(entityTableFieldAnnotationEnable);

//        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
//        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
//        strategyConfig.setTableFillList(new ArrayList<>(Arrays.asList(gmtCreate,gmtModified)));
//        strategyConfig.setLogicDeleteFieldName("deleted");
//        strategyConfig.setVersionFieldName("version");

        TemplateConfig templateConfig = new TemplateConfig();

        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                //.setTemplate(templateConfig)
                //.execute()
        ;
    }

    private static String getPropertyByType(String key, String type){
        if (!dataSourcePrefix.endsWith(".")) dataSourcePrefix += ".";
        if ("properties".equals(type.toLowerCase())) key = dataSourcePrefix + key;
        return SuperBootMpGen.SETTINGS.getProperty(key);
    }
}
