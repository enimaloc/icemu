/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import java.util.Arrays;
import java.util.Optional;

public interface Language {

    default String beforeParsing(Processor processor, String instruction) {
        if (!commentRegex().isEmpty() && instruction.contains(commentRegex())) {
            String[] split = instruction.split(commentRegex());
            instruction = split[0];
        }
        if (!labelRegex().isEmpty() && instruction.contains(labelRegex())) {
            String[] split = instruction.split(labelRegex());
            processor.labels.put(split[0], String.valueOf(processor.pc()));
            instruction = split.length > 1 ? split[1] : "";
        }
        return instruction;
    }

    default void afterParsing(Processor processor, String instruction) {
    }

    default String name() {
        return this.getClass()
                   .getSimpleName();
    }

    String commentRegex();

    String labelRegex();

    default String argumentDelimiterRegex() {
        return " ";
    }

    default String instructionDelimiterRegex() {
        return argumentDelimiterRegex();
    }

    Instruction[] instructions();

    default void exec(Processor processor, String name, String arguments) {
        try {
            Optional<Instruction> instructionOpt = Arrays.stream(instructions())
                                                         .filter(instruction -> instruction.name
                                                                                           .equalsIgnoreCase(name))
                                                         .findFirst();
            if (instructionOpt.isEmpty()) {
                throw new ClassNotFoundException(String.format("Instruction '%s' not found", name));
            }

            Instruction instruction = instructionOpt.get();
            String[] s = Arrays.stream(arguments.split(this.argumentDelimiterRegex()))
                               .filter(st -> !st.isEmpty() && !st.isBlank())
                               .map(arg -> processor.labels.getOrDefault(arg, arg))
                               .toArray(String[]::new);
            switch (s.length) {
                case 0 -> instruction.execute(processor);
                case 1 -> instruction.execute(processor, s[0]);
                case 2 -> instruction.execute(processor, s[0], s[1]);
                case 3 -> instruction.execute(processor, s[0], s[1], s[2]);
                case 4 -> instruction.execute(processor, s[0], s[1], s[2], s[3]);
                case 5 -> instruction.execute(processor, s[0], s[1], s[2], s[3], s[4]);
                case 6 -> instruction.execute(processor, s[0], s[1], s[2], s[3], s[4], s[5]);
                default -> instruction.execute(processor, s);
            }
        } catch (Throwable throwable) {
            processor.listeners
                    .forEach(listener -> listener.onError(processor, new ProcessorException(
                            processor.pc(),
                            name+(!arguments.isEmpty() ? " "+arguments : ""),
                            throwable
                    )));
        }
    }

}
