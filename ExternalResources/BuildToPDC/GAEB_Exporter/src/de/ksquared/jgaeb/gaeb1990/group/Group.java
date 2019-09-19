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

import static de.ksquared.jgaeb.gaeb1990.frame.Frame.Type.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Vector;
import de.ksquared.jgaeb.gaeb1990.element.Element;
import de.ksquared.jgaeb.gaeb1990.file.File;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.FrameException;
import de.ksquared.jgaeb.internal.ArrayIterator;

/**
 * A <code>Group</code> consists of several {@link Frame} or further
 * {@link Group} objects. A <code>Group</code> is a logically grouped
 * set of <code>Frame's</code> which is defined by the GAEB1990 standard.
 * <p>
 * One {@link File} has 0-n* [{@link Group}] has 0-n* {@link GroupElement}.
 * {@link GroupElement} is another {@link Group} or a {@link Frame} containing
 * 1-n* {@link Element}.
 * <p>
 * A <code>Group</code> has a {@link Group.Name}, the <code>Group.Name</code>
 * defines which standard GAEB1990 Group is defined. The <code>Group.Name</code>
 * limits the {@link Frame.Type} which can be put into the <code>Group</code>.
 * It defines the order of {@link Frame} and how often the {@link Frame} can
 * be put into this <code>Group</code>.
 * <p>
 * As {@link Frame} and {@link File},{@link Group} is immutable. Therefore the
 * group can be directly converted into a valid GAEB1990 {@link String}.
 * 
 * @author Kristian Kraljic
 */
