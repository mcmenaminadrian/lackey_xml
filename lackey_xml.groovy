
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
	}
	
}


def lackeyIn
if (args.size() == 0)
	println "Have to specify input file"
else if (args.size() == 1)
	lackeyIn = new lackeyXmlFile(args[0])
else
	lackeyIn = new lackeyXmlFile(args[0], args[1])
	