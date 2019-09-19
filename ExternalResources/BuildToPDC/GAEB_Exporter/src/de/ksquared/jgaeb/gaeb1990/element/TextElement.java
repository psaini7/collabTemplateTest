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

import java.util.regex.Pattern;

/**
 * <code>TextElement</code> contains the textual representation of any object
 * limited to the length specified by the {@link Element.Type}.
 * <p>
 * If the string has more characters than specified in the <code>Element.Type</code>
 * the text is capepd at the right.
 * <p>
 * If the <code>Element.Type</code> has a {@link Pattern}, and the value of
 * the <code>TextElement</code> is not empty {@link Pattern#matches(String, CharSequence)}
 * is invoked. If the <code>Pattern</code> does not match a {@link ElementException}
 * is thrown.
 * <p>
 * <code>'\t'</code> <code>'\r'</code> and <code>'\n'</code> are replaced by
 * spaces because they are invalid characters in GAEB1990 files.
 * 
 * @author Kristian Kraljic
 */
public class TextElement extends Element<String> {
	private static final Pattern REPLACE = Pattern.compile("[\t\r\n]"),
			                           EMPTY = Pattern.compile("^ *$");
	private String text;
	
	
	/**
	 * Creates a new <code>TextElement</code> of a speicifc type
	 * @param type The type to be used
	 * @param text The value of the Element
	 */
	public TextElement(Type type,String text) {
		super(type);
		if(type.isNumeric())
			throw new ElementException("Can not create a text element of type "+type);
		this.text = REPLACE.matcher(text).replaceAll(" ");
		if(type.hasPattern()&&!isEmpty()&&!type.getPattern().matcher(format(type.format,text)).matches())
			throw new ElementException("Can not create the text element "+type+" with value '"+text+"' pattern '"+type.getPattern().pattern()+"' does not match");
	}
	
	@Override public String getValue() { return text; }
	@Override public boolean isEmpty() { return EMPTY.matcher(this.text).matches(); }
	@Override public String toString() { return format(type.format,text); }
	
	/**
	 * A method splitting a text into text parts with a maximum length
	 * @param text The text to be splitted
	 * @param length The maximum line length of one split
	 * @return An array with the length of the mimimum required splits
	 */
	public static String[] partitionText(String text,int length) {
		int number = text.length()/length;
		if(text.length()%length>0)
			number+=1;
		String[] splits = new String[number];
		for(int index=0;index<number;index++) {
			int start=index*length,end=start+length;
			if(end>text.length()) end = text.length();
			splits[index] = text.substring(start,end);
		}
		return splits;
	}
}