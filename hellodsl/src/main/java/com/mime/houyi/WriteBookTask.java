package com.mime.houyi;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private File targetDirectory = new File("");
    private Map<Library, List<Book>> mBooks;
    private ArrayList<String> mLibraries;

    @InputDirectory
    public File getTargetDirectory() {
        return  targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public Map<Library, List<Book>> getBooks() {
        return mBooks;
    }

    public void setBooks(Map<Library, List<Book>> books) {
        mBooks = books;
    }

    @Input
    public ArrayList<String> getLibraries(){
        return mLibraries;
    }

    public void setLibraries(ArrayList<String> libraries){
        this.mLibraries = libraries;
    }

    @OutputDirectory
    public File getResultDirectory(){
        return  targetDirectory;
    }

    @TaskAction
    public void createLibraries(){
        for (Library library : mBooks.keySet()) {
            createLibrary(library,mBooks.get(library));
        }
    }

    private void createLibrary(Library library, List<Book> books) {
        File libraryDir = new File(getResultDirectory(),library.getName());
        if(!libraryDir.exists()){
            libraryDir.mkdir();
        }
        for (Book book : books) {
            File file = new File(libraryDir, book.getName());
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
