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

package de.ksquared.jgaeb.gaeb1990.frame;

import java.util.Iterator;
import de.ksquared.jgaeb.gaeb1990.element.Element;
import de.ksquared.jgaeb.gaeb1990.frame.Frame.Type;
import de.ksquared.jgaeb.gaeb1990.group.GroupElement;
import de.ksquared.jgaeb.gaeb1990.group.GroupElementBuilder;
import de.ksquared.jgaeb.internal.ArrayIterator;

/**
 * A <code>FrameBuilder</code> can be used to construct a {@link Frame} object.
 * <p>
 * The <code>FrameBuilder</code> is not immutable, so it can be modified and
 * a immutable {@link Frame} can be created at any time using {@link FrameBuilder#toFrame()}.
 * Also invoking {@link FrameBuilder#toString()} will create {@link Frame} first.
 * <p> 
 * Add any {@link Element} to the <code>FrameBuilder</code>. The <code>Element</code>
 * will always be added to the next free element slot in the <code>FrameBuilder</code>.
 * An element slot is a valid position for a {@link Element} with a specific
 * {@link Element.Type}. If there is no valid position (so no element slot) for
 * one {@link Element} a {@link FrameException} is thrown.
 * <p>
 * All builders are generated at the time <code>toFrame</code> is invoked.
 * 
 * @author Kristian Kraljic
 */
public class FrameBuilder implements FrameDefintion,GroupElement,GroupElementBuilder<Frame> {
	private Type type;
	private int satznr;
	
	private Element<?>[] elements;
	
	@Override public Frame toFrame() { return getFrame(); }
	/**
	 * Creates a new <code>FrameBuilder</code> for one specific {@link Frame.Type}
	 * @param type The type of the FrameBuilder object
	 */
	public FrameBuilder(Type type) { this(type,0); }
	/**
	 * Creates a new <code>FrameBuilder</code> for one specific {@link Frame.Type} with a given SATZNR
	 * @param type The type of the Frame object
	 * @param satznr The satz number to use
	 */	
	public FrameBuilder(Type type,int satznr) {
		this.type = type;
		this.satznr = satznr;
		this.elements = new Element<?>[type.getElementTypes().length-2]; //ignore ZA and SATZNR
	}
	
	/**
	 * Returns the type of this <code>FrameBuilder</code>
	 * @return The Frame.Type
	 */
	public Type getType() { return type; }
	/**
	 * The numeric vaulue of the SATZNR of this frame
	 * @return A positive number if a SATZNR was set or 0 if not
	 */
	public int getSatznr() { return satznr; }
	/**
	 * Sets the SATZNR for this <code>FrameBuilder</code>
	 * @param satznr The SATZNR to set
	 */
	public void setSatznr(int satznr) { this.satznr = satznr; }
	
	/**
	 * Clears all elements currently in this <code>FrameBuilder</code>.
	 * This resets the <code>FrameBuilder</code>.
	 */
	public void clearElements() { this.elements = new Element<?>[type.getElementTypes().length]; }
	/**
	 * Get a list of all {@link Element} currently in this frame.
	 * @return An array of Element
	 */
	public Element<?>[] getElements() { return elements; }
	/**
	 * Creates a new {@link Element} and adds it to this <code>FrameBuilder</code>.
	 * {@link Element#create(de.ksquared.jgaeb.gaeb1990.element.Element.Type, Object)}
	 * is invoked before calling {@link FrameBuilder#addElement(Element)}
	 * @param type The Element.Type of the Element to create
	 * @param value The value of the Element
	 */
	public void addElement(Element.Type type,Object value) { addElement(Element.create(type,value)); }
	/**
	 * Adds an existing {@link Element} to the <code>FrameBuilder</code>. The 
	 * <code>Element</code> will be added to the right position always. The
	 * right position is determined by the {@link Element.Type} of the
	 * <code>Element</code>. If the same <code>Element.Type</cod> is used multiple
	 * times in one {@link Frame.Type}, the <code>Element</code> gets added to the´
	 * next unset element slot. If there are not free slots an {@link FrameException}
	 * is thrown.
	 * 
	 * @throws FrameException if the Element can not be added to this Frame.Type or if all slots for this Element were filled already.
	 * @param element The Element to be added
	 */
	public void addElement(Element<?> element) {
		if(element.type.equals(Element.Type.ZA)||element.type.equals(Element.Type.SATZNR))
			throw new FrameException("Adding the element types ZA or SATZNR to frame "+type+" is not possible.");
		boolean found=false,filled=false;
		for(int index=1;index<type.getElementTypes().length-1;index++)
			if(type.getElementTypes()[index].equals(element.type)) //implicit check for @code{instanceof Element.Type}
				if(elements[index-1]==null) {
					elements[index-1] = element;
					found = filled = true;
					break;
				} else found = true;
		if(!found) throw new FrameException("Cant add element "+element.type+" to frame "+type+", element type is not used in this frame type");
		if(!filled)throw new FrameException("Cant add element "+element.type+" to frame "+type+", all slots for this element type have already been filled");
	}
	
	/**
	 * Similar to the toFrame() method
	 * @return A new immutable Frame object
	 */
	public Frame getFrame() { return new Frame(type,satznr,getElements()); }
	@Override public String toString() { return getFrame().toString(); }
	@Override public Frame getGroupElement() { return getFrame(); }
	
	@Override public Iterator<Element<?>> iterator() { return new ArrayIterator<Element<?>>(elements); }
}
