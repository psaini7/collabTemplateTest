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

import static de.ksquared.jgaeb.gaeb1990.element.Element.Type.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import de.ksquared.jgaeb.gaeb1990.element.Element;
import de.ksquared.jgaeb.gaeb1990.element.ElementException;
import de.ksquared.jgaeb.gaeb1990.element.FillerElement;
import de.ksquared.jgaeb.gaeb1990.element.NumberElement;
import de.ksquared.jgaeb.gaeb1990.file.File;
import de.ksquared.jgaeb.gaeb1990.file.FileException;
import de.ksquared.jgaeb.gaeb1990.group.Group;
import de.ksquared.jgaeb.gaeb1990.group.GroupElement;
import de.ksquared.jgaeb.internal.ArrayIterator;

/**
 * A <code>Frame</code> covers all {@link Element} of a valid GAEB1990 line:
 * <p>
 * One {@link File} has 0-n* {@link Group} has 0-n* {@link GroupElement}.
 * {@link GroupElement} is another {@link Group} or a [{@link Frame}] containing
 * 1-n* {@link Element}.
 * <p>
 * A <code>Frame</code> has a {@link Frame.Type}, the <code>Frame.Type</code>
 * defines which standard GAEB1990 Frame is defined. The <code>Frame.Type</code>
 * limits the {@link Element.Type} which can be put into the <code>Frame</code>.
 * It defines the order of {@link Element} and if they are obligatory.
 * <p>
 * As {@link Group} and {@link File}, {@link Frame} is immutable. Therefore the
 * frame can be directly converted into a valid GAEB1990 line {@link String}.
 * 
 * @author Kristian Kraljic
 */
public class Frame implements FrameDefintion {
	private static final String LINE_SEPARATOR = "\r\n";
	
