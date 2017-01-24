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
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class HelloDSLPlugin implements Plugin<Project> {
    private Project project;
    private Instantiator instantiator;
    private MyExtension myExtension;

    @Override
    public void apply(Project project) {
        this.project = project;
        instantiator = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator.class);
        configExtension();
        configWriteBookTask();
    }

    private void configWriteBookTask() {
        WriteBookTask writeBooks = project.getTasks().create("writeBooks", WriteBookTask.class);
        SortedMap<String, Book> asMap = myExtension.getBooks().getAsMap();
        SortedSet<String> names = myExtension.getLibraries().getNames();
        Map<Library,List<Book>> bookCategories = new HashMap<>();
        for (String name : names) {
                List<Book> books = new ArrayList<>();
            for (String book : asMap.keySet()) {
                if(asMap.get(book).getBookLocation().getName().equals(name)){
                    books.add(asMap.get(book));
                }
            }
            bookCategories.put(myExtension.getLibraries().getByName(name),books);
        }


    }

    private void configExtension() {
        NamedDomainObjectContainer<Book> BookContainer = project.container(Book.class, new BookFactory(instantiator, project));
        NamedDomainObjectContainer<Library> libraryContainer = project.container(Library.class, new LibraryFactory(instantiator));
        myExtension = project.getExtensions().create("helloDSL", MyExtension.class, new Object[]{project, instantiator, BookContainer, libraryContainer});
    }

}
