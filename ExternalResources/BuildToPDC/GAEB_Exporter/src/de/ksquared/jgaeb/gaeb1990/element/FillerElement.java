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

package de.ksquared.jgaeb.gaeb1990.element;

import java.text.MessageFormat;

/**
 * A <code>FillerElement</code> is an {@link Element} of a specific length
 * containing only <code>' '</code>.
 * <p>
 * The <code>FillerElement</code> is the only <code>Element</code> where
 * each instance can have different length definitions.
 * <p>
 * A <code>FillerElement</code> is never empty (even if it has the length 0).
 * If {@link FillerElement#getValue()} is invoked an {@link ElementException}
 * is thrown.
 * 
 * @author Kristian Kraljic
 */
public class FillerElement extends Element<Object> {
	private static final String EMPTY = new String().intern();
	
	private int length;
	private String format;
	
	/**
	 * Creates a new <code>FillerElement</code> of specific length
	 * @param length The length of the filler
	 */
	public FillerElement(int length) {
		super(Type.FILLER);
		this.length = length;
		this.format = MessageFormat.format("%{0}s",length);
	}
	
	/**
	 * Returns the dynamic length of this FILLER
	 * @return The length
	 */
	public int getLength() { return length; }
	
	@Override public boolean isEmpty() { return false; }
	@Override public Object getValue() {
		throw new ElementException("A filler does not implement the getValue() method");
	}
	
	@Override public String toString() { return format(format,EMPTY); }
}
