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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <code>Element</code> is the abstract super class of all elements
 * used in GAEB1990 files. Each element has a unique {@link Element.Type}.
 * The <code>Element.Type</code> sets the length and type information
 * for the <code>Element</code>
 * <p>
 * Each <code>Element.Type</code> sets the following parameters:
 * <ul>
 * 	<li>length: Output length in the GAEB1990 file
 *  <li>pattern: (only for character types) the content of this field must match this pattern
 *  <li>numeric: the value of this type must be numeric
 *  <li>decimal: (only for numeric types) the number of decimals stored
 * </ul> 
 * <p>  
 * If {@link Element#toString()} is called, the element must return
 * a valid string which can be appended to a GAEB1990 file.
 * 
 * @author Kristian Kraljic
 */
public abstract class Element<Typ> {
	public static final Element<?> VERSDTA = Element.create(Type.VERSDTA,"90");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	
	/**
	 * <code>Element.Type</code> provides an enumeration of all default data types 
	 * of a GAEB1990 file. Each <code>Element.Type</code> has a fixed definition 
	 * of length. For character <code>Element.Type</code> a fixed pattern is optional.
	 * For decimal <code>Element.Type</code> also the number of decimals is required.
	 * <p>
	 * The special case is the {@link Element.Type#FILLER} type, which is the
	 * only character type using a generic length. The value of the <code>FILLER</code>
	 * <code>Element.Type</code> is always the <code>char ' '</code> (hex 0x20).
	 *  
	 * @author Kristian Kraljic
	 */
	public enum Type {
		FILLER,AENDSATZ(4,true,3),AGBEZ(60),ANBEZ(60),ANGEBOTS(12,true,2),ANGGRLOS(12,true,2),ANGLOS(12,true,2),ANZEP(1,true),ANZTEIL(5,true),AUFABDM(12,true,2),AUFABDMM(1,Pattern.compile("[ -]")),AUFABFAK(7,true,6),AUFLOHN(12,true,2),AUSNR(4),AUSBEZ(55),BEZEITAN(14),BEZEPAN1(14),BEZEPAN2(14),BEZEPAN3(14),BEZEPAN4(14),BIETERAG(3),DATANG(8,Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{2}")),DATUHR(5,Pattern.compile("\\d{2}:\\d{2}")),DATUM(8,Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{2}")),DATZUS(8,Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{2}")),DP(2,Pattern.compile("8[1-6]")),DVNRAG(8),DVNRAN(8),EINHEIT(4),EP(10,true,2),EPANTEL1(10,true,2),EPANTEL2(10,true,2),EPANTEL3(10,true,2),EPANTEL4(10,true,2),EPAUFGL(1,Pattern.compile("[ X]")),EPMIN(1,Pattern.compile("[ -]")),EPZPF(1,true),ERSTLOHN(12,true,2),FILOAEND(3,true),FREIEMEN(11,true,3),FREMEMIN(1,Pattern.compile("[ -]")),GB(12,true,2),GBMIN(1,Pattern.compile("[ -]")),KENNWORT(14),KNR(2),KOSTENNR(9),KURZAN(55),KURZLANG(1,Pattern.compile("[ KL]")),KURZTEXT(70),KZBELO(1,Pattern.compile("[ X]")),KZFRMENG(1,Pattern.compile("[ X]")),KZPREIS(1,Pattern.compile("[ X]")),KZLOHN(1,Pattern.compile("[ X]")),LAENTEXT(55),LANGTEXT(55),LBKE(3),LOKALIT(20),LOKALNR(12),LOSBEZ(40),LOSGRNR(2),LOSGRSUM(12,true,2),LOSKZ(1,Pattern.compile("[ X]")),LOSNR(2),LOSUMME(12,true,2),LVBEZ(40),LVDATUM(8,Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{2}")),LVGRART(1,Pattern.compile("[ NGA]")),LVGRBEZ(40),LVSUMME(12,true,2),MENGE(11,true,3),MENGEMIN(1,Pattern.compile("[ -]")),NACHTRKZ(3),OZ(9),OZMASKE(9,Pattern.compile("[ 1234PI0]*P[ 1234PI0]*")),PAUSCH(12,true,2),PAUSCHMI(1,Pattern.compile("[ -]")),POSART1(1,Pattern.compile("[ NGAS]")),POSART2(1,Pattern.compile("[ NEM]")),POSSTAT(1,Pattern.compile("[ R]")),POSTYP(1,Pattern.compile("[ NL]")),PROBEZ(60),SATZNR(6,true),SEBETEIL(12,true,2),SKONTO(4,true,2),STLNR(17,true),SUMME(12,true,2),SUMMIN(1,Pattern.compile("[ -]")),TA(1,Pattern.compile("[ X]")),TB(1,Pattern.compile("[ X]")),TEILMENG(11,true,3),TEKZ(1,Pattern.compile("[ AB]")),TEXT(72),TEXTSYST(1,Pattern.compile("[ 12]")),UB(2),UBMENGE(11,true,3),UST(4,true,2),VEP(10,true,2),VEPMIN(1,Pattern.compile("[ -]")),VEPZPF(1,true),VERGAG(15),VERGAN(15),VERSDTA(2,Pattern.compile("\\d{2}")),VFREIEME(11,true,3),VFREMEMI(1,Pattern.compile("[ -]")),VGB(12,true,2),VGBMIN(1,Pattern.compile("[ -]")),VZUSCHP(5,true,2),VZUSCHPM(1,Pattern.compile("[ -]")),VZUSCHSM(1,Pattern.compile("[ -]")),VZUSCHSU(12,true,2),WAEKU(6),WAEBEZ(50),WIEVOR(1,Pattern.compile("[ BWA]")),ZA(2),ZAFRIST(2,true),ZEITANS(10,true,4),ZUSCHLAG(1,Pattern.compile("[ EZ]")),ZUSCHPR(5,true,2),ZUSCHPRM(1,Pattern.compile("[ -]")),ZUSCHSU(12,true,2),ZUSCHSUM(1,Pattern.compile("[ -]")),ZZ(4,true),ZZV(4,true);
		private int length,decimals;
		private boolean numeric;
		private Pattern pattern;
		public final String format;
		Type() { this(0); }
		Type(int length) { this(length,false); }
		Type(int length,Pattern pattern) {
			this(length,false);
			this.pattern = pattern;
		}
		Type(int length,boolean numeric) { this(length,numeric,0); }
		Type(int length,boolean numeric,int decimals) {
			this.length = length;
			if(this.numeric=numeric) {
				if((this.decimals=decimals)==0)
				     this.format = MessageFormat.format("%0{0,number,integer}d",length);
				else this.format = MessageFormat.format("%0{0,number,integer}d%-{1,number,integer}d",length-decimals,decimals);
			} else this.format = MessageFormat.format("%-{0,number,integer}s",length);
		}
		/**
		 * Returns the length the data type has to fill in a valid GAEB1990 file
		 * @return The length of this type, or 0 for the FILLER type
		 */
		public int getLength() { return length; }
		/**
		 * Check if this element type is a {@link Element.Type#FILLER} type
		 * @return True if this element type equals {@link Element.Type#FILLER}
		 */
		public boolean isFiller() { return this.equals(FILLER); }
		/**
		 * Check if this element is a numeric type
		 * @return True if the type is numeric (and/or decimal)
		 */
		public boolean isNumeric() { return numeric; }
		/**
		 * Checks if a numeric element type is a decimal
		 * @return True if the type is decimal
		 */
		public boolean isDecimal() { return numeric&&decimals!=0; }
		/**
		 * Returns the number of decimals to be stored
		 * @return The number of decimal places
		 */
		public int getDecimals() { return decimals; }
		/**
		 * Checks if this character type has a pattern to match to be valid
		 * @return True if the elemen type has a pattern
		 */
		public boolean hasPattern() { return pattern!=null; }
		/**
		 * Returns the pattern to match to validate a value for this Element.Type 
		 * @return A regular expression pattern object to match
		 */
		public Pattern getPattern() { return pattern; }
	}
	
	public final Type type;
	
	/**
	 * Creates a new Element of a specific {@link Element.Type}. The type
	 * of the element is determined by the given <code>Element.Type</code>
	 * and the class of the value parameter.
	 * <p>
	 * <ol>
	 * 	<li>If the <code>type</code> is {@link Element.Type#FILLER}, a {@link FillerElement} of length <code>value</code> is created.
	 * 	<li>If the <code>value</code> is a boolean and <code>type</code> is a numeric <code>Element.Type</code> the <code>Element</code> value is <code>0</code> or <code>1</code>.
	 * 	<li>If the <code>value</code> is a boolean and <code>type</code> is  a character like <code>Element.Type</code> the <code>Element</code> value is <code>' '</code> or <code>'X'</code>.
	 * 	<li>If the <code>value</code> is a {@link Calendar} or {@link Date} a character type <code>Element</code> is created.
	 * 	<li>If the <code>type</code> is numeric, depending on the <code>value</code> class either a {@link DecimalElement} or {@link NumberElement} is created.
	 * 	<li>In all other cases a character type {@link TextElement} is created
	 * </ol>
	 * <p>
	 * This method either returns a valid {@link Element} or throws a {@link ElementException}.
	 * 
	 * @param type The type of this Element
	 * @param value The value of the Element (either Boolean, Date, Calendar, Numeric or String)
	 * @return A Element<?> of the specified type with the value
	 */
	public static Element<?> create(Type type,Object value) {
		if(type.isFiller())
			if(value instanceof Number)
				   return new FillerElement(((Number)value).byteValue());
			else throw new ElementException("Can only create filler element with a numeric length.");
		if(value instanceof Boolean) 
			if(type.isNumeric())
				   return Element.create(type,(Boolean)value?1:0);
			else return Element.create(type,(Boolean)value?"X":" ");
		if(value instanceof Calendar)
			return Element.create(type,((Calendar)value).getTime());
		if(value instanceof Date)
			return Element.create(type,DATE_FORMAT.format((Date)value));
		if(type.isNumeric()) {
			if(value instanceof Number) {
				Number number = (Number)value;
				if(type.isDecimal()) {
					if(value instanceof Float)
						   return new DecimalElement(type,number.floatValue());
					else return new DecimalElement(type,number.doubleValue());
				} else return new NumberElement(type,number.longValue());
			} else try {
				String parse = value.toString().trim();
				if(parse.isEmpty())
					if(type.isDecimal())
						   return new DecimalElement(type,0d);
					else return new NumberElement(type,0);
				if(type.isDecimal()) {
					if(parse.indexOf(',')!=-1)
						parse = parse.replace(',','.');
					if(parse.indexOf('.')!=-1||parse.length()!=type.length)
					     return new DecimalElement(type,Double.parseDouble(parse));
					else return new DecimalElement(type,Double.parseDouble(parse.substring(0,type.length-type.decimals)+'.'+parse.substring(type.length-type.decimals)));
				} else return new NumberElement(type,Long.parseLong(parse));
			} catch(NumberFormatException e) { throw new ElementException(e); }
		} else return new TextElement(type,value.toString());
	}
	
	/**
	 * Creates an empty <code>Element</code> of a speicifc type
	 * @param type The type the elmement should have
	 */
	public Element(Type type) { this.type = type; }
	/**
	 * Returns the value of this <code>Element</code>
	 * @return An Object representing the value of this <code>Element</code>
	 */
	public abstract Typ getValue();
	/**
	 * Checks if the value of this object is initial for the respective {@link Element.Type}.
	 * <p>
	 * <ul> 
	 * 	<li>{@link TextElement} is empty if the length is 0 or it contains only <code>' '</code>.
	 * 	<li>{@link NumberElement} or {@link DecimalElement} is empty if it is equal 0 (or 0.0).
	 * 	<li>{@link FillerElement} is never empty.
	 * </ul>
	 * 
	 * @return True if the value of this Element is still initial
	 */
	public abstract boolean isEmpty();
	/**
	 * Converts the Element into a valid string representation which can be written
	 * to a GAEB1990 file. 
	 */
	@Override public abstract String toString();
	
	protected String format(String format,Object... args) {
		String formatted = String.format(format,args);
		if(type.isDecimal())
			formatted = formatted.replace('\u0020','0');
		if(!type.isFiller()&&type.getLength()<formatted.length())
			if(type.isNumeric())
				   return formatted.substring(formatted.length()-type.getLength());
			else return formatted.substring(0,type.getLength());
		else return formatted;
	}
}
