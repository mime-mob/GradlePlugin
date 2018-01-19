package com.mime.houyi;

import java.io.Serializable;

/**
 * <p>write the description
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class Library implements Serializable{
    private static final long serialVersionUID = 79987269263359336L;
    private final String mName;
    private String libraryDetail;
    private Integer bookCount;

    public Library(String name) {
        mName = name;
    }

    public Library(String name, String libraryDetail, int bookCount) {
        this.mName = name;
        this.libraryDetail = libraryDetail;
        this.bookCount = bookCount;
    }

    public String getName() {
        return mName;
    }

    public String getLibraryDetail() {
        return libraryDetail;
    }

    public void setLibraryDetail(String libraryDetail) {
        this.libraryDetail = libraryDetail;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}
