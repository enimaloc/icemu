/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import java.io.PrintWriter;

/**
 * Used when event occur in {@link Processor}, a default value is provided {@link Processor#DEFAULT_LISTENER}
 */
public interface ProcessorListener {

    /**
     * Called when {@link Processor#run()} is called
     *
     * @param processor where the method was called
     * @see Processor#run()
     */
    default void onRunStart(Processor processor) {}

    /**
     * Called when {@link Processor#instructions} end
     *
     * @param processor where the program was executed
     * @see Processor
     */
    default void onRunStop(Processor processor) {}

    /**
     * Called when {@link Processor#step()} is called
     *
     * @param processor where the method is called
     * @see Processor#step()
     */
    default void onStepStart(Processor processor) {}

    /**
     * Called when an instruction end
     *
     * @param processor where the instruction was executed
     * @see Processor
     */
    default void onStepStop(Processor processor) {}

    /**
     * Called when error occur in processor
     *
     * @param processor where the error was occurred
     * @param exception exception indicate the line with the original exception as {@literal Caused By} {@link Throwable#getCause()}
     * @implNote default: {@link Throwable#printStackTrace(PrintWriter)} to {@link Processor#err}
     */
    default void onError(Processor processor, ProcessorException exception) {
        exception.printStackTrace(processor.err);
    }
}
