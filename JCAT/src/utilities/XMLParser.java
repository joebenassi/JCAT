package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import packets.cmd.CmdParam;
import packets.cmd.CmdPkt;
import packets.cmd.ParameterDetails;
import packets.tlm.Telemetry;

import applications.App;


/**
 * IN DEVELOPMENT. WILL BE CHANGED. Used to parse XML files for configuration
 * details of Apps and return those Apps.
 * 
 * @author Joe Benassi
 * 
 */
public final class XMLParser {
	/**
	 * 
	 * Parses input files for configuration details of Apps and returns those
	 * Apps in array form.
	 * 
	 * @param files
	 *            The array of files to parse for configuration details of Apps.
	 * @return The array of Apps
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static final App[] getApps(File[] files)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		List<App> packets = new ArrayList<App>();

		for (int i = 0; i < files.length; i++) {
			Document document = documentBuilder.parse(files[i]);
			document.getDocumentElement().normalize();

				try {
					final String name = getInstance("name", document);
					final String prefix = getInstance("prefix", document);
				
					packets.add(new App(name, prefix, getCommands(document),
						getTelemetry(document), getID(document)));
			} catch (NullPointerException e) {
			}
		}
		return packets.toArray(new App[packets.size()]);
	}

	private static final int getID(Document document) {
		final String IDString = getInstance("id", document);

		final String IDString2 = IDString.substring(2, IDString.length());
		return Integer.parseInt(IDString2, 16);
	}

	private static final int getCommandOffset(Document document) throws Exception
	{
		final String COString = getInstance("commandoffset", document);
		String modString;
		int value = 0;

		switch(COString.substring(0,2))
		{
			case "-": {
				modString = COString.substring(1, COString.length());
				value = Integer.parseInt(modString);
				value *= -1;
			}
			case "+": {
				modString = COString.substring(1, COString.length());
				value = Integer.parseInt(modString);
				value *= 1;
			}
			
			default: {
				modString = COString;
				value = Integer.parseInt(modString);
				value *= 1;
			}
		}
		
		return value;
	}

	private static final String getInstance(String tag, Document document) {
		return document.getElementsByTagName(tag).item(0).getTextContent();
	}

	/**
	 * Returns the array of Commands as defined in the input document
	 * 
	 * @param document
	 *            The document to parse for App configuration details
	 * @return the array of Commands as defined in the input document
	 */

