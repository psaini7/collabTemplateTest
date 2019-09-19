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

/**
 * <code>NumberElement</code> is the non-decimal equivalent of {@link DecimalElement}
 * and holds a <code>Long</code> object.
 * <p>
 * The <code>NumberElement</code> is converted into a string representation
 * of the {@link Element.Type} length padded with zeros. For example the literal
 * <code>9</code> is converted to:
 * <ul>
 * 	<li>length 1: 9
 *  <li>length 4: 0009
 * </ul>
 * <p>
 * An <code>NumberElement</code> is empty if the value <strong>IS SMALLER</strong> or
 * equal to 0. If negative values should be stored in GAEB1990 files a boolean
 * flag for negative values is requried.
 *  
 * @author Kristian Kraljic
 *
 */
public class NumberElement extends Element<Long> {
	private long number;
	
	/**
	 * Creates a new <code>NumberElement</code> of a speicifc type. The type has
	 * to be numeric and not decimal
	 * @param type A 'numeric' type
	 * @param number The value of this number element
	 */
	public NumberElement(Type type,long number) {
		super(type);
		if(!type.isNumeric()||type.isDecimal())
			throw new ElementException("Can not create a number element of type "+type);
		this.number = number;
	}

	@Override public Long getValue() { return number; }
	@Override public boolean isEmpty() { return number<=0; }
	@Override public String toString() { return format(type.format,Math.abs(number)); }
}