public class Group implements GroupDefinition {
	/**
	 * <code>Group.Name</code> provides a enumeration of all
	 * Groups usable in a GAEB1990 {@link File}.
	 * <p>
	 * Each <code>Group.Name</code> has a fixed definition of
	 * which {@link Frame.Type} are allowed to use in a {@link Group} 
	 * of this <code>Group.Type</code> and how often they may occour.
	 * <p>
	 * A group has always a designated starting and an optional
	 * ending {@link Frame.Type}. If a {@link Frame} of this {@link Frame.Type}
	 * occours in a valid GAEB1990 {@link File} a new <code>Group</code>
	 * matching this starting <code>Frame.Type</code> begins.
	 * 
	 * @author Kristian Kraljic
	 */
	public enum Name {
		GR_01("Contractual Arrangements (Vertragliche Regelungen)",
				  new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9},
		             new int[]{1    ,-1   ,1    },
		      ZA_T0,ZA_T9),
 		GR_02("Specifications (Leistungsverzeichnis)",
 				  new Frame.Type[]{ZA_00,ZA_01,ZA_02,ZA_03,ZA_04,ZA_06,ZA_07,ZA_08,ZA_99},
                 new int[]{1    ,1    ,9    ,1    ,1    ,1    ,1    ,1    ,1    },
          ZA_00,ZA_99),
 		GR_03("Lot and Lotgroup (Los und Losgruppe)",
 				  new Frame.Type[]{ZA_10,ZA_33,ZA_34,ZA_35},
                 new int[]{1    ,1    ,99   ,99   },
          ZA_10,ZA_33),
 		GR_04("CA-Group (LV-Gruppe)",
 				  new Frame.Type[]{ZA_11,ZA_12,ZA_31,ZA_32},
                 new int[]{1    ,9    ,1    ,1    },
          ZA_11,ZA_31),
 		GR_05("Part Performance (Teilleistung (Position))",
 				  new Frame.Type[]{ZA_21,ZA_22,ZA_23,ZA_30,ZA_27,ZA_28,ZA_25,ZA_26},
                 new int[]{1    ,1    ,1    ,1    ,999  ,1    ,9    ,999  },
          ZA_21),
 		GR_06("Part Performance Descriptions (Teilleistung (Position) Beschreibungen)",
 				  new Frame.Type[]{ZA_24,ZA_28,ZA_25,ZA_26},
                 new int[]{1    ,1    ,9    ,999  },
          ZA_21),
		GR_07("Execution Description (Ausführungsbeschreibung)",
				  new Frame.Type[]{ZA_41,ZA_42,ZA_25,ZA_26},
                 new int[]{1    ,1    ,9    ,999  },
          ZA_41),
	  GR_08("Note Text (Hinweistext)",
	  		  new Frame.Type[]{ZA_20,ZA_26},
	               new int[]{1    ,999  },
	        ZA_20),
 	  GR_09("Wage Changes (Lohnänderungen)",
 	  		  new Frame.Type[]{ZA_45,ZA_46,ZA_47},
 	               new int[]{999  ,9    ,1    },
 	        ZA_45);
		private final String name;
		private final Frame.Type types[],begin,end;
		private final int[] quantities;
		Name(String name,Frame.Type[] types,int[] quantities,Frame.Type begin) { this(name,types,quantities,begin,null); }
		Name(String name,Frame.Type[] types,int[] quantities,Frame.Type begin,Frame.Type end) {
			if(types.length!=quantities.length)
				throw new GroupException("Group "+this+" has not the same size of types and quantity specifications");
			this.name = name;
			this.types = types;
			this.quantities = quantities;
			this.begin = begin; this.end = end;
		}
		/**
		 * Gets the string representation of this <code>Group.Name</code> (Format: English (German))
		 * @return The {@link String} representation
		 */
		public String getName() { return name; }
		/**
		 * Returns a list of all {@link Frame.Type} which may be used for this <code>Group.Type</code>.
		 * @return An array of Frame.Type
		 */
		public Frame.Type[] getTypes() { return types; }
		/**
		 * Returns the quantity of Frames at one index
		 * @param index The index value of the array returned by getTypes()
		 * @return The quantity of Frames used in thos Group.Name (-1 for unlimited)
		 */
		public int getQuantity(int index) { return quantities[index]; }
		/**
		 * Returns the quantity of Frames for one <code>Frame.Type</code>
		 * @param type The type of the Frame
		 * @return The quantity of Frames used in thos <code>Group.Name</code> (-1 for unlimited)
		 */
		public int getQuantity(Frame.Type type) throws NoSuchElementException {
			for(int index=0;index<types.length;index++)
				if(types[index].equals(type))
					return quantities[index];
			throw new NoSuchElementException();
		}
		/**
		 * Get the start element of this <code>Group.Name</code>
		 * @return The begin element Frame.Type of this <code>Group.Name</code> or null
		 */
		public Frame.Type getBegin() { return begin; }
		/**
		 * Get the end element of this <code>Group.Name</code>
		 * @return The end element Frame.Type of this <code>Group.Name</code> or null
		 */
		public Frame.Type getEnd() { return end; }
	}
	
	private Name name;
	
	private String group;
	private GroupElement[] elements;
	
	@Override public Group toGroup() { return this; }
	/**
	 * Create a loose <code>Group</code> of some {@link GroupElement} (no type check!).
	 * <p>
	 * <strong>IMPORTANT</strong>: Handle with care. This constructor
	 * could possibly result in invalid GEAB1990 files. 
	 * @param elements The {@link GroupElement} which should be added to this <code>Group</code>
	 */
	public Group(GroupElement... elements) {
		StringBuilder group = new StringBuilder();
		for(GroupElement element:elements)
			group.append(element.toString());
		this.group = group.toString();
		this.elements = elements;
	}
	/**
	 * Creates and checks a named <code>Group</code> with the {@link GroupElement} given.
	 * <p>
	 * It will be checked if all requrired {@link GroupElement} are given and if the
	 * quanity limits are met.
	 * @param name The <code>Group.Name</code> of the Group
	 * @param elements The {@link GroupElement} which should be added to this <code>Group</code>
	 */
	public Group(Name name,GroupElement... elements) {
		this.name = name;
		StringBuilder group = new StringBuilder();
		Map<Frame.Type,Integer> quantities = new HashMap<Frame.Type,Integer>();
		for(Frame.Type type:name.types)
			quantities.put(type,0);
		for(GroupElement element:elements) {
			if(element instanceof GroupElementBuilder)
				element = ((GroupElementBuilder<?>)element).getGroupElement();
			if(element instanceof Frame) {
				Frame frame = (Frame)element;
				Frame.Type type = frame.getType();
				if(!quantities.containsKey(type))
					throw new GroupException("Unexpected frame "+type+" in group "+name);
				else quantities.put(type,quantities.get(type)+1);
				group.append(frame.toString());
			} else group.append(element.toString()); //another group (or other inherited element) (if it is a builder toString of getGroupElement will be invoked)
		}
		for(Entry<Frame.Type,Integer> quantity:quantities.entrySet()) {
			int maximumQuantity = name.getQuantity(quantity.getKey());
			if(maximumQuantity>=0&&maximumQuantity<quantity.getValue()) //negative means no check
				throw new FrameException("In group "+name.name+" frame "+quantity.getKey().getType()+" can only appear "+maximumQuantity+" time(s), but it was given "+quantity.getValue()+" time(s)");	
		}
		this.group = group.toString();
		this.elements = elements;
	}
	
	/**
	 * Tries to convert a loose group of {@link Frame} into a named {@link Group} 
	 * @param frames The frames which should be converted into a {@link Group}
	 * @return A <code>Group</code> object
	 */
	public static Group buildGroup(ArrayIterator<Frame> frames) { return buildGroup(frames,null); }
	private static Group buildGroup(ArrayIterator<Frame> frames,Name groupName) {
		List<GroupElement> elements = new Vector<GroupElement>();
		if(groupName!=null)
			elements.add(frames.next());
		while(frames.hasNext()) {
			Frame frame = frames.peekNext();
			Frame.Type frameType = frame.getType();
			Group group = null;
			for(Name name:Name.values())
				if(frameType.equals(name.getBegin())) {
					group = buildGroup(frames,name);
					break;
				}
			if(group==null) {
				if(groupName!=null) {
					for(Frame.Type type:groupName.types)
						if(type.equals(frameType)) {
							elements.add(frames.next());
							frame = null;
							break;
						}
					if(frame!=null||frameType.equals(groupName.getEnd())) //this frame does not belong to this group and is no group start, so finish this group
						break;						
				} else elements.add(frames.next());
			} else elements.add(group);
		}
		if(groupName==null)
			   return new Group(elements.toArray(new GroupElement[0]));
		else return new Group(groupName,elements.toArray(new GroupElement[0]));
	}
	
	/**
	 * Returns the name of the Group
	 * @return The Group.Name
	 */
	public Name getName() { return name; }
	
	/**
	 * Get a list of all {@link GroupElement} of this <code>Group</code>
	 * @return An array of <code>GroupElement</code>
	 */
	public GroupElement[] getElements() { return elements; }
	@Override public String toString() { return group; }
	
	/**
	 * Checks if this <code>Group</code> contains a sub-group with a certain
	 * name and returns it.
	 * <p>
	 * If multiple sub-groups with the same name are in this <code>Group</code>
	 * always the first <code>Group</code> matching will be returned.
	 * @param name The <code>Group.Name</code> of a <code>Group</code>
	 * @return A <code>Group</code> object or <code>null</code>
	 */
	public Group getGroup(Group.Name name) {
		for(GroupElement element:elements)
			if(element instanceof Group&&((Group)element).getName().equals(name))
				return (Group)element;
		return null;
	}
	/**
	 * Checks if this <code>Group</code> contains a {@link Frame} with a certain
	 * {@link Frame.Type} and returns it.
	 * <p>
	 * If multiple <code>Frame's</code> with the same <code>Frame.Type</code> are
	 * in this <code>Group</code> always the first <code>Frame</code> matching
	 * will be returned.
	 * @param type The <code>Frame.Type</code> of a <code>Frame</code>
	 * @return A <code>Frame</code> object or <code>null</code>
	 */
	public Frame getFrame(Frame.Type type) {
		for(GroupElement element:elements)
			if(element instanceof Frame&&((Frame)element).getType().equals(type))
				return (Frame)element;
		return null;
	}
	
	@Override public Iterator<GroupElement> iterator() { return new ArrayIterator<GroupElement>(elements); }
}
