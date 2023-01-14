# CmdlineParser

行命令解析器  
超简单的行命令解析器，用于解析与执行行命令，例如:

```shell
--username Verlif --commit "hello world!" --allowed
```

总的来说就是：__解析字符串，执行对应方法__

## 使用

```java
String line = "-abc --username Verlif --commit \"hello world!\" --allowed";
CmdlineParser parser = new CmdlineParser();
// 忽略未知命令，否则会抛出UnknownCmdKeyException异常
parser.ignoreUnknownKey();
// 忽略关键词大小写，可以让"--KEy"也能匹配到"key"
parser.ignoreCase();
// 设置参数解析器
parser.setArgParser(new PrefixWithConfigArgParser("--"));
// 添加指令执行器
parser.addHandler("username", param -> {
    System.out.println("所以username是 - "  + param);
});
parser.addHandler("allowed", param -> {
    System.out.println("所以allowed是 - "  + param);
});
parser.addHandler("a", param -> {
    System.out.println("这里是a - " + param);
});
parser.addHandler("b", param -> {
    System.out.println("这里是b - " + param);
});
parser.addHandler("c", param -> {
    System.out.println("这里是c - " + param);
});
// 执行指令
parser.exec(line);
// 上方指令结果与下方相同
ArgValues argValues = new ArgValues();
argValues.add("a", null);
argValues.add("b", null);
argValues.add("c", null);
argValues.add("username", "Verlif");
argValues.add("commit", "hello world!");
argValues.add("allowed", null);
parser.exec(argValues);
// 甚至相当于这样
parser.setArgParser(new HtmlUrlArgParser());
parser.exec("127.0.0.1:81/queue/queue?a&b&c&username=Verlif&commit=hello world&allowed");
```

执行结果如下：

*以下内容输出3次*

```text
这里是a - null
这里是b - null
这里是c - null
所以username是 - Verlif
所以allowed是 - null
```

## 自定义

### 参数解析器

用于解析指令key与对应参数值的类，与需要处理的指令格式有关。例如将`--username Verlif --commit "hello world!" --allowed`解析成以下内容：

| 指令key    | 指令值         |
|----------|-------------|
| username | Verlif      |
| commit   | hello world |
| allowed  | `null`      |

开发者可以通过自定义参数解析器来解析不同指令格式的字符串，例如解析`HTMLUrl`的参数，例如解析`127.0.0.1:81/queue/queue?a&b&c&username=Verlif&commit=hello world&allowed`。

示例：

```java
/**
 * 简单的HTMLUrl参数解析器
 *
 * @author Verlif
 */
public class HtmlUrlArgParser implements ArgParser {

    @Override
    public ArgValues parseLine(String line) {
        String[] ss = line.split("\\?", 2);
        if (ss.length == 1) {
            return new ArgValues();
        }
        return toArgValues(ss[1]);
    }

    private ArgValues toArgValues(String paramStr) {
        ArgValues argValues = new ArgValues();
        String[] params = paramStr.split("&");
        for (String param : params) {
            int i = param.indexOf('=');
            if (i == -1) {
                argValues.add(param, null);
            } else {
                argValues.add(param.substring(0, i), param.substring(i + 1));
            }
        }
        return argValues;
    }
}
```

### 指令执行器

执行对应指令的方法类，类似`Runnale`，只有一个`handle(String param)`方法。

```java
public interface CmdHandler {

    /**
     * 执行器执行核心
     *
     * @param param 参数；可能为null
     */
    void handle(String param);
}
```

## 注意

指令调用的顺序与关键词的传入顺序有关。

例如`--a 123 --b 321 --a 789`就会调用以下流程：

> `a`执行器 -> `b`执行器 -> `a`执行器

## 添加依赖

1. 添加Jitpack仓库源

   __lastVersion__: [![](https://jitpack.io/v/Verlif/cmdline-parser.svg)](https://jitpack.io/#Verlif/cmdline-parser)

   maven
   ```xml
   <repositories>
          <repository>
              <id>jitpack.io</id>
              <url>https://jitpack.io</url>
          </repository>
   </repositories>
   ```

   Gradle
   ```text
   allprojects {
     repositories {
         maven { url 'https://jitpack.io' }
     }
   }
   ```

2. 加依赖

   maven
   ```xml
     <dependencies>
             <dependency>
                 <groupId>com.github.Verlif</groupId>
                 <artifactId>cmdline-parser</artifactId>
                 <version>lastVersion</version>
             </dependency>
         </dependencies>
   ```

   Gradle
   ```text
   dependencies {
     implementation 'com.github.Verlif:cmdline-parser:lastVersion'
   }
   ```
