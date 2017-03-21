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
做完上面这里，下面就直接上代码了，首先是build.gradle这里我原本建的是一个Android Library的Module，所以原本的内容全部删掉，新建内容如下

    apply plugin: 'java'//导入java插件用于，编译打包我们的插件
    apply plugin: 'maven'//maven插件，用于上传插件到仓库

    //uploadArchives 类型是upload，这个task不是'maven'创建的
    //而是'maven'定义了一个rule,而后由我们自己创建的，关于rule，请看后面内容
    uploadArchives{
        //本地仓库的一种
        repositories{
            flatDir{
                name "localRepository"
                dir "localRepository/libs"
            }
        }
    }
    group = "com.mime.houyi"//project属性
    version = "1.0"//project属性
    dependencies {
        //导入Gradle的api，要写插件，肯定要使用Gradle的api
        compile gradleApi()
    }
我们先分析一下上面代码，很多东西在注释中已经提到，我就不一一赘述了,`uploadArchives`这个Task虽然不是maven插件创建的，但是这里可以不做太多关注，后面对于这种会更详细讲解，这里姑且认为它就是maven插件创建的，用于上传整个工程的jar包。这里我们主要说明一下repositories的几种仓库定义（*这个属于题外话，既然用到了就解释一下，方便更好的理解*）
##### 几种仓库说明
首先是上面的使用flatDir方法创建一个仓库，定义比较简单，属性也就上面两个，name和dir，使用类似如下

    repositories {
        flatDir name: 'libs', dirs: "$projectDir/libs"
        flatDir dirs: ["$projectDir/libs1", "$projectDir/libs2"]
    }
