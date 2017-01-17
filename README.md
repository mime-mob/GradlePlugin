## 前言
学习Gradle也有一段时间了，感觉知道了很多，但是还是有些朦朦胧胧，这时候就该写点代码来融会贯通一下， 于是便决定做一个简单的插件来真正理解一下Gradle

## 插件开发
在开始之前，我们要知道，插件是做什么的，Gradle的插件类似java中的jar包，主要用于代码复用和逻辑封装，复杂的如同java插件，提供了一整套的java编译系统，简单的也可以就仅仅只是封装一些共有的方法、task之类，这里我们就由浅入深，从最简单的插件说起。

#### 语言选择
[Gradle User Guide](https://docs.gradle.org/current/userguide/userguide_single.html#custom_plugins)里面的原话是“You can implement a custom plugin in any language you like”，虽然是这么说，但是我也见过用groovy，js，java这些语言实现的插件，并没有见过c++实现的插件，本文选择使用的是java，毕竟groovy不熟

#### 插件形式
在Gradle中，一个插件并不是什么很神奇的东西，它其实就是Class，只是实现的一个plugin的接口，有了一个apply的方法，能够被apply到构建脚本中，它可以直接写在build.gradle的脚本中（当然这样就只能使用java或是groovy的语法），可以写在/src/main这样的资源目录下，当然也可以作为一个jar包导入 ，考虑到插件的用途，把插件打包成一个jar也算是必需的

#### 简单插件
首先是开发工具的选择，这里我使用的是AndroidStudio（据说更好的是使用IntelliJ Idea，但是我在尝试了几个小时之后放弃了，还是Studio好），首先创建一个工程，然后新建个Module（类型无所谓，反正我们只用用这个目录而已），然后开始目录改造，该删的删，该建的建，最后的目录结构如下：

![目录结构.png](http://upload-images.jianshu.io/upload_images/3789849-bb1e73fe8e77810d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
res和androidTest这两个目录直接删掉，然后新建`src/main/resources/META-INF/gradle-plugins`目录，这个目录算是插件id的索引，只有通过这个目录定义了插件的id和与之关联的插件类，比如上面，我们定义了一个`com.mime.houyi.helloworld.preperties`的文件，那我们的插件id就是`com.mime.houyi.helloworld`，至于关联的类后面再细说。
做完上面这里，下面就直接上代码了，首先是build.gradle这里我原本建的是一个Android Library的Module
