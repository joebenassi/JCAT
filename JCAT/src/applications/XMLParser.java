package applications;

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

import fsw.CmdPkt;
import fsw.CmdStrParam;
import gui.menu.prompts.ParameterDetails;

/**
 * IN DEVELOPMENT. WILL BE CHANGED.
 * Used to parse XML files for configuration details of Apps and return those
 * Apps.
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
	public static final App[] getPackets(File[] files)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		List<App> packets = new ArrayList<App>();

		for (int i = 0; i < files.length; i++) {
			Document temp = documentBuilder.parse(files[i]);
			temp.getDocumentElement().normalize();
			try{packets.add(getPacket(temp, i + 1, i));} catch(Throwable e){};
		}
		return packets.toArray(new App[packets.size()]);
	}

	/**
	 * Parses the input document for configuration details of an App, and
	 * returns the App.
	 * 
	 * @param document
	 *            The document to parse for configuration details of.
	 * @param hotkey
	 *            The number shown with "Ctrl" in the Menu as a hotkey for the
	 *            App.
	 * @param appID
	 *            The unique ID given to the app.
	 * @return The App configured by the input parameters.
	 */
	private static final App getPacket(Document document, int hotkey, int appID) {
		boolean prefixError = false;
		boolean telemetryError = false;
		boolean nameError = false;
		boolean commandError = false;
		
		String prefix = null;
		Telemetry[] telemetry = null;
		String name = null;
		ArrayList<CmdPkt> commands = null;
		
		try{prefix = getPrefix(document);}
		catch(NullPointerException e){prefixError = true;}

		try{telemetry = getTelemetry(document);}
		catch(NullPointerException e){telemetryError = true;}
		
		try{name = getName(document);}
		catch(NullPointerException e){nameError = true;}

		try{commands = getCommands(document);}
		catch(Throwable e){commandError = true;}

		if (prefixError || telemetryError || nameError || commandError)
		{
			ArrayList<String> errors = new ArrayList<String>();
			
			if (nameError) errors.add("Name initialization");
			if (prefixError) errors.add("Prefix initialization");
			if (telemetryError) errors.add("Telemetry configuration");
			if (commandError) errors.add("Command configuration");
			
			System.out.println("ERROR IN XML:");
			for (int i = 0; i < errors.size() - 1; i++)
			{
				System.out.print(" " + errors.get(i) + ",");
			}
			System.out.print(" " + errors.get(errors.size() - 1) + ".");
			
		}
		return new App(name, prefix, commands, telemetry, appID);
	}

	/**
	 * Returns the title of the App as displayed in its popup telemetry window.
	 * 
	 * @param document
	 *            The document to parse within to find the name.
	 * @return The title of the App as displayed in its telemetry window.
	 */
	private static final String getName(Document document) {
		return document.getElementsByTagName("name").item(0).getTextContent();
	}

	/**
	 * Returns the prefix of the App.
	 * 
	 * @param document
	 *            The document to parse within to find the prefix.
	 * @return The prefix of the App.
	 */
	private static final String getPrefix(Document document) {
		return document.getElementsByTagName("prefix").item(0).getTextContent();
	}

	/**
	 * Returns the array of Commands as defined in the input document
	 * 
	 * @param document
	 *            The document to parse for App configuration details
	 * @return the array of Commands as defined in the input document
	 */

	private static final ArrayList<CmdPkt> getCommands(Document document) {

		final String prefix = getPrefix(document);
		final Element firstTree = (Element) document.getElementsByTagName(
				"commands").item(0);
		final NodeList nodeList = firstTree.getElementsByTagName("command");

		ArrayList<CmdPkt> commands = new ArrayList<CmdPkt>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			commands.add(getCommand((Element) nodeList.item(i), prefix));
		}
		return commands;
	}

	/**
	 * HARDCODED
	 * 
	 * @param commandElement
	 *            The Element that represents the particular command within the
	 *            XML document.
	 * @param prefix
	 *            The prefix for the App.
	 * @return
	 */
	private static final CmdPkt getCommand(Element commandBase, String prefix) {
		final String appPrefix = prefix;
		final String name = getFirstInstance("name", commandBase);

		final int messageID = 101; // HARDCODED
		final int funcCode = 101; // HARDCODED
		final int dataLength = 5; // HARDCODED
		CmdPkt command = new CmdPkt(appPrefix, name, messageID, funcCode,
				dataLength);

		for (CmdStrParam c : getParameters(commandBase))
			command.addParam(c);

		command.loadParamList();
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
	private static final ArrayList<CmdStrParam> getParameters(Element commandBase) {
		if (commandBase.getElementsByTagName("parameters").item(0) == null)
			return new ArrayList<CmdStrParam>();

		String defValue = "/cf/cfe_es_errlog.log";
		int numBytes = 64;

		ArrayList<Element> elements = getParameterElements(commandBase);
		ArrayList<CmdStrParam> parameters = new ArrayList<CmdStrParam>();

		for (int i = 0; i < elements.size(); i++) {

			final String name = getFirstInstance("name", elements.get(i));

			final ParameterDetails parameterDetails = getParameterDetails(elements
					.get(i));

			parameters.add(new CmdStrParam(name, parameterDetails, defValue,
					numBytes));
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