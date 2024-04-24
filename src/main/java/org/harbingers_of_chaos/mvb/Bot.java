package org.harbingers_of_chaos.mvb;

import org.jetbrains.annotations.NotNull;

public interface Bot {
        void start();
        void stop();

        void log(@NotNull String s);
}
