/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.mips;

import fr.enimaloc.procemu.Processor;

public class Utils {

    public static double getRegisterOrVal(Processor processor, String s) {
        return s.startsWith("r") ?
                processor.registers[Integer.parseInt(s.replaceFirst("r", ""))] :
                Double.parseDouble(s);
    }

}
