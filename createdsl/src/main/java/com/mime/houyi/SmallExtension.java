package com.mime.houyi;

/**
 * <p>write the description
 *
 * @author houyif2@outlook.com
 * @version 1.1
 * @see SmallExtensionFactory
 * @since 1.1
 */
public class SmallExtension {
    final String mName;
    private String mExtensionName;

    public SmallExtension(String name) {
        this.mName = name;
    }

    public String getExtensionName() {
        return mExtensionName;
    }

    public void setExtensionName(String extensionName) {
        mExtensionName = extensionName;
    }

    public String getName() {
        return mName;
    }
}
