/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

public class ProcessorException extends RuntimeException {

    public ProcessorException(int pc, String instruction, Throwable caused) {
        super(String.format("\"%s\" [Line %d]", instruction, pc), caused, false, false);
    }
}