	/**
	 * <code>Frame.Type</code> provides a enumeration of all
	 * Frames usable in a GAEB1990 {@link File}.
	 * <p>
	 * Each <code>Frame.Type</code> has a fixed definition of
	 * which {@link Element.Type} are requried (obligatory) and allowed
	 * to use in a {@link Frame} of this <code>Frame.Type</code>.
	 *  
	 * @author Kristian Kraljic
	 */
	public enum Type {
		ZA_T0(new Object[]{ZA,new FillerElement(72),SATZNR},
		     new boolean[]{true,false,true}),
		ZA_T1(new Object[]{ZA,TEXT,SATZNR},
		     new boolean[]{true,false,true}),
		ZA_T9(new Object[]{ZA,new FillerElement(72),SATZNR},
		     new boolean[]{true,false,true}),
		ZA_00(new Object[]{ZA,new FillerElement(8),DP,KURZLANG,VERGAG,DVNRAG,BIETERAG,VERGAN,DVNRAN,OZMASKE,VERSDTA,LOSKZ,SATZNR},
		     new boolean[]{true,false,true,true,false,false,false,false,false,true,true,false,true}),
		ZA_01(new Object[]{ZA,LVBEZ,LVDATUM,DATANG,DATUHR,DATZUS,KZPREIS,KZLOHN,new FillerElement(1),SATZNR},
		     new boolean[]{true,true,true,false,false,false,false,false,false,true}),
		ZA_02(new Object[]{ZA,PROBEZ,new FillerElement(12),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_03(new Object[]{ZA,AGBEZ,new FillerElement(12),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_04(new Object[]{ZA,ANBEZ,new FillerElement(12),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_06(new Object[]{ZA,ANZEP,BEZEITAN,BEZEPAN1,BEZEPAN2,BEZEPAN3,BEZEPAN4,new FillerElement(1),SATZNR},
		     new boolean[]{true,true,false,true,true,false,false,false,true}),
		ZA_07(new Object[]{ZA,KOSTENNR,LOKALIT,LOKALNR,DATUM,LBKE,new FillerElement(20),SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,true}),
		ZA_08(new Object[]{ZA,WAEKU,WAEBEZ,new FillerElement(16),SATZNR},
		     new boolean[]{true,true,true,false,true}),
		ZA_10(new Object[]{ZA,LOSNR,LOSBEZ,new FillerElement(30),SATZNR},
		     new boolean[]{true,true,false,false,true}),
		ZA_11(new Object[]{ZA,OZ,LVGRART,ZZV,KENNWORT,new FillerElement(44),SATZNR},
		     new boolean[]{true,true,true,false,false,false,true}),
		ZA_12(new Object[]{ZA,LVGRBEZ,new FillerElement(32),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_20(new Object[]{ZA,TA,new FillerElement(1),TEXTSYST,STLNR,new FillerElement(52),SATZNR},
		     new boolean[]{true,false,false,false,false,false,true}),
		ZA_21(new Object[]{ZA,OZ,POSART1,POSART2,POSTYP,ZZ,EPAUFGL,ZUSCHLAG,WIEVOR,KZFRMENG,MENGEMIN,MENGE,EINHEIT,NACHTRKZ,TA,TB,TEXTSYST,STLNR,POSSTAT,KZBELO,new FillerElement(11),SATZNR},
		     new boolean[]{true,true,true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_22(new Object[]{ZA,OZ,VEPMIN,VEP,VEPZPF,VGBMIN,VGB,VFREMEMI,VFREIEME,new FillerElement(7),VZUSCHPM,VZUSCHP,VZUSCHSM,VZUSCHSU,SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_23(new Object[]{ZA,OZ,EPMIN,EP,EPZPF,GBMIN,GB,FREMEMIN,FREIEMEN,AUFABFAK,ZUSCHPRM,ZUSCHPR,ZUSCHSUM,ZUSCHSU,SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_24(new Object[]{ZA,UB,MENGEMIN,UBMENGE,EINHEIT,TA,TB,TEXTSYST,STLNR,KZFRMENG,new FillerElement(33),SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,false,true}),
		ZA_25(new Object[]{ZA,KURZTEXT,KNR,SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_26(new Object[]{ZA,KNR,TEKZ,LANGTEXT,new FillerElement(14),SATZNR},
		     new boolean[]{true,false,false,true,false,true}),
		ZA_27(new Object[]{ZA,KOSTENNR,LOKALIT,MENGEMIN,TEILMENG,LOKALNR,DATUM,LBKE,new FillerElement(8),SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,false,false,true}),
		ZA_28(new Object[]{ZA,KURZAN,OZ,AUSNR,new FillerElement(4),SATZNR},
		     new boolean[]{true,true,false,false,false,true}),
		ZA_30(new Object[]{ZA,ZEITANS,EPANTEL1,EPANTEL2,EPANTEL3,EPANTEL4,new FillerElement(22),SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,true}),
		ZA_31(new Object[]{ZA,OZ,new FillerElement(63),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_32(new Object[]{ZA,OZ,SUMMIN,SUMME,AUFABFAK,AUFABDMM,AUFABDM,PAUSCHMI,PAUSCH,new FillerElement(17),SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,true}),
		ZA_33(new Object[]{ZA,LOSNR,LOSUMME,AUFABFAK,UST,ANGLOS,AUFABDMM,AUFABDM,PAUSCHMI,PAUSCH,SKONTO,ZAFRIST,new FillerElement(3),SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_34(new Object[]{ZA,LOSGRNR,LOSGRSUM,AUFABFAK,UST,ANGGRLOS,AUFABDMM,AUFABDM,PAUSCHMI,PAUSCH,SKONTO,ZAFRIST,new FillerElement(3),SATZNR},
		     new boolean[]{true,true,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_35(new Object[]{ZA,LOSGRNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,LOSNR,SATZNR},
		     new boolean[]{true,true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true}),
		ZA_41(new Object[]{ZA,AUSNR,AUSBEZ,new FillerElement(13),SATZNR},
		     new boolean[]{true,true,true,false,true}),
		ZA_42(new Object[]{ZA,TA,TB,TEXTSYST,STLNR,new FillerElement(52),SATZNR},
		     new boolean[]{true,false,false,false,false,false,true}),
		ZA_45(new Object[]{ZA,OZ,LOSNR,LOSGRNR,FILOAEND,AENDSATZ,AUFLOHN,new FillerElement(40),SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,false,true}),
		ZA_46(new Object[]{ZA,LAENTEXT,new FillerElement(17),SATZNR},
		     new boolean[]{true,true,false,true}),
		ZA_47(new Object[]{ZA,FILOAEND,AENDSATZ,AUFLOHN,SEBETEIL,ERSTLOHN,new FillerElement(29),SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,true}),
		ZA_99(new Object[]{ZA,LVSUMME,AUFABFAK,UST,ANGEBOTS,AUFABDMM,AUFABDM,PAUSCHMI,PAUSCH,SKONTO,ZAFRIST,ANZTEIL,SATZNR},
		     new boolean[]{true,false,false,false,false,false,false,false,false,false,false,true,true});
		private final String type;
		private final Object[] blocks;
		private final Element.Type[] elementTypes;
		private final boolean[] obligatory;
		Type(Object[] blocks,boolean[] obligatory) {
			if(blocks.length!=obligatory.length)
				throw new FileException("Frame "+this+" has not the same size of blocks and obligatory specifications");
			int length = 0;
			List<Element.Type> elementTypes = new Vector<Element.Type>();
			for(Object block:blocks)
				if(block instanceof Element) {
					Element<?> element = (Element<?>)block;
					if(element.type.isFiller())
						   length+=((FillerElement)element).getLength();
					else length+=element.type.getLength();
				} else {
					Element.Type elementType = (Element.Type)block;
					elementTypes.add(elementType);
					length += elementType.getLength();
				}
			if(length!=80)
				throw new FrameException("Frame "+this+" has not the required length of 80 characters");
			this.type = toString().substring(3);
			this.blocks = blocks;
			this.elementTypes = elementTypes.toArray(new Element.Type[0]);
			this.obligatory = obligatory;
		}
		/**
		 * Returns the name of this <code>Frame.Type</code> (Example: A ZA_T0
		 * <code>Frame.Type</code> name is T0).
		 * 
		 * @return The name of this Frame.Type
		 */
		public String getType() { return type; }
		/**
		 * Returns an array of {@link Element.Type} and {@link Element} objects.
		 * <ul>
		 * 	<li>If an <code>Element.Type</code> is provided the {@link Frame} has to contain this <code>Element.Type</code> at the specified position.
		 * 	<li>If an <code>Element</code> is provided the {@link Frame} must contain this <code>Element</code> at the specified position.
		 * </ul>
		 * @return An Array of Element.Type and Element
		 */
		public Object[] getBlocks() { return blocks; }
		/**
		 * Returns an array of all {@link Element.Type} for this {@link Frame.Type}
		 * @return An array of Element.Type
		 */
		public Element.Type[] getElementTypes() { return elementTypes; }
		/**
		 * Checks wether the block (<code>Element.Type</code> or <code>Element</code>)
		 * at one index position is obligatory.
		 * 
		 * @param index The index value of the array returned by getBlocks()
		 * @return True if this block is obligatory for this Frame.Type
		 */
		public boolean isObligatory(int index) { return obligatory[index]; }
		/**
		 * Checks wether the block (<code>Element.Type</code> or <code>Element</code>)
		 * is obligatory for this <code>Frame.Type</code>.
		 * 
		 * @param block The type which should be checked
		 * @return True if this block is obligatory for this Frame.Type
		 */
		public boolean isObligatory(Object block) {
			for(int index=0;index<blocks.length;index++)
				if(blocks[index].equals(block))
					return isObligatory(index);
			return false;
		}
	}
	
	private Type type;
	private int satznr;
	
	private String frame;
	private Element<?>[] elements;
	
	@Override public Frame toFrame() { return this; }
	/**
	 * Creates a new <code>Frame</code> with a specific {@link Frame.Type} and
	 * {@link Element.Type#SATZNR}.
	 * 
	 * @param type The type of the Frame
	 * @param satznr The SATZNR (couting from 1)
	 * @param elements The elements of this Frame. Must only! contain Elements for all Element.Types in this Frame.Type.getBlocks without a SATZNR Element 
	 */
	public Frame(Type type,int satznr,final Element<?>... elements) {
		this.type = type;
		this.satznr = satznr;
		this.frame = concatenateFrame(type,satznr,elements).append(LINE_SEPARATOR).toString();
		this.elements = elements;
	}
	
	/**
	 * Parses one line of a GAEB1990 file into a {@link Frame} object (using
	 * {@link File.Strategy#PERMISSIVE})
	 * 
	 * @param frame The resulting frame 
	 */
	public Frame(String frame) { this(frame,File.Strategy.PERMISSIVE); }
	/**
	 * Parses one line of a GAEB1990 file into a {@link Frame} object (the
	 * {@link Frame.Type} of the <code>Frame<(code> is determined by the first
	 * two characters of the frame {@link String}). Throws an {@link FrameException}
	 * if the line could not be parsed successfully.
	 * 
	 * @param frame The resulting frame 
	 * @param strategy The strategy to use
	 */
	public Frame(String frame,File.Strategy strategy) {
		if(frame.trim().isEmpty())
			throw new FrameException("frame could not be created from empty data string.");
		if(frame.length()<2)
			throw new FrameException("frame could not be created. any data string must have at least the length of 2.");
		if(!strategy.equals(File.Strategy.STRICT)) {
			frame = frame.trim();
			if(frame.length()>80)
				frame = frame.substring(0,80);
		} else {
			if(frame.endsWith(LINE_SEPARATOR))
				frame = frame.substring(0,frame.length()-LINE_SEPARATOR.length());
			if(frame.length()!=80)
				throw new FrameException("frame could not be created. data string must be of length 80.");
		}
		
		String za = frame.substring(0,2).toUpperCase();
		try { type = Type.valueOf("ZA_"+za); }
		catch(IllegalArgumentException e) {
			throw new FrameException("frame could not be created. Frame type ZA "+za+" is unknown.");
		}
		
		int index,offset = 2;
		List<Element<?>> elements = new Vector<Element<?>>(type.blocks.length,1);
		//elements.add(Element.create(Element.Type.ZA,za));
		for(index=1;index<type.blocks.length;index++) {
			Object block = type.blocks[index];
			if(!(block instanceof Element)) {
				Element<?> element = null;
				Element.Type elementType = (Element.Type)block;
				try { element = Element.create(elementType,frame.substring(offset,offset+elementType.getLength())); }
				catch(Exception e) {
					if(!type.equals(Element.Type.SATZNR)&&type.isObligatory(index))
						throw new ElementException("frame ZA_"+za+" could not be created. Obligatory element "+elementType+" was missing or not well formated.",e);
					else if(!strategy.equals(File.Strategy.LAZY))
						throw new ElementException("frame ZA_"+za+" could not be created. Element "+elementType+" was missing or not well formated.",e);
					else element = Element.create(elementType,elementType.isNumeric()?0:new String().intern());
				} finally { offset += elementType.getLength(); }
				if(element.type.equals(Element.Type.SATZNR))
					satznr = ((NumberElement)element).getValue().intValue();
				elements.add(element);
			} else {
				Element<?> element = (Element<?>)block;
				if(!element.type.isFiller()) {
					elements.add(element);
					offset += element.type.getLength();
				} else offset += ((FillerElement)element).getLength();
			}
		}
		this.elements = elements.toArray(new Element[0]);
		this.frame = concatenateFrame(type,satznr,this.elements).append(LINE_SEPARATOR).toString();
	}
	
	/**
	 * Returns the type of the <code>Frame</code>
	 * @return The Frame.Type
	 */
	public Type getType() { return type; }
	/**
	 * Returns the SATZNR of this <code>Frame</code>
	 * @return The numeric value of the SATZNR
	 */
	public int getSatznr() { return satznr; }
	
	/**
	 * Returns all Elements of this Frame object
	 * @return An array of Element
	 */
	public Element<?>[] getElements() { return elements; }
	@Override public String toString() { return frame; }
	
	public Element<?> getElement(Element.Type type) {
		for(Element<?> element:elements)
			if(element.type.equals(type))
				return element;
		return null;
	}
	
	@Override public Iterator<Element<?>> iterator() { return new ArrayIterator<Element<?>>(elements); }
	
	private StringBuilder concatenateFrame(Type type,int satznr,final Element<?>... elements) {
		StringBuilder frame = new StringBuilder();
		ArrayIterator<Element<?>> iterator = new ArrayIterator<Element<?>>(elements);
		for(int index=0;index<type.blocks.length;index++) {
			Object block = type.blocks[index];
			if(!(block instanceof Element)) {
				Element<?> element = null;
				Element.Type elementType = (Element.Type)block;
				switch(elementType) {
				    case ZA: element = Element.create(ZA,type.type);	break;
				case SATZNR: element = Element.create(SATZNR,satznr); break;
				default:
					element = iterator.next();
					if(index<=0&&(element!=null&&ZA.equals(element.type)))
						element = iterator.next();
					if(element==null)
						if(type.isObligatory(index))
							throw new FrameException(type+" frame could not be created. Element "+elementType+" at position "+(index+1)+" is obligatory, but 'null' was given.");
						else element = new FillerElement(elementType.getLength());
					else if(!elementType.equals(element.type))
						throw new FrameException(type+" frame could not be created. Element at position "+(index+1)+" has the wrong element type. "+elementType+" expected, "+element.type+" given.");
					break;
				}
				frame.append(element.toString());
			} else frame.append(((Element<?>)block).toString());
		}
		if(frame.length()!=80)
			throw new FrameException(type+" frame could not be created. It has not the required length of 80 characters ("+frame+")");
		return frame;
	}
}