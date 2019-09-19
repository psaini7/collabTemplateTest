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

package de.ksquared.jgaeb.gaeb1990.group;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.FrameBuilder;
import de.ksquared.jgaeb.gaeb1990.group.Group.Name;

/**
 * A <code>GroupBuilder</code> can be used to construct a {@link Group} object.
 * <p>
 * The <code>GroupBuilder</code> is not immutable, so it can be modified and
 * a immutable {@link Group} can be created at any time using {@link GroupBuilder#toGroup()}.
 * Also invoking {@link GroupBuilder#toString()} will create {@link Group} first.
 * <p> 
 * Add any {@link GroupElement} to the <code>GroupBuilder</code>. The <code>GroupElement</code>
 * will always be appended to the end of the <code>GroupBuilder</code> groups.
 * <p>
 * All builders are generated at the time <code>toGroup</code> is invoked.
 * 
 * @author Kristian Kraljic
 */
public class GroupBuilder implements GroupDefinition,GroupElement,GroupElementBuilder<Group> {
	private Name name;
	
	private List<GroupElement> elements;
	
	@Override public Group toGroup() { return getGroup(); }
	/**
	 * Creates a new initial <code>GroupBuilder</code> for creating an unnamed {@link Group} 
	 */
	public GroupBuilder() { this.elements = new Vector<GroupElement>(); }
	/**
	 * Creates a new initial <code>GroupBuilder</code> for creating a named {@link Group}
	 * @param name The Group.Name used
	 */
	public GroupBuilder(Name name) {
		this();
		this.name = name;
	}
	
	/**
	 * Returns the {@link Group.Name} if this <code>GroupBuilder</code>
	 * @return The Group.Name or <code>null</code>
	 */
	public Name getName() { return name; }
	
	/**
	 * Clears all elements currently in this <code>GroupBuilder</code>.
	 * This resets the <code>GroupBuilder</code>.
	 */
	public void clearElements() { elements.clear(); }
	/**
	 * Get a list of all {@link GroupElement} currently in this group.
	 * @return An array of GroupElement
	 */
	public GroupElement[] getElements() { return elements.toArray(new GroupElement[0]); }
	/**
	 * Adds a existing {@link GroupElement} to the end of this {@link GroupBuilder}
	 * @param element The GroupElement to be added
	 */
	public void addElement(GroupElement element) { elements.add(element); }
	/**
	 * Creates a new sub-group and appends it to the end of the {@link GroupBuilder}
	 * @param name The Group.Name of the Group to be added
	 * @return A {@link GroupBuilder} of the newly added Group
	 */
	public GroupBuilder newGroup(Name name) {
		GroupBuilder builder = new GroupBuilder(name);
		addElement(builder);
		return builder;
	}
	/**
	 * Creates a new frame and appends it to the end of the {@link GroupBuilder}
	 * @param type The Frame.Type of the Frame to be added
	 * @return A {@link FrameBuilder} of the newly added Frame
	 */
	public FrameBuilder newFrame(Frame.Type type) { return newFrame(type,0); }
	/**
	 * Creates a new frame with a specific SATZNR and appends it to the end of the {@link GroupBuilder}
	 * @param type The Frame.Type of the Frame to be added
	 * @param satznr The Frames SATZNR
	 * @return A {@link FrameBuilder} of the newly added Frame
	 */
	public FrameBuilder newFrame(Frame.Type type,int satznr) {
		FrameBuilder builder = new FrameBuilder(type,satznr);
		addElement(builder);
		return builder;
	}
	
	/**
	 * Similar to the toGroup() method
	 * @return A new immutable Group object
	 */
	public Group getGroup() { return name!=null?new Group(name,getElements()):new Group(getElements()); }
	@Override public String toString() { return getGroup().toString(); }
	@Override public Group getGroupElement() { return getGroup(); }
	
	@Override public Iterator<GroupElement> iterator() { return elements.iterator(); }
}
