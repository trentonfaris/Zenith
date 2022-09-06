package com.trentonfaris.zenith.graphics.model;

import com.trentonfaris.zenith.Zenith;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Vertex} stores data associated with a {@link Mesh}.
 *
 * @author Trenton Faris
 */
public final class Vertex {
    /**
     * The list of attributes.
     */
    private final List<Attribute<?>> attributes;

    public Vertex(List<Attribute<?>> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            String errorMsg = "Cannot create a Vertex from a null or empty list of attributes.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.attributes = attributes;
    }

    public Vertex copy() {
        List<Attribute<?>> attributesCopy = new ArrayList<Attribute<?>>();
        for (Attribute<?> attribute : attributes) {
            attributesCopy.add(attribute.copy());
        }

        return new Vertex(attributesCopy);
    }

    /**
     * Gets the list of the {@link #attributes}.
     *
     * @return The list of {@link #attributes}.
     */
    public List<Attribute<?>> getAttributes() {
        return attributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        return true;
    }
}
