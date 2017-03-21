package com.mime.houyi;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.internal.reflect.Instantiator;

/**
 * <p>write the description
 *
 * @author houyif2@outlook.com
 * @version 1.1
 * @see SmallExtension
 * @since 1.1
 */
public class SmallExtensionFactory implements NamedDomainObjectFactory<SmallExtension> {

    private Instantiator mInstantiator;

    public SmallExtensionFactory(Instantiator instantiator) {
        this.mInstantiator = instantiator;
    }

    @Override
    public SmallExtension create(String name) {
        return mInstantiator.newInstance(SmallExtension.class, name);
    }
}
