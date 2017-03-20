package com.mime.houyi;

/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

public class LibraryManager {
    private String managerName;
    private boolean emptyLibraryUsable;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public boolean isEmptyLibraryUsable() {
        return emptyLibraryUsable;
    }

    public void setEmptyLibraryUsable(boolean emptyLibraryUsable) {
        this.emptyLibraryUsable = emptyLibraryUsable;
    }
}
