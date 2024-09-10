一个简易的 <font size=2>[mojang brigadier](https://github.com/Mojang/brigadier)</font> 封装.

> 最终呈现效果借鉴自 [TabooLib](https://github.com/TabooLib/taboolib)

### 注册命令

在保证已经实现 **平台命令注册** 的前提下,可以直接创建一个 CommandRegistry 来注册命令.   
以 Bukkit 为例:

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
        dynamic("key") {
            execute {
                println("key = ${it["key"]}")
            }
        }
    }

    @CommandBody
    val kill = subCommand {
        //               TypePlayer 与 TypeWorld 均为已实现的参数类型
        dynamic("player", TypePlayer.player()) {
            dynamic("world", TypeWorld.world()) {
                execute {
                    println("player = ${it.get<Player>("player")} | world = ${it.get<World>("world")}")
                }
            }
            execute {
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
        literal.execute(context -> {
            System.out.println("this is main command");
        });
    });

    @CommandBody
    public static final SimpleSubCommand kill = ComponentExtraJava.subCommand(literal -> {
        literal.argument("player", TypePlayer.player(), playerArg -> {
            playerArg.argument("world", TypeWorld.world(), worldArg -> {
                worldArg.execute(context -> {
                    System.out.println("kill player " + context.getBy("player", Player.class) + " in world " + context.getBy("world", World.class));
                });
            });
            playerArg.execute(context -> {
                System.out.println(context.getBy("player", Player.class));
            });
        });
    });

    @CommandBody
    public static final SimpleSubCommand sub = ComponentExtraJava.subCommand(literal -> {
        literal.argument("arg1", TypeFactory.string(), argument -> {
            argument.execute(context -> {
                System.out.println("arg1 = " + context.get("arg1"));
            });

            argument.argument("arg2", TypeFactory.intType(), argument2 -> {
                argument2.execute(context -> {
                    System.out.println("arg2 = " + context.getBy("arg2", Integer.class));
                });
            });
        });
        literal.execute(context -> {
            System.out.println(context);
        });
    });

}
```