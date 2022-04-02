# CmdlineParser

行命令解析器  
超简单的行命令解析器，用于解析与执行行命令，例如:

```shell
--username Verlif --commit "hello world!" --allowed
```

目前支持的指令格式有：

* `[前缀][指令] [指令参数|可选]`，例如`--username Verlif`

需要自定义命令前缀。

## 使用

```java
String line = "--username Verlif --commit \"hello world!\" --allowed";
CmdlineParser parser = new CmdlineParser("--");
// 忽略未知命令，否则会抛出UnknownCmdKeyException异常
parser.ignoreUnknownKey();
// 忽略关键词大小写，可以让"--KEy"也能匹配到"key"
parser.ignoreCase();
// 添加指令执行器
parser.addHandler("username", UsernameCmd);
parser.addHandler("allowed", AllowedCmd);
// 执行指令
parser.exec(line);
// 以下方式也是可以的
parser.exec(new String[]{"--username", "Verlif", "--commit", "hello world", "--allowed"});
```

此时会调用以下执行器方法：

1. `UsernameCmd`的`handle`方法，带有参数`Verlif`。
2. `AllowedCmd`的`handle`方法，参数为`null`。

## 注意

指令调用的顺序与关键词的传入顺序有关。

例如`--a 123 --b 321 --a 789`就会调用以下流程：

> `a`执行器 -> `b`执行器 -> `a`执行器

## 添加依赖

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

> Gradle
> ```text
> allprojects {
>   repositories {
>       maven { url 'https://jitpack.io' }
>   }
> }
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>cmdline-parser</artifactId>
>            <version>1.2</version>
>        </dependency>
>    </dependencies>
> ```

> Gradle
> ```text
> dependencies {
>   implementation 'com.github.Verlif:cmdline-parser:1.2'
> }
> ```
