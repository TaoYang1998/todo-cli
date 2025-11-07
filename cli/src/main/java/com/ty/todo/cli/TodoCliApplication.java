package com.ty.todo.cli;

import com.ty.todo.cli.command.TodoCommand;

import picocli.CommandLine;

/**
 * Entry point for the CLI executable.
 */
public final class TodoCliApplication {

    private TodoCliApplication() {
    }

    public static void main(String[] args) {
        int exit = new CommandLine(new TodoCommand()).execute(args);
        System.exit(exit);
    }
}
