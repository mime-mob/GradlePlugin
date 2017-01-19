package com.mime.houyi;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

/**
 * <p>简单的自定义插件
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class HelloWorldPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.container(HelloManData.class);
        WriteHelloManTask task = project.getTasks().create("writeHello", WriteHelloManTask.class);
        task.setFileName("HelloWorld.txt");
        task.setHelloMan(new HelloManData("Jim",19));
        task.setTargetDirectory(new File("D:/workspace"));
        task.setGroup("hello");
    }
}
