package com.mime.houyi;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.invocation.DefaultGradle;

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
        Instantiator instantiator = ((DefaultGradle) project.getGradle()).getServices().get(
                Instantiator.class);
        NamedDomainObjectContainer<SmallExtension> smallExtensionsContainer = project.container(
                SmallExtension.class, new SmallExtensionFactory(instantiator));
        MyExtension mMyExtension = project.getExtensions().create("myExtension", MyExtension.class,
                new Object[]{instantiator,smallExtensionsContainer});
        //create方法的第三个参数是MyExtension的构造参数
    }
}
