/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.mips.instruction;

import fr.enimaloc.procemu.Instruction;
import fr.enimaloc.procemu.Processor;
import fr.enimaloc.procemu.mips.Utils;

public class Sub extends Instruction {

    @Override
    public void execute(Processor processor, String reg, String a) throws Throwable {
        processor.registers[Integer.parseInt(reg)] -= Utils.getRegisterOrVal(processor, a);
    }

    @Override
    public void execute(Processor processor, String reg, String a, String b) throws Throwable {
        processor.registers[Integer.parseInt(reg)] = Utils.getRegisterOrVal(processor, a) - Utils.getRegisterOrVal(processor, b);
    }
}
