
class lackeyXmlFile {

	lackeyXmlFile(String inFile) {
		def dateStr = new Date().time.toString()
		processXml(inFile, "proc_${inFile}_${dateStr}.xml")
	}
	
	lackeyXmlFile(String inFile, String outFile) {
		processXml(inFile, outFile)
	}
	
	void processXml(String iFile, String oFile)
	{
		println "Reading $iFile Writing $oFile"
		def inFile = new File(iFile)
		def outFile = new File(oFile)
		def writer = new FileWriter(outFile)
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
		writer.write("<!DOCTYPE lackeyml [\n")
		def elStr = new String("<!ELEMENT lackeyml (application,")
		elStr += "(instruction|store|load|modify)*)>\n"
		writer.write(elStr)
		writer.write("<!ATTLIST lackeyml version CDATA #FIXED \"0.1\">")
		def attStr = new String("<!ATTLIST lackeyml xmlns CDATA #FIXED")
		attStr += " \"http://cartesianproduct.wordpress.com\">"
		writer.write(attStr)
		writer.write("<!ELEMENT application EMPTY>\n")
		writer.write("<!ATTLIST application command CDATA #REQUIRED>\n")
		writer.write("<!ELEMENT instruction EMPTY>\n")
		writer.write("<!ATTLIST instruction address CDATA #REQUIRED>\n")
		writer.write("<!ATTLIST instruction size CDATA #REQUIRED>\n")
		writer.write("<!ELEMENT modify EMPTY>\n")
		writer.write("<!ATTLIST modify address CDATA #REQUIRED>\n")
		writer.write("<!ATTLIST modify size CDATA #REQUIRED>\n")
		writer.write("<!ELEMENT store EMPTY>\n")
		writer.write("<!ATTLIST store address CDATA #REQUIRED>\n")
		writer.write("<!ATTLIST store size CDATA #REQUIRED>\n")
		writer.write("<!ELEMENT load EMPTY>\n")
		writer.write("<!ATTLIST load address CDATA #REQUIRED>\n")
		writer.write("<!ATTLIST load size CDATA #REQUIRED>\n")
		writer.write("]>\n")
		writer.write(
			"<lackeyml xmlns=\"http://cartesianproduct.wordpress.com\">\n")
		inFile.eachLine { line->
			if (line =~/Command:/) {
				writer.write("<application command=\"")
				def cmdStr = line =~ /(\w)+$/
				writer.write("${cmdStr[0][0]}\"/>\n")
			}
			if (!(line =~ /^==/)) {
				if (line =~ /^I/)
					writeInstruction(line, writer)
				else if (line =~ /^ S/)
					writeStore(line, writer)
				else if (line =~ /^ L/)
					writeLoad(line, writer)
				else if (line =~ /^ M/)
					writeModify(line, writer)
				else
					println("could not process $line")
			}
		}
		writer.write("</lackeyml>\n\n")
		writer.close()	
	}
	
	void writeAddressSize(String line, FileWriter writer)
	{
		def aStr = line =~/(\w*),(\w*)$/
		writer.write(" address=\"0x${aStr[0][1]}\" size=\"0x${aStr[0][2]}\"") 
		writer.write("/>\n")
	}
	
	void writeInstruction(String line, FileWriter writer)
	{
		writer.write("<instruction ")
		writeAddressSize(line, writer)
	}
	
	void writeStore(String line, FileWriter writer)
	{
		writer.write("<store ")
		writeAddressSize(line, writer)		
	}
	
	void writeLoad(String line, FileWriter writer)
	{
		writer.write("<load ")
		writeAddressSize(line, writer)
	}
	
	void writeModify(String line, FileWriter writer)
	{
		writer.write("<modify ")
		writeAddressSize(line, writer)
	}
	
}


def lackeyIn
if (args.size() == 0)
	println "Have to specify input file"
else if (args.size() == 1)
	lackeyIn = new lackeyXmlFile(args[0])
else
	lackeyIn = new lackeyXmlFile(args[0], args[1])
	