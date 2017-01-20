package com.mime.houyi;

import org.gradle.api.DefaultTask;

import java.io.File;
import java.util.List;

/**
 * <p>write the books defined in the Gradle DSL to an given
 * directory ,and the book will be categorized by the libraries
 * defined in the book</p>
 *
 * @author houyi
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */


public class WriteBookTask extends DefaultTask {
    private File targetDirectory;
    private List<Book> mBooks;
}