第二种是ivy仓库，ivy是[Apache Ant](https://en.wikipedia.org/wiki/Apache_Ant)的子项目，一种类似maven的仓库，搭建和使用请自行Google，这里配置方法也很简单

    repositories {
        ivy {
            //这里url可以是远程地址，也可以是本地地址
            url "http://repo.mycompany.com/repo"
        }
    }
第三种是maven仓库，使用和ivy仓库一样

    repositories {
        maven {
            url "http://repo.mycompany.com/maven2"
        }
    }
第四种是jcenter，Bintray的JCenter仓库，这个用的多，不多说
第五种是mavenCentral，这个类似jcenter，也不多说
第六种是mavenLocal，这个是一个默认的本地仓库，具体位置可以进行配置,如果没有配置默认是在(user)/.m2/repository

##### 插件实现
如果仅仅只是实现一个插件，那是非常的简单，在java目录下创建一个class，实现Plugin接口

    public class HelloWorldPlugin implements Plugin<Project> {
        @Override
        public void apply(Project project) {
            project.getTasks().create("hello", DefaultTask.class);
        }
    }
可以看到我们上面的代码几乎没有做什么，仅仅只是在project中创建了一个名为hello的task，TaskType是DefaultTask，不过也可以看出来，我们去apply一个插件，事实上是把我们编译脚本的project对象作为一个参数传给了插件
做完上面这一步，事实上Gradle并不能找到我们的插件，这时候就需要`MATE-INF/gradle-plugins`这个目录下的properties文件了，我们已经建好了，然后

    implementation-class=com.mime.houyi.HelloWorldPlugin
我们的插件就已经完成了，虽然这个插件没有什么功能，接下来，我们使用运行uploadArchives这个task把我们的插件打包成jar，上传到我们先前的那个flatDir创建的仓库。接下来可以实测一下我们的插件是否有问题
首先在整个工程的build.gradle下添加

     buildscript {
        repositories {
            jcenter()
            flatDir  name:'localRepository',dir:"helloworld/localRepository/libs'
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:2.2.3'
            classpath 'com.mime.houyi:helloworld:1.0'//命名是我们的groupId：moduleName：version
        }
    }
然后在app的build.gradle下添加

    apply plugin: 'com.mime.houyi.helloworld'
可以看到，在Gradle的task视窗里面:app/other下多了一个hello的task，到此最最简单的插件就完成的。**Then Next**

####自定义Task
在开始之前我们再次对Task的做一个介绍，Gradle中所有的Task都是继承自DefaultTask，我们可以把它理解成Task中的Object类，所有的Task都是继承自DefaultTask（虽然DefaultTask也是实现了一个Task接口，但是我们不会去直接实现Task接口），Task有什么，是做什么的，这里我们用Gradle文档里面的一张图来做一个解释
![](https://docs.gradle.org/current/userguide/img/taskInputsOutputs.png)
在正常情况下，一个task都是有一些输入，处理然后产出一些输出，我们之前也在build.gradle中写过简单的task，使用doLast，但是这种类型的task没有输入输出的概念，真正要实现一个类似于java中的jar这种的插件，我们就得去写一个自定义的Task
##### 基本概念
在开始之前，有几个基本概念需要认识一下
* 输出：Task的目标，我们的task都是为了，达到一个目的，这就是输出，比如jar的输出是jar文件，copy的输出是目标目录
* 输入：只有会影响一个或多个输出结果的才算是输入
* 属性：会影响task的执行过程，但是不会影响结果
* action：这个官方文档没有提到，我自己加的，具体的处理过程

为什么Gradle中的Task会有这几种的类型区别，主要是在Gradle中支持Incremental Build
什么？你不知道这是什么东西，打开AndroidStudio的Gradle Console你就会看到一堆的UP-TO-DATE，这就是Incremental Build，为了加快构建的速度，Gradle每次执行一个task之前就会检查task的输入和输出，如果和上次的相比都没没有变化，那么Gradle就会认为这个task是up to date的，从而跳过这个task，以此来加快构建的速度。这也就是在Gradle Console中出现的UP-TO-DATE。

关于输入输出，Gradle支持三种类型的输入输出
* 简单类型
String，int几种简单类型必须是可以的，但这里只是的任何实现了Serializable的类
* 文件类型
包括java中的File和Files以及Gradle中的FileCollection类型
* Nested类型
自定义的类型，不属于上面两种，但是类型里的属性都属于上面两种

#####具体实现
首先是我们Task类

    public class WriteHelloManTask extends DefaultTask {
        private HelloManData helloMan;
        private File targetDirectory;
        private String fileName;

        @Nested
        public HelloManData getHelloMan(){
            return helloMan;
        }

        @OutputFile
        public File getTargetFile(){
            return new File(targetDirectory, fileName);
        }

        @Input
        public String getFileName(){
            return fileName;
        }

        @InputDirectory
        public File getTargetDirectory() {
            return targetDirectory;
        }

        @TaskAction
        public void writeObject(){
            File targetFile = new File(targetDirectory, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(targetFile);
                byte[] bytes = helloMan.toString().getBytes();
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setHelloMan(HelloManData helloMan) {
            this.helloMan = helloMan;
        }

        public void setTargetDirectory(File targetDirectory) {
            this.targetDirectory = targetDirectory;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
HelloManData 类

    public class HelloManData {
        private String name;
        private int age;

        public HelloManData(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Input
        public String getName() {
            return name;
        }

        @Input
        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Hello "+age+" years old "+name+" !";
        }
    }
上面的代码，一眼就能看到诸如`Input`、`Output`、`TaskAction`之类的注解，这些注解就是用来标记输入，输出以及action的，因为Gradle的Incremental Build机制，我们必须把输入都标记上才能正确的运行（如果没有标记，可能会出现输入改变了，但是Task却跳过了），但是同时Gradle也不建议把不会影响输出的属性标记为输入，这样会在整体上降低Gradle的构建速度，下面我就一张列表来看下，我们可能会使用到的注解

注解名称|文件类型|详情
---|---|---
@Input|serializable的类型|也就是前面所说的简单类型
@InputFile|File*|单个的文件类型的输入（非文件夹）
@InputDirectory|File*|单个的文件类型的输入（非文件）
@InputFiles|Iterable<File>*|可Iterable的文件或者文件夹的集合类型的输入
@Nested|自定义类|没有实现serializable，但是至少有一个属性使用表里的注解，包括@Nested注解
@OutputFile|File*|单个的文件类型的输出（非文件夹）
@OutputDirectory|File*|单个的文件类型的输入（非文件）
@OutputFiles|Map<String, File>** 或者Iterable<File>*|可Iterable的文件的集合类型的输出（非文件夹）
@OutputDirectories|Map<String, File>** 或者Iterable<File>*|可Iterable的文件夹的集合类型的输出（非文件）
@TaskAction|-|用于标注Task真正执行的action

从表中我们可以看出，Gradle对于Task的输出偏向于文件方面，同时Task的定义也是大致如此，接下来我们就可以在我们的插件中使用这个Task

    public class HelloWorldPlugin implements Plugin<Project> {
        @Override
        public void apply(Project project) {
            WriteHelloManTask task = project.getTasks().create("writeHello", WriteHelloManTask.class);
            task.setFileName("HelloWorld.txt");
            task.setHelloMan(new HelloManData("Jim",19));
            task.setTargetDirectory(new File("D:/workspace"));
            task.setGroup("hello");
        }
    }
修改一下version，重复一次之前的uploadArchives操作，我们可以在:app的Task下看到一个新的类别hello（我不会告诉你，我仅仅只是因为好找，因为other下的task实在是太多了），运行writeHello的task，就可以看到我们的相应目录下多了一个HelloWorld.txt的文件，然后再次运行，我们就可以看到

    :app:writeHello UP-TO-DATE
Task直接跳过了，因为Gradle看来输入输出和上一次没有变化。到此为止，我们一个相对简单插件就能够完成了，但是看起来和我们平时使用的插件还是有一定的差距，所以让我们继续下一节

####关于DSL
前面我们已经提到过DSL（领域专用语言/domain specific language），虽然还没有学过实现DSL，但我们已经使用了很多了，是否需要定义DSL，是根据需求来的，很多插件并不需要定义DSL，需要定义DSL的大多都是需要和使用者交互，使用DSL，使用者不用关心我们的实现方式，只需要通过DSL就可以完成他们需要的配置。
#####几个重要的点（或者说是类，概念）
* Extension
通过Extension，我们可以向目标对象添加DSL扩展，这一过程通过project中的ExtensionContainer来实现，我们可以通过ExtensionContainer的create来创建新的DSL域，并与一个对应的委托类关联起来（即新建一个DSL域，并委托给一个具体类）
* Convention
与Extension类似，但是又有所不同，通过Convention的getPlugin方法，我们会把一个类融合到Convention所在的域，而不是新建一个域（具体区别可以参考java插件和android插件）
* NamedDomainObjectContainer
命名对象容器，可以用于在buildscript中创建对象,创建的对象必须要有name属性作为容器内元素的标识
* Instantiator
Instantiator 用于实例化对象， 使用Instantiator而不直接使用new，是因为使用Instantiator实例化对象时，会添加DSL特性
* ext 是project的一个属性，维持一个命名空间，用于为project添加键值对属性，其实与DSL无关，只是形式和类型上和extension类似

##### 新建DSL
简单插件的创建过程，就和上面一样创建一个名为createdsl的model，我就不一一赘述，创建DSL我们需要使用前面提到的Extension，在代码中我们可以使用`project.getExtension()`获取Extension（ExtensionContainer的实现类）。通过ExtensionContainer的`create`方法创建一个新的DSL

    MyExtension mMyExtension = project.getExtensions().create("myExtension", MyExtension.class);
`MyExtension`对象（一个简单的实体类）

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

这样我们就成功创建了一个DSL，在apply过我们的plugin之后，我们就可以在buildscript中定义这个DSL了，我们可以直接配置简单属性如String、Boolean之类,但是直接定义对象类就会出错

    myExtension {
        extensionName "ddd" //这行代码没有 问题，可以正常通过
        innerExtension{ //这个会报错
        }
    }
> 关于上面`extensionName`这个属性名，大家可能会有所疑惑，因为我们上面定义的成员名是`mExtensionName`，这里这个属性名其实是根据set方法生成的，这个属性赋值，实质上也是调用的set方法。

对于普通对象型的extension属性，我们如果想要在buildscript中直接定义它的话，就需要使用上面说过的Instantiator。

这里希望大家有一个概念，我们写在buildscript中的myExtension并不直接就是我们代码中的mMyExtension对象， 这中间存在对应的关系，对于MyExtension我们通过`extension.create`来实现这一对应(其实在create方法中也是通过Instantiator来实现的)，但是对于MyExtension中的一个普通成员对象innerExtension，我们需要使用Instantiator实现这一关系。

修改我们的代码,主要是两个地方，一个是apply方法

    Instantiator instantiator = ((DefaultGradle) project.getGradle()).getServices().get(
            Instantiator.class);
    MyExtension mMyExtension = project.getExtensions().create("myExtension", MyExtension.class,
            new Object[]{instantiator});
    //create方法的第三个参数是MyExtension的构造参数
另一个是MyExtension构造方法

    public MyExtension(Instantiator instantiator) {
        mInnerExtension = instantiator.newInstance(InnerExtension.class);
    }
但是这样还不够，我们在上面说到，gradle是根据set方法来在buildscript中定义属性的，我们上面在buildscript中传递给set方法的是scriptblock块，所以我们的set方法要接受一个scriptblock块。。。

![](http://upload-images.jianshu.io/upload_images/3789849-ed0af6300998ff6e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
不过还好，gradle中定义了一个Action的interface，就是用于处理这种场景，让我们修改InnerExtension的set方法

    public void innerExtension(Action<InnerExtension> action) {
        //这里方法名写成setInnerExtension,gradle也是可以会调用的，
        //但是为了防止意外，还是写出我们想要的名字
        action.execute(mInnerExtension);
    }
修改后的set方法接受一个action类型的参数，perfect！

除了上面所说的简单类型和对象类型，大家可能还见过另一种，比如android中的buildTypes、productFlavors,这种类型可以用于在buildscript中创建新的指定类型的对象，也就是上面提到的NamedDomainObjectContainer,但是,How to use?这种时候就需要一份[官方文档](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:container(java.lang.Class, org.gradle.api.NamedDomainObjectFactory))了,我们看到project中有这样一个方法:

    NamedDomainObjectContainer<T> container(Class<T> type, NamedDomainObjectFactory<T> factory)
    //创建一个容器来管理指定对象T的命名对象，参数factory是用于创建指定对象的，
    //所有需要被创建的对象必须拥有切暴露一个"name"的属性,这个属性在对像的生命周期内必须是不变的
感觉是可以开始了,根据文档，我们首先创建一个factory类

    public class SmallExtensionFactory implements NamedDomainObjectFactory<SmallExtension> {
        @Override
        public SmallExtension create(String name) {
            return new SmallExtension(name);
        }
    }
好像没有问题，但是真的可以了吗？少侠，你还缺少一个`Instantiator`，想要写在buildscript中定义那就彻底把`new`放弃掉吧！ 正确的姿势应该是

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
现在就是构造一个NamedDomainObjectContainer，把它放到MyExtension中。

    Instantiator instantiator = ((DefaultGradle) project.getGradle()).getServices().get(
            Instantiator.class);
    NamedDomainObjectContainer<SmallExtension> smallExtensionsContainer = project.container(
            SmallExtension.class, new SmallExtensionFactory(instantiator));
    MyExtension mMyExtension = project.getExtensions().create("myExtension", MyExtension.class,
            new Object[]{instantiator,smallExtensionsContainer});
MyExtension中同时添加一个NamedDomainObjectContainer<SmallExtension>的成员，set方法如下

    public void smallExtensions(
            Action<? super NamedDomainObjectContainer<SmallExtension>> action) {
        //这里还是是用action的方式来接受buildscript的参数
        //这里方法名写成setSmallExtensions,gradle就无法找到对应的方法了
        action.execute(mSmallExtensions);
    }
还有一点需要注意的是，SmallExtension需要有这么要给属性

    final String mName;
到此为止，我们一个简单的DSL就完成了,我们可以在buildscript中如下定义

    myExtension {
        extensionName "ddd" //简单属性
        innerExtension{//对象类型
            extensionName "innerExtension"
        }
        smallExtensions{//命名对象容器
            extension1{
                extensionName "11"
            }
            extension2{
                extensionName "22"
            }
        }
    }
上面三种类型是可以组合是用的，组成更多样的DSL。
## 小结
这次内容就到此为止，当然Gradle的插件开发远不是这么简单，还有一些高级特性这里还没有写到，更多的就要靠大家自己去探索了。