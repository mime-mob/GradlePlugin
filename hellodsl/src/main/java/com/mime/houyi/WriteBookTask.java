package com.mime.houyi;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>write the books defined in the Gradle DSL to an given
 * directory ,and the book will be categorized by the libraries
 * defined in the book</p>
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class WriteBookTask extends DefaultTask{
    private File targetDirectory;
    private Map<Library,List<Book>> mBooks;

    @InputDirectory
    public File getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @Input
    public Map<Library, List<Book>> getBooks() {
        return mBooks;
    }

    public void setBooks(Map<Library, List<Book>> books) {
        mBooks = books;
    }

    @OutputDirectory
    public File getResultDirectory(){
        return new File(targetDirectory,"Libraries");
    }

    @TaskAction
    public void createLibraries(){

    }
}
