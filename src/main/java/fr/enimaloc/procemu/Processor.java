/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package fr.enimaloc.procemu;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class can execute processor {@link Language language}
 */
public class Processor {

    /**
     * Default millisecond per instruction
     */
    public static final long                    DEFAULT_MILLIS   = 500;
    /**
     * Default listeners list, this contains an instance of {@link ProcessorListener} with no {@link Override @Override}
     */
    public static final List<ProcessorListener> DEFAULT_LISTENER = List.of(new ProcessorListener() {});

    /**
     * {@link Language} used by this {@link Processor}, this can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final Language                language;
    /**
     * Register of the {@link Processor}, the size can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final double[]                registers;
    /**
     * Stacks of the {@link Processor}, the size can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final double[]                stacks;
    /**
     * Program of the {@link Processor}, this can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final String[]                instructions;
    /**
     * Labels used by {@link Processor}, it's fill automatically with {@link Language#labelRegex()} in {@link Language#beforeParsing(Processor, String)}
     */
    public final Map<String, String>     labels = new HashMap<>();
    /**
     * No usage for now, this can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final PrintStream             out;
    /**
     * Used to redirect error happen during execution of the {@link Processor}, this can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final PrintStream             err;
    /**
     * Listeners of the {@link Processor} used to do action during event, this can only be set in {@link Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)}
     */
    public final List<ProcessorListener> listeners;

    /**
     * Used to lock the thread when asked, like in {@link Processor#stop(boolean)}
     */
    public final Lock lock = new ReentrantLock();

    /**
     * Used to verify if the program is running
     */
    private boolean running = false;
    /**
     * Define current instruction, called Program Counter or Instruction Pointer
     */
    private int     pc      = 0;

    /**
     * Millisecond per instruction, this can be set at any moment
     */
    public  long    millis  = DEFAULT_MILLIS;


    /**
     * Constructor for a Processor with some defaults values:
     * <ul>
     *     <li>out: {@link System#out}</li>
     *     <li>err: {@link System#err}</li>
     *     <li>listeners: {@link Processor#DEFAULT_LISTENER}</li>
     * </ul>
     *
     * @param language     used to parse
     * @param registerSize size of the register
     * @param stackSize    size of the stack
     * @param instructions program to parse
     * @see Processor#Processor(Language, int, int, List, String...)
     * @see Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)
     */
    public Processor(
            Language language, int registerSize, int stackSize, String... instructions
    ) {
        this(language, registerSize, stackSize, System.out, System.err, DEFAULT_LISTENER, instructions);
    }

    /**
     * Constructor for a Processor with some defaults values:
     * <ul>
     *     <li>out: {@link System#out}</li>
     *     <li>err: {@link System#err}</li>
     * </ul>
     *
     * @param language     used to parse
     * @param registerSize size of the register
     * @param stackSize    size of the stack
     * @param listeners    list of listeners
     * @param instructions program to parse
     * @see Processor#Processor(Language, int, int, String...)
     * @see Processor#Processor(Language, int, int, PrintStream, PrintStream, List, String...)
     */
    public Processor(
            Language language, int registerSize, int stackSize, List<ProcessorListener> listeners,
            String... instructions
    ) {
        this(language, registerSize, stackSize, System.out, System.err, listeners, instructions);
    }

    /**
     * Raw constructor for Processor
     *
     * @param language     used to parse
     * @param registerSize size of the register
     * @param stackSize    size of the stack
     * @param out          output print stream
     * @param err          error print stream
     * @param listeners    list of listeners
     * @param instructions program to parse
     * @see Processor#Processor(Language, int, int, String...)
     * @see Processor#Processor(Language, int, int, List, String...)
     */
    public Processor(
            Language language,
            int registerSize,
            int stackSize,
            PrintStream out,
            PrintStream err,
            List<ProcessorListener> listeners,
            String... instructions
    ) {
        this.language     = language;
        this.registers    = new double[registerSize];
        this.stacks       = new double[stackSize];
        this.out          = out;
        this.err          = err;
        this.listeners    = listeners;
        this.instructions = instructions;
    }

    /**
     * Pass one instruction in processor
     * @see Processor#run()
     * @see Processor#jump(long)
     */
    public void step() {
        listeners.forEach(listener -> listener.onStepStart(this));
        String instruction = instructions[pc];
        instruction = language.beforeParsing(this, instruction)
                              .trim();
        if (!instruction.isBlank() && !instruction.isEmpty()) {
            String[] s = instruction.contains(language.instructionDelimiterRegex()) ? instruction.split(
                    language.instructionDelimiterRegex(),
                    2
            ) : new String[]{instruction};
            language.exec(this, s[0], s.length > 1 ? s[1] : "");
        }
        language.afterParsing(this, instruction);
        pc++;
        listeners.forEach(listener -> listener.onStepStop(this));
    }

    private final Timer     timer      = new Timer();
    private       boolean   needCancel = false;
    public final  Condition runLock    = lock.newCondition();

    /**
     * Same as {@link Processor#step()} but run until the program is finished
     * @see Processor#stop(boolean)
     * @see Processor#step()
     * @see Processor#jump(long)
     */
    public void run() {
        running    = true;
        needCancel = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listeners.forEach(listener -> listener.onRunStart(Processor.this));
                lock.lock();
                while (pc < instructions.length) {
                    if (needCancel) {
                        break;
//                        set("error", new Hcf.HaltCatchAndFireException());
                    }
                    step();
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                listeners.forEach(listener -> listener.onRunStop(Processor.this));
                runLock.signalAll();
                lock.unlock();
                this.cancel();
            }
        }, 0, 1);
    }

    /**
     * Request a stop when {@link Processor#run()}
     *
     * @param lock block until the end of the program
     * @see Processor#run()
     */
    public void stop(boolean lock) {
        needCancel = true;
        if (lock && running) {
            running = false;
            this.lock.lock();
            try {
                runLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.lock.unlock();
            }
        }
    }

    /**
     * Do {@code jump} steps
     *
     * @param jump a many {@link Processor#step()} to do
     * @see Processor#step()
     * @see Processor#run()
     */
    public void jump(long jump) {
        for (; jump > 0; jump--) {
            step();
        }
    }

    /**
     * Move PC [Program Counter], cannot be done with raw variable {@link Processor#pc} because need some edition before set
     *
     * @param pc new Program Counter
     */
    public void movePC(int pc) {
        this.pc = pc - 1;
    }

/*    private int (String s) {
        return s.startsWith("r") ? registers[Integer.parseInt(s.replaceFirst("r", ""))] : Integer.parseInt(s);
    }
*/

    /**
     * Getter for {@link Processor#pc}
     *
     * @return Program Counter
     */
    public int pc() {
        return pc;
    }
}
