package com.mime.houyi;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.invocation.DefaultGradle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

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
        configWriteBookTask();
    }

    private void configWriteBookTask() {
        WriteBookTask writeBooks = mProject.getTasks().create("writeBooks", WriteBookTask.class);
        SortedMap<String, Book> asMap = mMyExtension.getBooks().getAsMap();
        SortedSet<String> names = mMyExtension.getLibraries().getNames();
        Map<Library, List<Book>> bookCategories = new HashMap<>();
        for (String name : names) {
            List<Book> books = new ArrayList<>();
            for (String book : asMap.keySet()) {
                if (asMap.get(book).getBookLocation().getName().equals(name)) {
                    books.add(asMap.get(book));
                }
            }
            bookCategories.put(mMyExtension.getLibraries().getByName(name), books);
        }


    }

    private void configExtension() {
        NamedDomainObjectContainer<Book> bookContainer = mProject.container(Book.class,
                new BookFactory(mInstantiator, mProject));
        NamedDomainObjectContainer<Library> libraryContainer = mProject.container(Library.class,
                new LibraryFactory(mInstantiator));
        mMyExtension = mProject.getExtensions().create("bookManager", MyExtension.class,
                new Object[]{mProject, mInstantiator, bookContainer, libraryContainer});
    }

}
