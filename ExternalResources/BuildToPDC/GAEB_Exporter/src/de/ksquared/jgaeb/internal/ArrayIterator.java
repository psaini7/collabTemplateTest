/*
 * Copyright 2011 Kristian Kraljic (kSquared.de) All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY KRISTIAN KRALJIC (KSQUARED.DE) ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL KRISTIAN KRALJIC (KSQUARED.DE) OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Kristian Kraljic (kSquared.de).
 */

package de.ksquared.jgaeb.internal;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An implementation of an {@link ListIterator} for any type of
 * Java arrays. Extended support for <code>peek</code> methods.
 *  
 * @author Kristian Kraljic
 */
public class ArrayIterator<Type> implements Iterator<Type>,ListIterator<Type> {
	private int index = 0;
	private Type[] array;
	
	/**
	 * Creates a new array Iterator for an array of a given type
	 * @param array The array to create this Iterator for
	 */
	public ArrayIterator(Type[] array) { this.array = array; }
	@Override public void remove() { throw new UnsupportedOperationException(); }
	@Override public boolean hasNext() { return index<array.length; }
	@Override public boolean hasPrevious() { return index>0; }
	@Override public int nextIndex() { return index; }
	@Override public int previousIndex() { return index-1; }
	@Override public Type next() {
    if(hasNext())
         return array[index++];
    else throw new NoSuchElementException();
	}
	@Override public Type previous() {
    if(hasPrevious())
      return array[--index];
    else throw new NoSuchElementException();
	}
	/**
	 * Peeks the next element of the array without incrementing the Iterator
	 * @return The next array object
	 */
	public Type peekNext() {
		if(hasNext())
      return array[nextIndex()];
    else throw new NoSuchElementException();
	}
	/**
	 * Peeks the previous element of the array without decrementing the Iterator
	 * @return The next array object
	 */
	public Type peekPrevious() {
    if(hasPrevious())
      return array[previousIndex()];
    else throw new NoSuchElementException();
	}
	
	@Override public void set(Type element) { array[index] = element; }
	@Override public void add(Type e) { throw new UnsupportedOperationException(); }
}
