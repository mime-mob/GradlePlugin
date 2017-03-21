package com.mime.houyi;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.internal.reflect.Instantiator;

/**
 * <p>write the description</p>
 *
 * @author houyif2@outlook.com
 * @version 1.1
 * @see CreateDSLPlugin
 * @since 1.1
 */


public class MyExtension {
    private String mExtensionName;
    private InnerExtension mInnerExtension;
    private NamedDomainObjectContainer<SmallExtension> mSmallExtensions;

    public MyExtension(Instantiator instantiator,
            NamedDomainObjectContainer<SmallExtension> smallExtensions) {
        mInnerExtension = instantiator.newInstance(InnerExtension.class);
        mSmallExtensions = smallExtensions;
    }

    public String getExtensionName() {
        return mExtensionName;
    }

    public void setExtensionName(String extensionName) {
        this.mExtensionName = extensionName;
    }

    public InnerExtension getInnerExtension() {
        return mInnerExtension;
    }

    public void innerExtension(Action<InnerExtension> action) {
        //这里方法名写成setInnerExtension,gradle也是可以会调用的，
        //但是为了防止意外，还是写出我们想要的名字
        action.execute(mInnerExtension);
    }

    public NamedDomainObjectContainer<SmallExtension> getSmallExtensions() {
        return mSmallExtensions;
    }

    public void smallExtensions(
            Action<? super NamedDomainObjectContainer<SmallExtension>> action) {
        //这里还是是用action的方式来接受buildscript的参数
        //这里方法名写成setSmallExtensions,gradle就无法找到对应的方法了
        action.execute(mSmallExtensions);
    }
}
