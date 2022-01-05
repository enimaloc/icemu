/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.test;

import fr.enimaloc.procemu.Instruction;
import fr.enimaloc.procemu.Language;
import fr.enimaloc.procemu.Processor;

public class TestLanguage implements Language {
    @Override
    public String commentRegex() {
        return "";
    }

    @Override
    public String labelRegex() {
        return "";
    }

    @Override
    public Instruction[] instructions() {
        return new Instruction[]{new Instruction("error") {
            @Override
            public void execute(Processor processor) throws Throwable {
                throw new AssertException("Listen");
            }
        }, new Instruction("noop"), new Instruction("add") {
            @Override
            public void execute(Processor processor, String a, String b) throws Throwable {
                processor.registers[Integer.parseInt(a)] += Utils.getRegisterOrVal(processor, b);
            }
        }, new Instruction("j") {
            @Override
            public void execute(Processor processor, String a) throws Throwable {
                processor.movePC(Integer.parseInt(a));
            }
        }};
    }
}
