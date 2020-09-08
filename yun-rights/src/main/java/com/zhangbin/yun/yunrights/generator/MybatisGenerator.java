package com.zhangbin.yun.yunrights.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Running MyBatis Generator With Java
 * http://mybatis.org/generator/running/runningWithJava.html
 */
public class MybatisGenerator {
    public static void main(String[] args) {
        generate("generator/mybatis-generator-rights-config.xml");
//        generate("generator/mybatis-generator-email-config.xml");
//        generate("generator/mybatis-generator-logging-config.xml");
    }

    private static void generate(String generatorConfigXml)  {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        ClassLoader classLoader = MybatisGenerator.class.getClassLoader();
        try {
            Configuration config = cp.parseConfiguration(classLoader.getResourceAsStream(generatorConfigXml));
            // 解决IDEA下运行，多个模块路径冲突问题
            String cpath = classLoader.getResource(generatorConfigXml).toString();
            cpath = cpath.substring(0, cpath.indexOf("target")).replace("file:/", "");
            Context context = config.getContexts().get(0);
            context.getJavaModelGeneratorConfiguration().setTargetProject(cpath + context.getJavaModelGeneratorConfiguration().getTargetProject());
            context.getSqlMapGeneratorConfiguration().setTargetProject(cpath + context.getSqlMapGeneratorConfiguration().getTargetProject());
            context.getJavaClientGeneratorConfiguration().setTargetProject(cpath + context.getJavaClientGeneratorConfiguration().getTargetProject());
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
