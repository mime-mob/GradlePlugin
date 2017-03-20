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
 * @since [产品/模块版本].
 */


public class Book implements Serializable {
    private static final long serialVersionUID = 4933666750282521704L;
    final String mName;
    private Project mProject;
    private Instantiator mInstantiator;
    private Library bookLocation;
    private String description;

    private Book(String name) {
        this.mName = name;
    }

    /**
     *
     */
    public Book(String name, Instantiator instantiator, Project project){
        this.mName = name;
        this.mInstantiator = instantiator;
        this.mProject = project;
    }

    public String getName() {
        return mName;
    }

    public Project getProject() {
        return mProject;
    }

    public void setProject(Project project) {
        this.mProject = project;
    }

    public Instantiator getInstantiator() {
        return mInstantiator;
    }

    public void setInstantiator(Instantiator instantiator) {
        this.mInstantiator = instantiator;
    }

    public Library getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(Library bookLocation) {
        this.bookLocation = bookLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
