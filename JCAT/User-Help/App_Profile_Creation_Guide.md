#App Profile Creation Guide
###Summary of Document 
This document is to aid creation of App Profiles for JCAT. You should use other App Profiles for reference.
<br>

###Requirements/Assumptions for the XML: 
######Everything for the App is defined, and everything is in correct order, including:
1. All <strong>&lt;commands&gt;&lt;command&gt;s</strong> for the App
2. All <strong>&lt;telemetry&gt;&lt;parameter&gt;s</strong> for the App
3. All <strong>&lt;inputparameter&gt;s</strong> and <strong>&lt;choiceparameter&gt;s</strong> in <strong>&lt;commands&gt;&lt;command&gt;&lt;parameters&gt;</strong> for the App

######The App’s commands must abide by the following requirement:
1. The command code for each command must be equal to <strong>&lt;commandoffset&gt;</strong> + X, where X is the index of the applicable <strong>&lt;command&gt;</strong> within <strong>&lt;commands&gt;</strong>


###Basic Format Example (not a current App profile):
	<?xml version="1.0" encoding="UTF-8"?>
	<channel>
		<name>[APPNAME]</name>
		<configs>
			<config>
				<name>[CPU 1]</name>
				<cmdmid>[0x1804]</cmdmid>
				<tlmmid>[0x0804]</tlmmid>
			</config>
		</configs>
		<commandoffset>[+0]</commandoffset&gt;
		<commands></commands>
		<telemetry></telemetry>
	</channel>

<strong>Notes: </strong>The above example meets the minimum requirements to define an App Profile. The parts contained in brackets are specific to the App.

#####New Terms:
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as its name in the GUI</li>
	<li><strong>&lt;configs&gt;:</strong> Contains the different configurations of the App.</li>
	<li><strong>&lt;config&gt;:</strong> Contains a <name>, <cmdmid>, and <tlmmid>. You must define at least one <config> to load an App, as a config is required to send commands or receive telemetry. You can interact with more than one instance of an App by selecting a <config> for each instance at runtime.</li>
	<li><strong>&lt;commandoffset&gt;:</strong> The lowest functional command code for the App. Usually 0.</li>
	<li><strong>&lt;commands&gt;:</strong> Contains the various <command>s for an App.</li>
	<li><strong>&lt;telemetry&gt;:</strong> Contains the various <parameter>s for an App Housekeeping Packet.</li>
</ul>
<br>
###Moderate-Depth Format Example (the above example, with new parts <strong>bolded</strong>):
	<?xml version="1.0" encoding="UTF-8"?>
	<channel>
		<name>[APPNAME]</name>
		<configs>
			<config>
				<name>[CPU 1]</name>
				<cmdmid>[0x1804]</cmdmid>
				<tlmmid>[0x0804]</tlmmid>
			</config>
		</configs>
		<commandoffset>[+0]</commandoffset&gt;
		<commands>
			<command>
				<name>CFE_TBL_VALIDATE_CC</name>
				<parameters> 					
					<choiceparameter></choiceparameter>
					<inputparameter></inputparameter>
					<spare></spare>
				</parameters>
			</command>
		</commands>
		<telemetry>
			<parameter>
				<name>LastFileDumped</name>
				<type>char</type>
				<primitive>string</primitive>
				<const>OS_MAX_PATH_LEN</const>
			</parameter>
			<parameter>
				<name>ByteAlignPad2</name>
				<type>uint16</type>
			</parameter>	
		</telemetry>
	</channel>

<strong>Notes: </strong>The above example is <strong>not</strong> a correctly-formatted App Profile. This is because, and only because, the contents within <strong>&lt;choiceparameter&gt;, &lt;inputparameter&gt;, and &lt;spare&gt; are not defined;
	
#####New Terms:

<strong>Within &lt;commands&gt;&lt;command&gt;: Defines a command that can be used on the App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The name of the command.</li>
	<li><strong>&lt;parameters&gt;:</strong> The parameters for the command. This can be absent if there are no parameters for the command.</li>
	<ul>
		<li><strong>&lt;choiceparameter&gt;:</strong> A parameter where the user chooses from predetermined values in drop-down form.</li>
		<li><strong>&lt;inputparameter&gt;:</strong> A parameter where the user manually types in the value.</li>
		<li><strong>&lt;spare&gt;:</strong> A parameter that is invisible to the user, which contributes only byte values of zero to the packet.</li>
	</ul>
</ul>

<strong>Within &lt;telemetry&gt;&lt;parameter&gt;: Defines an aspect of telemetry about the App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The name of the parameter to display.
	<li><strong>&lt;type&gt;:</strong> The data type of the parameter. The only valid values are:
	<ul>
		<li>int8, int16, int32</li>
		<li>uint8, uint16, uint32</li>
		<li>char</li>
	</ul>
	<li><strong>&lt;primitive&gt;:</strong> Contains a <name>, <cmdmid>, and <tlmmid>. You must define at least one <config> to load an App, as a config is required to send commands or receive telemetry. You can interact with more than one instance of an App by selecting a <config> for each instance at runtime.</li>
	<li><strong>&lt;const&gt;:</strong> The lowest functional command code for the App. Usually 0.</li>
</ul>
	
	
	
	
