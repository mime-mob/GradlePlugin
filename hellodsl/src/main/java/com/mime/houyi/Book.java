package com.mime.houyi;

import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;

import java.io.Serializable;

/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class Book implements Serializable {
    private static final long serialVersionUID = 4933666750282521704L;
    final String name;
    private Project project;
    private Instantiator instantiator;
    private Library bookLocation;

    private Book(String name) {
        this.name = name;
    }

    public Book(String name, Instantiator instantiator, Project project){
        this.name = name;
        this.instantiator = instantiator;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Instantiator getInstantiator() {
        return instantiator;
    }

    public void setInstantiator(Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    public Library getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(Library bookLocation) {
        this.bookLocation = bookLocation;
    }
}
