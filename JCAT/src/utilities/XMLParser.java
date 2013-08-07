package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import network.Networker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import packets.cmd.CmdPkt;
import packets.parameters.ChoiceOption;
import packets.parameters.CmdParam;
import packets.parameters.ScalarConstant;
import packets.tlm.TlmPkt;
import resources.ResourceLoader;

import applications.App;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
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

	public static final void addConstants(File f) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			final DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document doc = documentBuilder.parse(f);
			doc.getDocumentElement().normalize();

			final NodeList nodeList = doc.getElementsByTagName("const");
			for (int i = 0; i < nodeList.getLength(); i++) {
				try {
					Element e = (Element) nodeList.item(i);
					String name = e.getElementsByTagName("name").item(0)
							.getTextContent();
					String value = e.getElementsByTagName("value").item(0)
							.getTextContent();

					ScalarConstant.addConstant(name, value);
				} catch (Throwable e1) {
				}
			}

			final NodeList nodes = doc.getElementsByTagName("config");
			for (int i = 0; i < nodes.getLength(); i++) {
				try {
					Element e = (Element) nodes.item(i);
					String name = e.getElementsByTagName("name").item(0)
							.getTextContent();
					String msgid = e.getElementsByTagName("msgid").item(0)
							.getTextContent();

					Networker.addConfig(name, msgid);
				} catch (Throwable e1) {
				}
			}
		} catch (Throwable e) {
		}
	}

	public static final App getApp(Document document, int CmdID, int TlmID,
			String config) {
		final String name = getInstance("name", document);

		return new App(name, config, getCommands(document, CmdID),
				getTelemetry(document), TlmID, CmdID);
	}

	public static ArrayList<String> getConfigNames(Document document) {
		final Element firstTree = (Element) document.getElementsByTagName(
				"configs").item(0);
		final NodeList nodeList = firstTree.getElementsByTagName("config");

		ArrayList<String> configNames = new ArrayList<String>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element e = (Element) nodeList.item(i);
			configNames.add(e.getElementsByTagName("name").item(0)
					.getTextContent());
		}

		return configNames;
	}

	private static final int getCommandOffset(Document document) {
		final String COString = getInstance("commandoffset", document);
		String modString;
		int value = 0;

		/* TODO fix for Linux */
		String s = COString.substring(0, 2);
		if (s.equals("-")) {
			modString = COString.substring(1, COString.length());
			value = Integer.parseInt(modString);
			value *= -1;
		} else if (s.equals("+")) {
			modString = COString.substring(1, COString.length());
			value = Integer.parseInt(modString);
			value *= 1;
		} else {
			modString = COString;
			try {
				value = Integer.parseInt(modString);
				value *= 1;
			} catch (Throwable e) {
			}
		}

		return value;
	}

	public static final int[] getIDs(Document document, int configNum) {
		final Element firstTree = (Element) document.getElementsByTagName(
				"configs").item(0);
		final NodeList nodeList = firstTree.getElementsByTagName("config");

		int[] IDs = new int[2];
		Element e = (Element) nodeList.item(configNum);
		String cmdmid = e.getElementsByTagName("cmdmid").item(0)
				.getTextContent();
		String tlmmid = e.getElementsByTagName("tlmmid").item(0)
				.getTextContent();

		IDs[0] = Integer.parseInt(cmdmid.substring(2), 16);
		IDs[1] = Integer.parseInt(tlmmid.substring(2), 16);

		return IDs;
	}

	private static final String getInstance(String tag, Document document) {
		return document.getElementsByTagName(tag).item(0).getTextContent();
	}

	/**
	 * Returns the array of Commands as defined in the input document
	 * 
	 * @param document
	 *            The document to parse for App configuration details
	 * @param cmdMID
	 * @return the array of Commands as defined in the input document
	 */

	private static final ArrayList<CmdPkt> getCommands(Document document,
			int CmdMID) {
		int commandOffset = 0;

		commandOffset = getCommandOffset(document);

		final Element firstTree = (Element) document.getElementsByTagName(
				"commands").item(0);
		final NodeList nodeList = firstTree.getElementsByTagName("command");

		ArrayList<CmdPkt> commands = new ArrayList<CmdPkt>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			final int functCode = i + commandOffset;
			commands.add(getCommand((Element) nodeList.item(i), functCode,
					CmdMID));
		}
		return commands;
	}

	/**
	 * @param commandElement
	 *            The Element that represents the particular command within the
	 *            XML document.
	 * @return
	 */
	private static final CmdPkt getCommand(Element commandBase, int funcCode,
			int appID) {
		final String name = getFirstInstance("name", commandBase);

		final int messageID = appID;
		int dataLength = 0;

		ArrayList<CmdParam> parameters = getParameters(commandBase);

		for (CmdParam c : parameters)
			dataLength += c.getNumBytes();

		CmdPkt command = new CmdPkt(name, messageID, funcCode, dataLength);

		for (CmdParam c : parameters)
			command.addParam(c);

		return command;
	}

	private static final String getFirstInstance(String type, Element element) {
		try {
			return element.getElementsByTagName(type).item(0).getTextContent();
		} catch (Throwable ex) {
			return "DNE";
		}
	}

	private static final ArrayList<Element> getParameterElements(
			Element commandBase) {
		ArrayList<Element> elements = new ArrayList<Element>();

		NodeList nodeList = commandBase.getElementsByTagName("parameters")
				.item(0).getChildNodes();

		int nodeListLength = nodeList.getLength();

		for (int i = 0; i < nodeListLength; i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE
					&& nodeList.item(i).hasChildNodes())
				elements.add((Element) nodeList.item(i));
		}

		return elements;
	}

	private static final ArrayList<CmdParam> getParameters(Element commandBase) {
		if (commandBase.getElementsByTagName("parameters").item(0) == null)
			return new ArrayList<CmdParam>();
		ArrayList<Element> elements = getParameterElements(commandBase);
		ArrayList<CmdParam> parameters = new ArrayList<CmdParam>();

		for (int i = 0; i < elements.size(); i++) {
			final String name = getFirstInstance("name", elements.get(i));
			final String type = getFirstInstance("type", elements.get(i));

			final String constant = getFirstInstance("const", elements.get(i));
			/*
			 * if (!constant.equals("DNE")) try { if
			 * (!ScalarConstant.hasConstant(constant))
			 * 
			 * } catch (ParserConfigurationException | SAXException |
			 * IOException e1) { e1.printStackTrace(); }
			 */

			final String bytes = ScalarConstant.getValue(constant);

			final String primitive = getFirstInstance("primitive", // may have
					elements.get(i));

			boolean isInputParam = true;

			String param = elements.get(i).getNodeName();
			if (param.equalsIgnoreCase("spare")) {
				parameters.add(ParamGen.getSpareParam(type));
			}

			else if (elements.get(i).getNodeName()
					.equalsIgnoreCase("choiceparameter")) {
				isInputParam = false;

				NodeList choicesList = elements.get(i).getElementsByTagName(
						"choice");

				ArrayList<ChoiceOption> choiceOptions = new ArrayList<ChoiceOption>();

				for (int j = 0; j < choicesList.getLength(); j++) {
					try {
						final String choiceName = ((Element) choicesList
								.item(j)).getElementsByTagName("name").item(0)
								.getTextContent();
						final String choiceValue = ((Element) choicesList
								.item(j)).getElementsByTagName("value").item(0)
								.getTextContent();
						choiceOptions.add(new ChoiceOption(choiceName,
								choiceValue));
					} catch (Throwable e) {
					}
				}

				ChoiceOption[] choiceArray = new ChoiceOption[choiceOptions
						.size()];
				choiceOptions.toArray(choiceArray);
				parameters.add(ParamGen.getCmdParam(name, type, primitive,
						bytes, isInputParam, choiceArray));
			} else
				parameters.add(ParamGen.getCmdParam(name, type, primitive,
						bytes, isInputParam, new ChoiceOption[0]));
		}
		return parameters;
	}

	/**
	 * Returns the telemetry data array as defined in the input Document.
	 * 
	 * @param document
	 *            The Document to parse for configuration details.
	 * @return The telemetry data array as defined in the input Document.
	 */
	private static final TlmPkt[] getTelemetry(Document document) {
		/* TODO will all TlmPkts be encoded as strings?? */
		final Element firstTree = (Element) document.getElementsByTagName(
				"telemetry").item(0);
		if (firstTree.getChildNodes().getLength() > 1) {
			final NodeList nodeList = firstTree
					.getElementsByTagName("parameter");

			ArrayList<TlmPkt> telemetry = new ArrayList<TlmPkt>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				String name = getFirstInstance("name",
						((Element) (nodeList.item(i))));
				String dataType = getFirstInstance("type",
						((Element) (nodeList.item(i))));
				String primitive = getFirstInstance("primitive",
						((Element) nodeList.item(i)));
				String constant = getFirstInstance("const",
						((Element) nodeList.item(i)));
				String val = ScalarConstant.getValue(constant);

				telemetry.add(new TlmPkt(name, dataType, primitive, val));
			}
			return telemetry.toArray(new TlmPkt[telemetry.size()]);
		}
		return new TlmPkt[] { new TlmPkt("null", "uint8", "string", "1") };
	}

	public static String getName(Document document) {
		return document.getElementsByTagName("name").item(0).getTextContent();
	}
}