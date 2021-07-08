package ru.otus;

public class CustomSequence {

    private final int min;
    private final int max;
    private final int step;
    private final int copies;

    private int index;
    private int copied;
    private boolean isForward;

    public CustomSequence(int min, int max, int step, int copies) {
        if (max < min) throw new IllegalArgumentException("The max value must be >= min");
        this.min = min;
        this.max = max;
        if (step <= 0) throw new IllegalArgumentException("The step value must be >= 1");
        this.step = step;
        if (copies <= 0) throw new IllegalArgumentException("The copies value must be >= 1");
        this.copies = copies;
        this.index = min - step;
        this.copied = 0;
        this.isForward = true;
    }

    public int next() {
        if (isForward) {
            if ((copied == 0) || (copied >= copies)) {
                index = index + step;
                copied = 1;
            } else {
                copied += 1;
            }
            if (index > max) {
                index = max;
            }
            if ((index >= max) && (copied == copies) ) {
                isForward = false;
                copied = 0;
            }
        } else {
            if ((copied == 0) || (copied >= copies)) {
                index = index - step;
                copied = 1;
            } else {
                copied += 1;
            }
            if (index < min) {
                index = min;
            }
            if ((index <= min) && (copied == copies) ) {
                isForward = true;
                copied = 0;
            }
        }
        return index;
    }
}