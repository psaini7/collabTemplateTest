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
 * <code>DecimalElement</code> is the decimal equivalent of {@link NumberElement}
 * and holds either a <code>Float</code> or <code>Double</code> object.
 * <p>
 * Depending on length and decimal places of the <code>Element.Type</code>
 * the literal <code>123.456</code> is converted to (examples):
 * <ul>
 * 	<li>length 6, decimals 3: <code>"123456"</code>
 * 	<li>length 8, decimals 2: <code>"00012345"</code>
 * 	<li>length 3, decimals 0: <code>"123"</code>
 * </ul>
 * 
 * @author Kristian Kraljic
 */
public class DecimalElement extends Element<Number> {
	private Number decimal;
	
	/**
	 * Create a new <code>DecimalElement</code> using a <code>Float</code>
	 * @param type The type of the element
	 * @param decimal The decimal value of the element (float)
	 */
	public DecimalElement(Type type,float decimal) { this(type,new Float(decimal)); }
	/**
	 * Create a new <code>DecimalElement</code> using a <code>Double</code>
	 * @param type The type of the element
	 * @param decimal The decimal value of the element (double)
	 */
	public DecimalElement(Type type,double decimal) { this(type,new Double(decimal)); }
	private DecimalElement(Type type,Number decimal) {
		super(type);
		if(!type.isDecimal())
			throw new ElementException("Can not create a decimal element of type "+type);
		this.decimal = decimal;
	}
	
	@Override public Number getValue() { return decimal; }
	@Override public boolean isEmpty() { return (this.decimal instanceof Float?this.decimal.floatValue():this.decimal.doubleValue())==0; }
	@Override public String toString() {
		int number,decimal,factor=(int)Math.round(Math.pow(10,type.getDecimals()));
		if(this.decimal instanceof Float) {
			float value = Math.abs(this.decimal.floatValue());
			number = (int)value;
			decimal = (int)((value-number)*factor);
		}	else {
			double value = Math.abs(this.decimal.doubleValue());
			number = (int)value;
			decimal = (int)((value-number)*factor);
		}
		return format(type.format,number,decimal);
	}
}