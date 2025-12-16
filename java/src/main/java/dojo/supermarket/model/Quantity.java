package dojo.supermarket.model;

import java.util.Objects;

public final class Quantity {

    public static final Quantity ZERO = new Quantity(0.0);

    private final double value;

    public Quantity(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + value);
        }
        this.value = value;
    }

    public double getDouble() {
        return value;
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        double result = this.value - other.value;
        return new Quantity(Math.max(0, result));
    }

    public Quantity multiply(int factor) {
        return new Quantity(this.value * factor);
    }

    public boolean isZero() {
        return value == 0.0;
    }

    public boolean isPositive() {
        return value > 0.0;
    }

    public int floorDivide(double divisor) {
        return (int) Math.floor(value / divisor);
    }

    public int floorDivide(Quantity divisor) {
        return (int) Math.floor(value / divisor.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Quantity))
            return false;
        Quantity quantity = (Quantity) o;
        return Double.compare(quantity.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
