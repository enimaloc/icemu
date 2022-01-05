/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.mips;

import fr.enimaloc.procemu.Instruction;
import fr.enimaloc.procemu.Language;
import fr.enimaloc.procemu.mips.instruction.*;

public class CustomLang implements Language {
    @Override
    public String commentRegex() {
        return "//";
    }

    @Override
    public String labelRegex() {
        return "->";
    }

    @Override
    public Instruction[] instructions() {
        return new Instruction[] {
                new Define(),
                new Move(),
                new Add(),
                new Sub(),
                new Div(),
                new Cmp(),
                new Jnz(),
        };
    }
}
