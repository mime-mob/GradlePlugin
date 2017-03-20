package com.mime.houyi;

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

    public MyExtension() {

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

    public void setInnerExtension(InnerExtension innerExtension) {
        mInnerExtension = innerExtension;
    }
}
