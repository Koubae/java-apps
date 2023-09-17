package com.koubae.app.terminal;

/**
 *
 * Minimalistic Stack implementation
 * It uses an index as 'cursor' to identify the current latest element added in the stack (therefore being at the top of the stack)
 * <p></p>
 * We can go through the stack via <strong><code>getNextOnStack</code></strong> method which automatically wrap back up to the top element,
 * and is doing this by simply going backward from the current index
 *
 * @see <a href="https://stackoverflow.com/a/15840632/13903942">Credit: Java Stack with elements limit<a>
 * @credit <a href="https://stackoverflow.com/users/150016/tom">Tom<a>
 */
public class CommandStack {
    private static final int STACK_DEFAULT_SIZE = 10;
    private static final int STACK_MAX_SIZE = 1000;

    private final int size;
    private final String[] stack;

    private int index = 0;
    private int stackScanPosition = 0;

    public CommandStack() {
        size = STACK_DEFAULT_SIZE;
        stack = new String[size];
    }

    public CommandStack(int maxSize) throws IllegalArgumentException {
        if (maxSize <= 0 || maxSize > STACK_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("Max size must be between 0 and %d, %s provided", STACK_MAX_SIZE, maxSize));
        }

        size = maxSize;
        stack = new String[size];
    }

    public synchronized void add(String element) {
        indexWrap();
        stackPut(index, element);
        stackScanPosition = index; // when adding a new element, the stackScanPosition reset. This can be changed depending on use case
        index ++;
    }

    public String top() {
        return stackGet(getIndex());
    }

    public String bottom() {
        String element = stackGet(index);
        if (element == null)
            element = stackGet(0);
        return element;
    }

    public String currentStackElement() {
        return stackGet(getStackScanPosition());
    }

    /**
     * Will scan the current stack backwards returning current elements
     * If the index position of the scan is over 0 than it wraps back to the max limit of the stack
     * i.e:
     * <pre><code>
     *     Given a stack of size 10 and currently only 4 elements, that's how it would look getting the stack one by one
     *
     *                |
     *      {a, b, c, d, null, null, null, null, null, null}
     *             |
     *      {a, b, c, d, null, null, null, null, null, null}
     *          |
     *      {a, b, c, d, null, null, null, null, null, null}
     *       |
     *      {a, b, c, d, null, null, null, null, null, null}
     *                |  <--- It reset to the current index, if a null is encountered than the stack must not be full ;)
     *      {a, b, c, d, null, null, null, null, null, null}
     *             |
     *      {a, b, c, d, null, null, null, null, null, null}
     *          |
     *      {a, b, c, d, null, null, null, null, null, null}
     * </code></pre>
     *
     * @return String
     */
    public synchronized String stackDown() {
        String element = stackGet(stackScanPosition);
        if (element == null) {
            stackScanPosition = getIndex();
            element = stackGet(stackScanPosition);
        }

        stackScanPosition --;
        stackScanPositionWrapDown();
        return element;
    }

    /**
     * Same as stackDown but goes on opposite direction
     * @return String
     */
    public synchronized String stackUp() {
        String element = stackGet(stackScanPosition);
        if (element == null) {
            stackScanPosition = 0;
            element = stackGet(stackScanPosition);
        }

        stackScanPosition ++;
        stackScanPositionWrapUp();
        return element;

    }

    public String[] getStack() {
        return stack;
    }

    public int getIndex() {
        return index - 1;
    }

    public int getStackScanPosition() {
        return stackScanPosition;
    }

    public boolean isCurrentStackPositionTop() {
        return getStackScanPosition() == getIndex();
    }

    private synchronized void indexWrap() {
        if (index + 1 > size)
            index = 0;
    }

    private synchronized void stackScanPositionWrapDown() {
        if (stackScanPosition < 0) {
            stackScanPosition = size - 1;
        }
    }

    private synchronized void stackScanPositionWrapUp() {
        if (stackScanPosition + 1 > size) {
            stackScanPosition = 0;
        }
    }

    private synchronized void stackPut(int index, String element) {
        stack[index] = element;
    }

    private String stackGet(int position) {
        try {
            return stack[position];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return null;
        }
    }

}