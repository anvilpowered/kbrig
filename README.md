# KBrig - Modern Brigadier for Multiplatform Plugins

*Portions of this project have been inspired by https://github.com/Mojang/brigadier copyrighted by Microsoft Corporation under the terms of the MIT license.*

## What is this?

KBrig is a modern Kotlin rewrite of Brigadier's command building API with a focus on multiplatform support.
It does not replace Brigadier's dispatching logic, so it is still necessary to register and execute commands via the standard Brigadier `CommandDispatcher`.

## Who is this library for?

A signficant advantage of KBrig is the ability to map the source type of commands.
This allows you to write commands without depending on any specific platform (like Velocity, Paper or Sponge).
With this approach, you can avoid relying on a generic source type.
To convert a KBrig command node to a Brigadier command node, use the `.toBrigadier()` extension function from kbrig-brigadier.

The API for building commands is almost identical to standard Brigadier with a few improvements, so you should feel right at home.

## Basic Usage

1. In your common module, add a dependency on kbrig-core in the build.gradle(.kts) replacing `<version>` with the latest version:

```kt
dependencies {
    implementation("org.anvilpowered:kbrig-core:<version>")
}
```

2. Next, define your custom source and command:

Kotlin:
```kt
interface MyCommandSource {
    fun sendMessage(text: Component)
}

fun createPingCommand(): LiteralCommandNode<MyCommandSource> {
    return ArgumentBuilder.literal<MyCommandSource>("ping")
        .executesSingleSuccess { it.source.sendMessage(Component.text("Pong!")) }
        .build()
}
```

Java:
```java
interface MyCommandSource {
    void sendMessage(String message);
}

public static LiteralCommandNode<MyCommandSource> createPingCommand() {
    return ArgumentBuilder.literal<MyCommandSource>("ping")
        .executes(context -> {
            context.getSource().sendMessage("Pong!");
            return Command.SINGLE_SUCCESS;
        }).build();
}
```

3. In your platform module (e.g. Velocity), add a dependency on kbrig-brigadier in the build.gradle(.kts) replacing `<version>` with the latest version:

```kt
dependencies {
    implementation("org.anvilpowered:kbrig-brigadier:<version>")
}
```

4. Finally, register your custom command with the platform (in this example Velocity)

Kotlin:
```kt
class BridgeSource(private val velocityCommandSource: CommandSource) : MyCommandSource {
    override fun sendMessage(message: Component) = velocityCommandSource.sendMessage(message)
}

val commandNode = createPingCommand()
    .mapSource<_, CommandSource> { BridgeSource(it) }
    .toBrigadier()

proxyServer.commandManager.register(BrigadierCommand(commandNode))
```

Java:
```java
class BridgeSource implements MyCommandSource {
    private final CommandSource velocityCommandSource;

    BridgeSource(CommandSource velocityCommandSource) {
        this.velocityCommandSource = velocityCommandSource;
    }

    public void sendMessage(Component message) {
        velocityCommandSource.sendMessage(message);
    }
}

final LiteralCommandNode<CommandSource> mappedSourceNode =
    BrigadierConverter.toBrigadier(
        SourceConverter.mapSource(CustomCommand.createPing(), BridgeSource::new)
    );

proxyServer.getCommandManager().register(new BrigadierCommand(mappedSourceNode));
```

