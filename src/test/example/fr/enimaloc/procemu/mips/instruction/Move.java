/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.mips.instruction;

import fr.enimaloc.procemu.Instruction;
import fr.enimaloc.procemu.Processor;

public class Move extends Instruction {

    @Override
    public void execute(Processor processor, String reg, String val) throws Throwable {
        processor.registers[Integer.parseInt(reg)] = Double.parseDouble(val);
    }
}
