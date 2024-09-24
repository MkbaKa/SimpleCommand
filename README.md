一个简易的 <font size=2>[mojang brigadier](https://github.com/Mojang/brigadier)</font> 封装.

> 最终呈现效果借鉴自 [TabooLib](https://github.com/TabooLib/taboolib)

## 添加依赖

### gradle (kts)

```kotlin 
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/MkbaKa/SimpleCommand")
        credentials {
            // 请自行更改
            username = "..."
            password = "..."
        }
    }
}

dependencies {
    // common 模块不包含任何平台实现
    // implementation("me.mkbaka.simplecommand:common:{latest version}")
    
    // Bukkit 平台实现, 该依赖会自动包含 common 模块
    implementation("me.mkbaka.simplecommand:platform-bukkit:{latest version}")
}
```

### gradle (groovy)
```groovy
repositories {
    maven {
        url "https://maven.pkg.github.com/MkbaKa/SimpleCommand"
        credentials {
            // 请自行更改
            username = "..."
            password = "..."
        }
    }
}

dependencies {
    implementation 'me.mkbaka.simplecommand:platform-bukkit:{latest version}'
}
```

## 食用方式

在保证已经实现 **平台命令注册** 的前提下,可以直接创建一个 CommandRegistry 来注册命令.

#### kotlin:
```kotlin
// 插件主类
class BukkitPlugin : JavaPlugin() {

    // 创建由 SimpleCommand 实现的 Bukkit registry
    private val registry = BukkitCommandRegistry(this)

    // 以 onEnable 时注册为例
    override fun onEnable() {
        registry.register(TestCommand())
    }

}

@CommandHeader("Test", aliases = ["ttt"])
class TestCommand {

    // mainCommand 用于操作 Test 这个命令头相关的内容
    @CommandBody
    val main = mainCommand {
        onPermissionCheckFailure(CommandNotify { sender, input, _, _ ->
            sender.sendMessage("You need permission to use: $input")
        })
        // 如果这样编写 其效果等同于下面的 val run = subCommand...  不过会很丑(
        //    literal("run") {
        //        dynamic("key") {
        //            execute {
        //                println("key = ${it["key"]}")
        //            }
        //        }
        //    }
    }

    @CommandBody
    val run = subCommand {
        // kotlin 请使用 dynamic 函数创建命令参数
        // 如果用 argument 需要显式声明lambda参数名(或者使用 it.xxx 调用)
        // 不然这个this会被推断成 this@subCommand 导致结构出错
        dynamic("key") {
            // 命令建议
            suggest { suggestionContext ->
                listOf("value1", "value2", "value3")
            }
            
            // 若使用 exec 来增加 executor 组件
            // 则默认限定执行者为 CommandSource
            // exec {
            //    println("key = ${it["key"]}")
            // }
            
            // 若使用 execute 来增加 executor 组件
            // 可以指定平台的对象类型 前提是已经用包装过了对应的 CommandSource
            // 比如 https://github.com/MkbaKa/SimpleCommand/blob/main/platform-bukkit/src/main/kotlin/me/mkbaka/simplecommand/platform/BukkitCommandSource.kt
            // 重写了 origin 字段 返回的是 bukkit 平台的 CommandSender
            // execute 在执行时 会判断这个 CommandSender 是不是 Player 类型的实例
            // 若是则执行代码块   若不是则执行 incorrectCommandSource 回调函数
            execute<Player> { ctx ->
                ctx.source.kickPlayer("疯狂星期四!")
            }
        }
    }

    // 直接将某个类的实例作为一层明文节点
    // 以单例对象为例:
    @CommandBody
    val test = object {

        @CommandBody
        val run = subCommand {
            dynamic("player") {
                exec {
                    println("player = ${it["player"]}")
                }
            }
        }

    }

    @CommandBody
    val kill = subCommand {
        //               TypePlayer 与 TypeWorld 均为已实现的参数类型
        dynamic("player", TypePlayer.player()) {
            dynamic("world", TypeWorld.world()) {
                exec {
                    println("player = ${it.get<Player>("player")} | world = ${it.get<World>("world")}")
                }
            }
            exec {
                println(it.get<Player>("player"))
            }
        }
    }

}
```

#### java:
```java
public class BukkitPlugin extends JavaPlugin {

    private final CommandRegistry registry = new BukkitCommandRegistry(this);

    @Override
    public void onEnable() {
        registry.register(new ExampleCommand());
    }
    
}

@CommandHeader(name = "example")
public class ExampleCommand {

    @CommandBody
    public static final SimpleMainCommand main = ComponentExtraJava.mainCommand(literal -> {
        //      ↓ java这个泛型好丑(
        literal.<Player>execute(Player.class, context -> {
            context.getSource().kickPlayer("疯狂星期四!");
        });
    });

    @CommandBody
    public static final SimpleSubCommand kill = ComponentExtraJava.subCommand(literal -> {
        literal.argument("player", TypePlayer.player(), playerArg -> {
            playerArg.argument("world", TypeWorld.world(), worldArg -> {
                worldArg.exec(context -> {
                    System.out.println("kill player " + context.getBy("player", Player.class) + " in world " + context.getBy("world", World.class));
                });
            });
            playerArg.exec(context -> {
                System.out.println(context.getBy("player", Player.class));
            });
        });
    });

    @CommandBody
    public static final SimpleSubCommand sub = ComponentExtraJava.subCommand(literal -> {
        literal.argument("arg1", TypeFactory.string(), argument -> {
            argument.exec(context -> {
                System.out.println("arg1 = " + context.get("arg1"));
            });

            argument.argument("arg2", TypeFactory.intType(), argument2 -> {
                argument2.exec(context -> {
                    System.out.println("arg2 = " + context.getBy("arg2", Integer.class));
                });
            });
        });
        literal.exec(context -> {
            System.out.println(context);
        });
    });

}
```