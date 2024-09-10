package me.mkbaka.simplecommand.platform;

import me.mkbaka.simplecommand.common.command.argument.TypeFactory;
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault;
import me.mkbaka.simplecommand.common.util.ComponentExtraJava;
import me.mkbaka.simplecommand.common.util.SimpleMainCommand;
import me.mkbaka.simplecommand.common.util.SimpleSubCommand;
import me.mkbaka.simplecommand.common.util.simple.CommandBody;
import me.mkbaka.simplecommand.common.util.simple.CommandHeader;
import me.mkbaka.simplecommand.platform.argument.TypePlayer;
import me.mkbaka.simplecommand.platform.argument.TypeWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
