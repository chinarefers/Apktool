### Apktool
修改自官方的Apktool，最终产物只有public.xml

使用编译后产生jar的文件apktool-cli.jar进行解析。

```
java -jar apktool-cli.jar p[ublic] apk文件路径
```

最终会生成res/values/public.xml文件