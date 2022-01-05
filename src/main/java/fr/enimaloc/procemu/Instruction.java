/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import java.util.Locale;

public class Instruction {

    public static final String INSTRUCTION_ERROR_MESSAGE = "Instruction '%s' not exist, please check number of parameter, I've %d parameters";

    public final String name;

    public Instruction() {
        this.name = this.getClass()
                        .getSimpleName()
                        .toLowerCase(Locale.ROOT);
    }

    public Instruction(String name) {
        this.name = name;
    }

    public void execute(Processor processor) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                0
        ));
    }

    public void execute(Processor processor, String a) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                1
        ));
    }

    public void execute(Processor processor, String a, String b) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                2
        ));
    }

    public void execute(Processor processor, String a, String b, String c) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                3
        ));
    }

    public void execute(Processor processor, String a, String b, String c, String d) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                4
        ));
    }

    public void execute(Processor processor, String a, String b, String c, String d, String e) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                5
        ));
    }

    public void execute(Processor processor, String a, String b, String c, String d, String e, String f) throws
            Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                6
        ));
    }

    public void execute(Processor processor, String[] s) throws Throwable {
        throw new IllegalArgumentException(String.format(
                INSTRUCTION_ERROR_MESSAGE,
                name,
                s.length
        ));
    }

}