	private static final ArrayList<CmdPkt> getCommands(Document document) {

		final int appID = getID(document);

		int commandOffset = 0;
		try {commandOffset = getCommandOffset(document);} 
		catch (Exception e) {}

		final String prefix = getInstance("prefix", document);

		final Element firstTree = (Element) document.getElementsByTagName(
				"commands").item(0);
		final NodeList nodeList = firstTree.getElementsByTagName("command");

		ArrayList<CmdPkt> commands = new ArrayList<CmdPkt>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			final int functCode = i + commandOffset;
			commands.add(getCommand((Element) nodeList.item(i), prefix,
					functCode, appID));
		}
		return commands;
	}

	/**
	 * @param commandElement
	 *            The Element that represents the particular command within the
	 *            XML document.
	 * @param prefix
	 *            The prefix for the App.
	 * @return
	 */
	private static final CmdPkt getCommand(Element commandBase, String prefix,
			int funcCode, int appID) {
		final String appPrefix = prefix;
		final String name = getFirstInstance("name", commandBase);

		final int messageID = appID; // /* SET MSG ID = APP ID (for now) */
		// final int funcCode = fcode;///* 0, 1th Cmd */

		int dataLength = 0;
		/*
		 * param 1 = 20. param 2 = 15.
		 * 
		 * dataLength = 35
		 */

		ArrayList<CmdParam> parameters = getParameters(commandBase);

		for (CmdParam c : parameters)
			dataLength += c.getNumBytes();

		System.out.println("ZERO = " + dataLength);
		CmdPkt command = new CmdPkt(appPrefix, name, messageID, funcCode,
				dataLength);

		System.out.println(command.getAppPrefix() + ": FUNCTCODE: " + funcCode
				+ ": NAME == " + command.getName());
		for (CmdParam c : parameters)
			command.addParam(c);

		// command.loadParamList(); <- this was causing the error @ 7/10/2013,
		// 3:30pm
		return command;
	}

	private static final String getFirstInstance(String type, Element element) {
		return element.getElementsByTagName(type).item(0).getTextContent();
	}

	private static final ArrayList<Element> getParameterElements(
			Element commandBase) {
		ArrayList<Element> elements = new ArrayList<Element>();
		NodeList nodeList = (NodeList) commandBase
				.getElementsByTagName("parameters").item(0).getChildNodes();

		int nodeListLength = nodeList.getLength();

		for (int i = 0; i < nodeListLength; i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE
					&& nodeList.item(i).hasChildNodes())
				elements.add((Element) nodeList.item(i));
		}

		return elements;
	}

	/**
	 * HARDCODED
	 * 
	 * @param commandBase
	 * @return
	 */
	private static final ArrayList<CmdParam> getParameters(Element commandBase) {
		if (commandBase.getElementsByTagName("parameters").item(0) == null)
			return new ArrayList<CmdParam>();

		/** @todo HARDCODE number of numBytes for each parameter **/
		/** @todo HARDCODE string vs int **/
		/** @todo HARDCODE defValue? **/

		ArrayList<Element> elements = getParameterElements(commandBase);
		ArrayList<CmdParam> parameters = new ArrayList<CmdParam>();

		for (int i = 0; i < elements.size(); i++) {
			final String name = getFirstInstance("name", elements.get(i));
			final String type = getFirstInstance("type", elements.get(i));
			final String primitive = getFirstInstance("primitive",
					elements.get(i));
			final String bytes = getFirstInstance("bytes", elements.get(i));
			final ParameterDetails parameterDetails = getParameterDetails(elements
					.get(i));
			final String defValue = "/cf/cfe_es_errlog.log";

			parameters.add(CmdParamGen.getCmdParam(name, type, primitive,
					bytes, parameterDetails, defValue));
		}

		return parameters;
	}

	private static final ParameterDetails getParameterDetails(Element element) {
		if (element.getNodeName().equals("inputparameter"))
			return new ParameterDetails(true);

		if (element.getNodeName().equals("choiceparameter")) {
			NodeList choicesList = element.getElementsByTagName("choice");

			String[] choices = new String[choicesList.getLength()];

			for (int j = 0; j < choicesList.getLength(); j++)
				choices[j] = choicesList.item(j).getTextContent();

			return new ParameterDetails(false, choices);
		}

		return new ParameterDetails(false,
				new String[] { "ERROR WTIH PARAMETERDETAILS" });
	}

	/**
	 * Returns the telemetry data array as defined in the input Document.
	 * 
	 * @param document
	 *            The Document to parse for configuration details.
	 * @return The telemetry data array as defined in the input Document.
	 */
	private static final Telemetry[] getTelemetry(Document document) {

		String[] telemetryNames = getParse(document, "telemetrynames", "name");
		Telemetry[] telemetry = new Telemetry[telemetryNames.length];
		for (int i = 0; i < telemetryNames.length; i++) {
			telemetry[i] = new Telemetry(telemetryNames[i]);
		}

		return telemetry;
	}

	/**
	 * Helper method; used to parse for second-order XML data. For example, if
	 * My App contained a single "telemetrynames" entry, and that
	 * "telemetrynames" entry contained an array of "name"'s, you would call
	 * getParse(document, "telemetrynames", "name") to return that array of
	 * "name"'s;
	 * 
	 * @param document
	 *            The document to parse for configuration details.
	 * @param first
	 *            The name of the first entry to search.
	 * @param second
	 *            The name of the data intended to be returned.
	 * @return the array of second-order XML data.
	 */
	private static final String[] getParse(Document document, String first,
			String second) {
		final Element firstTree = (Element) document
				.getElementsByTagName(first).item(0);

		final NodeList nodeList = firstTree.getElementsByTagName(second);

		final String[] temp = new String[nodeList.getLength()];

		for (int i = 0; i < nodeList.getLength(); i++) {
			temp[i] = nodeList.item(i).getTextContent();
		}

		return temp;
	}
}