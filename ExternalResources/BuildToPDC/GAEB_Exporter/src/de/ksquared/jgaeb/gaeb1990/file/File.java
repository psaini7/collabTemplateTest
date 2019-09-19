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

import static de.ksquared.jgaeb.gaeb1990.frame.Frame.Type.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import de.ksquared.jgaeb.gaeb1990.element.Element;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.Frame.Type;
import de.ksquared.jgaeb.gaeb1990.frame.FrameBuilder;
import de.ksquared.jgaeb.gaeb1990.frame.FrameException;
import de.ksquared.jgaeb.gaeb1990.group.Group;
import de.ksquared.jgaeb.gaeb1990.group.GroupElement;
import de.ksquared.jgaeb.gaeb1990.group.GroupElementBuilder;
import de.ksquared.jgaeb.gaeb1990.group.GroupException;
import de.ksquared.jgaeb.internal.ArrayIterator;

/**
 * A <code>File</code> covers all {@link Group}s of a valid GAEB1990 file:
 * <p>
 * One [{@link File}] has 0-n* {@link Group} has 0-n* {@link GroupElement}.
 * {@link GroupElement} is another {@link Group} or a {@link Frame} containing
 * 1-n* {@link Element}.
 * <p>
 * A <code>File</code> has an {@link File.Identifier}, the <code>File.Identifier</code>
 * defines the file type and the GAEB1990 data transfer phase. Depending on
 * the <code>File.Identifier</code> a valid GAEB1990 must contain certain
 * {@link Frame.Type}.
 * <p>
 * As {@link Group} and {@link Frame}, {@link File} is immutable. Therefore the
 * file can be directly converted into a valid GAEB1990 {@link String}.
 * 
 * @author Kristian Kraljic
 */
public class File implements FileDefinition {
	public static final Charset CHARSET = Charset.forName("IBM850");
	
