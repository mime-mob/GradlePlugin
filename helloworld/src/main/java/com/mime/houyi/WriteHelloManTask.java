package com.mime.houyi;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class WriteHelloManTask extends DefaultTask {
    private HelloManData helloMan;
    private File targetDirectory;
    private String fileName;
    private File targetFile;

    @Nested
    public HelloManData getHelloMan(){
        return helloMan;
    }

    @OutputFile
    public File getTargetFile(){
        return targetFile;
    }

    @Input
    public String getFileName(){
        return fileName;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    @InputDirectory
    public File getTargetDirectory() {
        return targetDirectory;
    }

    @TaskAction
    public void writeObject(){
        targetFile = new File(targetDirectory, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] bytes = helloMan.toString().getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHelloMan(HelloManData helloMan) {
        this.helloMan = helloMan;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
