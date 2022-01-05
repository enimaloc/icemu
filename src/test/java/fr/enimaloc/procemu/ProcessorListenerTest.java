/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import fr.enimaloc.procemu.test.TestLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class ProcessorListenerTest {

    ProcessorListener mockListener;

    @BeforeEach
    void setUp() {
        mockListener = mock(ProcessorListener.class);
    }

    @Test
    void onRunStart() {
        Processor processor = new Processor(new TestLanguage(), 0, 0, List.of(mockListener), "noop");
        processor.millis = 0;
        processor.run();
        verify(mockListener, timeout(100)).onRunStart(eq(processor));
    }

    @Test
    void onRunStop() {
        Processor processor = new Processor(new TestLanguage(), 0, 0, List.of(mockListener), "noop");
        processor.millis = 0;
        processor.run();
        verify(mockListener, timeout(100)).onRunStop(eq(processor));
    }

    @Test
    void onStepStart() {
        Processor processor = new Processor(new TestLanguage(), 0, 0, List.of(mockListener), "noop");
        processor.millis = 0;
        processor.step();
        verify(mockListener).onStepStart(eq(processor));
    }

    @Test
    void onStepStop() {
        Processor processor = new Processor(new TestLanguage(), 0, 0, List.of(mockListener), "noop");
        processor.millis = 0;
        processor.step();
        verify(mockListener).onStepStop(eq(processor));
    }

    @Test
    void onError() {
        Processor processor = new Processor(new TestLanguage(), 0, 0, List.of(mockListener), "error");
        processor.millis = 0;
        processor.run();
        verify(mockListener, timeout(100)).onError(eq(processor), any());
    }

}