package com.mime.houyi;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * <p>write the description
 *
 * @author houyif2@outlook.com
 * @version 1.1
 * @since 1.1
 */


public class CreateDSLPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        MyExtension mMyExtension = project.getExtensions().create("myExtension", MyExtension.class);
    }
}
