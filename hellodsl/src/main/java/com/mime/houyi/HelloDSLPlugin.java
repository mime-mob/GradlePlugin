package com.mime.houyi;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.invocation.DefaultGradle;

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

    @Override
    public void apply(Project project) {
        this.project = project;
        instantiator = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator.class);
        project.getGradle();
        NamedDomainObjectContainer<Book> BookContainer = project.container(Book.class, new BookFactory(instantiator, project));
        NamedDomainObjectContainer<Library> libraryContainer = project.container(Library.class, new LibraryFactory(instantiator));
        project.getExtensions().create("helloDSL",MyExtension.class,new Object[]{project,instantiator,BookContainer,libraryContainer});
    }

}
