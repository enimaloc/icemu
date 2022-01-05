/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import fr.enimaloc.procemu.test.TestLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.*;
import static fr.enimaloc.procemu.test.AssumptionUtils.*;

class ProcessorTest {

    Processor processor;

    @BeforeEach
    void setUp(TestInfo info) {
        processor = new Processor(
                new TestLanguage(),
                1,
                0,
                "add 0 1",
                "add 0 2",
                info.getTags().contains("loop") ? "j 0" : ""
        );
        processor.millis = 0;

        assumeEquals(1, processor.registers.length);
        assumeEquals(0, processor.stacks.length);
        assumeEquals(3, processor.instructions.length);
    }

    @Test
    @Tag("loop")
    void step() {
        assertEquals(0, processor.pc());
        assertEquals("add 0 1", processor.instructions[processor.pc()]);
        assertEquals(0, processor.registers[0]);

        processor.step();

        assertEquals(1, processor.pc());
        assertEquals("add 0 2", processor.instructions[processor.pc()]);
        assertEquals(1, processor.registers[0]);
    }

    @Test
    void run() throws InterruptedException {
        assertEquals(0, processor.pc());
        assertEquals("add 0 1", processor.instructions[processor.pc()]);
        assertEquals(0, processor.registers[0]);

        processor.run();

        Thread.sleep(100L * processor.instructions.length);

        assertEquals(3, processor.pc());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> assertNull(processor.instructions[processor.pc()]));
        assertEquals(3, processor.registers[0]);
    }

    @Test
    @Tag("loop")
    void stop() throws InterruptedException {
        processor.millis = 1;

        assertEquals(0, processor.pc());
        assertEquals("add 0 1", processor.instructions[processor.pc()]);
        assertEquals(0, processor.registers[0]);

        processor.run();
        Thread.sleep(1);
        processor.stop(false);

        assertEquals(1, processor.pc());
        assertEquals("add 0 2", processor.instructions[processor.pc()]);
        assertEquals(1, processor.registers[0]);
    }

    @Test
    @Tag("loop")
    void jump() {
        processor.jump(3*3);

        assertEquals(0, processor.pc());
        assertEquals("add 0 1", processor.instructions[processor.pc()]);
        assertEquals(9, processor.registers[0]);
    }

    @Test
    @Tag("loop")
    void movePC() {
        processor.movePC(2);

        assertEquals(1, processor.pc());
        assertEquals("add 0 2", processor.instructions[processor.pc()]);
        assertEquals(0, processor.registers[0]);
    }

    @Test
    @Tag("loop")
    void getPC() {
    }
}