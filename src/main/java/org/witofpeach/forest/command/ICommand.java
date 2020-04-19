package org.witofpeach.forest.command;

import java.util.List;

public interface ICommand {

    void handle(CommandContext commandContext);

    String getName();

     default List<String> getAliases() {
         return List.of();
     }
}
