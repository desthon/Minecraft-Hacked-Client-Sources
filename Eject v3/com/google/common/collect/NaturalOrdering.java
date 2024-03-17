package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NaturalOrdering
        extends Ordering<Comparable>
        implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();
    private static final long serialVersionUID = 0L;

    public int compare(Comparable paramComparable1, Comparable paramComparable2) {
        Preconditions.checkNotNull(paramComparable1);
        Preconditions.checkNotNull(paramComparable2);
        return paramComparable1.compareTo(paramComparable2);
    }

    public <S extends Comparable> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "Ordering.natural()";
    }
}




