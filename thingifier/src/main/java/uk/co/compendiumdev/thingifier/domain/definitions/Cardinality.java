package uk.co.compendiumdev.thingifier.domain.definitions;

// todo: cardinality is not used or enforced yet
public class Cardinality {
    private final String from;
    private final String to;

    public Cardinality(final String fromAmount, final String toAmount) {
        this.from = fromAmount;
        this.to = toAmount;
    }
}
