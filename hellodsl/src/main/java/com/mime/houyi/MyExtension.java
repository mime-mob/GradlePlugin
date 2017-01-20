package com.mime.houyi;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;


/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class MyExtension {
    private NamedDomainObjectContainer<Book> books;
    private NamedDomainObjectContainer<Library> libraries;
    private Project project;

    public MyExtension(Project project, Instantiator instantiator,NamedDomainObjectContainer<Book> bookContainer,NamedDomainObjectContainer<Library> libraryContainer) {
        this.project = project;
        this.books = bookContainer;
        this.libraries = libraryContainer;
    }

    public NamedDomainObjectContainer<Book> getBooks() {
        return books;
    }

    public NamedDomainObjectContainer<Library> getLibraries() {
        return libraries;
    }

    public Project getProject() {
        return project;
    }

    public void books(Action<? super NamedDomainObjectContainer<Book>> action){
        action.execute(this.books);
    }
    public void libraries(Action<? super NamedDomainObjectContainer<Library>> action){
        action.execute(this.libraries);
    }
}
