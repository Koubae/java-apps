package com.koubae.app;

public final class Constants {
    public static final String WELCOME_BANNER =
"""
     ____.           __________         _________.__           .__  .__  \s
    |    |____ ___  _\\______   \\___.__./   _____/|  |__   ____ |  | |  | \s
    |    \\__  \\\\  \\/ /|     ___<   |  |\\_____  \\ |  |  \\_/ __ \\|  | |  | \s
/\\__|    |/ __ \\\\   / |    |    \\___  |/        \\|   Y  \\  ___/|  |_|  |__
\\________(____  /\\_/  |____|    / ____/_______  /|___|  /\\___  >____/____/
              \\/                \\/            \\/      \\/     \\/          \s
""";
    public static final String WELCOME_MESSAGE = "Welcome to %s, type /help for help and command instruction...\n";
    public static final String GOODBYE_MESSAGE = "\nThanks for using %s, now shutting down...\n\n";


    public static final String HELP_COMMAND_BASE = "These are the base commands:\n";
    public static final String HELP_COMMAND = "These are all other available commands:\n";
    public static final String HELP_TEXT = """



%s you can write Python Interpreter Shell commands directly to the terminal environment that runs inside Java.
Basically it enables to use Python Interpreter Shell in Interactive Mode: https://docs.python.org/3/tutorial/interpreter.html#interactive-mode

Why you may ask? What's the benefit of this? Why can't you just run it normally by calling the Python interpreter?

Well, for no reason, I did this just for fun XD.
Use this program is not safe at all and not recommended, probably there are tons of safety issues and risks.\s
This program was an experiment while learn Java and discovering its fetures.
 
You this program at your own risk!      \s
""";

    private Constants() {}
}
