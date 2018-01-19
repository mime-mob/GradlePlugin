package com.mime.houyi;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.invocation.DefaultGradle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * <p>write the description</p>
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本].
 */


public class HelloDSLPlugin implements Plugin<Project> {
    private Project mProject;
    private Instantiator mInstantiator;
    private MyExtension mMyExtension;

    @Override
    public void apply(Project project) {
        this.mProject = project;
        mInstantiator = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator.class);
        configExtension();
        mProject.afterEvaluate(project1 -> configWriteBookTask());
    }

    private void configWriteBookTask() {
        WriteBookTask writeBooks = mProject.getTasks().create("writeBooks", WriteBookTask.class);
        SortedMap<String, Book> asMap = mMyExtension.getBooks().getAsMap();
        SortedSet<String> libraryNames = mMyExtension.getLibraries().getNames();
        if(asMap.isEmpty()||libraryNames.isEmpty()){
            return;
        }
        ArrayList<String> names = new ArrayList<>(libraryNames);
        Map<Library, List<Book>> bookCategories = new HashMap<>();
        for (String name : libraryNames) {
            List<Book> books = new ArrayList<>();
            for (String book : asMap.keySet()) {
                if (asMap.get(book).getBookLocation().getName().equals(name)) {
                    books.add(asMap.get(book));
                }
            }
            bookCategories.put(mMyExtension.getLibraries().getByName(name), books);
        }
        writeBooks.setLibraries(names);
        writeBooks.setBooks(bookCategories);
        File writeBook = new File(mProject.getProjectDir(),"writeBook");
        if(!writeBook.exists()){
            writeBook.mkdir();
        }
        writeBooks.setTargetDirectory(writeBook);
        writeBooks.createLibraries();
    }

    private void configExtension() {
        NamedDomainObjectContainer<Book> bookContainer = mProject.container(Book.class,
                new BookFactory(mInstantiator));
        NamedDomainObjectContainer<Library> libraryContainer = mProject.container(Library.class,
                new LibraryFactory(mInstantiator));
        mMyExtension = mProject.getExtensions().create("bookManager", MyExtension.class,
                new Object[]{mProject, mInstantiator, bookContainer, libraryContainer});
    }

}
