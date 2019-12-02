##spring boot + vue +ueditor

##项目说明
* 集成vue和ueditor 前后端分离Demo
* 由于ueditor是基于jsp开发，所以部分需要改造；
* 图片和文件上传支持本地上传，也支持ceph
* 服务端的资源文件中需要有config.json文件
* 前端模块中，由于上传时，使用了jquery,所以在package.json需要引入jquery，
    然后在webpack.base.conf.js中添加
    'jquery': "jquery/src/jquery"
       
 ##项目开发环境启动
 
 * 修改本地maven配置文件settings.xml。常见配置如下：
    ```xml
    		<mirror>
    			<id>framework-repo</id>
    			<mirrorOf>framework-repo</mirrorOf>
    			<name>framework-repo</name>
    			 <url>https://raw.githubusercontent.com/wengsongwei8/maven-repo/master</url>  
    		</mirror>
    		
    		<mirror>
    			<id>aliyun</id>
    			<mirrorOf>central</mirrorOf>
    			<name>aliyun</name>
    			   <url>https://maven.aliyun.com/repository/jcenter</url>  
    		</mirror>
    ```
 特殊情况如果使用*，则把mirror中的mirrorOf属性改为：<mirrorOf>*,!my-maven-repo</mirrorOf>
   例如：
  ```xml
       		<mirror>
       			<id>aliyun</id>
       			<mirrorOf>*,!framework-repo</mirrorOf>
       			<name>sonatype</name>
       			   <url>https://maven.aliyun.com/repository/jcenter</url>  
       		</mirror>
  
``` 
因为
 ```xml
   <mirrorOf>*</mirrorOf>
``` 
   会覆盖掉所有的repository,导致项目中的repository不生效,所以需要以上配置


   



##配置说明
* 添加权限编码为：*all_pri  表示拥有所有权限
* 添加用户的默认密码为:123456

##常见问题
* 没安装lombok插件，导致部分代码显示报错（不影响正常运行）。

    * lombok插件可以在我们简化很多代码，安插也非常方便
    * idea中安装：打开IDEA的Setting –> 选择Plugins选项 –> 选择Browse repositories –> 搜索lombok –> 点击安装 –> 安装完成重启IDEA –> 安装成功
    * eclipse安装，先到官网下载lombok.jar，官网地址：https://projectlombok.org/download，
        将lombok.jar包拷贝到eclipse的安装目录下，并且在eclipse.ini文件中添加如下两行
    
    -Xbootclasspath/a:lombok.jar    --备注：如果没有这一行也是没问题的 <br/>
    -javaagent:lombok.jar

* 执行./start.sh 会报以下错误：
    bin/sh^M: bad interpreter: No such file or directory
  
    * 原因分析：.sh脚本在windows系统下用记事本文件编写的。不同系统的编码格式引起的。
    * 解决方法：vi start.sh，然后输入:set ff=unix ，然后保存退出即可
* 执行打包部署以后，然后再在idea或 eclipse启动项目，会报找不到数据库连接的错：
  
    * 原因分析：为了部署方便，在打包的时候避免把配置文件放在jar里，所以打包时会把配置方向往外放。所以在ide里启动就找不到对应的配置文件
    * 解决方法：再执行命令mvn install即可
* 下载包依赖时，提示Downloading from framework-repo: https://raw.githubusercontent.com/...失败，无法从该地址下载包：
    * 原因分析：可能无法访问github的资源网站：https://raw.githubusercontent.com
    * 解决方法：修改本地本地hosts【windows下路径为：C:\Windows\System32\drivers\etc\hosts】 
    把以下内容复制到hosts文件里，并重启浏览器即可。    
  ```txt
  192.30.253.112 github.com
  192.30.253.119 gist.github.com
  151.101.100.133 assets-cdn.github.com
  151.101.100.133 raw.githubusercontent.com
  151.101.100.133 gist.githubusercontent.com
  151.101.100.133 cloud.githubusercontent.com
  151.101.100.133 camo.githubusercontent.com
  151.101.100.133 avatars0.githubusercontent.com
  151.101.100.133 avatars1.githubusercontent.com
  151.101.100.133 avatars2.githubusercontent.com
  151.101.100.133 avatars3.githubusercontent.com
  151.101.100.133 avatars4.githubusercontent.com
  151.101.100.133 avatars5.githubusercontent.com
  151.101.100.133 avatars6.githubusercontent.com
  151.101.100.133 avatars7.githubusercontent.com
  151.101.100.133 avatars8.githubusercontent.com
  
