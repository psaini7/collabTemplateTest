
' Constants
Const WdDoNotSaveChanges = 0

' see WdSaveFormat enumeration constants: 
' http://msdn2.microsoft.com/en-us/library/bb238158.aspx
Const wdFormatPDF = 17  ' PDF format. 
Const wdFormatXPS = 18  ' XPS format. 


' Global variables
Dim arguments
Set arguments = WScript.Arguments



' ***********************************************
' DOC2PDF
'
' Converts a Word document to PDF using Word 2007.
'
' Input:
' sDocFile - Full path to Word document.
' sPDFFile - Optional full path to output file.
'
' If not specified the output PDF file
' will be the same as the sDocFile except
' file extension will be .pdf.
'
Function DOC2PDF( sDocFile, sPDFFile )

  Dim fso ' As FileSystemObject
  Dim wdo ' As Word.Application
  Dim wdoc ' As Word.Document
  Dim wdocs ' As Word.Documents
  Dim sPrevPrinter ' As String

  Set fso = CreateObject("Scripting.FileSystemObject")
  Set wdo = CreateObject("Word.Application")
  Set wdocs = wdo.Documents

  sDocFile = fso.GetAbsolutePathName(sDocFile)

  ' Debug outputs...
  If bShowDebug Then
    WScript.Echo "Doc file = '" + sDocFile + "'"
    WScript.Echo "PDF file = '" + sPDFFile + "'"
  End If

  sFolder = fso.GetParentFolderName(sDocFile)

  If Len(sPDFFile)=0 Then
    sPDFFile = fso.GetBaseName(sDocFile) + ".pdf"
  End If

  If Len(fso.GetParentFolderName(sPDFFile))=0 Then
    sPDFFile = sFolder + "\" + sPDFFile
  End If

  ' Enable this line if you want to disable autoexecute macros
  ' wdo.WordBasic.DisableAutoMacros

  ' Open the Word document
  Set wdoc = wdocs.Open(sDocFile)

  ' Let Word document save as PDF
  ' - for documentation of SaveAs() method,
  '   see http://msdn2.microsoft.com/en-us/library/bb221597.aspx 
  wdoc.SaveAs sPDFFile, wdFormatPDF

  wdoc.Close WdDoNotSaveChanges
  wdo.Quit WdDoNotSaveChanges
  Set wdo = Nothing

  Set fso = Nothing

End Function

' *** MAIN **************************************


Call DOC2PDF( arguments.Unnamed.Item(0), arguments.Named.Item("o") )

Set arguments = Nothing