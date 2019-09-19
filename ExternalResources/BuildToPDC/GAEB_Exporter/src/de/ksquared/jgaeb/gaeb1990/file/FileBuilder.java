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

package de.ksquared.jgaeb.gaeb1990.file;

import java.util.List;
import java.util.Vector;
import de.ksquared.jgaeb.gaeb1990.file.File.Identifier;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.FrameBuilder;
import de.ksquared.jgaeb.gaeb1990.frame.FrameDefintion;
import de.ksquared.jgaeb.gaeb1990.group.Group;
import de.ksquared.jgaeb.gaeb1990.group.Group.Name;
import de.ksquared.jgaeb.gaeb1990.group.GroupBuilder;
import de.ksquared.jgaeb.gaeb1990.group.GroupDefinition;
import de.ksquared.jgaeb.gaeb1990.group.GroupElement;
import de.ksquared.jgaeb.gaeb1990.group.GroupElementBuilder;

/**
 * A <code>FileBuilder</code> can be used to construct a {@link File} object.
 * <p>
 * The <code>FileBuilder</code> is not immutable, so it can be modified and
 * a immutable {@link File} can be created at any time using {@link FileBuilder#toFile()}.
 * Also invoking {@link FileBuilder#toString()} will create {@link File} first.
 * <p> 
 * Add either {@link Group} or {@link GroupBuilder} to the <code>FileBuilder</code>.
 * All builders are generated at the time <code>toFile</code> is invoked.
 * 
 * @author Kristian Kraljic
 */
public class FileBuilder implements FileDefinition,GroupElementBuilder<Group> {
	private Identifier identifier;
	
	private List<GroupDefinition> groups;
	
	private int satznr = 0;
	
	@Override public File toFile() { return getFile(); }
	/**
	 * Create a new initial <code>FileBuilder</code> for one {@link File.Identifier}
	 * @param identifier The identifier of the file
	 */
	public FileBuilder(Identifier identifier) {
		this.identifier = identifier;
		this.groups = new Vector<GroupDefinition>();
	}
	/**
	 * Returns the {@link File.Identifier} for this <code>FileBuilder</code>
	 * @return The identifier
	 */
	public Identifier getIdentifier() { return identifier; }
	
	/**
	 * Clears all groups currently in the <code>FileBuilder</code>. This
	 * resets the <code>FileBuilder</code> completely.
	 */
	public void clearGroups() { groups.clear(); }
	/**
	 * Returns a list to all {@link Group} (or {@link GroupBuilder}) currently in
	 * the <code>FileBuilder</code>.
	 * @return an array of {@link GroupDefinition}
	 */
	public GroupDefinition[] getGroups() { return groups.toArray(new GroupDefinition[0]); }
	/**
	 * Adds a existing {@link Group} or {@link GroupBuilder} to this <code>FileBuilder</code>
	 * @param group The {@link GroupDefinition} to add
	 */
	public void addGroup(GroupDefinition group) { groups.add(group); }
	/**
	 * Creates a new {@link GroupBuilder} and adds it to this <code>FileBuilder</code>
	 * @param name The {@link Group.Name} of this Group (use <code>null</code> 
	 * for unnamed <code>Group's</code>)
	 * @return A new <code>GroupBuilder</code> object
	 */
	public GroupBuilder newGroup(Name name) {
		GroupBuilder builder = new GroupBuilder(name);
		addGroup(builder);
		return builder;
	}
	
	/**
	 * Generates all builders and returns an immutable {@link File} object
	 * @return An GAEB1990 file
	 */
	public File getFile() {
		GroupBuilder builder = new GroupBuilder();
		for(GroupDefinition group:groups)
			builder.addElement(group);
		return new File(identifier,defineGroup(builder));
	}
	@Override public String toString() { return getFile().toString(); }
	@Override public Group getGroupElement() { return new Group(getGroups()); }
	
	private Group defineGroup(GroupDefinition group) throws FileException {
		for(GroupElement element:group)
			if(element instanceof FrameDefintion)
           defineFrame((FrameDefintion)element);
			else defineGroup((GroupDefinition)element);
		return group.toGroup();
	}
	private void defineFrame(FrameDefintion frame) {
		if(frame instanceof FrameBuilder) {
			FrameBuilder builder = (FrameBuilder)frame;
			if(builder.getSatznr()!=0) {
				int satznr = builder.getSatznr();
				if(this.satznr<satznr)
					this.satznr = satznr;
			} else builder.setSatznr(++satznr);
		} else {
			int satznr = ((Frame)frame).getSatznr();
			if(this.satznr<satznr)
				this.satznr = satznr;			
		}
	}
}