	/**
	 * <code>File.Identifier</code> provides a enumeration of all data transfer
	 * phases used in GAEB1990. Each data transfer phase is using a specific set
	 * of {@link Frame.Type}. Some <code>Frame.Type's</code> are required and
	 * therefore flagged as <code>obligatory</code>. The method {@link File.Identifier#isObligatory(Type)}
	 * can be used to determine if one {@link Frame.Type} is obligatory.
	 * <p>
	 * Each <code>File.Identifier</code> also has an unique file name suffix.
	 * 
	 * @author Kristian Kraljic
	 */
	public enum Identifier {
		KE_81(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_07,ZA_08,ZA_10,ZA_11,ZA_12,ZA_20,ZA_21,ZA_24,ZA_25,ZA_26,ZA_27,ZA_28,ZA_31,ZA_33,ZA_35,ZA_41,ZA_42,ZA_99},
				     new boolean[]{false,false,false,true ,true ,true ,false,false,false,false,false,false,false,true ,false,true ,true ,false,false,false,false,false,false,false,true },
				  Element.create(Element.Type.DP,"81"),".x81"),
		KE_82(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_07,ZA_08,ZA_11,ZA_12,ZA_21,ZA_22,ZA_24,ZA_25,ZA_26,ZA_27,ZA_28,ZA_31,ZA_41,ZA_42,ZA_99},
				     new boolean[]{false,false,false,true ,true ,true ,false,false,true ,false,false,true ,true ,false,true ,true ,false,false,false,false,false,true },
				  Element.create(Element.Type.DP,"82"),".x82"),
		KE_83(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_06,ZA_07,ZA_08,ZA_10,ZA_11,ZA_12,ZA_20,ZA_21,ZA_23,ZA_24,ZA_25,ZA_26,ZA_27,ZA_28,ZA_31,ZA_32,ZA_33,ZA_34,ZA_35,ZA_41,ZA_42,ZA_45,ZA_46,ZA_47,ZA_99},
				     new boolean[]{false,false,false,true ,true ,true ,true ,false,false,true ,false,false,false,false,true ,false,false,true ,true ,false,false,false,false,false,false,false,false,false,false,false,false,true },
				  Element.create(Element.Type.DP,"83"),".x83"),
		KE_84(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_04,ZA_06,ZA_08,ZA_10,ZA_21,ZA_23,ZA_24,ZA_26,ZA_30,ZA_32,ZA_33,ZA_34,ZA_35,ZA_41,ZA_42,ZA_45,ZA_46,ZA_47,ZA_99},
				     new boolean[]{false,false,false,true ,true ,true ,true ,true ,false,true ,false,true ,true ,false,true ,false,false,false,false,false,false,false,false,false,false,true },
				  Element.create(Element.Type.DP,"84"),".x84"),
		KE_85(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_04,ZA_06,ZA_07,ZA_08,ZA_10,ZA_11,ZA_12,ZA_20,ZA_21,ZA_23,ZA_24,ZA_25,ZA_26,ZA_27,ZA_28,ZA_30,ZA_31,ZA_32,ZA_33,ZA_34,ZA_35,ZA_41,ZA_42,ZA_45,ZA_46,ZA_47,ZA_99},
				     new boolean[]{false,false,false,true ,true ,true ,true ,true ,false,false,true ,false,false,false,false,true ,true ,false,true ,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,true },
				  Element.create(Element.Type.DP,"85"),".x85"),
		KE_86(new Frame.Type[]{ZA_T0,ZA_T1,ZA_T9,ZA_00,ZA_01,ZA_02,ZA_03,ZA_04,ZA_06,ZA_07,ZA_08,ZA_10,ZA_11,ZA_12,ZA_20,ZA_21,ZA_23,ZA_24,ZA_25,ZA_26,ZA_27,ZA_28,ZA_30,ZA_31,ZA_32,ZA_33,ZA_34,ZA_35,ZA_41,ZA_42,ZA_45,ZA_46,ZA_47,ZA_99},
				     new boolean[]{false,false,false,false ,false ,false ,false ,false ,false,false,false ,false,false,false,false,false ,false ,false,false ,false ,false,false,false,false,false,false,false,false,false,false,false,false,false,false },
				  Element.create(Element.Type.DP,"86"),".x86");
		private final Frame.Type[] types;
		private final boolean[] obligatory;
		private final String suffix;
		private final Element<?> dp;
		Identifier(Frame.Type[] types,boolean[] obligatory,Element<?> dp,String suffix) {
			if(!dp.type.equals(Element.Type.DP))
				throw new FileException("Identifier "+this+" must specify a valid DP element, but "+dp.type+" given");
			if(types.length!=obligatory.length)
				throw new FileException("Identifier "+this+" has not the same size of types and obligatory specifications");
			this.types = types;
			this.obligatory = obligatory;
			this.dp = dp;
			this.suffix = suffix;
		}
		
		/**
		 * Returns the types of Frames which are valid to use in this File.Identifier
		 * @return An array of Frame.Type's
		 */
		public Frame.Type[] getTypes() { return types; }
		/**
		 * Checks wether the Type at one index is obligatory
		 * @param index The index value of the array returned by getTypes()
		 * @return True if this Frame.Type is obligatory for this File.Identifier
		 */
		public boolean isObligatory(int index) { return obligatory[index]; }
		/**
		 * Checks wether the Frame.Type is obligatory for this File.Identifier
		 * @param type The type which should be checked
		 * @return True if this Frame.Type is obligatory for this File.Type
		 */
		public boolean isObligatory(Frame.Type type) {
			for(int index=0;index<types.length;index++)
				if(types[index].equals(type))
					return isObligatory(index);
			return false;
		}
		/**
		 * Gets the proposed file suffix for a valid File of this File.Identifier
		 * @return The string to append to the filename
		 */
		public String getSuffix() { return suffix; }
		/**
		 * The data type element (DP-Element) for a file of this File.Identifier
		 * @return A DP Element
		 */
		public Element<?> getDP() { return dp; }
	}
	/**
	 * The strategy for reading a file:
	 * <ul>
	 * 	<li>STRICT: Each error while reading the file results into an exception.
	 *	<li>PERMISSIVE: Only fatal errors result into an execption.
	 *  <li>LAZY: Errors are mostly ignored, even if a invalid GAEB1990 is read.
	 * </ul> 
	 * @author Kristian Kraljic
	 */
	public enum Strategy { STRICT,PERMISSIVE,LAZY }
	
	private Identifier identifier;
	
	private String file;
	private Group[] groups;
	
	private int satznr = 0;
	
	@Override public File toFile() { return this; }
	
	/**
	 * Creates a new GAEB1990 {@link File} with a certain number of groups.
	 * <p>  
	 * The {@link Group} are appended to the <code>File</code> in the order they are
	 * given to the constructor. All <code>Group's</code> are validated, so all
	 * <code>Group's</code> in total must result into a valid GAEB1990 <code>File</code>.
	 * <p>
	 * A valid GAEB1990 file at least contains all obligatory <code>Frame.Type's</code>,
	 * and does not include any invalid <code>Frame.Type's</code> for this {@link File.Identifier}.
	 * It is also checked if the {@link Element.Type#SATZNR} of all {@link Frame} are continious
	 * and valid (starting at 1). 
	 * 
	 * @param identifier The Identifier of this File
	 * @param groups The groups of the file
	 */
	public File(Identifier identifier,Group... groups) {
		this.identifier = identifier;
		Group group = new Group(groups);
		Map<Frame.Type,Boolean> found = new HashMap<Frame.Type,Boolean>();
		for(Frame.Type type:identifier.types)
			found.put(type,false);
		checkGroup(group,found);
		for(int index=0;index<identifier.types.length;index++)
			if(identifier.obligatory[index]&&!found.get(identifier.types[index]).booleanValue())
					throw new FrameException("Obligatory frame "+identifier.types[index]+" was missing in file "+identifier);
		this.file = group.toString();
		this.groups = groups;
	}
	
	private void checkGroup(Group group,Map<Type,Boolean> found) throws FileException {
		ArrayIterator<GroupElement> iterator = new ArrayIterator<GroupElement>(group.getElements());
		while(iterator.hasNext()) {
			GroupElement element = iterator.next();
			if(element instanceof GroupElementBuilder)
				if(element instanceof FrameBuilder) {
					FrameBuilder frame = (FrameBuilder)element;
					if(frame.getSatznr()==0)
						frame.setSatznr(satznr+1);
					element = frame.getFrame();
				} else element = ((GroupElementBuilder<?>)element).getGroupElement();
			if(element instanceof Frame) {
				Frame frame = (Frame)element;
				Frame.Type type = frame.getType();
				if(!found.containsKey(type))
					throw new FileException("Unexpected frame "+type+" in file "+identifier);
				else found.put(type,true);		
				if(frame.getSatznr()!=++satznr)
					throw new FrameException("Frame "+type+" with satznr "+satznr+" was expected in file "+identifier+" but frame "+frame.getSatznr()+" found");
			} else if(element instanceof Group) checkGroup((Group)element,found);
			  else throw new FileException("Unknown group element in file "+identifier);
		}
	}

	/**
	 * Gets the <code>File.Identifier</code>
	 * @return The File.identifier
	 */
	public Identifier getIdentifier() { return identifier; }

	/**
	 * Returns a list of all <code>Group's</code> of this file
	 * @return An array of Groups
	 */
	public Group[] getGroups() { return groups; }
	@Override public String toString() { return file; }
	
	/**
	 * This method writes the GAEB1990 File into a {@link java.io.File} object 
	 * @param file The target {@link java.io.File}
	 * @throws IOException
	 */
	public void writeTo(java.io.File file) throws IOException { writeTo(new FileOutputStream(file)); }
	/**
	 * This method writes the String representation of this GAEB1990 file
	 * into an output stream using the default IBM850 codepage (DOS Codepage).
	 * @param output The output stream to write to
	 * @throws IOException
	 */
	public void writeTo(OutputStream output) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(output,CHARSET));
		writer.write(this.file);
		writer.close();
	}
	
	/**
	 * Reads and returns a GAEB1990 file from a {@link java.io.File}
	 * (using {@link Strategy#STRICT})
	 * @param file The {@link java.io.File} to read
	 * @return A GAEB1990 <code>File</code>
	 * @throws IOException
	 */
	public static File readFrom(java.io.File file) throws IOException { return readFrom(new FileInputStream(file),Strategy.STRICT); }
	/**
	 * Reads and returns a GAEB1990 file from a {@link InputStream}
	 * @param input The stream to read
	 * @param strategy The stategy to use @see {@link Strategy}
	 * @return A GAEB1990 <code>File</code>
	 * @throws IOException
	 */
	public static File readFrom(InputStream input,Strategy strategy) throws IOException {
		String line = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(input,File.CHARSET));
		List<Frame> frames = new Vector<Frame>();
		while((line=reader.readLine())!=null)
			frames.add(new Frame(line,strategy));
		reader.close();
		
		Identifier identifier = null;
		for(Frame frame:frames)
			if(frame.getType().equals(Frame.Type.ZA_00)) {
				identifier = Identifier.valueOf("KE_"+frame.getElement(Element.Type.DP).getValue());
				break;
			}
		if(identifier==null)
			throw new FileException("Frame ZA_00 missing, no identifier can be determined");
				
		Group group = null;
		try { group = Group.buildGroup(new ArrayIterator<Frame>(frames.toArray(new Frame[0]))); }
		catch(GroupException e) {
			if(strategy.equals(Strategy.STRICT))
				throw e;
			group = new Group(frames.toArray(new Frame[0]));
		}
		
		return new File(identifier,group);
	}
}