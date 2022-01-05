/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu.test;

import org.junit.jupiter.api.Assumptions;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class AssumptionUtils {

    public static void assumeEquals(int expected, int actual) {
        assumeTrue(expected == actual);
    }

}
