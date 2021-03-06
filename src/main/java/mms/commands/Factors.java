package mms.commands;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.apache.logging.log4j.Level;

import mms.main.MmsInit;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class Factors {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            if (dedicated)
                MmsInit.log(Level.WARN, "/factors won't be registered on the server side");
            else
                dispatcher.register(getCommand());
        });
    }

    static private LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return literal("factors").then(argument("number", integer(2, Integer.MAX_VALUE)).executes(ctx -> run(ctx)));
    }

    static public int run(CommandContext<ServerCommandSource> ctx)
            throws CommandSyntaxException, IllegalArgumentException {
        int number = IntegerArgumentType.getInteger(ctx, "number");
        List<Integer> factors = calcFactors(number);
        StringBuilder response = new StringBuilder(String.format("Factors of %s: ", number));
        response.append(String.format("%s", factors.get(0)));
        for (int i = 1; i < factors.size() - 1; i++) {
            response.append(String.format(", %s", factors.get(i)));
        }
        response.append(String.format(" and %s", factors.get(factors.size() - 1)));
        ctx.getSource().sendFeedback(new LiteralText(response.toString()), false);
        return 0;
    }

    static private List<Integer> calcFactors(Integer number) {
        double limit = Math.sqrt(number);
        List<Integer> factors = new ArrayList<>();
        factors.add(1);
        for (int n = 2; n < limit; n++) {
            if (number % n == 0) {
                factors.add(n);
                factors.add(number / n);
            }
        }
        factors.add(number);
        // Collections.sort(factors);
        return factors;
    }
}
