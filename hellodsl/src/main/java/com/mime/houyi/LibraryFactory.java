package com.mime.houyi;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.internal.reflect.Instantiator;

/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class LibraryFactory implements NamedDomainObjectFactory<Library> {
    private Instantiator instantiator;
    public LibraryFactory(Instantiator instantiator){
        this.instantiator = instantiator;
    }
    @Override
    public Library create(String name) {
        return instantiator.newInstance(Library.class,new Object[]{name});
    }
}
